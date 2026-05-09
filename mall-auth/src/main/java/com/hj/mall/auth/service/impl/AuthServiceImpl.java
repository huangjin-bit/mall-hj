package com.hj.mall.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.auth.dto.TokenDTO;
import com.hj.mall.auth.entity.Member;
import com.hj.mall.auth.entity.MemberAuth;
import com.hj.mall.auth.entity.MemberLoginLog;
import com.hj.mall.auth.mapper.MemberAuthMapper;
import com.hj.mall.auth.mapper.MemberLoginLogMapper;
import com.hj.mall.auth.mapper.MemberMapper;
import com.hj.mall.auth.service.AuthService;
import com.hj.mall.auth.service.TokenService;
import com.hj.mall.auth.util.JwtUtil;
import com.hj.mall.common.result.Result;
import com.hj.mall.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberMapper memberMapper;
    private final MemberAuthMapper memberAuthMapper;
    private final MemberLoginLogMapper memberLoginLogMapper;
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final StringRedisTemplate stringRedisTemplate;

    private static final String SMS_CODE_PREFIX = "auth:sms:";
    private static final int SMS_CODE_EXPIRE_SECONDS = 300; // 5分钟
    private static final String IDENTITY_TYPE_USERNAME = "USERNAME";
    private static final String IDENTITY_TYPE_PHONE = "PHONE";
    private static final int LOGIN_TYPE_USERNAME = 1;
    private static final int LOGIN_TYPE_PHONE = 2;
    private static final int LOGIN_STATUS_SUCCESS = 1;
    private static final int LOGIN_STATUS_FAIL = 0;
    private static final int MEMBER_STATUS_NORMAL = 1;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<TokenDTO> loginByUsername(String username, String password, String ip, String userAgent) {
        log.info("[AuthServiceImpl] 用户名密码登录, username={}", username);

        // 1. 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "用户名或密码不能为空");
        }

        // 2. 查询用户认证信息
        LambdaQueryWrapper<MemberAuth> authWrapper = new LambdaQueryWrapper<>();
        authWrapper.eq(MemberAuth::getIdentityType, IDENTITY_TYPE_USERNAME)
            .eq(MemberAuth::getIdentifier, username);
        MemberAuth memberAuth = memberAuthMapper.selectOne(authWrapper);

        if (memberAuth == null) {
            log.warn("[AuthServiceImpl] 用户不存在, username={}", username);
            saveLoginLog(null, LOGIN_TYPE_USERNAME, ip, userAgent, LOGIN_STATUS_FAIL, "用户不存在");
            return Result.error(ResultCode.MEMBER_NOT_FOUND.getCode(), "用户名或密码错误");
        }

        // 3. 验证密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, memberAuth.getCredential())) {
            log.warn("[AuthServiceImpl] 密码错误, username={}", username);
            saveLoginLog(memberAuth.getMemberId(), LOGIN_TYPE_USERNAME, ip, userAgent, LOGIN_STATUS_FAIL, "密码错误");
            return Result.error(ResultCode.PASSWORD_ERROR.getCode(), "用户名或密码错误");
        }

        // 4. 查询会员信息
        Member member = memberMapper.selectById(memberAuth.getMemberId());
        if (member == null) {
            log.error("[AuthServiceImpl] 会员信息不存在, memberId={}", memberAuth.getMemberId());
            return Result.error(ResultCode.MEMBER_NOT_FOUND.getCode(), "会员信息不存在");
        }

        // 5. 检查账号状态
        if (member.getStatus() == null || member.getStatus() != MEMBER_STATUS_NORMAL) {
            log.warn("[AuthServiceImpl] 账号已被禁用, memberId={}", member.getId());
            saveLoginLog(member.getId(), LOGIN_TYPE_USERNAME, ip, userAgent, LOGIN_STATUS_FAIL, "账号已被禁用");
            return Result.error(ResultCode.FORBIDDEN.getCode(), "账号已被禁用");
        }

        // 6. 生成 Token
        String token = jwtUtil.generateToken(member.getId(), member.getUsername());

        // 7. 缓存 Token
        tokenService.cacheToken(member.getId(), token);

        // 8. 保存登录日志
        saveLoginLog(member.getId(), LOGIN_TYPE_USERNAME, ip, userAgent, LOGIN_STATUS_SUCCESS, null);

        // 9. 返回 Token 信息
        TokenDTO tokenDTO = TokenDTO.builder()
            .token(token)
            .memberId(member.getId())
            .username(member.getUsername())
            .build();

        log.info("[AuthServiceImpl] 登录成功, memberId={}, username={}", member.getId(), member.getUsername());
        return Result.ok(tokenDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<TokenDTO> loginByPhone(String phone, String code, String ip, String userAgent) {
        log.info("[AuthServiceImpl] 手机验证码登录, phone={}", phone);

        // 1. 参数校验
        if (!StringUtils.hasText(phone) || !StringUtils.hasText(code)) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "手机号或验证码不能为空");
        }

        // 2. 验证短信验证码
        String redisKey = SMS_CODE_PREFIX + phone;
        String savedCode = stringRedisTemplate.opsForValue().get(redisKey);

        if (savedCode == null) {
            log.warn("[AuthServiceImpl] 验证码不存在或已过期, phone={}", phone);
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "验证码不存在或已过期");
        }

        if (!savedCode.equals(code)) {
            log.warn("[AuthServiceImpl] 验证码错误, phone={}, input={}, saved={}", phone, code, savedCode);
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "验证码错误");
        }

        // 3. 查询会员信息
        LambdaQueryWrapper<Member> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(Member::getPhone, phone);
        Member member = memberMapper.selectOne(memberWrapper);

        if (member == null) {
            log.warn("[AuthServiceImpl] 手机号未注册, phone={}", phone);
            saveLoginLog(null, LOGIN_TYPE_PHONE, ip, userAgent, LOGIN_STATUS_FAIL, "手机号未注册");
            return Result.error(ResultCode.MEMBER_NOT_FOUND.getCode(), "该手机号未注册，请先注册");
        }

        // 4. 检查账号状态
        if (member.getStatus() == null || member.getStatus() != MEMBER_STATUS_NORMAL) {
            log.warn("[AuthServiceImpl] 账号已被禁用, memberId={}", member.getId());
            saveLoginLog(member.getId(), LOGIN_TYPE_PHONE, ip, userAgent, LOGIN_STATUS_FAIL, "账号已被禁用");
            return Result.error(ResultCode.FORBIDDEN.getCode(), "账号已被禁用");
        }

        // 5. 删除已使用的验证码
        stringRedisTemplate.delete(redisKey);

        // 6. 生成 Token
        String token = jwtUtil.generateToken(member.getId(), member.getUsername());

        // 7. 缓存 Token
        tokenService.cacheToken(member.getId(), token);

        // 8. 保存登录日志
        saveLoginLog(member.getId(), LOGIN_TYPE_PHONE, ip, userAgent, LOGIN_STATUS_SUCCESS, null);

        // 9. 返回 Token 信息
        TokenDTO tokenDTO = TokenDTO.builder()
            .token(token)
            .memberId(member.getId())
            .username(member.getUsername())
            .build();

        log.info("[AuthServiceImpl] 手机验证码登录成功, memberId={}, phone={}", member.getId(), phone);
        return Result.ok(tokenDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> register(String username, String phone, String password) {
        log.info("[AuthServiceImpl] 用户注册, username={}, phone={}", username, phone);

        // 1. 参数校验
        if (!StringUtils.hasText(username) || !StringUtils.hasText(phone) || !StringUtils.hasText(password)) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "用户名、手机号、密码不能为空");
        }

        // 2. 检查用户名是否已存在
        LambdaQueryWrapper<MemberAuth> authWrapper = new LambdaQueryWrapper<>();
        authWrapper.eq(MemberAuth::getIdentityType, IDENTITY_TYPE_USERNAME)
            .eq(MemberAuth::getIdentifier, username);
        MemberAuth existAuth = memberAuthMapper.selectOne(authWrapper);
        if (existAuth != null) {
            log.warn("[AuthServiceImpl] 用户名已存在, username={}", username);
            return Result.error(ResultCode.MEMBER_EXIST.getCode(), "用户名已存在");
        }

        // 3. 检查手机号是否已存在
        LambdaQueryWrapper<Member> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(Member::getPhone, phone);
        Member existMember = memberMapper.selectOne(memberWrapper);
        if (existMember != null) {
            log.warn("[AuthServiceImpl] 手机号已注册, phone={}", phone);
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "该手机号已注册");
        }

        // 4. 创建会员
        Member member = new Member();
        member.setUsername(username);
        member.setPhone(phone);
        member.setNickname(username);
        member.setStatus(MEMBER_STATUS_NORMAL);
        member.setCreateTime(LocalDateTime.now());
        memberMapper.insert(member);

        // 5. 加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        // 6. 创建用户名认证信息
        MemberAuth usernameAuth = new MemberAuth();
        usernameAuth.setMemberId(member.getId());
        usernameAuth.setIdentityType(IDENTITY_TYPE_USERNAME);
        usernameAuth.setIdentifier(username);
        usernameAuth.setCredential(encodedPassword);
        usernameAuth.setVerified(1);
        usernameAuth.setCreateTime(LocalDateTime.now());
        memberAuthMapper.insert(usernameAuth);

        // 7. 创建手机号认证信息
        MemberAuth phoneAuth = new MemberAuth();
        phoneAuth.setMemberId(member.getId());
        phoneAuth.setIdentityType(IDENTITY_TYPE_PHONE);
        phoneAuth.setIdentifier(phone);
        phoneAuth.setCredential(encodedPassword);
        phoneAuth.setVerified(1);
        phoneAuth.setCreateTime(LocalDateTime.now());
        memberAuthMapper.insert(phoneAuth);

        log.info("[AuthServiceImpl] 注册成功, memberId={}, username={}, phone={}", member.getId(), username, phone);
        return Result.ok();
    }

    @Override
    public Result<Void> sendSmsCode(String phone) {
        log.info("[AuthServiceImpl] 发送短信验证码, phone={}", phone);

        // 1. 参数校验
        if (!StringUtils.hasText(phone)) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "手机号不能为空");
        }

        // 2. 生成6位随机验证码
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(1000000));

        // 3. 存储到 Redis，5分钟过期
        String redisKey = SMS_CODE_PREFIX + phone;
        stringRedisTemplate.opsForValue().set(redisKey, code, SMS_CODE_EXPIRE_SECONDS, TimeUnit.SECONDS);

        // TODO: 实际项目中这里需要调用短信服务发送验证码
        log.info("[AuthServiceImpl] 短信验证码生成成功, phone={}, code={}", phone, code);

        return Result.ok();
    }

    @Override
    public Result<Long> validateToken(String token) {
        log.debug("[AuthServiceImpl] 验证 Token");

        // 1. 参数校验
        if (!StringUtils.hasText(token)) {
            return Result.error(ResultCode.BAD_REQUEST.getCode(), "Token 不能为空");
        }

        try {
            // 2. 解析 Token
            Long memberId = jwtUtil.getMemberId(token);

            // 3. 验证 Token 是否在缓存中
            if (!tokenService.isTokenValid(memberId, token)) {
                log.warn("[AuthServiceImpl] Token 无效或已失效, memberId={}", memberId);
                return Result.error(ResultCode.TOKEN_INVALID.getCode(), "Token 无效或已失效");
            }

            log.debug("[AuthServiceImpl] Token 验证成功, memberId={}", memberId);
            return Result.ok(memberId);
        } catch (Exception e) {
            log.error("[AuthServiceImpl] Token 验证失败", e);
            return Result.error(ResultCode.TOKEN_INVALID.getCode(), "Token 无效");
        }
    }

    /**
     * 保存登录日志
     */
    private void saveLoginLog(Long memberId, Integer loginType, String ip, String userAgent, Integer status, String remark) {
        try {
            MemberLoginLog loginLog = new MemberLoginLog();
            loginLog.setMemberId(memberId);
            loginLog.setLoginType(loginType == LOGIN_TYPE_USERNAME ? "USERNAME" : "PHONE");
            loginLog.setIp(ip);
            loginLog.setUserAgent(userAgent);

            // 解析操作系统和浏览器（简单实现，实际可使用 User-Agent 解析库）
            if (StringUtils.hasText(userAgent)) {
                String os = parseOs(userAgent);
                String browser = parseBrowser(userAgent);
                loginLog.setOs(os);
                loginLog.setBrowser(browser);
            }

            loginLog.setStatus(status);
            loginLog.setRemark(remark);
            loginLog.setCreateTime(LocalDateTime.now());

            memberLoginLogMapper.insert(loginLog);
        } catch (Exception e) {
            log.error("[AuthServiceImpl] 保存登录日志失败", e);
        }
    }

    /**
     * 解析操作系统
     */
    private String parseOs(String userAgent) {
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac")) {
            return "MacOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        }
        return "Unknown";
    }

    /**
     * 解析浏览器
     */
    private String parseBrowser(String userAgent) {
        if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("MicroMessenger")) {
            return "微信浏览器";
        }
        return "Unknown";
    }
}

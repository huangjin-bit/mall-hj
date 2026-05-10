package com.hj.mall.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hj.mall.common.exception.BizException;
import com.hj.mall.common.result.ResultCode;
import com.hj.mall.member.constant.MemberConstant;
import com.hj.mall.member.entity.Member;
import com.hj.mall.member.entity.MemberAuth;
import com.hj.mall.member.mapper.MemberAuthMapper;
import com.hj.mall.member.mapper.MemberMapper;
import com.hj.mall.member.service.MemberService;
import com.hj.mall.member.vo.MemberLoginVO;
import com.hj.mall.member.vo.MemberRegisterVO;
import com.hj.mall.member.vo.SocialUserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 会员服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final MemberAuthMapper memberAuthMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public IPage<Member> listPage(Page<Member> page, String key) {
        log.info("[MemberService] 分页查询会员列表, page={}, key={}", page.getCurrent(), key);

        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(key)) {
            wrapper.like(Member::getUsername, key)
                   .or()
                   .like(Member::getNickname, key)
                   .or()
                   .like(Member::getPhone, key);
        }
        wrapper.orderByDesc(Member::getCreateTime);

        return memberMapper.selectPage(page, wrapper);
    }

    @Override
    public Member getById(Long id) {
        if (id == null) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        Member member = memberMapper.selectById(id);
        if (member == null) {
            throw new BizException(ResultCode.MEMBER_NOT_FOUND);
        }

        return member;
    }

    @Override
    public Member getByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }

        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getUsername, username);

        return memberMapper.selectOne(wrapper);
    }

    @Override
    public Member getByPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return null;
        }

        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getPhone, phone);

        return memberMapper.selectOne(wrapper);
    }

    @Override
    public void save(Member member) {
        log.info("[MemberService] 保存会员, username={}", member.getUsername());

        // 检查用户名是否已存在
        Member existMember = getByUsername(member.getUsername());
        if (existMember != null) {
            throw new BizException(ResultCode.MEMBER_EXIST);
        }

        // 检查手机号是否已存在
        if (StringUtils.isNotBlank(member.getPhone())) {
            Member existPhone = getByPhone(member.getPhone());
            if (existPhone != null) {
                throw new BizException(ResultCode.MEMBER_EXIST);
            }
        }

        memberMapper.insert(member);
        log.info("[MemberService] 会员保存成功, id={}", member.getId());
    }

    @Override
    public void updateById(Member member) {
        log.info("[MemberService] 更新会员信息, id={}", member.getId());

        Member existMember = getById(member.getId());
        if (existMember == null) {
            throw new BizException(ResultCode.MEMBER_NOT_FOUND);
        }

        memberMapper.updateById(member);
        log.info("[MemberService] 会员信息更新成功, id={}", member.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(List<Long> ids) {
        log.info("[MemberService] 批量删除会员, ids={}", ids);

        if (ids == null || ids.isEmpty()) {
            throw new BizException(ResultCode.BAD_REQUEST);
        }

        memberMapper.deleteBatchIds(ids);
        log.info("[MemberService] 批量删除成功, count={}", ids.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateIntegration(Long memberId, int change) {
        log.info("[MemberService] 更新会员积分, memberId={}, change={}", memberId, change);

        Member member = getById(memberId);
        if (member == null) {
            throw new BizException(ResultCode.MEMBER_NOT_FOUND);
        }

        int newIntegration = member.getIntegration() + change;
        if (newIntegration < 0) {
            throw new BizException(400, "积分不足");
        }

        member.setIntegration(newIntegration);
        memberMapper.updateById(member);
        log.info("[MemberService] 积分更新成功, memberId={}, newIntegration={}", memberId, newIntegration);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateGrowth(Long memberId, int change) {
        log.info("[MemberService] 更新会员成长值, memberId={}, change={}", memberId, change);

        Member member = getById(memberId);
        if (member == null) {
            throw new BizException(ResultCode.MEMBER_NOT_FOUND);
        }

        int newGrowth = member.getGrowth() + change;
        if (newGrowth < 0) {
            throw new BizException(400, "成长值不足");
        }

        member.setGrowth(newGrowth);
        memberMapper.updateById(member);
        log.info("[MemberService] 成长值更新成功, memberId={}, newGrowth={}", memberId, newGrowth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(MemberRegisterVO vo) {
        log.info("[MemberService] 会员注册, username={}, phone={}", vo.getUserName(), vo.getPhone());

        // 检查用户名是否已存在
        Member existMember = getByUsername(vo.getUserName());
        if (existMember != null) {
            throw new BizException(ResultCode.MEMBER_EXIST);
        }

        // 检查手机号是否已存在
        Member existPhone = getByPhone(vo.getPhone());
        if (existPhone != null) {
            throw new BizException(ResultCode.MEMBER_EXIST);
        }

        // 创建新会员
        Member member = new Member();
        member.setUsername(vo.getUserName());
        member.setPhone(vo.getPhone());
        member.setLevelId((long) MemberConstant.DEFAULT_LEVEL_STATUS);
        member.setStatus(MemberConstant.Status.ENABLED);
        member.setIntegration(0);
        member.setGrowth(0);
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());

        memberMapper.insert(member);

        // 创建认证记录（用户名+密码）
        MemberAuth auth = new MemberAuth();
        auth.setMemberId(member.getId());
        auth.setIdentityType("username");
        auth.setIdentifier(vo.getUserName());
        auth.setCredential(passwordEncoder.encode(vo.getPassword()));
        auth.setVerified(1);
        auth.setCreateTime(LocalDateTime.now());
        auth.setUpdateTime(LocalDateTime.now());
        memberAuthMapper.insert(auth);

        log.info("[MemberService] 会员注册成功, id={}", member.getId());
        return member.getId();
    }

    @Override
    public Member login(MemberLoginVO vo) {
        log.info("[MemberService] 会员登录, account={}", vo.getLoginAccount());

        // 先通过 MemberAuth 查找认证记录
        LambdaQueryWrapper<MemberAuth> authWrapper = new LambdaQueryWrapper<>();
        authWrapper.eq(MemberAuth::getIdentifier, vo.getLoginAccount())
                   .in(MemberAuth::getIdentityType, "username", "phone");
        MemberAuth auth = memberAuthMapper.selectOne(authWrapper);
        if (auth == null) {
            throw new BizException(ResultCode.MEMBER_NOT_FOUND);
        }

        // 验证密码
        if (!passwordEncoder.matches(vo.getPassword(), auth.getCredential())) {
            throw new BizException(ResultCode.PASSWORD_ERROR);
        }

        // 查询会员信息
        Member member = memberMapper.selectById(auth.getMemberId());
        if (member == null) {
            throw new BizException(ResultCode.MEMBER_NOT_FOUND);
        }

        // 检查账号状态
        if (member.getStatus() == MemberConstant.Status.DISABLED) {
            throw new BizException("账号已被禁用");
        }

        log.info("[MemberService] 会员登录成功, id={}", member.getId());
        return member;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member oauthLogin(SocialUserVO vo) {
        log.info("[MemberService] 社交登录, socialType={}, uid={}", vo.getSocialType(), vo.getUid());

        // 根据 identityType + identifier 查询是否已绑定
        LambdaQueryWrapper<MemberAuth> authWrapper = new LambdaQueryWrapper<>();
        authWrapper.eq(MemberAuth::getIdentityType, vo.getSocialType())
                   .eq(MemberAuth::getIdentifier, vo.getUid());
        MemberAuth existAuth = memberAuthMapper.selectOne(authWrapper);

        if (existAuth != null) {
            Member member = memberMapper.selectById(existAuth.getMemberId());
            log.info("[MemberService] 社交账号已存在, id={}", member.getId());
            return member;
        }

        // 创建新会员（社交登录）
        Member member = new Member();
        member.setUsername("social_" + vo.getUid());
        member.setLevelId((long) MemberConstant.DEFAULT_LEVEL_STATUS);
        member.setStatus(MemberConstant.Status.ENABLED);
        member.setIntegration(0);
        member.setGrowth(0);
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        memberMapper.insert(member);

        // 创建社交认证记录
        MemberAuth auth = new MemberAuth();
        auth.setMemberId(member.getId());
        auth.setIdentityType(vo.getSocialType());
        auth.setIdentifier(vo.getUid());
        auth.setCredential(vo.getAccessToken());
        auth.setVerified(1);
        auth.setCreateTime(LocalDateTime.now());
        auth.setUpdateTime(LocalDateTime.now());
        memberAuthMapper.insert(auth);

        log.info("[MemberService] 社交登录创建新会员成功, id={}", member.getId());
        return member;
    }

    @Override
    public Long count() {
        Long count = memberMapper.selectCount(null);
        log.info("[MemberService] 会员总数, count={}", count);
        return count;
    }
}

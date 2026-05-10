package com.hj.mall.member.service;

import com.hj.mall.common.exception.BizException;
import com.hj.mall.member.entity.Member;
import com.hj.mall.member.entity.MemberAuth;
import com.hj.mall.member.mapper.MemberAuthMapper;
import com.hj.mall.member.mapper.MemberMapper;
import com.hj.mall.member.vo.MemberLoginVO;
import com.hj.mall.member.vo.MemberRegisterVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberAuthMapper memberAuthMapper;

    @MockitoBean
    private CustomerServiceService customerServiceService;

    @Test
    void register_shouldCreateMemberAndAuth() {
        MemberRegisterVO vo = new MemberRegisterVO();
        vo.setUserName("testuser");
        vo.setPassword("password123");
        vo.setPhone("13800138001");

        Long memberId = memberService.register(vo);

        assertNotNull(memberId);

        // 验证 Member 记录
        Member member = memberMapper.selectById(memberId);
        assertNotNull(member);
        assertEquals("testuser", member.getUsername());
        assertEquals("13800138001", member.getPhone());
        assertEquals(1, member.getStatus());

        // 验证 MemberAuth 记录
        LambdaQueryWrapper<MemberAuth> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberAuth::getMemberId, memberId)
               .eq(MemberAuth::getIdentityType, "username");
        MemberAuth auth = memberAuthMapper.selectOne(wrapper);
        assertNotNull(auth);
        assertEquals("testuser", auth.getIdentifier());
        assertNotNull(auth.getCredential());
        assertEquals(1, auth.getVerified());
    }

    @Test
    void register_duplicateUsername_shouldThrow() {
        MemberRegisterVO vo1 = new MemberRegisterVO();
        vo1.setUserName("dupuser");
        vo1.setPassword("password123");
        vo1.setPhone("13800138010");
        memberService.register(vo1);

        MemberRegisterVO vo2 = new MemberRegisterVO();
        vo2.setUserName("dupuser");
        vo2.setPassword("password456");
        vo2.setPhone("13800138011");

        assertThrows(BizException.class, () -> memberService.register(vo2));
    }

    @Test
    void login_withCorrectPassword_shouldReturnMember() {
        // 先注册
        MemberRegisterVO vo = new MemberRegisterVO();
        vo.setUserName("loginuser");
        vo.setPassword("mypassword");
        vo.setPhone("13800138002");
        memberService.register(vo);

        // 再登录
        MemberLoginVO loginVO = new MemberLoginVO();
        loginVO.setLoginAccount("loginuser");
        loginVO.setPassword("mypassword");

        Member member = memberService.login(loginVO);

        assertNotNull(member);
        assertEquals("loginuser", member.getUsername());
    }

    @Test
    void login_withWrongPassword_shouldThrow() {
        MemberRegisterVO vo = new MemberRegisterVO();
        vo.setUserName("wrongpwuser");
        vo.setPassword("correctpw");
        vo.setPhone("13800138003");
        memberService.register(vo);

        MemberLoginVO loginVO = new MemberLoginVO();
        loginVO.setLoginAccount("wrongpwuser");
        loginVO.setPassword("wrongpw");

        assertThrows(BizException.class, () -> memberService.login(loginVO));
    }

    @Test
    void login_userNotExist_shouldThrow() {
        MemberLoginVO loginVO = new MemberLoginVO();
        loginVO.setLoginAccount("nonexistent");
        loginVO.setPassword("anypassword");

        assertThrows(BizException.class, () -> memberService.login(loginVO));
    }
}

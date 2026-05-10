package com.hj.mall.member.service;

import com.hj.mall.common.exception.BizException;
import com.hj.mall.member.entity.Member;
import com.hj.mall.member.service.CustomerServiceService;
import com.hj.mall.member.vo.MemberRegisterVO;
import com.hj.mall.member.vo.SocialUserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberLifecycleTest {

    @Autowired
    private MemberService memberService;

    @MockitoBean
    private CustomerServiceService customerServiceService;

    private Long registerMember(String username, String phone) {
        MemberRegisterVO vo = new MemberRegisterVO();
        vo.setUserName(username);
        vo.setPassword("password123");
        vo.setPhone(phone);
        return memberService.register(vo);
    }

    @Test
    void updateIntegration_earnFromOrder() {
        Long memberId = registerMember("integ_user", "13900000001");

        memberService.updateIntegration(memberId, 100);

        Member member = memberService.getById(memberId);
        assertEquals(100, member.getIntegration());
    }

    @Test
    void updateIntegration_deductForOrder() {
        Long memberId = registerMember("integ_deduct", "13900000002");
        memberService.updateIntegration(memberId, 200);

        memberService.updateIntegration(memberId, -50);

        Member member = memberService.getById(memberId);
        assertEquals(150, member.getIntegration());
    }

    @Test
    void updateIntegration_insufficient_shouldThrow() {
        Long memberId = registerMember("integ_poor", "13900000003");

        assertThrows(BizException.class, () -> memberService.updateIntegration(memberId, -100));
    }

    @Test
    void updateGrowth_levelUp() {
        Long memberId = registerMember("growth_user", "13900000004");

        memberService.updateGrowth(memberId, 500);

        Member member = memberService.getById(memberId);
        assertEquals(500, member.getGrowth());
    }

    @Test
    void oauthLogin_newSocialUser() {
        SocialUserVO vo = new SocialUserVO();
        vo.setSocialType("wechat");
        vo.setUid("wx_12345");
        vo.setAccessToken("mock_access_token");

        Member member = memberService.oauthLogin(vo);

        assertNotNull(member);
        assertTrue(member.getUsername().startsWith("social_"));
        assertEquals(1, member.getStatus());
    }

    @Test
    void oauthLogin_existingSocialUser() {
        SocialUserVO vo = new SocialUserVO();
        vo.setSocialType("qq");
        vo.setUid("qq_99999");
        vo.setAccessToken("token1");
        Member first = memberService.oauthLogin(vo);

        SocialUserVO vo2 = new SocialUserVO();
        vo2.setSocialType("qq");
        vo2.setUid("qq_99999");
        vo2.setAccessToken("token2");
        Member second = memberService.oauthLogin(vo2);

        assertEquals(first.getId(), second.getId());
    }
}
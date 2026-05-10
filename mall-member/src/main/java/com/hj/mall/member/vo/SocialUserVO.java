package com.hj.mall.member.vo;

import lombok.Data;

/**
 * 社交登录用户信息（OAuth2回调后获取的信息）
 */
@Data
public class SocialUserVO {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String uid;
    private String socialType;
}

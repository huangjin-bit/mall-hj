package com.hj.mall.common.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class HeaderSigner {

    private static final String ALGORITHM = "HmacSHA256";

    private final SecretKeySpec keySpec;

    public HeaderSigner(String secretKey) {
        this.keySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
    }

    public String sign(String userId, String username) {
        String data = userId + ":" + username;
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(keySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("HMAC 签名失败", e);
        }
    }

    public boolean verify(String userId, String username, String signature) {
        String expected = sign(userId, username);
        return expected.equals(signature);
    }
}

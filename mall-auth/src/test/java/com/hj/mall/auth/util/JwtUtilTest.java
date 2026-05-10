package com.hj.mall.auth.util;

import com.hj.mall.auth.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        JwtConfig config = new JwtConfig();
        config.setSecret("mall-hj-secret-key-must-be-at-least-256-bits-long-for-hs256");
        config.setExpiration(86400000L);

        jwtUtil = new JwtUtil(config);
        jwtUtil.init();
    }

    @Test
    void generateAdminToken_shouldReturnValidToken() {
        String token = jwtUtil.generateAdminToken(1L, "admin");

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3);
    }

    @Test
    void generateToken_shouldContainCorrectClaims() {
        String token = jwtUtil.generateAdminToken(1L, "admin");

        Claims claims = jwtUtil.parseToken(token);
        assertEquals("admin", claims.getSubject());
        assertEquals(1L, claims.get("userId", Long.class));
        assertEquals("admin", claims.get("userType", String.class));
    }

    @Test
    void getUserId_shouldReturnCorrectId() {
        String token = jwtUtil.generateToken(42L, "testuser", "member", null);

        assertEquals(42L, jwtUtil.getUserId(token));
    }

    @Test
    void getUserType_shouldReturnCorrectType() {
        String adminToken = jwtUtil.generateAdminToken(1L, "admin");
        String memberToken = jwtUtil.generateToken(2L, "user");

        assertEquals("admin", jwtUtil.getUserType(adminToken));
        assertEquals("member", jwtUtil.getUserType(memberToken));
    }

    @Test
    void validateToken_validToken_shouldReturnTrue() {
        String token = jwtUtil.generateAdminToken(1L, "admin");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_invalidToken_shouldReturnFalse() {
        assertFalse(jwtUtil.validateToken("invalid.token.value"));
    }

    @Test
    void validateToken_nullToken_shouldReturnFalse() {
        assertFalse(jwtUtil.validateToken(null));
    }

    @Test
    void parseToken_tamperedToken_shouldThrow() {
        String token = jwtUtil.generateAdminToken(1L, "admin");
        // 修改 token 中间的 payload 部分
        String[] parts = token.split("\\.");
        String tampered = parts[0] + "." + parts[1].replace("a", "b") + "." + parts[2];

        assertThrows(RuntimeException.class, () -> jwtUtil.parseToken(tampered));
    }

    @Test
    void getUsername_shouldReturnSubject() {
        String token = jwtUtil.generateAdminToken(1L, "testadmin");
        assertEquals("testadmin", jwtUtil.getUsername(token));
    }

    @Test
    void generateToken_withDeptId_shouldContainDeptId() {
        String token = jwtUtil.generateToken(1L, "admin", "admin", 100L);

        assertEquals(100L, jwtUtil.getDeptId(token));
    }

    @Test
    void generateToken_withoutDeptId_deptIdShouldBeNull() {
        String token = jwtUtil.generateAdminToken(1L, "admin");

        assertNull(jwtUtil.getDeptId(token));
    }
}

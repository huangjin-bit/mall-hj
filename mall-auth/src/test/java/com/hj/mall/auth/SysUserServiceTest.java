package com.hj.mall.auth;

import com.hj.mall.auth.dto.SysLoginDTO;
import com.hj.mall.auth.entity.SysUser;
import com.hj.mall.auth.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 系统用户服务测试
 */
@Slf4j
@SpringBootTest
class SysUserServiceTest {

    @Autowired
    private SysUserService sysUserService;

    @Test
    void testLogin() {
        log.info("========== 测试管理员登录 ==========");

        try {
            var loginVO = sysUserService.login("admin", "admin123", "127.0.0.1");

            assertNotNull(loginVO, "登录结果不应为空");
            assertNotNull(loginVO.getToken(), "Token不应为空");
            assertEquals(1L, loginVO.getUserId(), "用户ID应为1");
            assertEquals("admin", loginVO.getUsername(), "用户名应为admin");

            log.info("登录成功：{}", loginVO);
        } catch (Exception e) {
            log.error("登录失败", e);
            fail("登录失败：" + e.getMessage());
        }
    }

    @Test
    void testLoginWithWrongPassword() {
        log.info("========== 测试错误密码登录 ==========");

        assertThrows(RuntimeException.class, () -> {
            sysUserService.login("admin", "wrongpassword", "127.0.0.1");
        }, "应该抛出异常");

        log.info("错误密码登录测试通过");
    }

    @Test
    void testGetUserInfo() {
        log.info("========== 测试获取用户信息 ==========");

        try {
            var userInfo = sysUserService.getUserInfo(1L);

            assertNotNull(userInfo, "用户信息不应为空");
            assertNotNull(userInfo.getUser(), "用户不应为空");
            assertNotNull(userInfo.getRoles(), "角色列表不应为空");
            assertNotNull(userInfo.getPermissions(), "权限集合不应为空");

            log.info("用户信息：{}", userInfo);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            fail("获取用户信息失败：" + e.getMessage());
        }
    }

    @Test
    void testGetRouters() {
        log.info("========== 测试获取用户路由 ==========");

        try {
            var routers = sysUserService.getRouters(1L);

            assertNotNull(routers, "路由列表不应为空");
            assertFalse(routers.isEmpty(), "路由列表不应为空");

            log.info("用户路由：{}", routers);
        } catch (Exception e) {
            log.error("获取用户路由失败", e);
            fail("获取用户路由失败：" + e.getMessage());
        }
    }

    @Test
    void testListPage() {
        log.info("========== 测试分页查询用户列表 ==========");

        try {
            var page = sysUserService.listPage(1, 10, null, null, null, null);

            assertNotNull(page, "分页结果不应为空");
            assertTrue(page.getRecords().size() > 0, "应该有用户数据");

            log.info("用户列表：{}", page.getRecords());
        } catch (Exception e) {
            log.error("分页查询失败", e);
            fail("分页查询失败：" + e.getMessage());
        }
    }

    @Test
    void testGetById() {
        log.info("========== 测试根据ID查询用户 ==========");

        try {
            SysUser user = sysUserService.getById(1L);

            assertNotNull(user, "用户不应为空");
            assertEquals("admin", user.getUsername(), "用户名应为admin");
            assertNotNull(user.getRoles(), "角色列表不应为空");

            log.info("用户详情：{}", user);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            fail("查询用户失败：" + e.getMessage());
        }
    }
}

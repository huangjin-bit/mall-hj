-- 系统用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户账号',
  `nickname` VARCHAR(50) NOT NULL COMMENT '用户昵称',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（BCrypt加密）',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `gender` TINYINT DEFAULT 2 COMMENT '性别：0-男 1-女 2-未知',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像地址',
  `status` TINYINT DEFAULT 1 COMMENT '帐号状态：0-禁用 1-正常',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `login_ip` VARCHAR(50) DEFAULT NULL COMMENT '最后登录IP',
  `login_date` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 系统角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` BIGINT NOT NULL COMMENT '角色ID',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(50) NOT NULL COMMENT '角色权限字符串',
  `sort` INT NOT NULL COMMENT '显示顺序',
  `status` TINYINT DEFAULT 1 COMMENT '角色状态：0-禁用 1-正常',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `data_scope` TINYINT DEFAULT 1 COMMENT '数据范围：1-全部 2-自定义 3-本部门 4-本部门及以下 5-仅本人',
  `create_by` VARCHAR(50) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 系统菜单表
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` BIGINT NOT NULL COMMENT '菜单ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
  `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '路由地址',
  `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
  `redirect` VARCHAR(200) DEFAULT NULL COMMENT '重定向地址',
  `menu_type` CHAR(1) NOT NULL COMMENT '菜单类型：M-目录 C-菜单 F-按钮',
  `perms` VARCHAR(100) DEFAULT NULL COMMENT '权限标识',
  `icon` VARCHAR(100) DEFAULT NULL COMMENT '菜单图标',
  `sort` INT NOT NULL COMMENT '显示顺序',
  `visible` TINYINT DEFAULT 1 COMMENT '是否可见：0-隐藏 1-显示',
  `status` TINYINT DEFAULT 1 COMMENT '菜单状态：0-禁用 1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统菜单表';

-- 系统部门表
CREATE TABLE IF NOT EXISTS `sys_dept` (
  `id` BIGINT NOT NULL COMMENT '部门ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
  `ancestors` VARCHAR(500) DEFAULT NULL COMMENT '祖级列表（逗号分隔）',
  `dept_name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `sort` INT NOT NULL COMMENT '显示顺序',
  `leader` VARCHAR(50) DEFAULT NULL COMMENT '负责人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `status` TINYINT DEFAULT 1 COMMENT '部门状态：0-禁用 1-正常',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统部门表';

-- 系统操作日志表
CREATE TABLE IF NOT EXISTS `sys_oper_log` (
  `id` BIGINT NOT NULL COMMENT '日志主键',
  `title` VARCHAR(50) DEFAULT NULL COMMENT '模块标题',
  `oper_type` INT DEFAULT 0 COMMENT '业务类型（0其它 1新增 2修改 3删除 4授权 5导出 6导入 7强退 8生成代码 9清空数据）',
  `method` VARCHAR(100) DEFAULT NULL COMMENT '方法名称',
  `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
  `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方式',
  `oper_param` TEXT DEFAULT NULL COMMENT '请求参数',
  `oper_result` TEXT DEFAULT NULL COMMENT '返回参数',
  `oper_user_id` BIGINT DEFAULT NULL COMMENT '操作人员ID',
  `oper_user_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人员名称',
  `oper_ip` VARCHAR(50) DEFAULT NULL COMMENT '操作IP',
  `status` INT DEFAULT 1 COMMENT '操作状态：0-失败 1-成功',
  `error_msg` VARCHAR(2000) DEFAULT NULL COMMENT '错误消息',
  `cost_time` BIGINT DEFAULT NULL COMMENT '消耗时间（毫秒）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_oper_user_id` (`oper_user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统操作日志表';

-- 系统用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户角色关联表';

-- 系统角色菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色菜单关联表';

-- 插入初始管理员账号（用户名：admin，密码：admin123）
INSERT INTO `sys_user` (`id`, `username`, `nickname`, `password`, `status`, `dept_id`, `create_time`, `update_time`)
VALUES (1, 'admin', '超级管理员', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE/sW5AiBfa5PK', 1, NULL, NOW(), NOW())
ON DUPLICATE KEY UPDATE `username` = `username`;

-- 插入初始角色
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `sort`, `status`, `data_scope`, `create_time`, `update_time`)
VALUES (1, '超级管理员', 'admin', 1, 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE `role_key` = `role_key`;

-- 为管理员分配角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`)
VALUES (1, 1)
ON DUPLICATE KEY UPDATE `user_id` = `user_id`;

-- 插入初始菜单数据
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`, `create_time`, `update_time`) VALUES
(1, 0, '系统管理', '/system', NULL, 'M', NULL, 'setting', 1, 1, 1, NOW(), NOW()),
(100, 1, '用户管理', '/system/user', 'system/user/index', 'C', 'system:user:list', 'user', 1, 1, 1, NOW(), NOW()),
(101, 1, '角色管理', '/system/role', 'system/role/index', 'C', 'system:role:list', 'team', 2, 1, 1, NOW(), NOW()),
(102, 1, '菜单管理', '/system/menu', 'system/menu/index', 'C', 'system:menu:list', 'list', 3, 1, 1, NOW(), NOW()),
(103, 1, '部门管理', '/system/dept', 'system/dept/index', 'C', 'system:dept:list', 'tree', 4, 1, 1, NOW(), NOW()),
(104, 1, '操作日志', '/system/oper-log', 'system/oper-log/index', 'C', 'system:oper-log:list', 'file-text', 5, 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE `id` = `id`;

-- 为超级管理员角色分配所有菜单
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, `id` FROM `sys_menu`
ON DUPLICATE KEY UPDATE `role_id` = `role_id`;

-- 插入初始部门数据
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_name`, `sort`, `leader`, `phone`, `email`, `status`, `create_time`, `update_time`) VALUES
(1, 0, '0,', '总公司', 1, '管理员', '15888888888', 'admin@mall.com', 1, NOW(), NOW()),
(2, 1, '0,1,', '技术部', 1, '张三', '15999999999', 'tech@mall.com', 1, NOW(), NOW()),
(3, 1, '0,1,', '市场部', 2, '李四', '15666666666', 'market@mall.com', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE `id` = `id`;

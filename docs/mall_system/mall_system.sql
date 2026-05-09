-- ========================================
-- Mall System 数据库初始化脚本
-- 版本: 1.0.0
-- 创建日期: 2026-05-10
-- 说明: 商城管理系统 RBAC 基础数据库结构
-- ========================================

-- 创建数据库
DROP DATABASE IF EXISTS `mall_system`;
CREATE DATABASE `mall_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `mall_system`;

-- ========================================
-- 1. 系统用户表
-- ========================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(64) NOT NULL COMMENT '用户名（登录账号）',
  `nickname` VARCHAR(64) NOT NULL COMMENT '用户昵称',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '用户邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号码',
  `gender` TINYINT DEFAULT 0 COMMENT '性别：0未知 1男 2女',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像地址',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0停用 1正常',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `login_ip` VARCHAR(64) DEFAULT NULL COMMENT '最后登录IP',
  `login_date` DATETIME DEFAULT NULL COMMENT '最后登录时间',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` BIGINT DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ========================================
-- 2. 系统角色表
-- ========================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` VARCHAR(64) NOT NULL COMMENT '角色名称',
  `role_key` VARCHAR(64) NOT NULL COMMENT '角色权限字符串',
  `sort` INT DEFAULT 0 COMMENT '显示顺序',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0停用 1正常',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '备注',
  `data_scope` TINYINT DEFAULT 1 COMMENT '数据范围：1全部数据 2自定义数据 3本部门数据 4本部门及以下数据 5仅本人数据',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` BIGINT DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- ========================================
-- 3. 系统菜单表
-- ========================================
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID，0为根菜单',
  `name` VARCHAR(64) NOT NULL COMMENT '菜单名称',
  `path` VARCHAR(255) DEFAULT NULL COMMENT '路由地址',
  `component` VARCHAR(255) DEFAULT NULL COMMENT '组件路径',
  `redirect` VARCHAR(255) DEFAULT NULL COMMENT '重定向地址',
  `menu_type` TINYINT NOT NULL COMMENT '菜单类型：0目录 1菜单 2按钮',
  `perms` VARCHAR(128) DEFAULT NULL COMMENT '权限标识',
  `icon` VARCHAR(128) DEFAULT NULL COMMENT '菜单图标',
  `sort` INT DEFAULT 0 COMMENT '显示顺序',
  `visible` TINYINT DEFAULT 1 COMMENT '显示状态：0隐藏 1显示',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0停用 1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- ========================================
-- 4. 系统部门表
-- ========================================
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID，0为顶级部门',
  `ancestors` VARCHAR(255) DEFAULT '0' COMMENT '祖级列表',
  `dept_name` VARCHAR(64) NOT NULL COMMENT '部门名称',
  `sort` INT DEFAULT 0 COMMENT '显示顺序',
  `leader` VARCHAR(64) DEFAULT NULL COMMENT '负责人',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
  `status` TINYINT DEFAULT 1 COMMENT '状态：0停用 1正常',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统部门表';

-- ========================================
-- 5. 用户角色关联表
-- ========================================
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ========================================
-- 6. 角色菜单关联表
-- ========================================
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- ========================================
-- 7. 操作日志表
-- ========================================
DROP TABLE IF EXISTS `sys_oper_log`;
CREATE TABLE `sys_oper_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` VARCHAR(128) DEFAULT NULL COMMENT '模块标题',
  `oper_type` TINYINT DEFAULT 0 COMMENT '操作类型：0其它 1新增 2修改 3删除',
  `method` VARCHAR(128) DEFAULT NULL COMMENT '方法名称',
  `request_url` VARCHAR(255) DEFAULT NULL COMMENT '请求URL',
  `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方式',
  `oper_param` TEXT DEFAULT NULL COMMENT '请求参数',
  `oper_result` TEXT DEFAULT NULL COMMENT '返回参数',
  `oper_user_id` BIGINT DEFAULT NULL COMMENT '操作员ID',
  `oper_user_name` VARCHAR(64) DEFAULT NULL COMMENT '操作员名称',
  `oper_ip` VARCHAR(64) DEFAULT NULL COMMENT '操作IP',
  `status` TINYINT DEFAULT 1 COMMENT '操作状态：0失败 1成功',
  `error_msg` TEXT DEFAULT NULL COMMENT '错误消息',
  `cost_time` BIGINT DEFAULT 0 COMMENT '消耗时间(毫秒)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_oper_time` (`create_time`),
  KEY `idx_oper_user_id` (`oper_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';

-- ========================================
-- 初始化数据
-- ========================================

-- ----------------------------
-- 初始化部门数据
-- ----------------------------
INSERT INTO `sys_dept` (`id`, `parent_id`, `ancestors`, `dept_name`, `sort`, `leader`, `phone`, `email`, `status`) VALUES
(100, 0, '0', '总公司', 0, '若依', '15888888888', 'ry@163.com', 1),
(101, 100, '0,100', '深圳总公司', 1, '若依', '15888888888', 'ry@163.com', 1),
(102, 100, '0,100', '长沙分公司', 2, '若依', '15888888888', 'ry@163.com', 1);

-- ----------------------------
-- 初始化用户数据
-- 密码: admin123
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `nickname`, `password`, `email`, `phone`, `gender`, `avatar`, `status`, `dept_id`, `remark`, `login_ip`, `login_date`) VALUES
(1, 'admin', '超级管理员', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'admin@163.com', '15888888888', 1, '', 1, 101, '管理员', '127.0.0.1', NOW());

-- ----------------------------
-- 初始化角色数据
-- ----------------------------
INSERT INTO `sys_role` (`id`, `role_name`, `role_key`, `sort`, `status`, `remark`, `data_scope`) VALUES
(1, '超级管理员', 'admin', 1, 1, '超级管理员', 1),
(2, '商品管理员', 'product_admin', 2, 1, '商品管理相关权限', 1),
(3, '订单管理员', 'order_admin', 3, 1, '订单管理相关权限', 1);

-- ----------------------------
-- 初始化用户角色关联数据
-- ----------------------------
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1);

-- ----------------------------
-- 初始化菜单数据
-- ----------------------------

-- 一级目录（menu_type=0）
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `redirect`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1000, 0, '商品管理', 'product', NULL, NULL, 0, NULL, 'shopping', 1, 1, 1),
(2000, 0, '会员管理', 'member', NULL, NULL, 0, NULL, 'user', 2, 1, 1),
(3000, 0, '订单管理', 'order', NULL, NULL, 0, NULL, 'list', 3, 1, 1),
(4000, 0, '营销管理', 'marketing', NULL, NULL, 0, NULL, 'price-tag', 4, 1, 1),
(5000, 0, '仓储管理', 'ware', NULL, NULL, 0, NULL, 'box', 5, 1, 1),
(6000, 0, '系统管理', 'system', NULL, NULL, 0, NULL, 'setting', 6, 1, 1);

-- 商品管理模块（1000）
-- 1001 分类管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1001, 1000, '分类管理', 'category', 'product/category/index', 1, NULL, 'tree', 1, 1, 1),
(10011, 1001, '分类查询', '', '', 2, 'product:category:list', '', 1, 1, 1),
(10012, 1001, '分类新增', '', '', 2, 'product:category:add', '', 2, 1, 1),
(10013, 1001, '分类修改', '', '', 2, 'product:category:edit', '', 3, 1, 1),
(10014, 1001, '分类删除', '', '', 2, 'product:category:delete', '', 4, 1, 1);

-- 1002 品牌管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1002, 1000, '品牌管理', 'brand', 'product/brand/index', 1, NULL, 'component', 2, 1, 1),
(10021, 1002, '品牌查询', '', '', 2, 'product:brand:list', '', 1, 1, 1),
(10022, 1002, '品牌新增', '', '', 2, 'product:brand:add', '', 2, 1, 1),
(10023, 1002, '品牌修改', '', '', 2, 'product:brand:edit', '', 3, 1, 1),
(10024, 1002, '品牌删除', '', '', 2, 'product:brand:delete', '', 4, 1, 1);

-- 1003 属性分组
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1003, 1000, '属性分组', 'attr-group', 'product/attrGroup/index', 1, NULL, 'tab', 3, 1, 1),
(10031, 1003, '分组查询', '', '', 2, 'product:attrGroup:list', '', 1, 1, 1),
(10032, 1003, '分组新增', '', '', 2, 'product:attrGroup:add', '', 2, 1, 1),
(10033, 1003, '分组修改', '', '', 2, 'product:attrGroup:edit', '', 3, 1, 1),
(10034, 1003, '分组删除', '', '', 2, 'product:attrGroup:delete', '', 4, 1, 1);

-- 1004 属性管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1004, 1000, '属性管理', 'attr', 'product/attr/index', 1, NULL, 'list', 4, 1, 1),
(10041, 1004, '属性查询', '', '', 2, 'product:attr:list', '', 1, 1, 1),
(10042, 1004, '属性新增', '', '', 2, 'product:attr:add', '', 2, 1, 1),
(10043, 1004, '属性修改', '', '', 2, 'product:attr:edit', '', 3, 1, 1),
(10044, 1004, '属性删除', '', '', 2, 'product:attr:delete', '', 4, 1, 1);

-- 1005 SPU管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1005, 1000, 'SPU管理', 'spu', 'product/spu/index', 1, NULL, 'goods', 5, 1, 1),
(10051, 1005, 'SPU查询', '', '', 2, 'product:spu:list', '', 1, 1, 1),
(10052, 1005, 'SPU新增', '', '', 2, 'product:spu:add', '', 2, 1, 1),
(10053, 1005, 'SPU修改', '', '', 2, 'product:spu:edit', '', 3, 1, 1),
(10054, 1005, 'SPU删除', '', '', 2, 'product:spu:delete', '', 4, 1, 1),
(10055, 1005, 'SPU上架', '', '', 2, 'product:spu:publish', '', 5, 1, 1);

-- 1006 SKU管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1006, 1000, 'SKU管理', 'sku', 'product/sku/index', 1, NULL, 'shopping-bag', 6, 1, 1),
(10061, 1006, 'SKU查询', '', '', 2, 'product:sku:list', '', 1, 1, 1),
(10062, 1006, 'SKU新增', '', '', 2, 'product:sku:add', '', 2, 1, 1),
(10063, 1006, 'SKU修改', '', '', 2, 'product:sku:edit', '', 3, 1, 1),
(10064, 1006, 'SKU删除', '', '', 2, 'product:sku:delete', '', 4, 1, 1);

-- 1007 审核日志
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(1007, 1000, '审核日志', 'audit', 'product/audit/index', 1, NULL, 'document', 7, 1, 1),
(10071, 1007, '审核查询', '', '', 2, 'product:audit:list', '', 1, 1, 1);

-- 会员管理模块（2000）
-- 2001 会员列表
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(2001, 2000, '会员列表', 'member', 'member/member/index', 1, NULL, 'peoples', 1, 1, 1),
(20011, 2001, '会员查询', '', '', 2, 'member:member:list', '', 1, 1, 1),
(20012, 2001, '会员修改', '', '', 2, 'member:member:edit', '', 2, 1, 1),
(20013, 2001, '会员删除', '', '', 2, 'member:member:delete', '', 3, 1, 1);

-- 2002 会员等级
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(2002, 2000, '会员等级', 'level', 'member/level/index', 1, NULL, 'level', 2, 1, 1),
(20021, 2002, '等级查询', '', '', 2, 'member:level:list', '', 1, 1, 1),
(20022, 2002, '等级新增', '', '', 2, 'member:level:add', '', 2, 1, 1),
(20023, 2002, '等级修改', '', '', 2, 'member:level:edit', '', 3, 1, 1),
(20024, 2002, '等级删除', '', '', 2, 'member:level:delete', '', 4, 1, 1);

-- 2003 登录日志
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(2003, 2000, '登录日志', 'login-log', 'member/loginLog/index', 1, NULL, 'login-log', 3, 1, 1),
(20031, 2003, '日志查询', '', '', 2, 'member:loginLog:list', '', 1, 1, 1);

-- 2004 积分记录
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(2004, 2000, '积分记录', 'integration', 'member/integration/index', 1, NULL, 'money', 4, 1, 1),
(20041, 2004, '积分查询', '', '', 2, 'member:integration:list', '', 1, 1, 1);

-- 2005 成长记录
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(2005, 2000, '成长记录', 'growth', 'member/growth/index', 1, NULL, 'chart', 5, 1, 1),
(20051, 2005, '成长查询', '', '', 2, 'member:growth:list', '', 1, 1, 1);

-- 2006 客服工单
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(2006, 2000, '客服工单', 'customer-service', 'member/customerService/index', 1, NULL, 'message', 6, 1, 1),
(20061, 2006, '工单查询', '', '', 2, 'member:customerService:list', '', 1, 1, 1),
(20062, 2006, '工单处理', '', '', 2, 'member:customerService:handle', '', 2, 1, 1);

-- 订单管理模块（3000）
-- 3001 订单列表
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(3001, 3000, '订单列表', 'order', 'order/order/index', 1, NULL, 'form', 1, 1, 1),
(30011, 3001, '订单查询', '', '', 2, 'order:order:list', '', 1, 1, 1),
(30012, 3001, '订单修改', '', '', 2, 'order:order:edit', '', 2, 1, 1);

-- 3002 支付记录
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(3002, 3000, '支付记录', 'payment', 'order/payment/index', 1, NULL, 'money', 2, 1, 1),
(30021, 3002, '支付查询', '', '', 2, 'order:payment:list', '', 1, 1, 1);

-- 3003 退货申请
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(3003, 3000, '退货申请', 'return-apply', 'order/returnApply/index', 1, NULL, 'return', 3, 1, 1),
(30031, 3003, '退货查询', '', '', 2, 'order:returnApply:list', '', 1, 1, 1),
(30032, 3003, '退货处理', '', '', 2, 'order:returnApply:handle', '', 2, 1, 1);

-- 3004 退货原因
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(3004, 3000, '退货原因', 'return-reason', 'order/returnReason/index', 1, NULL, 'question', 4, 1, 1),
(30041, 3004, '原因查询', '', '', 2, 'order:returnReason:list', '', 1, 1, 1),
(30042, 3004, '原因新增', '', '', 2, 'order:returnReason:add', '', 2, 1, 1),
(30043, 3004, '原因修改', '', '', 2, 'order:returnReason:edit', '', 3, 1, 1),
(30044, 3004, '原因删除', '', '', 2, 'order:returnReason:delete', '', 4, 1, 1);

-- 3005 订单设置
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(3005, 3000, '订单设置', 'setting', 'order/setting/index', 1, NULL, 'edit', 5, 1, 1),
(30051, 3005, '设置修改', '', '', 2, 'order:setting:edit', '', 1, 1, 1);

-- 营销管理模块（4000）
-- 4001 优惠券
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(4001, 4000, '优惠券', 'coupon', 'marketing/coupon/index', 1, NULL, 'ticket', 1, 1, 1),
(40011, 4001, '优惠券查询', '', '', 2, 'marketing:coupon:list', '', 1, 1, 1),
(40012, 4001, '优惠券新增', '', '', 2, 'marketing:coupon:add', '', 2, 1, 1),
(40013, 4001, '优惠券修改', '', '', 2, 'marketing:coupon:edit', '', 3, 1, 1),
(40014, 4001, '优惠券删除', '', '', 2, 'marketing:coupon:delete', '', 4, 1, 1);

-- 4002 优惠券记录
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(4002, 4000, '优惠券记录', 'coupon-history', 'marketing/coupon/history', 1, NULL, 'time', 2, 1, 1),
(40021, 4002, '记录查询', '', '', 2, 'marketing:coupon:history:list', '', 1, 1, 1);

-- 4003 秒杀场次
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(4003, 4000, '秒杀场次', 'seckill-session', 'marketing/seckill/session', 1, NULL, 'calendar', 3, 1, 1),
(40031, 4003, '场次查询', '', '', 2, 'marketing:seckill:session:list', '', 1, 1, 1),
(40032, 4003, '场次新增', '', '', 2, 'marketing:seckill:session:add', '', 2, 1, 1),
(40033, 4003, '场次修改', '', '', 2, 'marketing:seckill:session:edit', '', 3, 1, 1),
(40034, 4003, '场次删除', '', '', 2, 'marketing:seckill:session:delete', '', 4, 1, 1);

-- 4004 秒杀商品
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(4004, 4000, '秒杀商品', 'seckill-sku', 'marketing/seckill/skuBinding', 1, NULL, 'shopping-full', 4, 1, 1),
(40041, 4004, '商品查询', '', '', 2, 'marketing:seckill:sku:list', '', 1, 1, 1),
(40042, 4004, '商品新增', '', '', 2, 'marketing:seckill:sku:add', '', 2, 1, 1),
(40043, 4004, '商品修改', '', '', 2, 'marketing:seckill:sku:edit', '', 3, 1, 1),
(40044, 4004, '商品删除', '', '', 2, 'marketing:seckill:sku:delete', '', 4, 1, 1);

-- 4005 秒杀订单
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(4005, 4000, '秒杀订单', 'seckill-order', 'marketing/seckill/order', 1, NULL, 'order', 5, 1, 1),
(40051, 4005, '订单查询', '', '', 2, 'marketing:seckill:order:list', '', 1, 1, 1);

-- 4006 首页广告
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(4006, 4000, '首页广告', 'home-adv', 'marketing/homeAdv/index', 1, NULL, 'picture', 6, 1, 1),
(40061, 4006, '广告查询', '', '', 2, 'marketing:homeAdv:list', '', 1, 1, 1),
(40062, 4006, '广告新增', '', '', 2, 'marketing:homeAdv:add', '', 2, 1, 1),
(40063, 4006, '广告修改', '', '', 2, 'marketing:homeAdv:edit', '', 3, 1, 1),
(40064, 4006, '广告删除', '', '', 2, 'marketing:homeAdv:delete', '', 4, 1, 1);

-- 4007 首页专题
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(4007, 4000, '首页专题', 'home-subject', 'marketing/homeSubject/index', 1, NULL, 'star', 7, 1, 1),
(40071, 4007, '专题查询', '', '', 2, 'marketing:homeSubject:list', '', 1, 1, 1),
(40072, 4007, '专题新增', '', '', 2, 'marketing:homeSubject:add', '', 2, 1, 1),
(40073, 4007, '专题修改', '', '', 2, 'marketing:homeSubject:edit', '', 3, 1, 1),
(40074, 4007, '专题删除', '', '', 2, 'marketing:homeSubject:delete', '', 4, 1, 1);

-- 仓储管理模块（5000）
-- 5001 仓库管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(5001, 5000, '仓库管理', 'warehouse', 'ware/warehouse/index', 1, NULL, 'warehouse', 1, 1, 1),
(50011, 5001, '仓库查询', '', '', 2, 'ware:warehouse:list', '', 1, 1, 1),
(50012, 5001, '仓库新增', '', '', 2, 'ware:warehouse:add', '', 2, 1, 1),
(50013, 5001, '仓库修改', '', '', 2, 'ware:warehouse:edit', '', 3, 1, 1),
(50014, 5001, '仓库删除', '', '', 2, 'ware:warehouse:delete', '', 4, 1, 1);

-- 5002 库存管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(5002, 5000, '库存管理', 'stock', 'ware/stock/index', 1, NULL, 'stock', 2, 1, 1),
(50021, 5002, '库存查询', '', '', 2, 'ware:stock:list', '', 1, 1, 1),
(50022, 5002, '库存修改', '', '', 2, 'ware:stock:edit', '', 2, 1, 1);

-- 5003 采购管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(5003, 5000, '采购管理', 'purchase', 'ware/purchase/index', 1, NULL, 'shopping-cart', 3, 1, 1),
(50031, 5003, '采购查询', '', '', 2, 'ware:purchase:list', '', 1, 1, 1),
(50032, 5003, '采购新增', '', '', 2, 'ware:purchase:add', '', 2, 1, 1),
(50033, 5003, '采购修改', '', '', 2, 'ware:purchase:edit', '', 3, 1, 1),
(50034, 5003, '采购删除', '', '', 2, 'ware:purchase:delete', '', 4, 1, 1);

-- 5004 库存日志
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(5004, 5000, '库存日志', 'stock-log', 'ware/stockLog/index', 1, NULL, 'log', 4, 1, 1),
(50041, 5004, '日志查询', '', '', 2, 'ware:stockLog:list', '', 1, 1, 1);

-- 系统管理模块（6000）
-- 6001 用户管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(6001, 6000, '用户管理', 'user', 'system/user/index', 1, NULL, 'user', 1, 1, 1),
(60011, 6001, '用户查询', '', '', 2, 'system:user:list', '', 1, 1, 1),
(60012, 6001, '用户新增', '', '', 2, 'system:user:add', '', 2, 1, 1),
(60013, 6001, '用户修改', '', '', 2, 'system:user:edit', '', 3, 1, 1),
(60014, 6001, '用户删除', '', '', 2, 'system:user:delete', '', 4, 1, 1),
(60015, 6001, '重置密码', '', '', 2, 'system:user:resetPwd', '', 5, 1, 1);

-- 6002 角色管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(6002, 6000, '角色管理', 'role', 'system/role/index', 1, NULL, 'peoples', 2, 1, 1),
(60021, 6002, '角色查询', '', '', 2, 'system:role:list', '', 1, 1, 1),
(60022, 6002, '角色新增', '', '', 2, 'system:role:add', '', 2, 1, 1),
(60023, 6002, '角色修改', '', '', 2, 'system:role:edit', '', 3, 1, 1),
(60024, 6002, '角色删除', '', '', 2, 'system:role:delete', '', 4, 1, 1);

-- 6003 菜单管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(6003, 6000, '菜单管理', 'menu', 'system/menu/index', 1, NULL, 'tree-table', 3, 1, 1),
(60031, 6003, '菜单查询', '', '', 2, 'system:menu:list', '', 1, 1, 1),
(60032, 6003, '菜单新增', '', '', 2, 'system:menu:add', '', 2, 1, 1),
(60033, 6003, '菜单修改', '', '', 2, 'system:menu:edit', '', 3, 1, 1),
(60034, 6003, '菜单删除', '', '', 2, 'system:menu:delete', '', 4, 1, 1);

-- 6004 部门管理
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(6004, 6000, '部门管理', 'dept', 'system/dept/index', 1, NULL, 'tree', 4, 1, 1),
(60041, 6004, '部门查询', '', '', 2, 'system:dept:list', '', 1, 1, 1),
(60042, 6004, '部门新增', '', '', 2, 'system:dept:add', '', 2, 1, 1),
(60043, 6004, '部门修改', '', '', 2, 'system:dept:edit', '', 3, 1, 1),
(60044, 6004, '部门删除', '', '', 2, 'system:dept:delete', '', 4, 1, 1);

-- 6005 操作日志
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `path`, `component`, `menu_type`, `perms`, `icon`, `sort`, `visible`, `status`) VALUES
(6005, 6000, '操作日志', 'oper-log', 'system/operLog/index', 1, NULL, 'log', 5, 1, 1),
(60051, 6005, '日志查询', '', '', 2, 'system:operLog:list', '', 1, 1, 1);

-- ----------------------------
-- 初始化角色菜单关联数据
-- ----------------------------

-- 商品管理员（role_id=2）：拥有商品管理模块的所有权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
-- 商品管理目录
(2, 1000),
-- 分类管理及按钮
(2, 1001), (2, 10011), (2, 10012), (2, 10013), (2, 10014),
-- 品牌管理及按钮
(2, 1002), (2, 10021), (2, 10022), (2, 10023), (2, 10024),
-- 属性分组及按钮
(2, 1003), (2, 10031), (2, 10032), (2, 10033), (2, 10034),
-- 属性管理及按钮
(2, 1004), (2, 10041), (2, 10042), (2, 10043), (2, 10044),
-- SPU管理及按钮
(2, 1005), (2, 10051), (2, 10052), (2, 10053), (2, 10054), (2, 10055),
-- SKU管理及按钮
(2, 1006), (2, 10061), (2, 10062), (2, 10063), (2, 10064),
-- 审核日志及按钮
(2, 1007), (2, 10071);

-- 订单管理员（role_id=3）：拥有订单管理模块的所有权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
-- 订单管理目录
(3, 3000),
-- 订单列表及按钮
(3, 3001), (3, 30011), (3, 30012),
-- 支付记录及按钮
(3, 3002), (3, 30021),
-- 退货申请及按钮
(3, 3003), (3, 30031), (3, 30032),
-- 退货原因及按钮
(3, 3004), (3, 30041), (3, 30042), (3, 30043), (3, 30044),
-- 订单设置及按钮
(3, 3005), (3, 30051);

-- 超级管理员（role_id=1）：拥有所有菜单权限（这里为了简洁，不列出所有菜单，实际应用中应该包含所有菜单ID）

-- ========================================
-- 数据库初始化完成
-- 默认管理员账号：admin
-- 默认管理员密码：admin123
-- ========================================

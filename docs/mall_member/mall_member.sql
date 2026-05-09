-- ============================================================
-- Mall-HJ 会员库 DDL
-- Database: mall_member
-- Charset:  utf8mb4 / Collate: utf8mb4_general_ci
-- Engine:   InnoDB
-- ============================================================

CREATE DATABASE IF NOT EXISTS `mall_member`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `mall_member`;

-- ============================================================
-- 1. 会员等级表
-- ============================================================
CREATE TABLE `ums_member_level` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(64)  NOT NULL COMMENT '等级名称',
    `growth_point`  INT          NOT NULL DEFAULT 0 COMMENT '达到该等级所需成长值',
    `default_status`TINYINT      NOT NULL DEFAULT 0 COMMENT '是否默认等级 0否 1是',
    `free_freight`  DECIMAL(10,2) NULL COMMENT '免运费门槛（元）',
    `comment_extra_point` TINYINT NULL COMMENT '评价额外送积分倍数',
    `discount`      DECIMAL(3,2)  NULL COMMENT '该等级享受折扣（如 0.95 = 95折）',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='会员等级';

-- ============================================================
-- 2. 会员主表
-- ============================================================
CREATE TABLE `ums_member` (
    `id`            BIGINT       NOT NULL COMMENT '主键（雪花ID）',
    `level_id`      BIGINT       NULL     COMMENT '会员等级ID',
    `username`      VARCHAR(64)  NULL     COMMENT '用户名',
    `nickname`      VARCHAR(64)  NULL     COMMENT '昵称',
    `phone`         VARCHAR(20)  NULL     COMMENT '手机号',
    `email`         VARCHAR(128) NULL     COMMENT '邮箱',
    `avatar`        VARCHAR(255) NULL     COMMENT '头像URL',
    `gender`        TINYINT      NOT NULL DEFAULT 0 COMMENT '性别 0未知 1男 2女',
    `birthday`      DATE         NULL     COMMENT '生日',
    `integration`   INT          NOT NULL DEFAULT 0 COMMENT '当前积分',
    `growth`        INT          NOT NULL DEFAULT 0 COMMENT '当前成长值',
    -- 画像字段（推荐系统用）
    `age_group`     VARCHAR(16)  NULL     COMMENT '年龄段: UNDER_18/18_25/26_35/36_45/OVER_45',
    `occupation`    VARCHAR(32)  NULL     COMMENT '职业: STUDENT/OFFICE_WORKER/FREELANCER/OTHER',
    `price_sensitivity` TINYINT  NULL     COMMENT '价格敏感度 1低 2中 3高',
    `favorite_categories` VARCHAR(255) NULL COMMENT '偏好分类ID（逗号分隔）',
    `shopping_preference` VARCHAR(32) NULL COMMENT '购物偏好: QUALITY/COST_EFFECTIVE/BRAND_ORIENTED',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1正常',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_level_id` (`level_id`),
    KEY `idx_age_group` (`age_group`),
    KEY `idx_shopping_preference` (`shopping_preference`)
) ENGINE=InnoDB COMMENT='会员';

-- ============================================================
-- 3. 会员认证绑定表
-- ============================================================
CREATE TABLE `ums_member_auth` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '关联会员ID',
    `identity_type` VARCHAR(16)  NOT NULL COMMENT '认证类型: USERNAME/PHONE/WECHAT/ALIPAY',
    `identifier`    VARCHAR(128) NOT NULL COMMENT '身份标识（用户名/手机号/openId）',
    `credential`    VARCHAR(255) NULL     COMMENT '凭证（BCrypt密码hash，OAuth类为空）',
    `verified`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0未验证 1已验证',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type_identifier` (`identity_type`, `identifier`),
    KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB COMMENT='会员认证绑定';

-- ============================================================
-- 4. 收货地址表
-- ============================================================
CREATE TABLE `ums_member_receive_address` (
    `id`              BIGINT       NOT NULL COMMENT '主键',
    `member_id`       BIGINT       NOT NULL COMMENT '关联会员ID',
    `name`            VARCHAR(64)  NOT NULL COMMENT '收货人姓名',
    `phone`           VARCHAR(20)  NOT NULL COMMENT '收货人电话',
    `province`        VARCHAR(16)  NOT NULL COMMENT '省',
    `city`            VARCHAR(16)  NOT NULL COMMENT '市',
    `district`        VARCHAR(16)  NOT NULL COMMENT '区',
    `detail_address`  VARCHAR(255) NOT NULL COMMENT '详细地址',
    `postal_code`     VARCHAR(10)  NULL     COMMENT '邮编',
    `default_status`  TINYINT      NOT NULL DEFAULT 0 COMMENT '0非默认 1默认',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB COMMENT='会员收货地址';

-- ============================================================
-- 5. 用户标签表
-- ============================================================
CREATE TABLE `ums_member_tag` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(64)  NOT NULL COMMENT '标签名称',
    `tag_type`      VARCHAR(32)  NOT NULL COMMENT '标签类型: DEMOGRAPHIC/BEHAVIOR/PREFERENCE/CUSTOM',
    `description`   VARCHAR(255) NULL     COMMENT '标签描述',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='用户标签定义';

-- ============================================================
-- 5.2 用户-标签关联表
-- ============================================================
CREATE TABLE `ums_member_tag_relation` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '会员ID',
    `tag_id`        BIGINT       NOT NULL COMMENT '标签ID',
    `source`        VARCHAR(32)  NULL     COMMENT '来源: AUTO(系统自动)/MANUAL(人工)',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_tag` (`member_id`, `tag_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB COMMENT='用户-标签关联';

-- ============================================================
-- 6. 登录日志表
-- ============================================================
CREATE TABLE `ums_member_login_log` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '会员ID',
    `login_type`    VARCHAR(16)  NOT NULL COMMENT '登录方式: USERNAME/PHONE/WECHAT/ALIPAY',
    `ip`            VARCHAR(64)  NULL     COMMENT '登录IP',
    `city`          VARCHAR(64)  NULL     COMMENT 'IP所在城市',
    `user_agent`    VARCHAR(512) NULL     COMMENT '浏览器UA',
    `os`            VARCHAR(64)  NULL     COMMENT '操作系统',
    `browser`       VARCHAR(64)  NULL     COMMENT '浏览器',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0失败 1成功',
    `remark`        VARCHAR(255) NULL     COMMENT '备注（失败原因等）',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='会员登录日志';

-- ============================================================
-- 6. 积分流水表
-- ============================================================
CREATE TABLE `ums_integration_record` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '会员ID',
    `change_amount` INT          NOT NULL COMMENT '变动数量（正数增加，负数扣减）',
    `current_amount`INT          NOT NULL COMMENT '变动后积分',
    `source_type`   VARCHAR(32)  NOT NULL COMMENT '来源类型: ORDER/COMMENT/SIGN_IN/ADMIN',
    `source_id`     BIGINT       NULL     COMMENT '关联业务ID（订单ID等）',
    `remark`        VARCHAR(255) NULL     COMMENT '备注',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='积分流水';

-- ============================================================
-- 7. 成长值流水表
-- ============================================================
CREATE TABLE `ums_growth_record` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '会员ID',
    `change_amount` INT          NOT NULL COMMENT '变动数量',
    `current_amount`INT          NOT NULL COMMENT '变动后成长值',
    `source_type`   VARCHAR(32)  NOT NULL COMMENT '来源类型: ORDER/COMMENT/SIGN_IN/ADMIN',
    `source_id`     BIGINT       NULL     COMMENT '关联业务ID',
    `remark`        VARCHAR(255) NULL     COMMENT '备注',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='成长值流水';

-- ============================================================
-- 8. OAuth2 客户端注册表
-- ============================================================
CREATE TABLE `sys_client` (
    `id`                          BIGINT       NOT NULL COMMENT '主键',
    `client_id`                   VARCHAR(64)  NOT NULL COMMENT '客户端标识',
    `client_secret`               VARCHAR(255) NOT NULL COMMENT '客户端密钥（BCrypt）',
    `client_name`                 VARCHAR(128) NOT NULL COMMENT '应用名称',
    `client_authentication_method`VARCHAR(32)  NOT NULL DEFAULT 'client_secret_basic' COMMENT '认证方式',
    `authorization_grant_types`   VARCHAR(255) NOT NULL COMMENT '授权方式（逗号分隔）',
    `redirect_uris`               VARCHAR(512) NULL     COMMENT '回调地址（逗号分隔）',
    `scopes`                      VARCHAR(255) NOT NULL COMMENT '权限范围（逗号分隔）',
    `access_token_ttl`            INT          NOT NULL DEFAULT 7200 COMMENT 'access_token有效期（秒）',
    `refresh_token_ttl`           INT          NOT NULL DEFAULT 2592000 COMMENT 'refresh_token有效期（秒）',
    `status`                      TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`                 DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_client_id` (`client_id`)
) ENGINE=InnoDB COMMENT='OAuth2客户端注册';

-- ============================================================
-- 9. OAuth2 授权记录表
-- ============================================================
CREATE TABLE `sys_oauth2_authorization` (
    `id`                            BIGINT       NOT NULL COMMENT '主键',
    `member_id`                     BIGINT       NOT NULL COMMENT '授权用户ID',
    `client_id`                     VARCHAR(64)  NOT NULL COMMENT '客户端标识',
    `grant_type`                    VARCHAR(32)  NOT NULL COMMENT '授权类型',
    `authorization_code_value`      VARCHAR(255) NULL     COMMENT '授权码',
    `authorization_code_issued_at`  DATETIME     NULL     COMMENT '授权码签发时间',
    `authorization_code_expires_at` DATETIME     NULL     COMMENT '授权码过期时间',
    `access_token_value`            TEXT         NULL     COMMENT 'access_token（JWT）',
    `access_token_issued_at`        DATETIME     NULL     COMMENT '签发时间',
    `access_token_expires_at`       DATETIME     NULL     COMMENT '过期时间',
    `refresh_token_value`           VARCHAR(255) NULL     COMMENT 'refresh_token',
    `refresh_token_issued_at`       DATETIME     NULL     COMMENT '签发时间',
    `refresh_token_expires_at`      DATETIME     NULL     COMMENT '过期时间',
    `scopes`                        VARCHAR(255) NULL     COMMENT '授权的权限范围',
    `state`                         VARCHAR(255) NULL     COMMENT 'CSRF state',
    `redirect_uri`                  VARCHAR(512) NULL     COMMENT '回调地址',
    `create_time`                   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`                   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_auth_code` (`authorization_code_value`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_client_id` (`client_id`),
    KEY `idx_access_token_expires` (`access_token_expires_at`)
) ENGINE=InnoDB COMMENT='OAuth2授权记录';

-- ============================================================
-- 10. OAuth2 用户授权同意表
-- ============================================================
CREATE TABLE `sys_oauth2_consent` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '用户ID',
    `client_id`     VARCHAR(64)  NOT NULL COMMENT '客户端标识',
    `scopes`        VARCHAR(255) NOT NULL COMMENT '同意的权限范围',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_client` (`member_id`, `client_id`)
) ENGINE=InnoDB COMMENT='OAuth2用户授权同意';

-- ============================================================
-- 初始数据
-- ============================================================

-- 会员等级
INSERT INTO `ums_member_level` (`id`, `name`, `growth_point`, `default_status`, `free_freight`, `comment_extra_point`, `discount`) VALUES
(1, '普通会员',   0,     1, NULL,   NULL, NULL),
(2, '银卡会员',   1000,  0, 99.00,  1,    0.98),
(3, '金卡会员',   5000,  0, 59.00,  2,    0.95),
(4, '铂金会员',   15000, 0, 0.00,   3,    0.90),
(5, '钻石会员',   50000, 0, 0.00,   5,    0.85);

-- 用户标签
INSERT INTO `ums_member_tag` (`id`, `name`, `tag_type`, `description`) VALUES
(1,  '高频用户',     'BEHAVIOR',  '月活跃天数>20'),
(2,  '低频用户',     'BEHAVIOR',  '月活跃天数<5'),
(3,  '高消费用户',   'BEHAVIOR',  '累计消费>10000'),
(4,  '价格敏感',     'PREFERENCE','偏好促销/优惠券'),
(5,  '品质导向',     'PREFERENCE','偏好高评分商品'),
(6,  '新品偏好',     'BEHAVIOR',  '常购买新品'),
(7,  '母婴人群',     'DEMOGRAPHIC','有母婴相关购买记录'),
(8,  '数码爱好者',   'PREFERENCE', '偏好数码3C类商品'),
(9,  '美妆达人',     'PREFERENCE', '偏好美妆个护类商品'),
(10, '品牌忠诚',     'BEHAVIOR',  '复购同一品牌>3次');

-- OAuth2 客户端（client_secret 需要 BCrypt 加密后替换，此处为明文占位）
INSERT INTO `sys_client` (`id`, `client_id`, `client_secret`, `client_name`, `client_authentication_method`, `authorization_grant_types`, `redirect_uris`, `scopes`, `access_token_ttl`, `refresh_token_ttl`) VALUES
(1, 'mall-web',  '{bcrypt}$2a$10$PLACEHOLDER', '商城PC端', 'client_secret_basic', 'authorization_code,refresh_token', 'http://localhost:3000/callback,http://localhost:8080/callback', 'read,write', 7200, 2592000),
(2, 'mall-app',  '{bcrypt}$2a$10$PLACEHOLDER', '商城APP',  'client_secret_basic', 'authorization_code,refresh_token', 'mall://callback', 'read,write', 7200, 2592000);

-- ============================================================
-- 11. 会员统计信息表
-- ============================================================
CREATE TABLE `ums_member_statistics_info` (
    `id`                     BIGINT       NOT NULL COMMENT '主键',
    `member_id`              BIGINT       NOT NULL COMMENT '会员ID',
    `consume_amount`         DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计消费金额',
    `coupon_amount`          DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '累计优惠金额',
    `order_count`            INT          NOT NULL DEFAULT 0 COMMENT '订单数量',
    `coupon_count`           INT          NOT NULL DEFAULT 0 COMMENT '已使用优惠券数量',
    `comment_count`          INT          NOT NULL DEFAULT 0 COMMENT '评价数量',
    `return_order_count`     INT          NOT NULL DEFAULT 0 COMMENT '退货数量',
    `login_count`            INT          NOT NULL DEFAULT 0 COMMENT '登录次数',
    `attend_count`           INT          NOT NULL DEFAULT 0 COMMENT '签到次数',
    `fans_count`             INT          NOT NULL DEFAULT 0 COMMENT '粉丝数量',
    `collect_product_count`  INT          NOT NULL DEFAULT 0 COMMENT '收藏的商品数量',
    `collect_subject_count`  INT          NOT NULL DEFAULT 0 COMMENT '收藏的专题数量',
    `create_time`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`            DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_id` (`member_id`)
) ENGINE=InnoDB COMMENT='会员统计信息';

-- ============================================================
-- 12. 会员收藏商品表
-- ============================================================
CREATE TABLE `ums_member_collect_spu` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '会员ID',
    `spu_id`        BIGINT       NOT NULL COMMENT 'SPU ID',
    `spu_name`      VARCHAR(256) NULL     COMMENT '商品名称（冗余字段）',
    `spu_img`       VARCHAR(255) NULL     COMMENT '商品图片（冗余字段）',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_spu` (`member_id`, `spu_id`),
    KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB COMMENT='会员收藏商品';

-- ============================================================
-- 13. 会员收藏专题表
-- ============================================================
CREATE TABLE `ums_member_collect_subject` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '会员ID',
    `subject_id`    BIGINT       NOT NULL COMMENT '专题ID',
    `subject_name`  VARCHAR(128) NULL     COMMENT '专题名称',
    `subject_img`   VARCHAR(255) NULL     COMMENT '专题图片',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_subject` (`member_id`, `subject_id`),
    KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB COMMENT='会员收藏专题';

-- ============================================================
-- 14. 客服工单表
-- ============================================================
CREATE TABLE `ums_customer_service_ticket` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '会员ID',
    `member_name`   VARCHAR(64)  NULL     COMMENT '会员名称',
    `title`         VARCHAR(128) NOT NULL COMMENT '工单标题',
    `content`       TEXT         NULL     COMMENT '工单内容',
    `ticket_type`   TINYINT      NOT NULL DEFAULT 0 COMMENT '工单类型 0咨询 1投诉 2售后 3其他',
    `order_sn`      VARCHAR(64)  NULL     COMMENT '关联订单号',
    `sku_id`        BIGINT       NULL     COMMENT '关联商品SKU ID',
    `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '工单状态 0待处理 1处理中 2已解决 3已关闭',
    `handler`       VARCHAR(64)  NULL     COMMENT '处理人',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='客服工单';

-- ============================================================
-- 15. 客服消息表
-- ============================================================
CREATE TABLE `ums_customer_service_message` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `ticket_id`     BIGINT       NOT NULL COMMENT '工单ID',
    `sender_type`   TINYINT      NOT NULL COMMENT '发送者类型 1会员 2客服',
    `sender_name`   VARCHAR(64)  NULL     COMMENT '发送者名称',
    `content`       TEXT         NOT NULL COMMENT '消息内容',
    `attachments`   VARCHAR(1024) NULL    COMMENT '附件URL（逗号分隔）',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_ticket_id` (`ticket_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='客服消息';

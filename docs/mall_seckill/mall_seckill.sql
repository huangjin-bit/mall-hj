-- ============================================================
-- Mall-HJ 秒杀库 DDL
-- Database: mall_seckill
-- Charset:  utf8mb4 / Collate: utf8mb4_general_ci
-- Engine:   InnoDB
-- ============================================================

CREATE DATABASE IF NOT EXISTS `mall_seckill`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `mall_seckill`;

-- ============================================================
-- 1. 秒杀活动场次表
-- ============================================================
CREATE TABLE `sms_seckill_session` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(128) NOT NULL COMMENT '场次名称（如"10点场""20点场"）',
    `start_time`    DATETIME     NOT NULL COMMENT '开始时间',
    `end_time`      DATETIME     NOT NULL COMMENT '结束时间',
    `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '0未开始 1进行中 2已结束',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='秒杀活动场次';

-- ============================================================
-- 2. 秒杀活动商品关联表
-- ============================================================
CREATE TABLE `sms_seckill_sku_relation` (
    `id`                BIGINT        NOT NULL COMMENT '主键',
    `session_id`        BIGINT        NOT NULL COMMENT '关联场次ID',
    `sku_id`            BIGINT        NOT NULL COMMENT 'SKU ID',
    `spu_id`            BIGINT        NOT NULL COMMENT 'SPU ID',
    `seckill_price`     DECIMAL(10,2) NOT NULL COMMENT '秒杀价',
    `original_price`    DECIMAL(10,2) NOT NULL COMMENT '原价',
    `seckill_stock`     INT           NOT NULL COMMENT '秒杀库存数量',
    `seckill_limit`     INT           NOT NULL DEFAULT 1 COMMENT '每人限购数量',
    `sort`              INT           NOT NULL DEFAULT 0 COMMENT '排序',
    `status`            TINYINT       NOT NULL DEFAULT 0 COMMENT '0未上架 1已上架',
    `create_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB COMMENT='秒杀活动商品关联';

-- ============================================================
-- 3. 秒杀订单表（快速下单，关联正式订单）
-- ============================================================
CREATE TABLE `sms_seckill_order` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `member_id`     BIGINT       NOT NULL COMMENT '用户ID',
    `session_id`    BIGINT       NOT NULL COMMENT '场次ID',
    `sku_id`        BIGINT       NOT NULL COMMENT 'SKU ID',
    `order_id`      BIGINT       NULL     COMMENT '关联正式订单ID（oms_order.id）',
    `order_sn`      VARCHAR(64)  NULL     COMMENT '关联正式订单编号',
    `seckill_price` DECIMAL(10,2) NOT NULL COMMENT '秒杀价',
    `quantity`      INT          NOT NULL DEFAULT 1 COMMENT '购买数量',
    `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '0待支付 1已支付 2已取消 3超时关闭',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_session_sku` (`member_id`, `session_id`, `sku_id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB COMMENT='秒杀订单';

-- ============================================================
-- 4. 优惠券表
-- ============================================================
CREATE TABLE `sms_coupon` (
    `id`                BIGINT        NOT NULL COMMENT '主键',
    `coupon_type`       TINYINT       NOT NULL COMMENT '1满减 2折扣 3无门槛',
    `name`              VARCHAR(128)  NOT NULL COMMENT '优惠券名称',
    `amount`            DECIMAL(10,2) NULL     COMMENT '优惠金额（满减/无门槛）',
    `min_point`         DECIMAL(10,2) NULL     COMMENT '使用门槛金额（满X元可用）',
    `discount`          DECIMAL(3,2)  NULL     COMMENT '折扣率（如0.85=85折）',
    `max_discount_amount` DECIMAL(10,2) NULL   COMMENT '最大优惠金额（折扣券封顶）',
    `total_count`       INT           NOT NULL COMMENT '发行总量',
    `received_count`    INT           NOT NULL DEFAULT 0 COMMENT '已领取数量',
    `used_count`        INT           NOT NULL DEFAULT 0 COMMENT '已使用数量',
    `per_limit`         INT           NOT NULL DEFAULT 1 COMMENT '每人限领数量',
    `start_time`        DATETIME      NOT NULL COMMENT '生效时间',
    `end_time`          DATETIME      NOT NULL COMMENT '失效时间',
    `status`            TINYINT       NOT NULL DEFAULT 0 COMMENT '0未开始 1进行中 2已结束',
    `create_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='优惠券';

-- ============================================================
-- 5. 优惠券领取记录表
-- ============================================================
CREATE TABLE `sms_coupon_history` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `coupon_id`     BIGINT       NOT NULL COMMENT '优惠券ID',
    `member_id`     BIGINT       NOT NULL COMMENT '领取用户ID',
    `order_sn`      VARCHAR(64)  NULL     COMMENT '使用的订单编号',
    `coupon_type`   TINYINT      NOT NULL COMMENT '1满减 2折扣 3无门槛',
    `amount`        DECIMAL(10,2) NULL    COMMENT '优惠金额',
    `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '0未使用 1已使用 2已过期',
    `use_time`      DATETIME     NULL     COMMENT '使用时间',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_coupon_id` (`coupon_id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='优惠券领取记录';

-- ============================================================
-- 6. 商品满减表
-- ============================================================
CREATE TABLE `sms_sku_full_reduction` (
    `id`                BIGINT        NOT NULL COMMENT '主键',
    `sku_id`            BIGINT        NOT NULL COMMENT 'SKU ID',
    `spu_id`            BIGINT        NOT NULL COMMENT 'SPU ID',
    `full_price`        DECIMAL(10,2) NOT NULL COMMENT '满X元',
    `reduce_price`      DECIMAL(10,2) NOT NULL COMMENT '减Y元',
    `status`            TINYINT       NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB COMMENT='商品满减';

-- ============================================================
-- 7. 商品阶梯价表
-- ============================================================
CREATE TABLE `sms_sku_ladder` (
    `id`                BIGINT        NOT NULL COMMENT '主键',
    `sku_id`            BIGINT        NOT NULL COMMENT 'SKU ID',
    `spu_id`            BIGINT        NOT NULL COMMENT 'SPU ID',
    `full_count`        INT           NOT NULL COMMENT '满X件',
    `discount`          DECIMAL(3,2)  NOT NULL COMMENT '打Y折（如0.95=95折）',
    `price`             DECIMAL(10,2) NOT NULL COMMENT '折后单价',
    `status`            TINYINT       NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB COMMENT='商品阶梯价';

-- ============================================================
-- 初始数据
-- ============================================================

-- 秒杀场次（示例：每日场次，需定时任务每天创建）
INSERT INTO `sms_seckill_session` (`id`, `name`, `start_time`, `end_time`, `status`) VALUES
(1, '10点场', '2026-05-04 10:00:00', '2026-05-04 12:00:00', 0),
(2, '14点场', '2026-05-04 14:00:00', '2026-05-04 16:00:00', 0),
(3, '20点场', '2026-05-04 20:00:00', '2026-05-04 22:00:00', 0);

-- ============================================================
-- 8. 首页广告表
-- ============================================================
CREATE TABLE `sms_home_adv` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(128) NOT NULL COMMENT '广告名称',
    `pic`           VARCHAR(255) NOT NULL COMMENT '图片地址',
    `url`           VARCHAR(255) NULL     COMMENT '点击跳转地址',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `type`          TINYINT      NOT NULL DEFAULT 0 COMMENT '0PC 1手机',
    `start_time`    DATETIME     NULL     COMMENT '广告开始时间',
    `end_time`      DATETIME     NULL     COMMENT '广告结束时间',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_sort` (`sort`)
) ENGINE=InnoDB COMMENT='首页广告';

-- 首页广告示例数据
INSERT INTO `sms_home_adv` (`id`, `name`, `pic`, `url`, `sort`, `status`, `type`, `start_time`, `end_time`) VALUES
(100001, '618大促Banner', 'https://example.com/banner1.jpg', '/activity/618', 1, 1, 0, '2026-05-01 00:00:00', '2026-06-30 23:59:59'),
(100002, '新机首发', 'https://example.com/banner2.jpg', '/product/new', 2, 1, 0, '2026-05-01 00:00:00', '2026-12-31 23:59:59'),
(100003, '手机端App下载', 'https://example.com/banner3.jpg', '/app/download', 3, 1, 1, '2026-05-01 00:00:00', '2026-12-31 23:59:59'),
(100004, '会员专享', 'https://example.com/banner4.jpg', '/member/vip', 4, 1, 0, '2026-05-01 00:00:00', '2026-12-31 23:59:59');

-- ============================================================
-- 9. 首页专题表
-- ============================================================
CREATE TABLE `sms_home_subject` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(128) NOT NULL COMMENT '专题名称',
    `title`         VARCHAR(256) NULL     COMMENT '专题标题',
    `sub_title`     VARCHAR(256) NULL     COMMENT '专题副标题',
    `url`           VARCHAR(255) NULL     COMMENT '详情页地址',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `img`           VARCHAR(255) NULL     COMMENT '专题封面图',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='首页专题';

-- 首页专题示例数据
INSERT INTO `sms_home_subject` (`id`, `name`, `title`, `sub_title`, `url`, `sort`, `img`, `status`) VALUES
(100001, '数码专区', '数码产品狂欢', '精选数码产品 低至5折', '/subject/digital', 1, 'https://example.com/subject1.jpg', 1),
(100002, '家电专场', '家电大促', '爆款家电 直降千元', '/subject/appliance', 2, 'https://example.com/subject2.jpg', 1),
(100003, '时尚穿搭', '春夏新品', '时尚穿搭 焕新衣橱', '/subject/fashion', 3, 'https://example.com/subject3.jpg', 1);

-- ============================================================
-- 10. 首页专题商品关联表
-- ============================================================
CREATE TABLE `sms_home_subject_spu` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `subject_id`    BIGINT       NOT NULL COMMENT '专题ID',
    `spu_id`        BIGINT       NOT NULL COMMENT 'SPU ID',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_subject_id` (`subject_id`),
    KEY `idx_spu_id` (`spu_id`)
) ENGINE=InnoDB COMMENT='首页专题商品关联';

-- ============================================================
-- 11. SPU积分成长表
-- ============================================================
CREATE TABLE `sms_spu_bounds` (
    `id`            BIGINT        NOT NULL COMMENT '主键',
    `spu_id`        BIGINT        NOT NULL COMMENT 'SPU ID',
    `buy_bounds`    DECIMAL(5,2)  NOT NULL DEFAULT 0 COMMENT '购买积分',
    `grow_bounds`   DECIMAL(5,2)  NOT NULL DEFAULT 0 COMMENT '成长积分',
    `work`          TINYINT       NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_spu_id` (`spu_id`)
) ENGINE=InnoDB COMMENT='SPU积分成长';

-- ============================================================
-- 12. 会员价格表
-- ============================================================
CREATE TABLE `sms_member_price` (
    `id`                BIGINT        NOT NULL COMMENT '主键',
    `sku_id`            BIGINT        NOT NULL COMMENT 'SKU ID',
    `member_level_id`   BIGINT        NOT NULL COMMENT '会员等级ID',
    `member_level_name` VARCHAR(64)   NULL     COMMENT '会员等级名（冗余）',
    `member_price`      DECIMAL(10,2) NOT NULL COMMENT '会员价',
    `add_other`         TINYINT       NOT NULL DEFAULT 1 COMMENT '是否可叠加其他优惠 0否 1是',
    `create_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_sku_id` (`sku_id`),
    KEY `idx_member_level_id` (`member_level_id`)
) ENGINE=InnoDB COMMENT='会员价格';

-- ============================================================
-- 13. 优惠券分类关联表
-- ============================================================
CREATE TABLE `sms_coupon_spu_category_relation` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `coupon_id`     BIGINT       NOT NULL COMMENT '优惠券ID',
    `category_id`   BIGINT       NOT NULL COMMENT '分类ID',
    `category_name` VARCHAR(64)  NULL     COMMENT '分类名（冗余）',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_coupon_id` (`coupon_id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB COMMENT='优惠券分类关联';

-- ============================================================
-- 14. 优惠券商品关联表
-- ============================================================
CREATE TABLE `sms_coupon_spu_relation` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `coupon_id`     BIGINT       NOT NULL COMMENT '优惠券ID',
    `spu_id`        BIGINT       NOT NULL COMMENT 'SPU ID',
    `spu_name`      VARCHAR(256) NULL     COMMENT 'SPU名（冗余）',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_coupon_id` (`coupon_id`),
    KEY `idx_spu_id` (`spu_id`)
) ENGINE=InnoDB COMMENT='优惠券商品关联';

-- ============================================================
-- 15. 秒杀活动表
-- ============================================================
CREATE TABLE `sms_seckill_promotion` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `title`         VARCHAR(128) NOT NULL COMMENT '活动标题',
    `start_time`    DATETIME     NOT NULL COMMENT '开始时间',
    `end_time`      DATETIME     NOT NULL COMMENT '结束时间',
    `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '0未开始 1进行中 2已结束',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_start_time` (`start_time`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='秒杀活动';

-- ============================================================
-- 16. 秒杀商品到货通知表
-- ============================================================
CREATE TABLE `sms_seckill_sku_notice` (
    `id`             BIGINT       NOT NULL COMMENT '主键',
    `member_id`      BIGINT       NOT NULL COMMENT '会员ID',
    `sku_id`         BIGINT       NOT NULL COMMENT 'SKU ID',
    `session_id`     BIGINT       NOT NULL COMMENT '场次ID',
    `subscribe_time` DATETIME     NOT NULL COMMENT '订阅时间',
    `send_time`      DATETIME     NULL     COMMENT '发送时间',
    `notice_type`    TINYINT      NOT NULL DEFAULT 0 COMMENT '0短信 1推送 2邮件',
    `create_time`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_sku_id` (`sku_id`),
    KEY `idx_session_id` (`session_id`)
) ENGINE=InnoDB COMMENT='秒杀商品到货通知';

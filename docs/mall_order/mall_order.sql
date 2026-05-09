-- ============================================================
-- Mall-HJ 订单库 DDL
-- Database: mall_order
-- Charset:  utf8mb4 / Collate: utf8mb4_general_ci
-- Engine:   InnoDB
-- ============================================================

CREATE DATABASE IF NOT EXISTS `mall_order`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `mall_order`;

-- ============================================================
-- 1. 订单主表
-- ============================================================
CREATE TABLE `oms_order` (
    `id`                      BIGINT        NOT NULL COMMENT '主键',
    `order_sn`                VARCHAR(64)   NOT NULL COMMENT '订单编号（业务唯一）',
    `member_id`               BIGINT        NOT NULL COMMENT '下单用户ID',
    `coupon_id`               BIGINT        NULL     COMMENT '使用的优惠券ID',
    `total_amount`            DECIMAL(10,2) NOT NULL COMMENT '订单总额',
    `pay_amount`              DECIMAL(10,2) NOT NULL COMMENT '实付金额（=total-freight-promotion-coupon）',
    `freight_amount`          DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '运费',
    `promotion_amount`        DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '促销优惠金额',
    `coupon_amount`           DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '优惠券抵扣金额',
    `pay_type`                TINYINT       NULL     COMMENT '支付方式 1支付宝 2微信 3模拟',
    `status`                  TINYINT       NOT NULL DEFAULT 0 COMMENT '0待付款 1待发货 2待收货 3已完成 4已取消 5退款中 6已退款 7售后中 8已关闭',
    `order_type`              TINYINT       NOT NULL DEFAULT 0 COMMENT '0普通订单 1秒杀订单',
    `receiver_name`           VARCHAR(64)   NOT NULL COMMENT '收货人姓名',
    `receiver_phone`          VARCHAR(20)   NOT NULL COMMENT '收货人电话',
    `receiver_province`       VARCHAR(16)   NOT NULL COMMENT '省',
    `receiver_city`           VARCHAR(16)   NOT NULL COMMENT '市',
    `receiver_district`       VARCHAR(16)   NOT NULL COMMENT '区',
    `receiver_detail_address` VARCHAR(255)  NOT NULL COMMENT '详细地址',
    `pay_time`                DATETIME      NULL     COMMENT '支付时间',
    `delivery_time`           DATETIME      NULL     COMMENT '发货时间',
    `receive_time`            DATETIME      NULL     COMMENT '确认收货时间',
    `cancel_time`             DATETIME      NULL     COMMENT '取消时间',
    `remark`                  VARCHAR(512)  NULL     COMMENT '订单备注',
    `delete_status`           TINYINT       NOT NULL DEFAULT 0 COMMENT '0未删除 1已删除（用户端隐藏）',
    `create_time`             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_sn` (`order_sn`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='订单';

-- ============================================================
-- 2. 订单项表
-- ============================================================
CREATE TABLE `oms_order_item` (
    `id`                BIGINT        NOT NULL COMMENT '主键',
    `order_id`          BIGINT        NOT NULL COMMENT '关联订单ID',
    `order_sn`          VARCHAR(64)   NOT NULL COMMENT '订单编号（冗余）',
    `spu_id`            BIGINT        NOT NULL COMMENT 'SPU ID',
    `sku_id`            BIGINT        NOT NULL COMMENT 'SKU ID',
    `spu_name`          VARCHAR(256)  NOT NULL COMMENT '商品名称（下单时快照）',
    `sku_name`          VARCHAR(256)  NOT NULL COMMENT 'SKU名称',
    `sku_img`           VARCHAR(255)  NULL     COMMENT 'SKU图片',
    `sku_price`         DECIMAL(10,2) NOT NULL COMMENT '下单时单价',
    `sku_quantity`      INT           NOT NULL COMMENT '购买数量',
    `sku_total_price`   DECIMAL(10,2) NOT NULL COMMENT '小计',
    `promotion_amount`  DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '促销分摊优惠',
    `coupon_amount`     DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '优惠券分摊金额',
    `create_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_order_sn` (`order_sn`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB COMMENT='订单项';

-- ============================================================
-- 3. 订单操作日志表
-- ============================================================
CREATE TABLE `oms_order_operate_history` (
    `id`              BIGINT       NOT NULL COMMENT '主键',
    `order_id`        BIGINT       NOT NULL COMMENT '订单ID',
    `order_sn`        VARCHAR(64)  NOT NULL COMMENT '订单编号',
    `operate_type`    VARCHAR(32)  NOT NULL COMMENT '操作类型: CREATE/PAY/DELIVER/RECEIVE/CANCEL/REFUND/APPLY_RETURN',
    `note`            VARCHAR(512) NULL     COMMENT '操作备注',
    `operate_by`      BIGINT       NULL     COMMENT '操作人ID',
    `operate_by_name` VARCHAR(64)  NULL     COMMENT '操作人姓名',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='订单操作日志';

-- ============================================================
-- 4. 支付信息表
-- ============================================================
CREATE TABLE `oms_payment_info` (
    `id`               BIGINT        NOT NULL COMMENT '主键',
    `order_sn`         VARCHAR(64)   NOT NULL COMMENT '订单编号',
    `pay_sn`           VARCHAR(128)  NULL     COMMENT '第三方支付流水号',
    `pay_type`         TINYINT       NOT NULL COMMENT '支付方式 1支付宝 2微信 3模拟',
    `pay_amount`       DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    `status`           TINYINT       NOT NULL DEFAULT 0 COMMENT '0待支付 1已支付 2退款中 3已退款 4支付失败',
    `callback_content` TEXT          NULL     COMMENT '第三方回调内容（JSON）',
    `callback_time`    DATETIME      NULL     COMMENT '回调时间',
    `create_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_sn` (`order_sn`),
    KEY `idx_pay_sn` (`pay_sn`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='支付信息';

-- ============================================================
-- 5. 退款退货申请表
-- ============================================================
CREATE TABLE `oms_order_return_apply` (
    `id`              BIGINT        NOT NULL COMMENT '主键',
    `order_sn`        VARCHAR(64)   NOT NULL COMMENT '订单编号',
    `order_id`        BIGINT        NOT NULL COMMENT '订单ID',
    `sku_id`          BIGINT        NOT NULL COMMENT '退货SKU ID',
    `member_id`       BIGINT        NOT NULL COMMENT '申请人ID',
    `return_type`     TINYINT       NOT NULL COMMENT '1仅退款 2退货退款',
    `reason`          VARCHAR(512)  NOT NULL COMMENT '退款原因',
    `description`     VARCHAR(512)  NULL     COMMENT '问题描述',
    `proof_imgs`      VARCHAR(1024) NULL     COMMENT '凭证图片（逗号分隔URL）',
    `return_amount`   DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    `status`          TINYINT       NOT NULL DEFAULT 0 COMMENT '0待审核 1同意 2拒绝 3已退款 4已取消',
    `handle_by`       BIGINT        NULL     COMMENT '处理人ID',
    `handle_note`     VARCHAR(512)  NULL     COMMENT '处理备注',
    `handle_time`     DATETIME      NULL     COMMENT '处理时间',
    `create_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_sn` (`order_sn`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='退款退货申请';

-- ============================================================
-- 6. 退货原因配置表
-- ============================================================
CREATE TABLE `oms_order_return_reason` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(64)  NOT NULL COMMENT '原因名称',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='退货原因配置';

-- ============================================================
-- 7. 购物车表（数据库副本，Redis为主存储）
-- ============================================================
CREATE TABLE `oms_cart_item` (
    `id`            BIGINT        NOT NULL COMMENT '主键',
    `member_id`     BIGINT        NOT NULL COMMENT '用户ID',
    `sku_id`        BIGINT        NOT NULL COMMENT 'SKU ID',
    `spu_id`        BIGINT        NOT NULL COMMENT 'SPU ID',
    `sku_name`      VARCHAR(256)  NOT NULL COMMENT 'SKU名称',
    `sku_img`       VARCHAR(255)  NULL     COMMENT 'SKU图片',
    `sku_price`     DECIMAL(10,2) NOT NULL COMMENT '加入时价格',
    `quantity`      INT           NOT NULL DEFAULT 1 COMMENT '数量',
    `is_checked`    TINYINT       NOT NULL DEFAULT 1 COMMENT '0未勾选 1已勾选',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_member_sku` (`member_id`, `sku_id`),
    KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB COMMENT='购物车';

-- ============================================================
-- 初始数据
-- ============================================================

-- 退货原因
INSERT INTO `oms_order_return_reason` (`id`, `name`, `sort`, `status`) VALUES
(1, '商品质量问题',     1, 1),
(2, '商品与描述不符',   2, 1),
(3, '收到商品破损',     3, 1),
(4, '不想要了',         4, 1),
(5, '拍错/多拍/不想要', 5, 1),
(6, '发货太慢',         6, 1),
(7, '其他原因',         7, 1);

-- ============================================================
-- 8. 订单超时配置表
-- ============================================================
CREATE TABLE `oms_order_setting` (
    `id`                      BIGINT   NOT NULL COMMENT '主键',
    `flash_order_overtime`    INT      NOT NULL DEFAULT 10 COMMENT '秒杀订单超时时间（分钟）',
    `normal_order_overtime`   INT      NOT NULL DEFAULT 30 COMMENT '正常订单超时时间（分钟）',
    `confirm_overtime`        INT      NOT NULL DEFAULT 7  COMMENT '自动确认收货超时时间（天）',
    `finish_overtime`         INT      NOT NULL DEFAULT 7  COMMENT '自动完成交易超时时间（收货后，天）',
    `comment_overtime`        INT      NOT NULL DEFAULT 7  COMMENT '评价超时时间（天）',
    `member_level`            BIGINT   NULL     COMMENT '关联会员等级ID',
    `create_time`             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`             DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='订单超时配置';

-- ============================================================
-- 9. 退款信息表
-- ============================================================
CREATE TABLE `oms_refund_info` (
    `id`               BIGINT        NOT NULL COMMENT '主键',
    `order_return_id`  BIGINT        NULL     COMMENT '关联退货申请ID',
    `order_sn`         VARCHAR(64)   NOT NULL COMMENT '订单编号',
    `refund_sn`        VARCHAR(64)   NULL     COMMENT '退款业务编号',
    `refund_amount`    DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    `refund_status`    TINYINT       NOT NULL DEFAULT 0 COMMENT '0待退款 1退款中 2退款成功 3退款失败',
    `refund_channel`   TINYINT       NULL     COMMENT '退款方式 1原路退回 2银行卡 3余额',
    `refund_content`   VARCHAR(512)  NULL     COMMENT '退款详情/备注',
    `create_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_sn` (`order_sn`),
    KEY `idx_order_return_id` (`order_return_id`),
    KEY `idx_refund_sn` (`refund_sn`)
) ENGINE=InnoDB COMMENT='退款信息';

-- ============================================================
-- 新增初始数据
-- ============================================================

-- 订单超时配置
INSERT INTO `oms_order_setting` (`id`, `flash_order_overtime`, `normal_order_overtime`, `confirm_overtime`, `finish_overtime`, `comment_overtime`) VALUES
(1, 10, 30, 7, 7, 7);

-- ============================================================
-- Mall-HJ 仓储库 DDL
-- Database: mall_ware
-- Charset:  utf8mb4 / Collate: utf8mb4_general_ci
-- Engine:   InnoDB
-- ============================================================

CREATE DATABASE IF NOT EXISTS `mall_ware`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `mall_ware`;

-- ============================================================
-- 1. 仓库信息表
-- ============================================================
CREATE TABLE `wms_ware_info` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(128) NOT NULL COMMENT '仓库名称',
    `address`       VARCHAR(255) NOT NULL COMMENT '仓库地址',
    `province`      VARCHAR(16)  NULL     COMMENT '省',
    `city`          VARCHAR(16)  NULL     COMMENT '市',
    `district`      VARCHAR(16)  NULL     COMMENT '区',
    `phone`         VARCHAR(20)  NULL     COMMENT '仓库联系电话',
    `is_default`    TINYINT      NOT NULL DEFAULT 0 COMMENT '0否 1默认仓库',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='仓库信息';

-- ============================================================
-- 2. 商品库存表（仓库+SKU维度）
-- ============================================================
CREATE TABLE `wms_ware_sku` (
    `id`            BIGINT        NOT NULL COMMENT '主键',
    `ware_id`       BIGINT        NOT NULL COMMENT '仓库ID',
    `sku_id`        BIGINT        NOT NULL COMMENT 'SKU ID',
    `spu_id`        BIGINT        NOT NULL COMMENT 'SPU ID',
    `stock`         INT           NOT NULL DEFAULT 0 COMMENT '可用库存',
    `stock_locked`  INT           NOT NULL DEFAULT 0 COMMENT '已锁定库存（未支付订单占用）',
    `stock_total`   INT           NOT NULL DEFAULT 0 COMMENT '总库存 = stock + stock_locked',
    `low_stock`     INT           NOT NULL DEFAULT 10 COMMENT '库存预警阈值',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_ware_sku` (`ware_id`, `sku_id`),
    KEY `idx_sku_id` (`sku_id`),
    KEY `idx_ware_id` (`ware_id`)
) ENGINE=InnoDB COMMENT='商品库存';

-- ============================================================
-- 3. 库存变动日志表
-- ============================================================
CREATE TABLE `wms_stock_log` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `ware_id`       BIGINT       NOT NULL COMMENT '仓库ID',
    `sku_id`        BIGINT       NOT NULL COMMENT 'SKU ID',
    `change_type`   VARCHAR(32)  NOT NULL COMMENT '变动类型: ORDER_LOCK/ORDER_UNLOCK/ORDER_DEDUCT/PURCHASE_IN/REFUND',
    `change_amount` INT          NOT NULL COMMENT '变动数量（正数增加，负数减少）',
    `before_stock`  INT          NOT NULL COMMENT '变动前可用库存',
    `after_stock`   INT          NOT NULL COMMENT '变动后可用库存',
    `before_locked` INT          NOT NULL COMMENT '变动前锁定库存',
    `after_locked`  INT          NOT NULL COMMENT '变动后锁定库存',
    `order_sn`      VARCHAR(64)  NULL     COMMENT '关联订单编号',
    `purchase_id`   BIGINT       NULL     COMMENT '关联采购单ID',
    `remark`        VARCHAR(255) NULL     COMMENT '备注',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_ware_sku` (`ware_id`, `sku_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='库存变动日志';

-- ============================================================
-- 4. 采购单表
-- ============================================================
CREATE TABLE `wms_purchase` (
    `id`                BIGINT       NOT NULL COMMENT '主键',
    `purchase_sn`       VARCHAR(64)  NOT NULL COMMENT '采购单编号',
    `ware_id`           BIGINT       NOT NULL COMMENT '目标仓库',
    `status`            TINYINT      NOT NULL DEFAULT 0 COMMENT '0新建 1已分配 2已采购 3已入库 4已取消',
    `total_amount`      DECIMAL(10,2) NULL    COMMENT '采购总金额',
    `create_by`         BIGINT       NULL     COMMENT '创建人ID',
    `create_by_name`    VARCHAR(64)  NULL     COMMENT '创建人姓名',
    `remark`            VARCHAR(512) NULL     COMMENT '备注',
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_purchase_sn` (`purchase_sn`),
    KEY `idx_ware_id` (`ware_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB COMMENT='采购单';

-- ============================================================
-- 5. 采购单详情表
-- ============================================================
CREATE TABLE `wms_purchase_detail` (
    `id`            BIGINT        NOT NULL COMMENT '主键',
    `purchase_id`   BIGINT        NOT NULL COMMENT '关联采购单ID',
    `sku_id`        BIGINT        NOT NULL COMMENT 'SKU ID',
    `spu_id`        BIGINT        NOT NULL COMMENT 'SPU ID',
    `sku_name`      VARCHAR(256)  NULL     COMMENT 'SKU名称',
    `sku_price`     DECIMAL(10,2) NULL     COMMENT '采购单价',
    `quantity`      INT           NOT NULL COMMENT '采购数量',
    `status`        TINYINT       NOT NULL DEFAULT 0 COMMENT '0待采购 1已采购 2已入库',
    `create_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_purchase_id` (`purchase_id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB COMMENT='采购单详情';

-- ============================================================
-- 6. 库存工作单表（Seata 分布式事务关联）
-- ============================================================
CREATE TABLE `wms_ware_order_task` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `order_sn`      VARCHAR(64)  NOT NULL COMMENT '订单编号',
    `order_id`      BIGINT       NOT NULL COMMENT '订单ID',
    `ware_id`       BIGINT       NULL     COMMENT '发货仓库ID',
    `task_status`   TINYINT      NOT NULL DEFAULT 0 COMMENT '0待锁定 1已锁定 2已扣减 3已释放',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_order_sn` (`order_sn`),
    KEY `idx_task_status` (`task_status`)
) ENGINE=InnoDB COMMENT='库存工作单（Seata事务关联）';

-- ============================================================
-- 7. 库存工作单详情表
-- ============================================================
CREATE TABLE `wms_ware_order_task_detail` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `task_id`       BIGINT       NOT NULL COMMENT '关联工作单ID',
    `ware_id`       BIGINT       NOT NULL COMMENT '仓库ID',
    `sku_id`        BIGINT       NOT NULL COMMENT 'SKU ID',
    `sku_name`      VARCHAR(256) NULL     COMMENT 'SKU名称',
    `lock_quantity` INT          NOT NULL COMMENT '锁定数量',
    `task_detail_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0待锁定 1已锁定 2已扣减 3已释放',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_task_id` (`task_id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB COMMENT='库存工作单详情';

-- ============================================================
-- 初始数据
-- ============================================================

-- 仓库
INSERT INTO `wms_ware_info` (`id`, `name`, `address`, `province`, `city`, `district`, `phone`, `is_default`) VALUES
(1, '北京总仓', '北京市大兴区XX路XX号', '北京', '北京市', '大兴区', '010-12345678', 1),
(2, '上海仓库', '上海市嘉定区XX路XX号', '上海', '上海市', '嘉定区', '021-87654321', 0),
(3, '广州仓库', '广州市白云区XX路XX号', '广东', '广州市', '白云区', '020-11223344', 0);

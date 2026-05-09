-- ============================================================
-- Mall-HJ 商品库 DDL
-- Database: mall_product
-- Charset:  utf8mb4 / Collate: utf8mb4_general_ci
-- Engine:   InnoDB
-- ============================================================

CREATE DATABASE IF NOT EXISTS `mall_product`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

USE `mall_product`;

-- ============================================================
-- 1. 商品分类表（三级树形）
-- ============================================================
CREATE TABLE `pms_category` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(64)  NOT NULL COMMENT '分类名称',
    `parent_id`     BIGINT       NOT NULL DEFAULT 0 COMMENT '父分类ID（一级为0）',
    `level`         TINYINT      NOT NULL COMMENT '层级：1/2/3',
    `icon`          VARCHAR(255) NULL     COMMENT '图标URL',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序（越小越靠前）',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_level` (`level`)
) ENGINE=InnoDB COMMENT='商品分类';

-- ============================================================
-- 2. 品牌表
-- ============================================================
CREATE TABLE `pms_brand` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(64)  NOT NULL COMMENT '品牌名称',
    `logo`          VARCHAR(255) NULL     COMMENT '品牌Logo URL',
    `description`   TEXT         NULL     COMMENT '品牌介绍',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='品牌';

-- ============================================================
-- 3. 分类-品牌关联表（多对多）
-- ============================================================
CREATE TABLE `pms_category_brand_relation` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `category_id`   BIGINT       NOT NULL COMMENT '三级分类ID',
    `brand_id`      BIGINT       NOT NULL COMMENT '品牌ID',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_brand` (`category_id`, `brand_id`),
    KEY `idx_brand_id` (`brand_id`)
) ENGINE=InnoDB COMMENT='分类-品牌关联';

-- ============================================================
-- 4. 属性分组表（绑定到三级分类）
-- ============================================================
CREATE TABLE `pms_attr_group` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(64)  NOT NULL COMMENT '属性组名称（如"主体""屏幕""网络"）',
    `category_id`   BIGINT       NOT NULL COMMENT '关联三级分类ID',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB COMMENT='属性分组';

-- ============================================================
-- 5. 属性定义表
-- ============================================================
CREATE TABLE `pms_attr` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `name`          VARCHAR(64)  NOT NULL COMMENT '属性名（如"颜色""内存""CPU"）',
    `attr_type`     TINYINT      NOT NULL COMMENT '1基本属性 2销售属性',
    `value_type`    TINYINT      NOT NULL DEFAULT 0 COMMENT '0手动输入 1单选 2多选',
    `icon`          VARCHAR(255) NULL     COMMENT '图标（可选）',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '0禁用 1启用',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='属性定义';

-- ============================================================
-- 6. 属性预定义可选值表（管理员维护，商家从中选择）
-- ============================================================
CREATE TABLE `pms_attr_value` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `attr_id`       BIGINT       NOT NULL COMMENT '关联属性ID',
    `value`         VARCHAR(128) NOT NULL COMMENT '可选值（如"黑色""128GB"）',
    `icon`          VARCHAR(255) NULL     COMMENT '图标（颜色属性可配色块图）',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_attr_id` (`attr_id`)
) ENGINE=InnoDB COMMENT='属性预定义可选值';

-- ============================================================
-- 7. 属性-分组关联表（一个属性可属于多个分组）
-- ============================================================
CREATE TABLE `pms_attr_attrgroup_relation` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `attr_id`       BIGINT       NOT NULL COMMENT '属性ID',
    `attr_group_id` BIGINT       NOT NULL COMMENT '属性组ID',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '组内排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_attr_group` (`attr_id`, `attr_group_id`),
    KEY `idx_attr_group_id` (`attr_group_id`)
) ENGINE=InnoDB COMMENT='属性-分组关联';

-- ============================================================
-- 8. SPU基本信息表
-- ============================================================
CREATE TABLE `pms_spu_info` (
    `id`                BIGINT       NOT NULL COMMENT '主键（= SPU ID）',
    `spu_name`          VARCHAR(256) NOT NULL COMMENT '商品名称',
    `spu_description`   VARCHAR(512) NULL     COMMENT '商品简介',
    `category_id`       BIGINT       NOT NULL COMMENT '三级分类ID',
    `brand_id`          BIGINT       NULL     COMMENT '品牌ID',
    `weight`            DECIMAL(10,2) NULL    COMMENT '重量(kg)',
    `publish_status`    TINYINT      NOT NULL DEFAULT 0 COMMENT '0下架 1上架',
    `audit_status`      TINYINT      NOT NULL DEFAULT 0 COMMENT '0待审核 1审核通过 2审核拒绝',
    `new_status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '0否 1新品',
    `recommend_status`  TINYINT      NOT NULL DEFAULT 0 COMMENT '0否 1推荐',
    `sort`              INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_by`         BIGINT       NULL     COMMENT '创建人（商家）ID',
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_brand_id` (`brand_id`),
    KEY `idx_audit_status` (`audit_status`),
    KEY `idx_create_by` (`create_by`)
) ENGINE=InnoDB COMMENT='SPU基本信息';

-- ============================================================
-- 9. SPU富文本描述表
-- ============================================================
CREATE TABLE `pms_spu_info_desc` (
    `spu_id`        BIGINT       NOT NULL COMMENT 'SPU ID（= pms_spu_info.id）',
    `description`   LONGTEXT     NULL     COMMENT '富文本详情（HTML）',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`spu_id`)
) ENGINE=InnoDB COMMENT='SPU富文本描述';

-- ============================================================
-- 10. SPU图片表
-- ============================================================
CREATE TABLE `pms_spu_images` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `spu_id`        BIGINT       NOT NULL COMMENT '关联SPU',
    `img_url`       VARCHAR(255) NOT NULL COMMENT '图片URL',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_spu_id` (`spu_id`)
) ENGINE=InnoDB COMMENT='SPU图片';

-- ============================================================
-- 11. SPU基本属性值表
-- ============================================================
CREATE TABLE `pms_spu_attr_value` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `spu_id`        BIGINT       NOT NULL COMMENT '关联SPU',
    `attr_id`       BIGINT       NOT NULL COMMENT '关联属性',
    `attr_name`     VARCHAR(64)  NOT NULL COMMENT '属性名（冗余）',
    `attr_value`    VARCHAR(255) NOT NULL COMMENT '属性值',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_spu_id` (`spu_id`),
    KEY `idx_attr_id` (`attr_id`)
) ENGINE=InnoDB COMMENT='SPU基本属性值';

-- ============================================================
-- 12. SKU信息表
-- ============================================================
CREATE TABLE `pms_sku_info` (
    `id`                BIGINT        NOT NULL COMMENT '主键（= SKU ID）',
    `spu_id`            BIGINT        NOT NULL COMMENT '关联SPU',
    `sku_name`          VARCHAR(256)  NOT NULL COMMENT 'SKU名称（如"iPhone 15 黑色 128GB"）',
    `sku_desc`          VARCHAR(512)  NULL     COMMENT 'SKU描述',
    `price`             DECIMAL(10,2) NOT NULL COMMENT '售价',
    `original_price`    DECIMAL(10,2) NULL     COMMENT '原价（划线价）',
    `sku_img`           VARCHAR(255)  NULL     COMMENT '默认图片',
    `weight`            DECIMAL(10,2) NULL     COMMENT '重量(kg)',
    `publish_status`    TINYINT       NOT NULL DEFAULT 1 COMMENT '0下架 1上架',
    `sort`              INT           NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_spu_id` (`spu_id`),
    KEY `idx_price` (`price`)
) ENGINE=InnoDB COMMENT='SKU信息';

-- ============================================================
-- 13. SKU图片表
-- ============================================================
CREATE TABLE `pms_sku_images` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `sku_id`        BIGINT       NOT NULL COMMENT '关联SKU',
    `img_url`       VARCHAR(255) NOT NULL COMMENT '图片URL',
    `default_img`   TINYINT      NOT NULL DEFAULT 0 COMMENT '0否 1默认图',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB COMMENT='SKU图片';

-- ============================================================
-- 14. SKU销售属性值表
-- ============================================================
CREATE TABLE `pms_sku_sale_attr_value` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `sku_id`        BIGINT       NOT NULL COMMENT '关联SKU',
    `spu_id`        BIGINT       NOT NULL COMMENT '关联SPU',
    `attr_id`       BIGINT       NOT NULL COMMENT '关联属性',
    `attr_name`     VARCHAR(64)  NOT NULL COMMENT '属性名',
    `attr_value`    VARCHAR(255) NOT NULL COMMENT '属性值',
    `sort`          INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_sku_id` (`sku_id`),
    KEY `idx_spu_id` (`spu_id`),
    KEY `idx_attr_id` (`attr_id`)
) ENGINE=InnoDB COMMENT='SKU销售属性值';

-- ============================================================
-- 15. 商品审核日志表
-- ============================================================
CREATE TABLE `pms_spu_audit_log` (
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `spu_id`        BIGINT       NOT NULL COMMENT '关联SPU',
    `status`        TINYINT      NOT NULL COMMENT '1审核通过 2审核拒绝',
    `reason`        VARCHAR(512) NULL     COMMENT '审核意见',
    `audit_by`      BIGINT       NULL     COMMENT '审核人ID',
    `audit_by_name` VARCHAR(64)  NULL     COMMENT '审核人姓名',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_spu_id` (`spu_id`)
) ENGINE=InnoDB COMMENT='商品审核日志';

-- ============================================================
-- 初始数据
-- ============================================================

-- ============================================================
-- 一级分类（12个大类）
-- ============================================================
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `icon`, `sort`) VALUES
(1,  '手机数码',   0, 1, '/icons/phone.png',      1),
(2,  '电脑办公',   0, 1, '/icons/computer.png',   2),
(3,  '家用电器',   0, 1, '/icons/appliance.png',  3),
(4,  '服饰内衣',   0, 1, '/icons/clothing.png',   4),
(5, '鞋靴箱包',   0, 1, '/icons/shoes.png',       5),
(6,  '美妆护肤',   0, 1, '/icons/beauty.png',     6),
(7,  '个护清洁',   0, 1, '/icons/personal.png',   7),
(8,  '食品饮料',   0, 1, '/icons/food.png',        8),
(9,  '母婴玩具',   0, 1, '/icons/baby.png',        9),
(10, '家居家装',   0, 1, '/icons/home.png',       10),
(11, '运动户外',   0, 1, '/icons/sports.png',     11),
(12, '图书文具',   0, 1, '/icons/book.png',       12);

-- ============================================================
-- 二级分类 + 三级分类
-- ID规则：一级=1~12，二级=一级*100+序号，三级=二级*10+序号
-- ============================================================

-- ========== 1. 手机数码 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(101, '手机通讯',    1, 2, 1),
(102, '手机配件',    1, 2, 2),
(103, '摄影摄像',    1, 2, 3),
(104, '影音娱乐',    1, 2, 4),
(105, '智能设备',    1, 2, 5);

-- 三级 - 手机通讯
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(1011, '手机',          101, 3, 1),
(1012, '对讲机',        101, 3, 2),
(1013, '老人机',        101, 3, 3);

-- 三级 - 手机配件
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(1021, '手机壳/保护套', 102, 3, 1),
(1022, '贴膜',          102, 3, 2),
(1023, '充电器',        102, 3, 3),
(1024, '数据线',        102, 3, 4),
(1025, '耳机',          102, 3, 5),
(1026, '移动电源',      102, 3, 6),
(1027, '手机支架',      102, 3, 7),
(1028, '存储卡',        102, 3, 8);

-- 三级 - 摄影摄像
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(1031, '单反相机',      103, 3, 1),
(1032, '微单相机',      103, 3, 2),
(1033, '运动相机',      103, 3, 3),
(1034, '镜头',          103, 3, 4),
(1035, '摄像机',        103, 3, 5),
(1036, '拍立得',        103, 3, 6),
(1037, '相机配件',      103, 3, 7);

-- 三级 - 影音娱乐
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(1041, '蓝牙音箱',      104, 3, 1),
(1042, '智能音箱',      104, 3, 2),
(1043, 'MP3/MP4',      104, 3, 3),
(1044, '数码相框',      104, 3, 4);

-- 三级 - 智能设备
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(1051, '智能手表',      105, 3, 1),
(1052, '智能手环',      105, 3, 2),
(1053, '智能眼镜',      105, 3, 3),
(1054, 'VR设备',        105, 3, 4),
(1055, '无人机',        105, 3, 5),
(1056, '智能机器人',    105, 3, 6);

-- ========== 2. 电脑办公 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(201, '电脑整机',    2, 2, 1),
(202, '电脑配件',    2, 2, 2),
(203, '外设产品',    2, 2, 3),
(204, '网络产品',    2, 2, 4),
(205, '办公设备',    2, 2, 5);

-- 三级 - 电脑整机
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(2011, '笔记本',        201, 3, 1),
(2012, '游戏本',        201, 3, 2),
(2013, '台式机',        201, 3, 3),
(2014, '一体机',        201, 3, 4),
(2015, '迷你主机',      201, 3, 5),
(2016, '服务器',        201, 3, 6);

-- 三级 - 电脑配件
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(2021, 'CPU',           202, 3, 1),
(2022, '显卡',          202, 3, 2),
(2023, '主板',          202, 3, 3),
(2024, '内存',          202, 3, 4),
(2025, '硬盘',          202, 3, 5),
(2026, '机箱',          202, 3, 6),
(2027, '电源',          202, 3, 7),
(2028, '散热器',        202, 3, 8),
(2029, '显示器',        202, 3, 9);

-- 三级 - 外设产品
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(2031, '机械键盘',      203, 3, 1),
(2032, '鼠标',          203, 3, 2),
(2033, '耳机',          203, 3, 3),
(2034, '鼠标垫',        203, 3, 4),
(2035, 'U盘',           203, 3, 5),
(2036, '移动硬盘',      203, 3, 6),
(2037, '摄像头',        203, 3, 7);

-- 三级 - 网络产品
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(2041, '路由器',        204, 3, 1),
(2042, '交换机',        204, 3, 2),
(2043, '网卡',          204, 3, 3),
(2044, '网络存储',      204, 3, 4);

-- 三级 - 办公设备
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(2051, '打印机',        205, 3, 1),
(2052, '投影仪',        205, 3, 2),
(2053, '扫描仪',        205, 3, 3),
(2054, '碎纸机',        205, 3, 4);

-- ========== 3. 家用电器 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(301, '大家电',       3, 2, 1),
(302, '厨卫大电',     3, 2, 2),
(303, '厨房小电',     3, 2, 3),
(304, '生活电器',     3, 2, 4),
(305, '个护健康',     3, 2, 5);

-- 三级 - 大家电
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(3011, '电视',          301, 3, 1),
(3012, '空调',          301, 3, 2),
(3013, '冰箱',          301, 3, 3),
(3014, '洗衣机',        301, 3, 4),
(3015, '冷柜/冰吧',     301, 3, 5);

-- 三级 - 厨卫大电
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(3021, '油烟机',        302, 3, 1),
(3022, '燃气灶',        302, 3, 2),
(3023, '热水器',        302, 3, 3),
(3024, '消毒柜',        302, 3, 4),
(3025, '洗碗机',        302, 3, 5),
(3026, '集成灶',        302, 3, 6);

-- 三级 - 厨房小电
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(3031, '电饭煲',        303, 3, 1),
(3032, '电磁炉',        303, 3, 2),
(3033, '微波炉',        303, 3, 3),
(3034, '电压力锅',      303, 3, 4),
(3035, '豆浆机',        303, 3, 5),
(3036, '破壁机',        303, 3, 6),
(3037, '榨汁机',        303, 3, 7),
(3038, '电烤箱',        303, 3, 8),
(3039, '空气炸锅',      303, 3, 9),
(3040, '电水壶',        303, 3, 10);

-- 三级 - 生活电器
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(3041, '扫地机器人',    304, 3, 1),
(3042, '吸尘器',        304, 3, 2),
(3043, '电风扇',        304, 3, 3),
(3044, '加湿器',        304, 3, 4),
(3045, '净水器',        304, 3, 5),
(3046, '空气净化器',    304, 3, 6),
(3047, '取暖器',        304, 3, 7);

-- 三级 - 个护健康
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(3051, '电动牙刷',      305, 3, 1),
(3052, '剃须刀',        305, 3, 2),
(3053, '吹风机',        305, 3, 3),
(3054, '卷/直发器',     305, 3, 4),
(3055, '按摩器',        305, 3, 5),
(3056, '血压计',        305, 3, 6),
(3057, '体温计',        305, 3, 7);

-- ========== 4. 服饰内衣 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(401, '女装',         4, 2, 1),
(402, '男装',         4, 2, 2),
(403, '内衣',         4, 2, 3),
(404, '配饰',         4, 2, 4);

-- 三级 - 女装
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(4011, 'T恤',          401, 3, 1),
(4012, '衬衫',         401, 3, 2),
(4013, '连衣裙',       401, 3, 3),
(4014, '半身裙',       401, 3, 4),
(4015, '牛仔裤',       401, 3, 5),
(4016, '休闲裤',       401, 3, 6),
(4017, '卫衣',         401, 3, 7),
(4018, '毛衣',         401, 3, 8),
(4019, '外套',         401, 3, 9),
(4020, '羽绒服',       401, 3, 10),
(4021, '风衣',         401, 3, 11),
(4022, '防晒衣',       401, 3, 12);

-- 三级 - 男装
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(4031, 'T恤',          402, 3, 1),
(4032, '衬衫',         402, 3, 2),
(4033, 'polo衫',      402, 3, 3),
(4034, '牛仔裤',       402, 3, 4),
(4035, '休闲裤',       402, 3, 5),
(4036, '西裤',         402, 3, 6),
(4037, '夹克',         402, 3, 7),
(4038, '外套',         402, 3, 8),
(4039, '羽绒服',       402, 3, 9),
(4040, '西装',         402, 3, 10);

-- 三级 - 内衣
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(4051, '文胸',         403, 3, 1),
(4052, '内裤',         403, 3, 2),
(4053, '袜子',         403, 3, 3),
(4054, '保暖内衣',     403, 3, 4),
(4055, '家居服',       403, 3, 5),
(4056, '塑身衣',       403, 3, 6);

-- 三级 - 配饰
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(4061, '帽子',         404, 3, 1),
(4062, '围巾',         404, 3, 2),
(4063, '手套',         404, 3, 3),
(4064, '皮带',         404, 3, 4),
(4065, '领带',         404, 3, 5);

-- ========== 5. 鞋靴箱包 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(501, '女鞋',         5, 2, 1),
(502, '男鞋',         5, 2, 2),
(503, '箱包',         5, 2, 3);

-- 三级 - 女鞋
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(5011, '高跟鞋',       501, 3, 1),
(5012, '平底鞋',       501, 3, 2),
(5013, '运动鞋',       501, 3, 3),
(5014, '凉鞋',         501, 3, 4),
(5015, '靴子',         501, 3, 5),
(5016, '帆布鞋',       501, 3, 6),
(5017, '拖鞋',         501, 3, 7);

-- 三级 - 男鞋
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(5021, '商务鞋',       502, 3, 1),
(5022, '运动鞋',       502, 3, 2),
(5023, '休闲鞋',       502, 3, 3),
(5024, '凉鞋',         502, 3, 4),
(5025, '帆布鞋',       502, 3, 5),
(5026, '拖鞋',         502, 3, 6);

-- 三级 - 箱包
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(5031, '双肩包',       503, 3, 1),
(5032, '手提包',       503, 3, 2),
(5033, '斜挎包',       503, 3, 3),
(5034, '拉杆箱',       503, 3, 4),
(5035, '钱包',         503, 3, 5),
(5036, '电脑包',       503, 3, 6);

-- ========== 6. 美妆护肤 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(601, '面部护肤',     6, 2, 1),
(602, '彩妆',         6, 2, 2),
(603, '香水',         6, 2, 3),
(604, '美妆工具',     6, 2, 4);

-- 三级 - 面部护肤
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(6011, '洁面',         601, 3, 1),
(6012, '爽肤水',       601, 3, 2),
(6013, '乳液/面霜',    601, 3, 3),
(6014, '精华',         601, 3, 4),
(6015, '防晒',         601, 3, 5),
(6016, '面膜',         601, 3, 6),
(6017, '眼霜',         601, 3, 7),
(6018, '卸妆',         601, 3, 8),
(6019, '祛痘',         601, 3, 9);

-- 三级 - 彩妆
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(6021, '粉底液',       602, 3, 1),
(6022, '口红',         602, 3, 2),
(6023, '眼影',         602, 3, 3),
(6024, '睫毛膏',       602, 3, 4),
(6025, '眉笔',         602, 3, 5),
(6026, '腮红',         602, 3, 6),
(6027, '散粉/定妆',    602, 3, 7),
(6028, '遮瑕',         602, 3, 8);

-- 三级 - 香水
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(6031, '女士香水',     603, 3, 1),
(6032, '男士香水',     603, 3, 2),
(6033, '中性香水',     603, 3, 3);

-- 三级 - 美妆工具
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(6041, '化妆刷',       604, 3, 1),
(6042, '化妆棉',       604, 3, 2),
(6043, '美妆蛋',       604, 3, 3),
(6044, '修眉工具',     604, 3, 4);

-- ========== 7. 个护清洁 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(701, '洗发护发',     7, 2, 1),
(702, '沐浴清洁',     7, 2, 2),
(703, '口腔护理',     7, 2, 3),
(704, '纸品湿巾',     7, 2, 4);

-- 三级
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(7011, '洗发水',       701, 3, 1),
(7012, '护发素',       701, 3, 2),
(7013, '发膜',         701, 3, 3),
(7014, '染发剂',       701, 3, 4),
(7021, '沐浴露',       702, 3, 1),
(7022, '香皂',         702, 3, 2),
(7023, '洗手液',       702, 3, 3),
(7031, '牙膏',         703, 3, 1),
(7032, '牙刷',         703, 3, 2),
(7033, '漱口水',       703, 3, 3),
(7041, '抽纸',         704, 3, 1),
(7042, '卷纸',         704, 3, 2),
(7043, '湿巾',         704, 3, 3);

-- ========== 8. 食品饮料 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(801, '休闲零食',     8, 2, 1),
(802, '饮料冲调',     8, 2, 2),
(803, '粮油调味',     8, 2, 3),
(804, '生鲜水果',     8, 2, 4),
(805, '酒类',         8, 2, 5),
(806, '茶',           8, 2, 6);

-- 三级 - 休闲零食
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(8011, '坚果',         801, 3, 1),
(8012, '饼干蛋糕',     801, 3, 2),
(8013, '糖果',         801, 3, 3),
(8014, '巧克力',       801, 3, 4),
(8015, '肉脯卤味',     801, 3, 5),
(8016, '蜜饯果干',     801, 3, 6),
(8017, '膨化食品',     801, 3, 7),
(8018, '速食干货',     801, 3, 8);

-- 三级 - 饮料冲调
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(8021, '碳酸饮料',     802, 3, 1),
(8022, '果汁',         802, 3, 2),
(8023, '茶饮料',       802, 3, 3),
(8024, '咖啡',         802, 3, 4),
(8025, '牛奶乳品',     802, 3, 5),
(8026, '酸奶',         802, 3, 6),
(8027, '冲饮麦片',     802, 3, 7);

-- 三级 - 粮油调味
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(8031, '大米',         803, 3, 1),
(8032, '食用油',       803, 3, 2),
(8033, '面粉',         803, 3, 3),
(8034, '调味品',       803, 3, 4),
(8035, '方便速食',     803, 3, 5);

-- 三级 - 生鲜水果
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(8041, '苹果',         804, 3, 1),
(8042, '橙子',         804, 3, 2),
(8043, '葡萄',         804, 3, 3),
(8044, '热带水果',     804, 3, 4),
(8045, '牛排',         804, 3, 5),
(8046, '海鲜',         804, 3, 6);

-- 三级 - 酒类
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(8051, '白酒',         805, 3, 1),
(8052, '啤酒',         805, 3, 2),
(8053, '葡萄酒',       805, 3, 3),
(8054, '洋酒',         805, 3, 4);

-- 三级 - 茶
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(8061, '绿茶',         806, 3, 1),
(8062, '红茶',         806, 3, 2),
(8063, '乌龙茶',       806, 3, 3),
(8064, '花茶',         806, 3, 4),
(8065, '普洱茶',       806, 3, 5);

-- ========== 9. 母婴玩具 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(901, '奶粉',         9, 2, 1),
(902, '纸尿裤',       9, 2, 2),
(903, '童车童床',     9, 2, 3),
(904, '玩具',         9, 2, 4),
(905, '孕妈装',       9, 2, 5);

-- 三级
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(9011, '婴幼儿奶粉',   901, 3, 1),
(9012, '成人奶粉',     901, 3, 2),
(9013, '羊奶粉',       901, 3, 3),
(9021, '婴儿纸尿裤',   902, 3, 1),
(9022, '拉拉裤',       902, 3, 2),
(9023, '湿巾',         902, 3, 3),
(9031, '婴儿车',       903, 3, 1),
(9032, '婴儿床',       903, 3, 2),
(9033, '安全座椅',     903, 3, 3),
(9041, '积木拼插',     904, 3, 1),
(9042, '遥控车',       904, 3, 2),
(9043, '毛绒玩具',     904, 3, 3),
(9044, '早教益智',     904, 3, 4),
(9045, '模型手办',     904, 3, 5),
(9051, '孕妇裙',       905, 3, 1),
(9052, '孕妇裤',       905, 3, 2),
(9053, '哺乳衣',       905, 3, 3);

-- ========== 10. 家居家装 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(1001, '家具',        10, 2, 1),
(1002, '家纺',        10, 2, 2),
(1003, '灯具',        10, 2, 3),
(1004, '家装建材',    10, 2, 4),
(1005, '家装饰品',    10, 2, 5);

-- 三级 - 家具
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(10011, '沙发',        1001, 3, 1),
(10012, '床',          1001, 3, 2),
(10013, '床垫',        1001, 3, 3),
(10014, '衣柜',        1001, 3, 4),
(10015, '书桌',        1001, 3, 5),
(10016, '椅子',        1001, 3, 6),
(10017, '鞋柜',        1001, 3, 7),
(10018, '电视柜',      1001, 3, 8);

-- 三级 - 家纺
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(10021, '四件套',      1002, 3, 1),
(10022, '被子',        1002, 3, 2),
(10023, '枕头',        1002, 3, 3),
(10024, '毛毯',        1002, 3, 4),
(10025, '毛巾浴巾',    1002, 3, 5),
(10026, '窗帘',        1002, 3, 6),
(10027, '地毯',        1002, 3, 7);

-- 三级 - 灯具
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(10031, '吸顶灯',      1003, 3, 1),
(10032, '吊灯',        1003, 3, 2),
(10033, '台灯',        1003, 3, 3),
(10034, '落地灯',      1003, 3, 4),
(10035, 'LED灯带',     1003, 3, 5),
(10036, '筒灯射灯',    1003, 3, 6);

-- 三级 - 家装建材
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(10041, '瓷砖',        1004, 3, 1),
(10042, '地板',        1004, 3, 2),
(10043, '涂料',        1004, 3, 3),
(10044, '开关插座',    1004, 3, 4),
(10045, '水龙头',      1004, 3, 5),
(10046, '花洒',        1004, 3, 6),
(10047, '马桶',        1004, 3, 7),
(10048, '浴室柜',      1004, 3, 8);

-- 三级 - 家装饰品
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(10051, '花瓶',        1005, 3, 1),
(10052, '装饰画',      1005, 3, 2),
(10053, '摆件',        1005, 3, 3),
(10054, '收纳用品',    1005, 3, 4),
(10055, '时钟',        1005, 3, 5);

-- ========== 11. 运动户外 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(1101, '运动鞋包',    11, 2, 1),
(1102, '运动服饰',    11, 2, 2),
(1103, '健身训练',    11, 2, 3),
(1104, '户外装备',    11, 2, 4),
(1105, '体育用品',    11, 2, 5);

-- 三级
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(11011, '跑步鞋',      1101, 3, 1),
(11012, '篮球鞋',      1101, 3, 2),
(11013, '运动包',      1101, 3, 3),
(11021, '运动T恤',     1102, 3, 1),
(11022, '运动裤',      1102, 3, 2),
(11023, '运动套装',    1102, 3, 3),
(11024, '瑜伽服',      1102, 3, 4),
(11031, '跑步机',      1103, 3, 1),
(11032, '哑铃',        1103, 3, 2),
(11033, '瑜伽垫',      1103, 3, 3),
(11034, '拉力器',      1103, 3, 4),
(11041, '帐篷',        1104, 3, 1),
(11042, '登山杖',      1104, 3, 2),
(11043, '睡袋',        1104, 3, 3),
(11044, '冲锋衣',      1104, 3, 4),
(11045, '背包',        1104, 3, 5),
(11051, '篮球',        1105, 3, 1),
(11052, '足球',        1105, 3, 2),
(11053, '羽毛球拍',    1105, 3, 3),
(11054, '乒乓球拍',    1105, 3, 4),
(11055, '轮滑鞋',      1105, 3, 5);

-- ========== 12. 图书文具 ==========
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
-- 二级
(1201, '图书',        12, 2, 1),
(1202, '文具',        12, 2, 2),
(1203, '电子书',      12, 2, 3);

-- 三级
INSERT INTO `pms_category` (`id`, `name`, `parent_id`, `level`, `sort`) VALUES
(12011, '小说',        1201, 3, 1),
(12012, '文学',        1201, 3, 2),
(12013, '计算机',      1201, 3, 3),
(12014, '经济管理',    1201, 3, 4),
(12015, '教育考试',    1201, 3, 5),
(12016, '少儿图书',    1201, 3, 6),
(12017, '生活百科',    1201, 3, 7),
(12018, '历史传记',    1201, 3, 8),
(12021, '笔类',        1202, 3, 1),
(12022, '笔记本',      1202, 3, 2),
(12023, '文件管理',    1202, 3, 3),
(12024, '书包',        1202, 3, 4),
(12025, '美术用品',    1202, 3, 5),
(12031, '电子阅读器',  1203, 3, 1);

-- 品牌
INSERT INTO `pms_brand` (`id`, `name`, `logo`, `sort`) VALUES
-- 手机数码
(1,  'Apple',       '/brands/apple.png',     1),
(2,  '华为',        '/brands/huawei.png',    2),
(3,  '小米',        '/brands/xiaomi.png',    3),
(4,  'OPPO',        '/brands/oppo.png',      4),
(5,  'vivo',        '/brands/vivo.png',      5),
(6,  '三星',        '/brands/samsung.png',   6),
(7,  '荣耀',        '/brands/honor.png',     7),
(8,  '索尼',        '/brands/sony.png',      8),
-- 电脑办公
(10, '联想',        '/brands/lenovo.png',   10),
(11, '戴尔',        '/brands/dell.png',     11),
(12, '惠普',        '/brands/hp.png',       12),
(13, '华硕',        '/brands/asus.png',     13),
(14, '宏碁',        '/brands/acer.png',     14),
(15, '微软',        '/brands/microsoft.png',15),
(16, '外星人',      '/brands/alienware.png',16),
-- 家用电器
(20, '美的',        '/brands/midea.png',    20),
(21, '格力',        '/brands/gree.png',     21),
(22, '海尔',        '/brands/haier.png',    22),
(23, 'TCL',        '/brands/tcl.png',      23),
(24, '海信',        '/brands/hisense.png',  24),
(25, '松下',        '/brands/panasonic.png',25),
(26, '西门子',      '/brands/siemens.png',  26),
(27, '方太',        '/brands/fotile.png',   27),
(28, '老板',        '/brands/robam.png',    28),
(29, '飞利浦',      '/brands/philips.png',  29),
-- 服饰内衣
(30, '优衣库',      '/brands/uniqlo.png',   30),
(31, 'ZARA',       '/brands/zara.png',     31),
(32, 'NIKE',       '/brands/nike.png',     32),
(33, 'Adidas',     '/brands/adidas.png',   33),
(34, '太平鸟',      '/brands/pepbird.png',  34),
(35, '李宁',        '/brands/lining.png',   35),
-- 美妆护肤
(40, '兰蔻',        '/brands/lancome.png',  40),
(41, '雅诗兰黛',    '/brands/estee.png',    41),
(42, '欧莱雅',      '/brands/loreal.png',   42),
(43, 'SK-II',      '/brands/skii.png',     43),
(44, '百雀羚',      '/brands/bqf.png',      44),
(45, '完美日记',    '/brands/perfect.png',  45),
-- 运动户外
(50, '安踏',        '/brands/anta.png',     50),
(51, '迪卡侬',      '/brands/decathlon.png',51),
(52, 'The North Face','/brands/tnf.png',    52),
(53, 'Columbia',    '/brands/columbia.png', 53),
-- 图书文具
(60, '得力',        '/brands/deli.png',     60),
(61, '晨光',        '/brands/mg.png',       61),
(62, 'Kindle',      '/brands/kindle.png',   62);

-- 分类-品牌关联
INSERT INTO `pms_category_brand_relation` (`id`, `category_id`, `brand_id`, `sort`) VALUES
-- 手机 → Apple/华为/小米/OPPO/vivo/三星/荣耀
(1,  1011, 1, 1),  (2,  1011, 2, 2),  (3,  1011, 3, 3),
(4,  1011, 4, 4),  (5,  1011, 5, 5),  (6,  1011, 6, 6),  (7, 1011, 7, 7),
-- 智能手表 → Apple/华为/小米/三星
(10, 1051, 1, 1),  (11, 1051, 2, 2),  (12, 1051, 3, 3),  (13, 1051, 6, 4),
-- 笔记本 → 联想/戴尔/惠普/华硕/Apple/华为/小米
(20, 2011, 10, 1), (21, 2011, 11, 2), (22, 2011, 12, 3),
(23, 2011, 13, 4), (24, 2011, 1, 5),  (25, 2011, 2, 6),  (26, 2011, 3, 7),
-- 游戏本 → 联想/外星人/华硕/惠普/戴尔
(30, 2012, 10, 1), (31, 2012, 16, 2), (32, 2012, 13, 3), (33, 2012, 12, 4),
-- 电视 → 海信/TCL/索尼/三星/小米/华为
(40, 3011, 24, 1), (41, 3011, 23, 2), (42, 3011, 8, 3),
(43, 3011, 6, 4),  (44, 3011, 3, 5),  (45, 3011, 2, 6),
-- 空调 → 格力/美的/海尔/TCL/松下
(50, 3012, 21, 1), (51, 3012, 20, 2), (52, 3012, 22, 3),
(53, 3012, 23, 4), (54, 3012, 25, 5),
-- 冰箱 → 海尔/美的/西门子/松下/格力
(60, 3013, 22, 1), (61, 3013, 20, 2), (62, 3013, 26, 3),
(63, 3013, 25, 4), (64, 3013, 21, 5),
-- 运动鞋 → NIKE/Adidas/李宁/安踏
(70, 11011, 32, 1), (71, 11011, 33, 2), (72, 11011, 35, 3), (73, 11011, 50, 4);

-- 属性组（手机分类下）
INSERT INTO `pms_attr_group` (`id`, `name`, `category_id`, `sort`) VALUES
(1, '主体',  1011, 1),
(2, '屏幕',  1011, 2),
(3, '存储',  1011, 3),
(4, '网络',  1011, 4),
(5, '电池',  1011, 5);

-- 属性定义
INSERT INTO `pms_attr` (`id`, `name`, `attr_type`, `value_type`, `sort`) VALUES
-- 基本属性
(1,  '品牌',       1, 1, 1),
(2,  '入网年份',   1, 1, 2),
(3,  'CPU型号',    1, 0, 3),
(4,  '屏幕尺寸',   1, 1, 4),
(5,  '分辨率',     1, 1, 5),
(6,  '电池容量',   1, 0, 6),
(7,  '操作系统',   1, 0, 7),
-- 销售属性
(8,  '颜色',       2, 1, 1),
(9,  '存储容量',   2, 1, 2);

-- 属性预定义可选值
INSERT INTO `pms_attr_value` (`id`, `attr_id`, `value`, `icon`, `sort`) VALUES
-- 品牌可选值
(1,  1, 'Apple',  NULL, 1),
(2,  1, '华为',   NULL, 2),
(3,  1, '小米',   NULL, 3),
-- 入网年份
(10, 2, '2024',   NULL, 1),
(11, 2, '2025',   NULL, 2),
-- 屏幕尺寸
(20, 4, '6.1英寸',  NULL, 1),
(21, 4, '6.5英寸',  NULL, 2),
(22, 4, '6.7英寸',  NULL, 3),
-- 分辨率
(30, 5, 'FHD+',   NULL, 1),
(31, 5, '2K',     NULL, 2),
-- 颜色
(40, 8, '黑色',   '/colors/black.png',  1),
(41, 8, '白色',   '/colors/white.png',  2),
(42, 8, '蓝色',   '/colors/blue.png',   3),
(43, 8, '红色',   '/colors/red.png',    4),
-- 存储容量
(50, 9, '128GB',  NULL, 1),
(51, 9, '256GB',  NULL, 2),
(52, 9, '512GB',  NULL, 3),
(53, 9, '1TB',    NULL, 4);

-- 属性-分组关联
INSERT INTO `pms_attr_attrgroup_relation` (`id`, `attr_id`, `attr_group_id`, `sort`) VALUES
(1, 1, 1, 1),   -- 品牌   → 主体组
(2, 2, 1, 2),   -- 入网年份 → 主体组
(3, 3, 1, 3),   -- CPU型号 → 主体组
(4, 4, 2, 1),   -- 屏幕尺寸 → 屏幕组
(5, 5, 2, 2),   -- 分辨率   → 屏幕组
(6, 6, 5, 1),   -- 电池容量 → 电池组
(7, 7, 4, 1),   -- 操作系统 → 网络组
(8, 8, 3, 1),   -- 颜色     → 存储组（销售属性也挂分组，方便展示）
(9, 9, 3, 2);   -- 存储容量 → 存储组

-- ============================================================
-- 16. 商品评论表
-- ============================================================
CREATE TABLE `pms_spu_comment` (
    `id`                BIGINT       NOT NULL COMMENT '主键',
    `spu_id`            BIGINT       NOT NULL COMMENT 'SPU ID',
    `sku_id`            BIGINT       NULL     COMMENT 'SKU ID (specific variant reviewed)',
    `member_id`         BIGINT       NOT NULL COMMENT 'Reviewer member ID',
    `member_nickname`   VARCHAR(64)  NULL     COMMENT 'Reviewer nickname',
    `member_avatar`     VARCHAR(255) NULL     COMMENT 'Reviewer avatar',
    `spu_name`          VARCHAR(256) NULL     COMMENT 'SPU name (denormalized)',
    `sku_attributes`    VARCHAR(512) NULL     COMMENT 'Reviewed SKU attributes snapshot',
    `star`              TINYINT      NOT NULL DEFAULT 5 COMMENT 'Rating 1-5',
    `content`           TEXT         NULL     COMMENT 'Review content',
    `resources`         VARCHAR(1024)NULL     COMMENT 'Image/video URLs, comma separated',
    `comment_type`      TINYINT      NOT NULL DEFAULT 1 COMMENT '1=actual purchase, 2=virtual',
    `show_status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '0=hidden, 1=visible',
    `likes_count`       INT          NOT NULL DEFAULT 0 COMMENT 'Like count',
    `reply_count`       INT          NOT NULL DEFAULT 0 COMMENT 'Reply count',
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_spu_id` (`spu_id`),
    KEY `idx_member_id` (`member_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='商品评论';

-- ============================================================
-- 17. 评论回复表
-- ============================================================
CREATE TABLE `pms_comment_replay` (
    `id`                BIGINT       NOT NULL COMMENT '主键',
    `comment_id`        BIGINT       NOT NULL COMMENT 'Parent comment ID',
    `reply_id`          BIGINT       NULL     COMMENT 'Reply to which reply ID (null=top level reply)',
    `member_id`         BIGINT       NOT NULL COMMENT 'Replier member ID',
    `member_nickname`   VARCHAR(64)  NULL     COMMENT 'Replier nickname',
    `member_avatar`     VARCHAR(255) NULL     COMMENT 'Replier avatar',
    `content`           TEXT         NOT NULL COMMENT 'Reply content',
    `create_time`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_comment_id` (`comment_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB COMMENT='评论回复';

-- ============================================================
-- 商品评论初始数据
-- ============================================================
INSERT INTO `pms_spu_comment` (`id`, `spu_id`, `sku_id`, `member_id`, `member_nickname`, `member_avatar`, `spu_name`, `sku_attributes`, `star`, `content`, `resources`, `comment_type`, `show_status`, `likes_count`, `reply_count`) VALUES
(100001, 1, 1, 1001, '用户A', '/avatars/1001.jpg', 'iPhone 15 Pro', '颜色:深空黑色,存储:256GB', 5, '非常满意的手机，拍照效果很棒，系统流畅，电池续航也不错！', '/images/review/100001_1.jpg,/images/review/100001_2.jpg', 1, 1, 128, 3),
(100002, 1, 2, 1002, '用户B', '/avatars/1002.jpg', 'iPhone 15 Pro', '颜色:白色钛金属,存储:512GB', 4, '手机很好用，就是价格稍微贵了一点。整体还是推荐的。', '/images/review/100002_1.jpg', 1, 1, 56, 1),
(100003, 2, 3, 1003, '用户C', '/avatars/1003.jpg', '华为 Mate 60 Pro', '颜色:雅川青,存储:512GB', 5, '支持国产！华为回来了，卫星通信功能很实用，拍照也非常出色。', '/images/review/100003_1.jpg,/images/review/100003_2.jpg,/images/review/100003_3.jpg', 1, 1, 234, 5),
(100004, 3, 4, 1004, '用户D', '/avatars/1004.jpg', '小米14 Pro', '颜色:黑色,存储:256GB', 4, '性价比很高，骁龙8 Gen3性能强劲，充电速度快。就是发热稍微有点明显。', NULL, 1, 1, 89, 2),
(100005, 1, NULL, 1005, '用户E', '/avatars/1005.jpg', 'iPhone 15 Pro', NULL, 3, '还在犹豫中，听说信号不太好，准备去实体店体验一下再决定。', NULL, 2, 1, 12, 0);

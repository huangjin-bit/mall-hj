# Mall-HJ v2.0 电商平台 -- 总体架构文档

> **版本**: 2.0.0
> **更新日期**: 2026-05-10
> **项目描述**: Mall-HJ E-Commerce Microservice Platform v2.0 -- 基于 Spring Boot 3 + Spring Cloud 的微服务电商后台系统

---

## 目录

1. [项目概览](#1-项目概览)
2. [系统架构](#2-系统架构)
3. [模块说明](#3-模块说明)
4. [数据库设计](#4-数据库设计)
5. [认证架构](#5-认证架构)
6. [服务间通信](#6-服务间通信)
7. [缓存策略](#7-缓存策略)
8. [搜索服务](#8-搜索服务)
9. [文件存储](#9-文件存储)
10. [API网关路由表](#10-api网关路由表)
11. [统一规范](#11-统一规范)
12. [部署要求](#12-部署要求)
13. [文档目录](#13-文档目录)

---

## 1. 项目概览

### 1.1 基本信息

| 项目 | 说明 |
|------|------|
| **项目名称** | Mall-HJ |
| **版本号** | 2.0.0 |
| **GroupId** | com.hj |
| **ArtifactId** | mall |
| **打包方式** | pom (多模块聚合) |
| **Java 版本** | 21 |
| **编码** | UTF-8 |

### 1.2 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.5.14 | 基础框架 |
| Spring Cloud | 2025.0.0 | 微服务治理 |
| Spring Cloud Alibaba | 2025.0.0.0 | Nacos 注册/配置中心 |
| Java | 21 | 运行时 |
| MyBatis-Plus | 3.5.7 | ORM 持久层 |
| Seata | 2.6.0 | 分布式事务 |
| RabbitMQ | 3.x | 消息队列 |
| Redis | 7.0+ | 缓存 / Session / 分布式锁 |
| JJWT | 0.12.6 | JWT Token 生成与验证 |
| Elasticsearch | 8.x + IK 分词器 | 全文检索 |
| MinIO | 8.5.7 | 对象存储 (文件上传/下载) |
| MapStruct | 1.5.5.Final | 对象映射转换 |
| Spring AI | 1.1.5 | AI 能力集成 |
| Spring Security | (随 Boot 版本) | 认证授权框架 |
| Spring Cloud Gateway | (随 Cloud 版本) | API 网关 |
| Spring Data Elasticsearch | (随 Boot 版本) | ES 数据访问 |
| Lombok | (随 Boot 版本) | 代码简化 |
| Nacos | 2.x | 服务注册与配置中心 |

---

## 2. 系统架构

### 2.1 微服务架构图

```
                          +------------------+
                          |    客户端/App    |
                          +--------+---------+
                                   |
                                   v
                          +--------+---------+
                          |  API Gateway     |
                          |  (mall-gateway)  |
                          |  Port: 8080      |
                          |  - JWT 鉴权      |
                          |  - 路由转发      |
                          |  - CORS 跨域     |
                          +--------+---------+
                                   |
           +-----------+-----------+-----------+-----------+-----------+-----------+-----------+-----------+
           |           |           |           |           |           |           |           |           |
           v           v           v           v           v           v           v           v           v
   +-------+---+ +-----+----+ +---+------+ +--+-------+ +-+--------+ +--+------+ +--+------+ +-+--------+ +-+-----+
   | mall-auth | | mall-    | | mall-    | | mall-   | | mall-   | | mall-  | | mall-  | | mall-    | | mall- |
   | 认证服务  | | member   | | product  | | order   | | seckill | | ware   | | search | | third-   | | cart  |
   | :8081     | | 会员服务 | | 商品服务 | | 订单服务| | 秒杀营销| | 仓储服务| | 搜索服务| | party    | | 购物车 |
   |           | | :8082    | | :8083    | | :8084   | | :8085   | | :8086  | | :8087  | | :8088    | | :8089 |
   +-----------+ +-----+----+ +---+------+ +--+-------+ +-+--------+ +--+------+ +--+------+ +----------+ +-------+
                       |          |            |            |            |          |
                       |          |            |            |            |          |
                       v          v            v            v            v          v
                 +-------------------------------------------------------------+
                 |                     基础设施层                              |
                 |  +----------+  +--------+  +----------+  +------+  +------+ |
                 |  |  MySQL   |  | Redis  |  | RabbitMQ |  | Nacos|  | MinIO| |
                 |  | (5库)    |  | (缓存) |  | (消息)   |  | (注册)|  | (文件)| |
                 |  +----------+  +--------+  +----------+  +------+  +------+ |
                 |                                                             |
                 |  +-------------+  +------------------+  +------+           |
                 |  |Elasticsearch|  | Seata Server     |  |      |           |
                 |  | (全文检索)  |  | (分布式事务)     |  |      |           |
                 |  +-------------+  +------------------+  +------+           |
                 +-------------------------------------------------------------+
```

### 2.2 端口分配表

| 服务 | 端口 | 说明 |
|------|------|------|
| mall-gateway | 8080 | API 网关 (统一入口) |
| mall-auth | 8081 | 认证服务 |
| mall-member | 8082 | 会员服务 |
| mall-product | 8083 | 商品服务 |
| mall-order | 8084 | 订单服务 |
| mall-seckill | 8085 | 秒杀营销服务 |
| mall-ware | 8086 | 仓储服务 |
| mall-search | 8087 | 搜索服务 |
| mall-third-party | 8088 | 第三方服务 |
| mall-cart | 8089 | 购物车服务 |
| Nacos Server | 8848 | 注册/配置中心 |

---

## 3. 模块说明

### 3.1 模块总览

本项目共包含 **11 个模块**, 其中 1 个公共模块 + 10 个微服务模块:

```
mall (父工程)
  |-- mall-common          公共模块
  |-- mall-gateway         API 网关
  |-- mall-auth            认证服务
  |-- mall-member          会员服务
  |-- mall-product         商品服务
  |-- mall-order           订单服务
  |-- mall-seckill         秒杀营销服务
  |-- mall-ware            仓储服务
  |-- mall-search          搜索服务
  |-- mall-third-party     第三方服务
  |-- mall-cart            购物车服务
```

### 3.2 mall-common -- 公共模块

| 属性 | 说明 |
|------|------|
| **端口** | 无 (JAR 依赖) |
| **职责** | 提供全局公共工具类和基础组件 |
| **Java 文件数** | 11 |

**核心类**:

| 类名 | 说明 |
|------|------|
| `Result<T>` | 统一响应封装 |
| `BizException` | 业务异常定义 |
| `GlobalExceptionHandler` | 全局异常处理器 |
| `UserContext` | 用户上下文 (ThreadLocal) |
| `UserContextFilter` | 用户上下文过滤器 |
| `HeaderSigner` | HMAC-SHA256 请求签名工具 |
| `FeignRequestInterceptor` | Feign 请求拦截器 (传播用户上下文) |

---

### 3.3 mall-gateway -- API 网关

| 属性 | 说明 |
|------|------|
| **端口** | 8080 |
| **职责** | 统一入口, 路由转发, JWT 鉴权, CORS, 白名单 |
| **Java 文件数** | 4 |

**核心功能**:
- 基于 Spring Cloud Gateway 的路由转发
- `AuthGlobalFilter` -- JWT Token 验证, 注入用户信息到请求头
- 全局 CORS 跨域配置 (allowed-origins: `*`)
- 白名单路径免鉴权

---

### 3.4 mall-auth -- 认证服务

| 属性 | 说明 |
|------|------|
| **端口** | 8081 |
| **职责** | 用户登录/注册, JWT 管理, SMS 验证码 |
| **Java 文件数** | 23 |

**核心功能**:
- 用户名/密码登录 (BCrypt 加密)
- 手机号 + 短信验证码登录
- 用户注册
- JWT Token 签发与刷新
- Spring Security 安全配置
- 短信验证码发送 (模拟/RabbitMQ)

---

### 3.5 mall-member -- 会员服务

| 属性 | 说明 |
|------|------|
| **端口** | 8082 |
| **职责** | 会员管理, 收货地址, 积分/成长值, 标签, 客服工单 |
| **Java 文件数** | 64 |

**内部结构**:
- 14 个实体类 (Entity)
- 10 个服务类 (Service)
- 8 个控制器 (Controller)
- Feign 远程调用 (订单/商品服务)

**主要业务**:
- 会员 CRUD / 等级管理
- 收货地址管理
- 积分流水 / 成长值流水
- 用户标签 (人口学/行为/偏好)
- 登录日志
- 会员统计信息
- 会员收藏 (商品/专题)
- 客服工单系统

---

### 3.6 mall-product -- 商品服务

| 属性 | 说明 |
|------|------|
| **端口** | 8083 |
| **职责** | 商品 SPU/SKU 管理, 分类/品牌/属性, 商品审核 |
| **Java 文件数** | 77 |

**内部结构**:
- 15 个实体类 (Entity)
- 14 个服务类 (Service)
- 10 个控制器 (Controller)
- Redis 缓存 (分类树等)
- Feign 远程调用

**主要业务**:
- 三级分类树管理 (支持 Redis 缓存)
- 品牌管理 + 分类-品牌关联
- 属性管理 (基本属性/销售属性) + 属性分组
- SPU 全生命周期 (创建/上架/审核)
- SKU 管理 (价格/图片/销售属性)
- 商品审核日志
- 商品评论与回复

---

### 3.7 mall-order -- 订单服务

| 属性 | 说明 |
|------|------|
| **端口** | 8084 |
| **职责** | 订单管理, 支付, 退款退货 |
| **Java 文件数** | 45 |

**内部结构**:
- 9 个实体类 (Entity)
- 8 个服务类 (Service)
- 6 个控制器 (Controller)
- Seata 分布式事务 (@GlobalTransactional)

**主要业务**:
- 订单创建/取消/支付/发货/收货
- 订单状态机 (8 种状态)
- 订单项管理
- 支付信息记录
- 退款退货申请与审核
- 退款信息管理
- 订单操作日志
- 订单超时配置
- 秒杀订单与普通订单区分

---

### 3.8 mall-seckill -- 秒杀营销服务

| 属性 | 说明 |
|------|------|
| **端口** | 8085 |
| **职责** | 秒杀活动, 优惠券, 首页广告/专题, 促销策略 |
| **Java 文件数** | 55 |

**内部结构**:
- 16 个实体类 (Entity)
- 7 个服务类 (Service)
- 7 个控制器 (Controller)

**主要业务**:
- 秒杀活动管理 (场次/商品关联/秒杀订单)
- 优惠券管理 (满减/折扣/无门槛 + 领取/使用)
- 商品满减策略
- 商品阶梯价策略
- SPU 积分成长配置
- 会员价格体系
- 首页广告管理
- 首页专题管理
- 秒杀到货通知

---

### 3.9 mall-ware -- 仓储服务

| 属性 | 说明 |
|------|------|
| **端口** | 8086 |
| **职责** | 仓库管理, 库存管理, 采购管理 |
| **Java 文件数** | 38 |

**内部结构**:
- 7 个实体类 (Entity)
- 7 个服务类 (Service)
- 5 个控制器 (Controller)
- Feign 远程调用

**主要业务**:
- 仓库信息管理
- 商品库存 (仓库+SKU 维度)
- 库存锁定/解锁/扣减
- 库存变动日志
- 采购单管理
- 库存工作单 (Seata 分布式事务关联)

---

### 3.10 mall-search -- 搜索服务

| 属性 | 说明 |
|------|------|
| **端口** | 8087 |
| **职责** | Elasticsearch 全文检索 |
| **Java 文件数** | 12 |

**核心技术**:
- Spring Data Elasticsearch
- IK 中文分词器
- 支持关键词/分类/品牌/价格区间/排序搜索

---

### 3.11 mall-third-party -- 第三方服务

| 属性 | 说明 |
|------|------|
| **端口** | 8088 |
| **职责** | MinIO 文件上传/下载/预签名 |
| **Java 文件数** | 8 |

**主要功能**:
- 文件上传 (单文件/批量)
- 文件下载
- 预签名 URL 生成
- 日期目录结构 (`yyyy/MM/dd/`)

---

### 3.12 mall-cart -- 购物车服务

| 属性 | 说明 |
|------|------|
| **端口** | 8089 |
| **职责** | 购物车管理 |
| **Java 文件数** | 9 |

**核心特点**:
- **纯 Redis Hash 存储**, 无数据库表
- 以 `member_id` 为 Key, `sku_id` 为 Field
- 支持增/删/改/查/勾选/全选

---

## 4. 数据库设计

### 4.1 数据库概览

本项目共使用 **5 个独立数据库**, 所有表均使用 **雪花 ID (BIGINT, ASSIGN_ID)** 作为主键,
字符集统一为 **utf8mb4 / utf8mb4_general_ci**.

| 数据库 | 表数量 | 所属服务 | 说明 |
|--------|--------|----------|------|
| `mall_product` | 17 | mall-product | 商品相关 |
| `mall_member` | 18 | mall-member | 会员相关 |
| `mall_order` | 9 | mall-order | 订单相关 |
| `mall_seckill` | 16 | mall-seckill | 秒杀营销相关 |
| `mall_ware` | 7 | mall-ware | 仓储相关 |

> **总计**: 67 张数据表

### 4.2 mall_product -- 商品库 (17 表)

| # | 表名 | 中文注释 |
|---|------|----------|
| 1 | `pms_category` | 商品分类 (三级树形) |
| 2 | `pms_brand` | 品牌 |
| 3 | `pms_category_brand_relation` | 分类-品牌关联 (多对多) |
| 4 | `pms_attr_group` | 属性分组 (绑定到三级分类) |
| 5 | `pms_attr` | 属性定义 (基本/销售) |
| 6 | `pms_attr_value` | 属性预定义可选值 |
| 7 | `pms_attr_attrgroup_relation` | 属性-分组关联 |
| 8 | `pms_spu_info` | SPU 基本信息 |
| 9 | `pms_spu_info_desc` | SPU 富文本描述 |
| 10 | `pms_spu_images` | SPU 图片 |
| 11 | `pms_spu_attr_value` | SPU 基本属性值 |
| 12 | `pms_sku_info` | SKU 信息 |
| 13 | `pms_sku_images` | SKU 图片 |
| 14 | `pms_sku_sale_attr_value` | SKU 销售属性值 |
| 15 | `pms_spu_audit_log` | 商品审核日志 |
| 16 | `pms_spu_comment` | 商品评论 |
| 17 | `pms_comment_replay` | 评论回复 |

### 4.3 mall_member -- 会员库 (18 表)

| # | 表名 | 中文注释 |
|---|------|----------|
| 1 | `ums_member_level` | 会员等级 |
| 2 | `ums_member` | 会员主表 |
| 3 | `ums_member_auth` | 会员认证绑定 |
| 4 | `ums_member_receive_address` | 会员收货地址 |
| 5 | `ums_member_tag` | 用户标签定义 |
| 6 | `ums_member_tag_relation` | 用户-标签关联 |
| 7 | `ums_member_login_log` | 会员登录日志 |
| 8 | `ums_integration_record` | 积分流水 |
| 9 | `ums_growth_record` | 成长值流水 |
| 10 | `sys_client` | OAuth2 客户端注册 |
| 11 | `sys_oauth2_authorization` | OAuth2 授权记录 |
| 12 | `sys_oauth2_consent` | OAuth2 用户授权同意 |
| 13 | `ums_member_statistics_info` | 会员统计信息 |
| 14 | `ums_member_collect_spu` | 会员收藏商品 |
| 15 | `ums_member_collect_subject` | 会员收藏专题 |
| 16 | `ums_customer_service_ticket` | 客服工单 |
| 17 | `ums_customer_service_message` | 客服消息 |
| 18 | *(以上 17 张业务表)* | |

> 注: 会员库含 15 张核心业务表 + 3 张 OAuth2 系统表.

### 4.4 mall_order -- 订单库 (9 表)

| # | 表名 | 中文注释 |
|---|------|----------|
| 1 | `oms_order` | 订单主表 |
| 2 | `oms_order_item` | 订单项 |
| 3 | `oms_order_operate_history` | 订单操作日志 |
| 4 | `oms_payment_info` | 支付信息 |
| 5 | `oms_order_return_apply` | 退款退货申请 |
| 6 | `oms_order_return_reason` | 退货原因配置 |
| 7 | `oms_cart_item` | 购物车 (数据库副本) |
| 8 | `oms_order_setting` | 订单超时配置 |
| 9 | `oms_refund_info` | 退款信息 |

### 4.5 mall_seckill -- 秒杀营销库 (16 表)

| # | 表名 | 中文注释 |
|---|------|----------|
| 1 | `sms_seckill_session` | 秒杀活动场次 |
| 2 | `sms_seckill_sku_relation` | 秒杀活动商品关联 |
| 3 | `sms_seckill_order` | 秒杀订单 |
| 4 | `sms_coupon` | 优惠券 |
| 5 | `sms_coupon_history` | 优惠券领取记录 |
| 6 | `sms_sku_full_reduction` | 商品满减 |
| 7 | `sms_sku_ladder` | 商品阶梯价 |
| 8 | `sms_home_adv` | 首页广告 |
| 9 | `sms_home_subject` | 首页专题 |
| 10 | `sms_home_subject_spu` | 首页专题商品关联 |
| 11 | `sms_spu_bounds` | SPU 积分成长 |
| 12 | `sms_member_price` | 会员价格 |
| 13 | `sms_coupon_spu_category_relation` | 优惠券分类关联 |
| 14 | `sms_coupon_spu_relation` | 优惠券商品关联 |
| 15 | `sms_seckill_promotion` | 秒杀活动 |
| 16 | `sms_seckill_sku_notice` | 秒杀商品到货通知 |

### 4.6 mall_ware -- 仓储库 (7 表)

| # | 表名 | 中文注释 |
|---|------|----------|
| 1 | `wms_ware_info` | 仓库信息 |
| 2 | `wms_ware_sku` | 商品库存 (仓库+SKU 维度) |
| 3 | `wms_stock_log` | 库存变动日志 |
| 4 | `wms_purchase` | 采购单 |
| 5 | `wms_purchase_detail` | 采购单详情 |
| 6 | `wms_ware_order_task` | 库存工作单 (Seata 事务关联) |
| 7 | `wms_ware_order_task_detail` | 库存工作单详情 |

### 4.7 数据库公共规范

- **主键**: BIGINT, 使用 MyBatis-Plus `ASSIGN_ID` (雪花算法)
- **字符集**: `utf8mb4` / `utf8mb4_general_ci`
- **引擎**: InnoDB
- **时间字段**: `create_time` (NOT NULL DEFAULT CURRENT_TIMESTAMP), `update_time` (NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)
- **软删除**: 部分表使用 `delete_status` 字段
- **状态字段**: 统一使用 `TINYINT`, 含义在 COMMENT 中注明

---

## 5. 认证架构

### 5.1 JWT 验证流程

```
客户端请求 (携带 Authorization: Bearer <token>)
        |
        v
+-----------------------------+
|     API Gateway (:8080)     |
|  AuthGlobalFilter           |
+-----------------------------+
        |
        v
  路径在白名单中?  --是-->  直接放行, 转发到下游服务
        |
       否
        |
        v
  提取 Authorization Header
        |
        v
  解析 JWT Token (HS256)
        |
        +-- 解析失败 --> 401 Unauthorized
        |
        v
  提取 userId / username
        |
        v
  注入请求头:
    X-User-Id: <userId>
    X-Username: <username>
        |
        v
  转发到下游服务
        |
        v
+-----------------------------+
|     下游微服务               |
|  UserContextFilter          |
|  读取 X-User-Id / X-Username|
|  设置 UserContext (ThreadLocal)|
+-----------------------------+
```

### 5.2 白名单路径

以下路径无需 JWT 鉴权, 直接放行:

| 路径模式 | 说明 |
|----------|------|
| `/auth/**` | 认证相关接口 (登录/注册/验证码) |
| `/categories/**` | 商品分类查询 (C 端公开) |
| `/brands/**` | 品牌查询 (C 端公开) |
| `/spu/**` | SPU 查询 (C 端公开) |
| `/sku/**` | SKU 查询 (C 端公开) |
| `/feign/**` | 服务间内部 Feign 调用 |

### 5.3 JWT 配置

```yaml
jwt:
  secret: mall-hj-secret-key-must-be-at-least-256-bits-long-for-hs256
  header: Authorization
  token-prefix: "Bearer "
```

---

## 6. 服务间通信

### 6.1 OpenFeign + FallbackFactory 模式

服务间调用使用 **Spring Cloud OpenFeign**, 并通过 `FallbackFactory` 实现服务降级:

```
mall-order  --Feign-->  mall-product  (查询商品信息)
mall-order  --Feign-->  mall-ware     (锁定库存)
mall-order  --Feign-->  mall-member   (查询会员信息)
mall-seckill --Feign--> mall-order    (创建秒杀订单)
mall-seckill --Feign--> mall-product  (查询秒杀商品)
mall-search --Feign-->  mall-product  (同步商品数据)
...
```

### 6.2 FeignRequestInterceptor -- 用户上下文传播

```java
// FeignRequestInterceptor 在所有 Feign 调用中自动注入:
//   X-User-Id: <当前用户ID>
//   X-Username: <当前用户名>
// 确保下游服务也能获取调用者身份
```

### 6.3 HeaderSigner -- HMAC-SHA256 签名

为防止 Feign 接口被非授权服务调用, 内部接口使用 `HeaderSigner` 进行 HMAC-SHA256 请求签名:
- 签名字段包含时间戳 + 请求路径 + 服务标识
- 下游服务通过 `/feign/**` 白名单放行并验证签名

---

## 7. 缓存策略

### 7.1 Redis 使用场景

| 场景 | 数据结构 | Key 格式 | TTL | 说明 |
|------|----------|----------|-----|------|
| 分类树缓存 | String (JSON) | `category:tree` | 1 小时 | 三级分类树, 减少数据库查询 |
| JWT Token | String | `jwt:token:<userId>` | 24 小时 | Token 黑名单/续期管理 |
| SMS 验证码 | String | `sms:code:<phone>` | 5 分钟 | 短信验证码, 过期自动失效 |
| 购物车 | Hash | `cart:<memberId>` | - (永久) | Field: `skuId`, Value: JSON |

### 7.2 缓存一致性策略

- 分类树: 写操作后主动删除缓存, 下次查询时重建
- 购物车: 纯 Redis 存储, 无需缓存一致性问题
- 热点数据: 可根据业务需要增加本地缓存 (Caffeine)

---

## 8. 搜索服务

### 8.1 Elasticsearch 集成

| 项目 | 说明 |
|------|------|
| **引擎** | Elasticsearch 8.x |
| **分词器** | IK Analysis Plugin |
| **索引分词** | `ik_max_word` (最细粒度拆分) |
| **搜索分词** | `ik_smart` (智能分词) |
| **数据访问** | Spring Data Elasticsearch |

### 8.2 搜索能力

支持以下搜索维度:

| 搜索维度 | 说明 |
|----------|------|
| 关键词搜索 | 对商品名称/描述进行全文检索 |
| 分类筛选 | 按三级分类过滤 |
| 品牌筛选 | 按品牌 ID 过滤 |
| 价格区间 | 按价格范围过滤 (min ~ max) |
| 排序 | 按价格/销量/评分/上架时间排序 |

### 8.3 数据同步

商品数据通过 Feign 调用 mall-product 获取, 写入 Elasticsearch 索引.

---

## 9. 文件存储

### 9.1 MinIO 对象存储

| 项目 | 说明 |
|------|------|
| **存储引擎** | MinIO |
| **SDK 版本** | 8.5.7 |
| **目录结构** | `yyyy/MM/dd/<uuid>.<ext>` |
| **访问方式** | 预签名 URL |

### 9.2 API 能力

| 接口 | 说明 |
|------|------|
| 单文件上传 | POST, MultipartFile |
| 批量上传 | POST, MultipartFile[] |
| 删除文件 | DELETE, 按文件路径 |
| 预签名 URL | GET, 生成临时访问链接 |

---

## 10. API 网关路由表

所有请求统一通过 `http://host:8080/api/<service>/<path>` 访问.
Gateway 使用 `StripPrefix=1` 去除 `/api` 前缀后转发到对应服务.

| 路由 ID | 路径匹配 | 目标服务 | 转发路径 |
|---------|----------|----------|----------|
| mall-auth | `/api/auth/**` | `lb://mall-auth` | `/auth/**` |
| mall-member | `/api/member/**` | `lb://mall-member` | `/member/**` |
| mall-product | `/api/product/**` | `lb://mall-product` | `/product/**` |
| mall-order | `/api/order/**` | `lb://mall-order` | `/order/**` |
| mall-seckill | `/api/seckill/**` | `lb://mall-seckill` | `/seckill/**` |
| mall-ware | `/api/ware/**` | `lb://mall-ware` | `/ware/**` |
| mall-search | `/api/search/**` | `lb://mall-search` | `/search/**` |
| mall-third-party | `/api/thirdparty/**` | `lb://mall-third-party` | `/thirdparty/**` |
| mall-cart | `/api/cart/**` | `lb://mall-cart` | `/cart/**` |

> **说明**: `lb://` 表示使用 Nacos 负载均衡; `StripPrefix=1` 表示去除路径第一段 (`/api`).

### 10.1 CORS 配置

```yaml
# 全局 CORS (允许所有来源)
allowed-origins: "*"
allowed-methods: "*"
allowed-headers: "*"
allow-credentials: true
```

---

## 11. 统一规范

### 11.1 统一响应 -- Result\<T\>

```java
public class Result<T> {
    private Integer code;    // 业务状态码 (200=成功, 400=参数错误, 500=服务器错误)
    private String message;  // 提示信息
    private T data;          // 业务数据
}
```

### 11.2 异常处理

```
BizException (业务异常)
    |
    v
GlobalExceptionHandler (@RestControllerAdvice)
    |
    v
Result.error(code, message)
```

- 业务层抛出 `BizException` 携带错误码和消息
- `GlobalExceptionHandler` 统一捕获, 返回 `Result` 格式
- 不允许向客户端暴露堆栈信息

### 11.3 分页规范

- 使用 MyBatis-Plus 的 `IPage<T>` / `Page<T>` 进行分页
- 默认分页参数: `pageNum=1`, `pageSize=10`
- 响应包含: `total`, `pages`, `current`, `records`

### 11.4 RESTful API 设计

| 端 | 规范 |
|----|------|
| **Admin (后台管理)** | 完整 CRUD: GET (列表/详情), POST (新增), PUT (修改), DELETE (删除) |
| **C 端 (用户)** | 以只读为主: GET (列表/详情/搜索); 写操作: POST (下单/领券/加购物车等) |

### 11.5 服务命名规范

| 规范 | 示例 |
|------|------|
| Controller 路径 | `/product/spu`, `/order/order` |
| Service 接口 | `SpuService`, `OrderService` |
| Mapper 接口 | `SpuMapper extends BaseMapper<Spu>` |
| Entity 命名 | 与表名对应 (驼峰): `PmsSpuInfo` -> `pms_spu_info` |

---

## 12. 部署要求

### 12.1 运行环境

| 组件 | 最低版本 | 说明 |
|------|----------|------|
| **JDK** | 21+ | 项目使用 Java 21 特性 |
| **MySQL** | 8.0+ | 5 个独立数据库, utf8mb4 |
| **Redis** | 7.0+ | 缓存/Session/分布式锁 |
| **Nacos** | 2.x | 服务注册与配置中心 |
| **RabbitMQ** | 3.x | 消息队列 |
| **Elasticsearch** | 8.x | 全文检索引擎 |
| **IK 分词器** | 对应 ES 版本 | 中文分词插件 |
| **MinIO** | 最新稳定版 | 对象存储 |
| **Seata Server** | 2.6.0 | 分布式事务协调器 |

### 12.2 基础设施启动顺序

```
1. MySQL       (创建数据库 + 执行 DDL)
2. Redis       (缓存服务)
3. Nacos       (注册中心, 默认 8848)
4. RabbitMQ    (消息队列)
5. Elasticsearch + IK  (搜索引擎)
6. MinIO       (文件存储)
7. Seata Server (分布式事务)
8. 各微服务     (按需启动, 至少 gateway + auth + 业务服务)
```

### 12.3 数据库初始化

按以下顺序执行 SQL:

1. `docs/mall_product/mall_product.sql` -- 商品库
2. `docs/mall_member/mall_member.sql` -- 会员库
3. `docs/mall_order/mall_order.sql` -- 订单库
4. `docs/mall_seckill/mall_seckill.sql` -- 秒杀营销库
5. `docs/mall_ware/mall_ware.sql` -- 仓储库

> 购物车服务 (mall-cart) 使用纯 Redis 存储, 无需数据库.

---

## 13. 文档目录

### 13.1 总文档

| 文件 | 说明 |
|------|------|
| `docs/Mall-HJ-总文档.md` | 本文档 -- 项目总体架构文档 |

### 13.2 各模块文档

| 模块 | 需求文档 | 技术方案 | SQL DDL |
|------|----------|----------|---------|
| mall_product | `docs/mall_product/需求文档.md` | `docs/mall_product/技术方案.md` | `docs/mall_product/mall_product.sql` |
| mall_member | `docs/mall_member/需求文档.md` | `docs/mall_member/技术方案.md` | `docs/mall_member/mall_member.sql` |
| mall_order | `docs/mall_order/需求文档.md` | `docs/mall_order/技术方案.md` | `docs/mall_order/mall_order.sql` |
| mall_seckill | `docs/mall_seckill/需求文档.md` | `docs/mall_seckill/技术方案.md` | `docs/mall_seckill/mall_seckill.sql` |
| mall_ware | `docs/mall_ware/需求文档.md` | `docs/mall_ware/技术方案.md` | `docs/mall_ware/mall_ware.sql` |
| mall_auth | `docs/mall_auth/需求文档.md` | `docs/mall_auth/技术方案.md` | -- (无独立数据库) |
| mall_gateway | `docs/mall_gateway/需求文档.md` | `docs/mall_gateway/技术方案.md` | -- (无独立数据库) |
| mall_search | `docs/mall_search/需求文档.md` | `docs/mall_search/技术方案.md` | -- (使用 Elasticsearch) |
| mall_third_party | `docs/mall_third_party/需求文档.md` | `docs/mall_third_party/技术方案.md` | -- (使用 MinIO) |
| mall_cart | `docs/mall_cart/需求文档.md` | `docs/mall_cart/技术方案.md` | -- (纯 Redis 存储) |

### 13.3 完整文件树

```
docs/
  |-- Mall-HJ-总文档.md                          <-- 本文档
  |
  |-- mall_product/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |   |-- mall_product.sql
  |
  |-- mall_member/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |   |-- mall_member.sql
  |
  |-- mall_order/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |   |-- mall_order.sql
  |
  |-- mall_seckill/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |   |-- mall_seckill.sql
  |
  |-- mall_ware/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |   |-- mall_ware.sql
  |
  |-- mall_auth/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |
  |-- mall_gateway/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |
  |-- mall_search/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |
  |-- mall_third_party/
  |   |-- 需求文档.md
  |   |-- 技术方案.md
  |
  |-- mall_cart/
      |-- 需求文档.md
      |-- 技术方案.md
```

---

> **文档维护说明**: 本文档应随项目迭代同步更新. 涉及模块增删、端口变更、技术栈升级时, 请及时修改对应章节.

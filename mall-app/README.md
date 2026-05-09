# Mall-HJ 移动端应用

Mall-HJ 电商平台的 uni-app 移动端应用，支持 H5、微信小程序和原生 App。

## 项目简介

这是 Mall-HJ 电商平台的移动端应用，基于 uni-app + Vue 3 + Pinia 开发，为用户提供完整的移动购物体验。

## 技术栈

- **框架**: uni-app 3.0
- **UI**: Vue 3 Composition API
- **状态管理**: Pinia
- **样式**: SCSS
- **构建工具**: Vite 6.0
- **代码规范**: ESLint

## 项目结构

```
mall-app/
├── api/                    # API 接口封装
│   ├── request.js         # 统一请求封装
│   ├── auth.js            # 认证相关接口
│   ├── product.js         # 商品相关接口
│   ├── search.js          # 搜索相关接口
│   ├── member.js          # 会员相关接口
│   ├── address.js         # 地址相关接口
│   ├── order.js           # 订单相关接口
│   ├── cart.js            # 购物车相关接口
│   ├── seckill.js         # 秒杀相关接口
│   ├── upload.js          # 文件上传接口
│   └── collect.js         # 收藏相关接口
├── components/            # 公共组件
│   ├── ProductCard.vue    # 商品卡片组件
│   ├── LoadMore.vue       # 加载更多组件
│   ├── EmptyState.vue     # 空状态组件
│   ├── CountdownTimer.vue # 倒计时组件
│   └── AddressCard.vue    # 地址卡片组件
├── pages/                 # 页面
│   ├── index/             # 首页
│   ├── category/          # 分类页
│   ├── cart/              # 购物车
│   ├── me/                # 我的
│   ├── product/           # 商品详情
│   ├── search/            # 搜索页
│   ├── auth/              # 登录注册
│   ├── order/             # 订单相关
│   ├── user/              # 用户中心
│   └── seckill/           # 秒杀活动
├── store/                 # 状态管理
│   ├── index.js           # Pinia 实例
│   ├── user.js            # 用户状态
│   └── cart.js            # 购物车状态
├── static/                # 静态资源
│   └── images/            # 图片资源
├── utils/                 # 工具函数
│   └── index.js           # 通用工具
├── App.vue                # 应用入口
├── main.js                # 主入口文件
├── pages.json             # 页面配置
├── manifest.json          # 应用配置
├── vite.config.ts         # Vite 配置
├── package.json           # 项目依赖
└── uni.scss               # 全局样式变量

```

## 功能特性

### 用户功能
- ✅ 用户名密码登录
- ✅ 手机号验证码登录
- ✅ 用户注册
- ✅ 个人信息管理
- ✅ 头像上传

### 商品功能
- ✅ 商品浏览
- ✅ 商品搜索
- ✅ 分类浏览
- ✅ 商品详情
- ✅ SKU 选择
- ✅ 商品收藏

### 购物车功能
- ✅ 加入购物车
- ✅ 购物车管理
- ✅ 数量修改
- ✅ 商品选择
- ✅ 批量删除

### 订单功能
- ✅ 创建订单
- ✅ 订单列表
- ✅ 订单详情
- ✅ 订单支付
- ✅ 取消订单
- ✅ 确认收货

### 地址管理
- ✅ 地址列表
- ✅ 添加地址
- ✅ 编辑地址
- ✅ 删除地址
- ✅ 设置默认地址

### 秒杀功能
- ✅ 秒杀活动列表
- ✅ 秒杀商品详情
- ✅ 秒杀倒计时
- ✅ 秒杀下单

### 优惠券
- ✅ 优惠券领取
- ✅ 我的优惠券
- ✅ 订单使用优惠券

## 快速开始

### 环境要求

- Node.js >= 16.0.0
- npm >= 8.0.0 或 pnpm >= 7.0.0
- HBuilderX (可选，用于 uni-app 开发)

### 安装依赖

```bash
# 使用 npm
npm install

# 或使用 pnpm
pnpm install
```

### 开发运行

```bash
# H5 开发
npm run dev:h5

# 微信小程序开发
npm run dev:mp-weixin

# App 开发 (需要使用 HBuilderX)
# 在 HBuilderX 中打开项目，运行到手机或模拟器
```

### 构建发布

```bash
# H5 构建
npm run build:h5

# 微信小程序构建
npm run build:mp-weixin
```

## 配置说明

### 后端接口地址

在 `api/request.js` 中配置后端接口地址：

```javascript
const BASE_URL = 'http://localhost:8080/api'
```

### 微信小程序 AppID

在 `manifest.json` 的 `mp-weixin` 配置中填写你的小程序 AppID：

```json
{
  "mp-weixin": {
    "appid": "your-appid"
  }
}
```

### 应用配置

在 `pages.json` 中配置页面路由和 tabBar：

```json
{
  "pages": [...],
  "tabBar": {...}
}
```

## 状态管理

### 用户状态 (useUserStore)

```javascript
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

// 登录
await userStore.loginByUsernameAction({ username, password })

// 退出登录
await userStore.logout()

// 检查登录状态
userStore.checkAuth()

// 获取用户信息
await userStore.fetchUserInfo()
```

### 购物车状态 (useCartStore)

```javascript
import { useCartStore } from '@/store/cart'

const cartStore = useCartStore()

// 获取购物车
await cartStore.fetchCart()

// 添加商品
await cartStore.addToCart(skuId, count)

// 更新数量
await cartStore.updateQuantity(skuId, count)

// 选择商品
await cartStore.checkItem(skuId, checked)
```

## API 调用

所有 API 接口都在 `api/` 目录下统一封装：

```javascript
import { getSpuDetail } from '@/api/product'
import { createOrder } from '@/api/order'

// 获取商品详情
const detail = await getSpuDetail(spuId)

// 创建订单
const order = await createOrder(orderData)
```

## 工具函数

在 `utils/index.js` 中提供了常用的工具函数：

```javascript
import { formatDate, formatPrice, getImageUrl } from '@/utils'

// 格式化日期
const dateStr = formatDate(new Date(), 'YYYY-MM-DD HH:mm:ss')

// 格式化价格
const priceStr = formatPrice(99.9)

// 获取图片完整URL
const imageUrl = getImageUrl('/images/product.jpg')
```

## 样式开发

### 全局样式变量

在 `uni.scss` 中定义了全局 SCSS 变量：

```scss
$primary-color: #e93b3d;
$text-primary: #333333;
$bg-color: #f5f5f5;
// ...
```

### 响应式单位

uni-app 使用 `rpx` 作为响应式单位，设计稿宽度为 750rpx。

## 开发规范

### 命名规范

- 组件名：大驼峰命名 (PascalCase)
- 文件夹名：小写短横线命名 (kebab-case)
- 变量/函数名：小驼峰命名 (camelCase)

### 代码风格

- 使用 Vue 3 Composition API
- 优先使用 `<script setup>` 语法
- 组件使用 props 定义数据类型
- 事件使用 emit 定义

### 注释规范

```javascript
/**
 * 函数功能说明
 * @param {类型} 参数名 - 参数说明
 * @returns {类型} - 返回值说明
 */
```

## 常见问题

### 1. 跨域问题

H5 开发时可能遇到跨域问题，在 `vite.config.ts` 中配置代理：

```javascript
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

### 2. 微信小程序登录

需要在微信公众平台配置服务器域名，并确保后端接口域名在小程序的合法域名列表中。

### 3. 图片上传

使用 `uni.chooseImage` 和 `uni.uploadFile` 上传图片，需要确保后端提供了上传接口。

## 部署发布

### H5 部署

1. 运行 `npm run build:h5`
2. 将 `dist/build/h5` 目录部署到 Web 服务器

### 微信小程序发布

1. 运行 `npm run build:mp-weixin`
2. 使用微信开发者工具打开 `dist/build/mp-weixin` 目录
3. 上传代码到微信平台
4. 提交审核

### App 发布

1. 使用 HBuilderX 打开项目
2. 选择"发行" -> "原生App-云打包"
3. 配置应用图标和启动图
4. 选择打包平台
5. 下载安装包并发布到应用商店

## 技术支持

如有问题，请联系开发团队或查看项目文档。

## 许可证

MIT License

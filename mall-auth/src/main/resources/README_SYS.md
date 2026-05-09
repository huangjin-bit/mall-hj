# 系统管理模块使用说明

## 概述

mall-auth 服务新增了完整的若依风格 RBAC 系统管理功能，支持管理员用户的认证授权，与原有的会员认证系统完全隔离。

## 数据库准备

### 1. 执行 SQL 脚本

在 `mall_member` 数据库中执行以下 SQL 脚本：

```bash
mysql -u root -p mall_member < src/main/resources/sql/sys_tables.sql
```

或者直接在数据库客户端中执行 `src/main/resources/sql/sys_tables.sql` 文件。

### 2. 默认账号

系统初始化后，默认创建一个超级管理员账号：

- 用户名：`admin`
- 密码：`admin123`

**登录后请立即修改密码！**

## API 接口

### 认证接口

#### 管理员登录
```
POST /sys/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}

Response:
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "admin",
    "nickname": "超级管理员",
    "avatar": null
  }
}
```

#### 退出登录
```
GET /sys/logout
Authorization: Bearer {token}
```

#### 获取用户信息
```
GET /sys/user/info
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "user": {...},
    "roles": ["admin"],
    "permissions": ["system:user:list", "system:role:list", ...]
  }
}
```

#### 获取用户路由
```
GET /sys/user/routers
Authorization: Bearer {token}

Response:
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "name": "System",
      "path": "/system",
      "component": null,
      "redirect": null,
      "meta": {
        "title": "系统管理",
        "icon": "setting",
        "noCache": 0,
        "link": null
      },
      "children": [...]
    }
  ]
}
```

### 用户管理接口

#### 分页查询用户列表
```
GET /sys/user/list?current=1&size=10&username=&status=1
Authorization: Bearer {token}
```

#### 创建用户
```
POST /sys/user
Authorization: Bearer {token}
Content-Type: application/json

{
  "username": "testuser",
  "nickname": "测试用户",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138000",
  "status": 1,
  "deptId": 2,
  "roleIds": [1]
}
```

#### 更新用户
```
PUT /sys/user
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": 2,
  "nickname": "测试用户2",
  "email": "test2@example.com",
  "status": 1
}
```

#### 删除用户
```
DELETE /sys/user/{id}
Authorization: Bearer {token}
```

#### 重置密码
```
PUT /sys/user/{id}/reset-password?newPassword=123456
Authorization: Bearer {token}
```

#### 分配角色
```
PUT /sys/user/{id}/roles
Authorization: Bearer {token}
Content-Type: application/json

[1, 2]
```

### 角色管理接口

#### 分页查询角色列表
```
GET /sys/role/list?current=1&size=10&roleName=&status=1
Authorization: Bearer {token}
```

#### 查询所有角色（下拉框）
```
GET /sys/role/all
Authorization: Bearer {token}
```

#### 创建角色
```
POST /sys/role
Authorization: Bearer {token}
Content-Type: application/json

{
  "roleName": "测试角色",
  "roleKey": "test",
  "sort": 10,
  "status": 1,
  "menuIds": [1, 100, 101]
}
```

#### 为角色分配菜单
```
PUT /sys/role/{id}/menus
Authorization: Bearer {token}
Content-Type: application/json

[1, 100, 101, 102]
```

### 菜单管理接口

#### 查询菜单树
```
GET /sys/menu/tree
Authorization: Bearer {token}
```

#### 创建菜单
```
POST /sys/menu
Authorization: Bearer {token}
Content-Type: application/json

{
  "parentId": 1,
  "name": "测试菜单",
  "path": "/test",
  "component": "test/index",
  "menuType": "C",
  "perms": "test:list",
  "icon": "test",
  "sort": 10,
  "visible": 1,
  "status": 1
}
```

#### 更新菜单
```
PUT /sys/menu
Authorization: Bearer {token}
Content-Type: application/json

{
  "id": 200,
  "name": "测试菜单2"
}
```

#### 删除菜单
```
DELETE /sys/menu/{id}
Authorization: Bearer {token}
```

### 部门管理接口

#### 查询部门树
```
GET /sys/dept/tree
Authorization: Bearer {token}
```

#### 创建部门
```
POST /sys/dept
Authorization: Bearer {token}
Content-Type: application/json

{
  "parentId": 1,
  "deptName": "测试部门",
  "sort": 10,
  "leader": "张三",
  "phone": "13800138000",
  "email": "test@example.com",
  "status": 1
}
```

### 操作日志接口

#### 分页查询操作日志
```
GET /sys/oper-log/list?current=1&size=10&title=&operType=1
Authorization: Bearer {token}
```

## JWT Token 说明

### Token 结构

管理员 JWT Token 包含以下信息：

```json
{
  "userId": 1,
  "username": "admin",
  "userType": "admin",
  "deptId": null,
  "iat": 1234567890,
  "exp": 1234654290
}
```

### Token 使用

在请求头中添加：

```
Authorization: Bearer {token}
```

### 用户类型区分

- `userType: "admin"` - 管理员用户
- `userType: "member"` - 会员用户

网关可以通过 `userType` 字段区分不同类型的用户。

## 权限设计

### 角色类型

- **超级管理员**：拥有所有权限
- **普通角色**：根据配置的菜单权限访问对应功能

### 数据权限

- **全部数据权限**：可以查看所有数据
- **自定义数据权限**：可以查看指定部门的数据
- **本部门数据权限**：只能查看本部门的数据
- **本部门及以下数据权限**：可以查看本部门及子部门的数据
- **仅本人数据权限**：只能查看自己创建的数据

### 菜单类型

- **M (目录)**：一级菜单，包含子菜单
- **C (菜单)**：具体的功能页面
- **F (按钮)**：页面上的操作按钮权限

## 安全建议

1. **修改默认密码**：首次登录后立即修改 admin 账号的默认密码
2. **定期更换密码**：建议管理员定期更换密码
3. **最小权限原则**：为用户分配最小必要的权限
4. **密码策略**：生产环境建议实施强密码策略
5. **HTTPS**：生产环境必须使用 HTTPS 传输
6. **Token 过期**：默认 Token 有效期为 24 小时，可根据需要调整

## 注意事项

1. 系统管理接口路径为 `/sys/**`，与会员认证接口 `/auth/**` 完全隔离
2. 所有系统管理接口都需要在请求头中携带有效的 JWT Token
3. 密码使用 BCrypt 加密存储，无法解密
4. 用户登录后会更新登录 IP 和登录时间
5. 删除菜单/部门前会检查是否有子节点
6. 所有分页接口默认每页 10 条记录
7. 时间字段使用 LocalDateTime 类型，数据库使用 DATETIME

## 下一步开发

建议后续添加的功能：

1. **操作日志注解**：使用 AOP 自动记录操作日志
2. **数据权限过滤**：在 MyBatis-Plus 中实现数据权限过滤
3. **在线用户管理**：查看和管理在线用户
4. **密码策略**：强制密码复杂度和定期更换
5. **多数据源**：将系统表迁移到独立的数据库
6. **缓存优化**：使用 Redis 缓存用户权限信息
7. **接口限流**：防止暴力破解和恶意访问

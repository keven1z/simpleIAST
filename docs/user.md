# 用户管理

## 添加用户

### 基本信息

**地址**：/api/v1/user/add
**请求方式**：POST
**请求数据类型**：application/json
**响应数据结构**：R«string»

### 接口描述

创建新用户

### 请求示例

```json
{
  "created": "",
  "email": "admin@example.com",
  "id": 1,
  "password": "",
  "phone": 13800138000,
  "state": "ENABLED",
  "updated": "",
  "username": "admin"
}
```

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| userEntity | 是 | UserEntity | 用户信息实体类 |
| &emsp;&emsp;created | 否 | string | 账号创建时间 |
| &emsp;&emsp;email | 否 | string | 注册邮箱 |
| &emsp;&emsp;id | 否 | integer | 用户ID |
| &emsp;&emsp;password | 是 | string | 密码 |
| &emsp;&emsp;phone | 否 | string | 注册手机号 |
| &emsp;&emsp;state | 否 | string | 账号状态 |
| &emsp;&emsp;updated | 否 | string | 账号更新时间 |
| &emsp;&emsp;username | 是 | string | 用户名 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | string | 响应数据 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": "",
  "flag": true,
  "message": ""
}
```

## 删除用户

### 基本信息

**地址**：/api/v1/user/delete
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«string»

### 接口描述

删除指定用户，支持批量删除

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| id | 是 | string | 用户ID，多个ID用逗号分隔 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | string | 响应数据 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": "",
  "flag": true,
  "message": ""
}
```

## 根据ID获取用户

### 基本信息

**地址**：/api/v1/user/get
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«UserEntity»

### 接口描述

通过用户ID查询用户信息

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| id | 是 | string | 用户ID |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | UserEntity | 用户实体 |
| &emsp;&emsp;created | string | 账号创建时间 |
| &emsp;&emsp;email | string | 注册邮箱 |
| &emsp;&emsp;id | integer | 用户ID |
| &emsp;&emsp;password | string | 密码 |
| &emsp;&emsp;phone | string | 注册手机号 |
| &emsp;&emsp;state | string | 账号状态 |
| &emsp;&emsp;updated | string | 账号更新时间 |
| &emsp;&emsp;username | string | 用户名 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": {
    "created": "",
    "email": "admin@example.com",
    "id": 1,
    "password": "",
    "phone": 13800138000,
    "state": "ENABLED",
    "updated": "",
    "username": "admin"
  },
  "flag": true,
  "message": ""
}
```

## 根据用户名获取用户

### 基本信息

**地址**：/api/v1/user/getByName
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«UserEntity»

### 接口描述

通过用户名查询用户信息

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| name | 是 | string | 用户名 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | UserEntity | 用户实体 |
| &emsp;&emsp;created | string | 账号创建时间 |
| &emsp;&emsp;email | string | 注册邮箱 |
| &emsp;&emsp;id | integer | 用户ID |
| &emsp;&emsp;password | string | 密码 |
| &emsp;&emsp;phone | string | 注册手机号 |
| &emsp;&emsp;state | string | 账号状态 |
| &emsp;&emsp;updated | string | 账号更新时间 |
| &emsp;&emsp;username | string | 用户名 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": {
    "created": "",
    "email": "admin@example.com",
    "id": 1,
    "password": "",
    "phone": 13800138000,
    "state": "ENABLED",
    "updated": "",
    "username": "admin"
  },
  "flag": true,
  "message": ""
}
```

## 获取用户信息

### 基本信息

**地址**：/api/v1/user/info
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«UserEntity»

### 接口描述

获取当前登录用户的详细信息

### 请求参数

暂无

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | UserEntity | 用户实体 |
| &emsp;&emsp;created | string | 账号创建时间 |
| &emsp;&emsp;email | string | 注册邮箱 |
| &emsp;&emsp;id | integer | 用户ID |
| &emsp;&emsp;password | string | 密码 |
| &emsp;&emsp;phone | string | 注册手机号 |
| &emsp;&emsp;state | string | 账号状态 |
| &emsp;&emsp;updated | string | 账号更新时间 |
| &emsp;&emsp;username | string | 用户名 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": {
    "created": "",
    "email": "admin@example.com",
    "id": 1,
    "password": "",
    "phone": 13800138000,
    "state": "ENABLED",
    "updated": "",
    "username": "admin"
  },
  "flag": true,
  "message": ""
}
```

## 获取用户列表

### 基本信息

**地址**：/api/v1/user/list
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«PageInfo«UserEntity»»

### 接口描述

分页查询用户列表

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| pageNum | 是 | integer | 页码 |
| pageSize | 是 | integer | 每页大小 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | PageInfo«UserEntity» | 分页用户信息 |
| &emsp;&emsp;endRow | integer | 结束行 |
| &emsp;&emsp;hasNextPage | boolean | 是否有下一页 |
| &emsp;&emsp;hasPreviousPage | boolean | 是否有上一页 |
| &emsp;&emsp;isFirstPage | boolean | 是否是第一页 |
| &emsp;&emsp;isLastPage | boolean | 是否是最后一页 |
| &emsp;&emsp;list | array | 用户列表 |
| &emsp;&emsp;&emsp;&emsp;created | string | 账号创建时间 |
| &emsp;&emsp;&emsp;&emsp;email | string | 注册邮箱 |
| &emsp;&emsp;&emsp;&emsp;id | integer | 用户ID |
| &emsp;&emsp;&emsp;&emsp;password | string | 密码 |
| &emsp;&emsp;&emsp;&emsp;phone | string | 注册手机号 |
| &emsp;&emsp;&emsp;&emsp;state | string | 账号状态 |
| &emsp;&emsp;&emsp;&emsp;updated | string | 账号更新时间 |
| &emsp;&emsp;&emsp;&emsp;username | string | 用户名 |
| &emsp;&emsp;navigateFirstPage | integer | 导航第一页 |
| &emsp;&emsp;navigateLastPage | integer | 导航最后一页 |
| &emsp;&emsp;navigatePages | integer | 导航页码数 |
| &emsp;&emsp;navigatepageNums | array | 导航页码数组 |
| &emsp;&emsp;nextPage | integer | 下一页 |
| &emsp;&emsp;pageNum | integer | 当前页码 |
| &emsp;&emsp;pageSize | integer | 每页大小 |
| &emsp;&emsp;pages | integer | 总页数 |
| &emsp;&emsp;prePage | integer | 上一页 |
| &emsp;&emsp;size | integer | 当前页大小 |
| &emsp;&emsp;startRow | integer | 开始行 |
| &emsp;&emsp;total | integer | 总记录数 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": {
    "endRow": 0,
    "hasNextPage": true,
    "hasPreviousPage": true,
    "isFirstPage": true,
    "isLastPage": true,
    "list": [
      {
        "created": "",
        "email": "admin@example.com",
        "id": 1,
        "password": "",
        "phone": 13800138000,
        "state": "ENABLED",
        "updated": "",
        "username": "admin"
      }
    ],
    "navigateFirstPage": 0,
    "navigateLastPage": 0,
    "navigatePages": 0,
    "navigatepageNums": [],
    "nextPage": 0,
    "pageNum": 0,
    "pageSize": 0,
    "pages": 0,
    "prePage": 0,
    "size": 0,
    "startRow": 0,
    "total": 0
  },
  "flag": true,
  "message": ""
}
```

## 用户登录

### 基本信息

**地址**：/api/v1/user/login
**请求方式**：POST
**请求数据类型**：application/json
**响应数据结构**：R«AuthenticationDto»

### 接口描述

用户登录获取访问令牌

### 请求示例

```json
{
  "created": "",
  "email": "admin@example.com",
  "id": 1,
  "password": "",
  "phone": 13800138000,
  "state": "ENABLED",
  "updated": "",
  "username": "admin"
}
```

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| userEntity | 是 | UserEntity | 用户信息实体类 |
| &emsp;&emsp;created | 否 | string | 账号创建时间 |
| &emsp;&emsp;email | 否 | string | 注册邮箱 |
| &emsp;&emsp;id | 否 | integer | 用户ID |
| &emsp;&emsp;password | 是 | string | 密码 |
| &emsp;&emsp;phone | 否 | string | 注册手机号 |
| &emsp;&emsp;state | 否 | string | 账号状态 |
| &emsp;&emsp;updated | 否 | string | 账号更新时间 |
| &emsp;&emsp;username | 是 | string | 用户名 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | AuthenticationDto | 认证信息 |
| &emsp;&emsp;agentId | string | 代理ID |
| &emsp;&emsp;token | string | 访问令牌 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": {
    "agentId": "",
    "token": ""
  },
  "flag": true,
  "message": ""
}
```

## 更新用户

### 基本信息

**地址**：/api/v1/user/update
**请求方式**：POST
**请求数据类型**：application/json
**响应数据结构**：R«string»

### 接口描述

更新用户信息

### 请求示例

```json
{
  "created": "",
  "email": "admin@example.com",
  "id": 1,
  "password": "",
  "phone": 13800138000,
  "state": "ENABLED",
  "updated": "",
  "username": "admin"
}
```

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| userEntity | 是 | UserEntity | 用户信息实体类 |
| &emsp;&emsp;created | 否 | string | 账号创建时间 |
| &emsp;&emsp;email | 否 | string | 注册邮箱 |
| &emsp;&emsp;id | 否 | integer | 用户ID |
| &emsp;&emsp;password | 是 | string | 密码 |
| &emsp;&emsp;phone | 否 | string | 注册手机号 |
| &emsp;&emsp;state | 否 | string | 账号状态 |
| &emsp;&emsp;updated | 否 | string | 账号更新时间 |
| &emsp;&emsp;username | 是 | string | 用户名 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | string | 响应数据 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": "",
  "flag": true,
  "message": ""
}
```
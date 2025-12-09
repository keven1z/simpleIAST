# 漏洞评论接口

## 添加漏洞评论

### 基本信息

**地址**：/api/v1/comment
**请求方式**：POST
**请求数据类型**：application/json
**响应数据结构**：R«string»

### 接口描述

添加漏洞评论

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| commentEntity | 是 | VulnerabilityCommentEntity | 漏洞评论实体类 |
| &emsp;&emsp;content | 是 | string | 评论内容 |
| &emsp;&emsp;createdAt | 否 | string | 评论时间 |
| &emsp;&emsp;id | 否 | integer | 评论ID |
| &emsp;&emsp;reportId | 是 | integer | 漏洞ID |
| &emsp;&emsp;user | 否 | UserEntity | 用户信息 |
| &emsp;&emsp;&emsp;&emsp;created | 否 | string | 账号创建时间 |
| &emsp;&emsp;&emsp;&emsp;email | 否 | string | 注册邮箱 |
| &emsp;&emsp;&emsp;&emsp;id | 否 | integer | 用户ID |
| &emsp;&emsp;&emsp;&emsp;password | 是 | string | 密码 |
| &emsp;&emsp;&emsp;&emsp;phone | 否 | string | 注册手机号 |
| &emsp;&emsp;&emsp;&emsp;state | 否 | string | 账号状态 |
| &emsp;&emsp;&emsp;&emsp;updated | 否 | string | 账号更新时间 |
| &emsp;&emsp;&emsp;&emsp;username | 是 | string | 用户名 |
| &emsp;&emsp;userId | 是 | integer | 用户ID |

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

### 请求示例

```json
{
  "content": "",
  "createdAt": "",
  "id": 1,
  "reportId": 1,
  "user": {
    "created": "",
    "email": "admin@example.com",
    "id": 1,
    "password": "",
    "phone": 13800138000,
    "state": "ENABLED",
    "updated": "",
    "username": "admin"
  },
  "userId": 1
}
```

### 响应示例

```json
{
  "data": "",
  "flag": true,
  "message": ""
}
```

## 获取指定漏洞的评论列表

### 基本信息

**地址**：/api/v1/comment/list/{reportId}
**请求方式**：GET
**响应数据结构**：R«List«VulnerabilityCommentEntity»»

### 接口描述

获取指定漏洞的评论列表

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| reportId | 是 | integer | 漏洞ID |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | array | 评论列表 |
| &emsp;&emsp;content | string | 评论内容 |
| &emsp;&emsp;createdAt | string | 评论时间 |
| &emsp;&emsp;id | integer | 评论ID |
| &emsp;&emsp;reportId | integer | 漏洞ID |
| &emsp;&emsp;user | UserEntity | 用户信息 |
| &emsp;&emsp;&emsp;&emsp;created | string | 账号创建时间 |
| &emsp;&emsp;&emsp;&emsp;email | string | 注册邮箱 |
| &emsp;&emsp;&emsp;&emsp;id | integer | 用户ID |
| &emsp;&emsp;&emsp;&emsp;password | string | 密码 |
| &emsp;&emsp;&emsp;&emsp;phone | string | 注册手机号 |
| &emsp;&emsp;&emsp;&emsp;state | string | 账号状态 |
| &emsp;&emsp;&emsp;&emsp;updated | string | 账号更新时间 |
| &emsp;&emsp;&emsp;&emsp;username | string | 用户名 |
| &emsp;&emsp;userId | integer | 用户ID |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": [
    {
      "content": "",
      "createdAt": "",
      "id": 1,
      "reportId": 1,
      "user": {
        "created": "",
        "email": "admin@example.com",
        "id": 1,
        "password": "",
        "phone": 13800138000,
        "state": "ENABLED",
        "updated": "",
        "username": "admin"
      },
      "userId": 1
    }
  ],
  "flag": true,
  "message": ""
}
```

## 获取评论详情

### 基本信息

**地址**：/api/v1/comment/{commentId}
**请求方式**：GET
**响应数据结构**：R«VulnerabilityCommentEntity»

### 接口描述

获取评论详情

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| commentId | 是 | integer | 评论ID |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | VulnerabilityCommentEntity | 评论详情 |
| &emsp;&emsp;content | string | 评论内容 |
| &emsp;&emsp;createdAt | string | 评论时间 |
| &emsp;&emsp;id | integer | 评论ID |
| &emsp;&emsp;reportId | integer | 漏洞ID |
| &emsp;&emsp;user | UserEntity | 用户信息 |
| &emsp;&emsp;&emsp;&emsp;created | string | 账号创建时间 |
| &emsp;&emsp;&emsp;&emsp;email | string | 注册邮箱 |
| &emsp;&emsp;&emsp;&emsp;id | integer | 用户ID |
| &emsp;&emsp;&emsp;&emsp;password | string | 密码 |
| &emsp;&emsp;&emsp;&emsp;phone | string | 注册手机号 |
| &emsp;&emsp;&emsp;&emsp;state | string | 账号状态 |
| &emsp;&emsp;&emsp;&emsp;updated | string | 账号更新时间 |
| &emsp;&emsp;&emsp;&emsp;username | string | 用户名 |
| &emsp;&emsp;userId | integer | 用户ID |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": {
    "content": "",
    "createdAt": "",
    "id": 1,
    "reportId": 1,
    "user": {
      "created": "",
      "email": "admin@example.com",
      "id": 1,
      "password": "",
      "phone": 13800138000,
      "state": "ENABLED",
      "updated": "",
      "username": "admin"
    },
    "userId": 1
  },
  "flag": true,
  "message": ""
}
```

## 更新漏洞评论

### 基本信息

**地址**：/api/v1/comment
**请求方式**：PUT
**请求数据类型**：application/json
**响应数据结构**：R«string»

### 接口描述

更新漏洞评论

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| commentEntity | 是 | VulnerabilityCommentEntity | 漏洞评论实体类 |
| &emsp;&emsp;content | 是 | string | 评论内容 |
| &emsp;&emsp;createdAt | 否 | string | 评论时间 |
| &emsp;&emsp;id | 否 | integer | 评论ID |
| &emsp;&emsp;reportId | 是 | integer | 漏洞ID |
| &emsp;&emsp;user | 否 | UserEntity | 用户信息 |
| &emsp;&emsp;&emsp;&emsp;created | 否 | string | 账号创建时间 |
| &emsp;&emsp;&emsp;&emsp;email | 否 | string | 注册邮箱 |
| &emsp;&emsp;&emsp;&emsp;id | 否 | integer | 用户ID |
| &emsp;&emsp;&emsp;&emsp;password | 是 | string | 密码 |
| &emsp;&emsp;&emsp;&emsp;phone | 否 | string | 注册手机号 |
| &emsp;&emsp;&emsp;&emsp;state | 否 | string | 账号状态 |
| &emsp;&emsp;&emsp;&emsp;updated | 否 | string | 账号更新时间 |
| &emsp;&emsp;&emsp;&emsp;username | 是 | string | 用户名 |
| &emsp;&emsp;userId | 是 | integer | 用户ID |
| userId | 是 | integer | 用户ID |

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

### 请求示例

```json
{
  "content": "",
  "createdAt": "",
  "id": 1,
  "reportId": 1,
  "user": {
    "created": "",
    "email": "admin@example.com",
    "id": 1,
    "password": "",
    "phone": 13800138000,
    "state": "ENABLED",
    "updated": "",
    "username": "admin"
  },
  "userId": 1
}
```

### 响应示例

```json
{
  "data": "",
  "flag": true,
  "message": ""
}
```

## 删除漏洞评论

### 基本信息

**地址**：/api/v1/comment/{commentId}
**请求方式**：DELETE
**响应数据结构**：R«string»

### 接口描述

删除漏洞评论

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| commentId | 是 | integer | 评论ID |

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
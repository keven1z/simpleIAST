# Webhook管理

## 创建Webhook配置

**接口地址**: `/api/v1/webhook`

**请求方式**: `POST`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 创建新的Webhook配置，包括URL、请求方法、自定义头和事件类型等

### 请求示例

```json
{
  "description": "",
  "events": [
    "VULNERABILITY_NEW",
    "VULNERABILITY_UPDATE"
  ],
  "headers": "{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer token\"}",
  "method": "POST",
  "name": "漏洞通知",
  "status": 1,
  "url": "https://example.com/webhook"
}
```

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| webhookDTO | Webhook配置数据传输对象 | body | true | WebhookDTO | |
| &emsp;&emsp;description | 备注 |  | false | string | |
| &emsp;&emsp;events | 触发事件类型列表 |  | false | array | string |
| &emsp;&emsp;headers | 请求头配置 |  | false | string | |
| &emsp;&emsp;method | 请求方法,可用值:GET,POST |  | false | string | |
| &emsp;&emsp;name | Webhook名称 |  | true | string | |
| &emsp;&emsp;status | 状态：0-禁用，1-启用 |  | false | integer(int32) | |
| &emsp;&emsp;url | Webhook URL |  | true | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«WebhookEntity» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | WebhookEntity | |
| &emsp;&emsp;createdTime | 创建时间 | string(date-time) | |
| &emsp;&emsp;description | 备注 | string | |
| &emsp;&emsp;events | 触发事件类型列表 | array | string |
| &emsp;&emsp;headers | 请求头配置 | string | |
| &emsp;&emsp;id | Webhook ID | integer(int64) | |
| &emsp;&emsp;method | 请求方法,可用值:GET,POST | string | |
| &emsp;&emsp;name | Webhook名称 | string | |
| &emsp;&emsp;status | 状态：0-禁用，1-启用 | integer(int32) | |
| &emsp;&emsp;updatedTime | 更新时间 | string(date-time) | |
| &emsp;&emsp;url | Webhook URL | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "createdTime": "",
    "description": "",
    "events": "[\"VULNERABILITY_NEW\",\"VULNERABILITY_UPDATE\"]",
    "headers": "{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer token\"}",
    "id": 1,
    "method": "POST",
    "name": "漏洞通知",
    "status": 1,
    "updatedTime": "",
    "url": "https://example.com/webhook"
  },
  "flag": true,
  "message": ""
}
```

## 删除Webhook配置

**接口地址**: `/api/v1/webhook/{id}`

**请求方式**: `DELETE`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 根据ID删除Webhook配置

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| id | Webhook ID | path | true | integer(int64) | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«boolean» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | boolean | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": true,
  "flag": true,
  "message": ""
}
```

## 更新Webhook状态

**接口地址**: `/api/v1/webhook/{id}/status`

**请求方式**: `PUT`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 根据ID更新Webhook状态（启用/禁用）

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| id | Webhook ID | path | true | integer(int64) | |
| status | 状态值（0:禁用, 1:启用） | query | true | integer(int32) | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«boolean» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | boolean | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": true,
  "flag": true,
  "message": ""
}
```

**接口地址**: `/api/v1/webhook/list`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 分页查询Webhook配置列表，支持按名称搜索

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| pageNum | 页码 | query | true | integer(int32) | |
| pageSize | 每页大小 | query | true | integer(int32) | |
| name | 搜索关键词（名称） | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«PageInfo«WebhookEntity»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | PageInfo«WebhookEntity» | |
| &emsp;&emsp;endRow | | integer(int64) | |
| &emsp;&emsp;hasNextPage | | boolean | |
| &emsp;&emsp;hasPreviousPage | | boolean | |
| &emsp;&emsp;isFirstPage | | boolean | |
| &emsp;&emsp;isLastPage | | boolean | |
| &emsp;&emsp;list | | array | WebhookEntity |
| &emsp;&emsp;&emsp;&emsp;createdTime | 创建时间 | string | |
| &emsp;&emsp;&emsp;&emsp;description | 备注 | string | |
| &emsp;&emsp;&emsp;&emsp;events | 触发事件类型列表 | array | string |
| &emsp;&emsp;&emsp;&emsp;headers | 请求头配置 | string | |
| &emsp;&emsp;&emsp;&emsp;id | Webhook ID | integer | |
| &emsp;&emsp;&emsp;&emsp;method | 请求方法,可用值:GET,POST | string | |
| &emsp;&emsp;&emsp;&emsp;name | Webhook名称 | string | |
| &emsp;&emsp;&emsp;&emsp;status | 状态：0-禁用，1-启用 | integer | |
| &emsp;&emsp;&emsp;&emsp;updatedTime | 更新时间 | string | |
| &emsp;&emsp;&emsp;&emsp;url | Webhook URL | string | |
| &emsp;&emsp;navigateFirstPage | | integer(int32) | |
| &emsp;&emsp;navigateLastPage | | integer(int32) | |
| &emsp;&emsp;navigatePages | | integer(int32) | |
| &emsp;&emsp;navigatepageNums | | array | integer |
| &emsp;&emsp;nextPage | | integer(int32) | |
| &emsp;&emsp;pageNum | | integer(int32) | |
| &emsp;&emsp;pageSize | | integer(int32) | |
| &emsp;&emsp;pages | | integer(int32) | |
| &emsp;&emsp;prePage | | integer(int32) | |
| &emsp;&emsp;size | | integer(int32) | |
| &emsp;&emsp;startRow | | integer(int64) | |
| &emsp;&emsp;total | | integer(int64) | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

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
        "createdTime": "",
        "description": "",
        "events": "[\"VULNERABILITY_NEW\",\"VULNERABILITY_UPDATE\"]",
        "headers": "{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer token\"}",
        "id": 1,
        "method": "POST",
        "name": "漏洞通知",
        "status": 1,
        "updatedTime": "",
        "url": "https://example.com/webhook"
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

## 测试Webhook连接

**接口地址**: `/api/v1/webhook/test`

**请求方式**: `POST`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 测试Webhook配置的连接是否正常

### 请求示例

```json
{
  "description": "",
  "events": [
    "VULNERABILITY_NEW",
    "VULNERABILITY_UPDATE"
  ],
  "headers": "{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer token\"}",
  "method": "POST",
  "name": "漏洞通知",
  "status": 1,
  "url": "https://example.com/webhook"
}
```

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| webhookDTO | Webhook配置数据传输对象 | body | true | WebhookDTO | |
| &emsp;&emsp;description | 备注 |  | false | string | |
| &emsp;&emsp;events | 触发事件类型列表 |  | false | array | string |
| &emsp;&emsp;headers | 请求头配置 |  | false | string | |
| &emsp;&emsp;method | 请求方法,可用值:GET,POST |  | false | string | |
| &emsp;&emsp;name | Webhook名称 |  | true | string | |
| &emsp;&emsp;status | 状态：0-禁用，1-启用 |  | false | integer(int32) | |
| &emsp;&emsp;url | Webhook URL |  | true | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«string» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": "",
  "flag": true,
  "message": ""
}
```

## 查询Webhook详情

**接口地址**: `/api/v1/webhook/{id}`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 根据ID查询Webhook详细配置信息

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| id | Webhook ID | path | true | integer(int64) | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«WebhookEntity» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | WebhookEntity | |
| &emsp;&emsp;createdTime | 创建时间 | string(date-time) | |
| &emsp;&emsp;description | 备注 | string | |
| &emsp;&emsp;events | 触发事件类型列表 | array | string |
| &emsp;&emsp;headers | 请求头配置 | string | |
| &emsp;&emsp;id | Webhook ID | integer(int64) | |
| &emsp;&emsp;method | 请求方法,可用值:GET,POST | string | |
| &emsp;&emsp;name | Webhook名称 | string | |
| &emsp;&emsp;status | 状态：0-禁用，1-启用 | integer(int32) | |
| &emsp;&emsp;updatedTime | 更新时间 | string(date-time) | |
| &emsp;&emsp;url | Webhook URL | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "createdTime": "",
    "description": "",
    "events": "[\"VULNERABILITY_NEW\",\"VULNERABILITY_UPDATE\"]",
    "headers": "{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer token\"}",
    "id": 1,
    "method": "POST",
    "name": "漏洞通知",
    "status": 1,
    "updatedTime": "",
    "url": "https://example.com/webhook"
  },
  "flag": true,
  "message": ""
}
```

## 更新Webhook配置

**接口地址**: `/api/v1/webhook/{id}`

**请求方式**: `PUT`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 根据ID更新Webhook配置信息

### 请求示例

```json
{
  "description": "",
  "events": [
    "VULNERABILITY_NEW",
    "VULNERABILITY_UPDATE"
  ],
  "headers": "{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer token\"}",
  "method": "POST",
  "name": "漏洞通知",
  "status": 1,
  "url": "https://example.com/webhook"
}
```

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| id | Webhook ID | path | true | integer(int64) | |
| webhookDTO | Webhook配置数据传输对象 | body | true | WebhookDTO | |
| &emsp;&emsp;description | 备注 |  | false | string | |
| &emsp;&emsp;events | 触发事件类型列表 |  | false | array | string |
| &emsp;&emsp;headers | 请求头配置 |  | false | string | |
| &emsp;&emsp;method | 请求方法,可用值:GET,POST |  | false | string | |
| &emsp;&emsp;name | Webhook名称 |  | true | string | |
| &emsp;&emsp;status | 状态：0-禁用，1-启用 |  | false | integer(int32) | |
| &emsp;&emsp;url | Webhook URL |  | true | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«WebhookEntity» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | WebhookEntity | |
| &emsp;&emsp;createdTime | 创建时间 | string(date-time) | |
| &emsp;&emsp;description | 备注 | string | |
| &emsp;&emsp;events | 触发事件类型列表 | array | string |
| &emsp;&emsp;headers | 请求头配置 | string | |
| &emsp;&emsp;id | Webhook ID | integer(int64) | |
| &emsp;&emsp;method | 请求方法,可用值:GET,POST | string | |
| &emsp;&emsp;name | Webhook名称 | string | |
| &emsp;&emsp;status | 状态：0-禁用，1-启用 | integer(int32) | |
| &emsp;&emsp;updatedTime | 更新时间 | string(date-time) | |
| &emsp;&emsp;url | Webhook URL | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "createdTime": "",
    "description": "",
    "events": "[\"VULNERABILITY_NEW\",\"VULNERABILITY_UPDATE\"]",
    "headers": "{\"Content-Type\":\"application/json\",\"Authorization\":\"Bearer token\"}",
    "id": 1,
    "method": "POST",
    "name": "漏洞通知",
    "status": 1,
    "updatedTime": "",
    "url": "https://example.com/webhook"
  },
  "flag": true,
  "message": ""
}
```
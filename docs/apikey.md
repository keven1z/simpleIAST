# API密钥管理

## 创建API密钥

**接口地址**: `/api/v1/apikey/create`

**请求方式**: `POST`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 创建新的API密钥

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| name | 密钥名称 | query | true | string | |
| description | 密钥描述 | query | false | string | |
| expiredDateStr | 过期日期 (支持ISO 8601格式如2025-11-17T02:20:11.400Z或yyyy-MM-dd HH:mm:ss, 可选) | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«API密钥实体类» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | API密钥实体类 | API密钥实体类 | |
| &emsp;&emsp;apiKey | API密钥 | string | |
| &emsp;&emsp;createdTime | 创建时间 | string(date-time) | |
| &emsp;&emsp;description | 密钥描述 | string | |
| &emsp;&emsp;expiredTime | 过期时间 | string(date-time) | |
| &emsp;&emsp;id | 主键ID | integer(int64) | |
| &emsp;&emsp;name | 密钥名称 | string | |
| &emsp;&emsp;status | 状态：0-禁用，1-启用 | integer(int32) | |
| &emsp;&emsp;userId | 关联的用户ID | integer(int64) | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "apiKey": "",
    "createdTime": "",
    "description": "",
    "expiredTime": "",
    "id": 1,
    "name": "",
    "status": 1,
    "userId": 1
  },
  "flag": true,
  "message": ""
}
```

## 删除API密钥

**接口地址**: `/api/v1/apikey/delete/{id}`

**请求方式**: `DELETE`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 删除指定的API密钥

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| id | API密钥ID | path | true | integer(int64) | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«boolean» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | 操作结果 | boolean | |
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

## 获取API密钥列表

**接口地址**: `/api/v1/apikey/list`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 获取所有API密钥列表

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| pageNum | 页码 | query | true | integer(int32) | |
| pageSize | 每页数量 | query | true | integer(int32) | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«PageInfo«API密钥实体类»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | 分页数据 | PageInfo«API密钥实体类» | |
| &emsp;&emsp;endRow | 当前页最后一行 | integer(int64) | |
| &emsp;&emsp;hasNextPage | 是否有下一页 | boolean | |
| &emsp;&emsp;hasPreviousPage | 是否有上一页 | boolean | |
| &emsp;&emsp;isFirstPage | 是否为第一页 | boolean | |
| &emsp;&emsp;isLastPage | 是否为最后一页 | boolean | |
| &emsp;&emsp;list | API密钥列表 | array | API密钥实体类 |
| &emsp;&emsp;&emsp;&emsp;apiKey | API密钥 | string | |
| &emsp;&emsp;&emsp;&emsp;createdTime | 创建时间 | string | |
| &emsp;&emsp;&emsp;&emsp;description | 密钥描述 | string | |
| &emsp;&emsp;&emsp;&emsp;expiredTime | 过期时间 | string | |
| &emsp;&emsp;&emsp;&emsp;id | 主键ID | integer | |
| &emsp;&emsp;&emsp;&emsp;name | 密钥名称 | string | |
| &emsp;&emsp;&emsp;&emsp;status | 状态：0-禁用，1-启用 | integer | |
| &emsp;&emsp;&emsp;&emsp;userId | 关联的用户ID | integer | |
| &emsp;&emsp;navigateFirstPage | 导航到第一页 | integer(int32) | |
| &emsp;&emsp;navigateLastPage | 导航到最后一页 | integer(int32) | |
| &emsp;&emsp;navigatePages | 导航页数 | integer(int32) | |
| &emsp;&emsp;navigatepageNums | 导航页码数组 | array | integer | |
| &emsp;&emsp;nextPage | 下一页 | integer(int32) | |
| &emsp;&emsp;pageNum | 当前页码 | integer(int32) | |
| &emsp;&emsp;pageSize | 每页数量 | integer(int32) | |
| &emsp;&emsp;pages | 总页数 | integer(int32) | |
| &emsp;&emsp;prePage | 上一页 | integer(int32) | |
| &emsp;&emsp;size | 当前页记录数 | integer(int32) | |
| &emsp;&emsp;startRow | 当前页第一行 | integer(int64) | |
| &emsp;&emsp;total | 总记录数 | integer(int64) | |
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
        "apiKey": "",
        "createdTime": "",
        "description": "",
        "expiredTime": "",
        "id": 1,
        "name": "",
        "status": 1,
        "userId": 1
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

## 搜索API密钥

**接口地址**: `/api/v1/apikey/search`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 根据条件搜索API密钥

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| pageNum | 页码 | query | true | integer(int32) | |
| pageSize | 每页数量 | query | true | integer(int32) | |
| name | 密钥名称（模糊匹配） | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«PageInfo«API密钥实体类»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | 分页数据 | PageInfo«API密钥实体类» | |
| &emsp;&emsp;endRow | 当前页最后一行 | integer(int64) | |
| &emsp;&emsp;hasNextPage | 是否有下一页 | boolean | |
| &emsp;&emsp;hasPreviousPage | 是否有上一页 | boolean | |
| &emsp;&emsp;isFirstPage | 是否为第一页 | boolean | |
| &emsp;&emsp;isLastPage | 是否为最后一页 | boolean | |
| &emsp;&emsp;list | API密钥列表 | array | API密钥实体类 |
| &emsp;&emsp;&emsp;&emsp;apiKey | API密钥 | string | |
| &emsp;&emsp;&emsp;&emsp;createdTime | 创建时间 | string | |
| &emsp;&emsp;&emsp;&emsp;description | 密钥描述 | string | |
| &emsp;&emsp;&emsp;&emsp;expiredTime | 过期时间 | string | |
| &emsp;&emsp;&emsp;&emsp;id | 主键ID | integer | |
| &emsp;&emsp;&emsp;&emsp;name | 密钥名称 | string | |
| &emsp;&emsp;&emsp;&emsp;status | 状态：0-禁用，1-启用 | integer | |
| &emsp;&emsp;&emsp;&emsp;userId | 关联的用户ID | integer | |
| &emsp;&emsp;navigateFirstPage | 导航到第一页 | integer(int32) | |
| &emsp;&emsp;navigateLastPage | 导航到最后一页 | integer(int32) | |
| &emsp;&emsp;navigatePages | 导航页数 | integer(int32) | |
| &emsp;&emsp;navigatepageNums | 导航页码数组 | array | integer | |
| &emsp;&emsp;nextPage | 下一页 | integer(int32) | |
| &emsp;&emsp;pageNum | 当前页码 | integer(int32) | |
| &emsp;&emsp;pageSize | 每页数量 | integer(int32) | |
| &emsp;&emsp;pages | 总页数 | integer(int32) | |
| &emsp;&emsp;prePage | 上一页 | integer(int32) | |
| &emsp;&emsp;size | 当前页记录数 | integer(int32) | |
| &emsp;&emsp;startRow | 当前页第一行 | integer(int64) | |
| &emsp;&emsp;total | 总记录数 | integer(int64) | |
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
        "apiKey": "",
        "createdTime": "",
        "description": "",
        "expiredTime": "",
        "id": 1,
        "name": "",
        "status": 1,
        "userId": 1
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

## 更新API密钥状态

**接口地址**: `/api/v1/apikey/updateStatus`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 更新API密钥的状态（启用/禁用）

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| id | 密钥ID | query | true | integer(int64) | |
| status | 状态值：0-禁用，1-启用 | query | true | integer(int32) | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«string» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | 操作结果 | string | |
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
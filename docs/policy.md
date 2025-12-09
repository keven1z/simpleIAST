# 下发指令接口

## 查询所有agent的下发指令

**接口地址**: `/client/policy/all`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

暂无

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«List«指令实体»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | array | 指令实体 |
| &emsp;&emsp;agentEnabled | | boolean | |
| &emsp;&emsp;agentId | | string | |
| &emsp;&emsp;debugMode | | boolean | |
| &emsp;&emsp;detectEnabled | | boolean | |
| &emsp;&emsp;excludeClasses | | array | string |
| &emsp;&emsp;excludePackages | | array | string |
| &emsp;&emsp;excludeUrls | | array | string |
| &emsp;&emsp;forceDisabledRules | | array | string |
| &emsp;&emsp;id | | integer(int32) | |
| &emsp;&emsp;logLevel | | string | |
| &emsp;&emsp;maxCpuUsage | | integer(int32) | |
| &emsp;&emsp;maxMemoryUsage | | integer(int32) | |
| &emsp;&emsp;modifiedTime | | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": [
    {
      "agentEnabled": true,
      "agentId": "",
      "debugMode": true,
      "detectEnabled": true,
      "excludeClasses": [],
      "excludePackages": [],
      "excludeUrls": [],
      "forceDisabledRules": [],
      "id": 0,
      "logLevel": "",
      "maxCpuUsage": 0,
      "maxMemoryUsage": 0,
      "modifiedTime": ""
    }
  ],
  "flag": true,
  "message": ""
}
```

## 查询对应agent的下发指令

**接口地址**: `/client/policy/get`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

暂无

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«指令实体» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | 指令实体 | |
| &emsp;&emsp;agentEnabled | | boolean | |
| &emsp;&emsp;agentId | | string | |
| &emsp;&emsp;debugMode | | boolean | |
| &emsp;&emsp;detectEnabled | | boolean | |
| &emsp;&emsp;excludeClasses | | array | string |
| &emsp;&emsp;excludePackages | | array | string |
| &emsp;&emsp;excludeUrls | | array | string |
| &emsp;&emsp;forceDisabledRules | | array | string |
| &emsp;&emsp;id | | integer(int32) | |
| &emsp;&emsp;logLevel | | string | |
| &emsp;&emsp;maxCpuUsage | | integer(int32) | |
| &emsp;&emsp;maxMemoryUsage | | integer(int32) | |
| &emsp;&emsp;modifiedTime | | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "agentEnabled": true,
    "agentId": "",
    "debugMode": true,
    "detectEnabled": true,
    "excludeClasses": [],
    "excludePackages": [],
    "excludeUrls": [],
    "forceDisabledRules": [],
    "id": 0,
    "logLevel": "",
    "maxCpuUsage": 0,
    "maxMemoryUsage": 0,
    "modifiedTime": ""
  },
  "flag": true,
  "message": ""
}
```
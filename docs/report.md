# 漏洞报告接口

## 通过agent id查询对应的漏洞

**接口地址**: `/api/v1/report/by-agent/{agentId}`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentId | agent标识 | path | true | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«List«报告实体»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | array | 报告实体 |
| &emsp;&emsp;agent | | Agent实体 | Agent实体 |
| &emsp;&emsp;&emsp;&emsp;agentId | agentId | string | |
| &emsp;&emsp;&emsp;&emsp;cpuUsage | cpu使用率 | number | |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间 | string | |
| &emsp;&emsp;&emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 | integer | |
| &emsp;&emsp;&emsp;&emsp;hostname | 主机名 | string | |
| &emsp;&emsp;&emsp;&emsp;jdkVersion | jdk版本 | string | |
| &emsp;&emsp;&emsp;&emsp;lastActiveTime | 最后活跃时间 | string | |
| &emsp;&emsp;&emsp;&emsp;memoryUsage | 内存使用率 | number | |
| &emsp;&emsp;&emsp;&emsp;os | 操作系统类型 | string | |
| &emsp;&emsp;&emsp;&emsp;process | 应用进程号 | string | |
| &emsp;&emsp;&emsp;&emsp;project | 所属项目信息 | 应用实体 | 应用实体 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id | id | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level | 应用重要性 | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name | 应用名 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag | 标签 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId | 用户ID | integer | |
| &emsp;&emsp;&emsp;&emsp;serverPath | 应用路径 | string | |
| &emsp;&emsp;&emsp;&emsp;state | 上线状态,1开启,2关闭 | integer | |
| &emsp;&emsp;&emsp;&emsp;version | agent版本 | string | |
| &emsp;&emsp;duplicateKey | 漏洞标识key | string | |
| &emsp;&emsp;findingData | 污染数据链 | string | |
| &emsp;&emsp;firstTimestamp | 首次报告时间 | string | |
| &emsp;&emsp;id | id | integer(int32) | |
| &emsp;&emsp;lastTimestamp | 上次报告时间 | string | |
| &emsp;&emsp;level | 漏洞等级 | integer(int32) | |
| &emsp;&emsp;protocol | 请求protocol | string | |
| &emsp;&emsp;requestBody | 请求body | string | |
| &emsp;&emsp;requestHeader | 请求header | object | |
| &emsp;&emsp;requestMethod | 请求方法 | string | |
| &emsp;&emsp;responseBody | 响应body | string | |
| &emsp;&emsp;responseHeader | 响应header | object | |
| &emsp;&emsp;status | | VulnerabilityStatusEntity | VulnerabilityStatusEntity |
| &emsp;&emsp;&emsp;&emsp;isDel | | integer | |
| &emsp;&emsp;&emsp;&emsp;statusId | | integer | |
| &emsp;&emsp;&emsp;&emsp;statusName | | string | |
| &emsp;&emsp;statusCode | 响应码 | integer(int32) | |
| &emsp;&emsp;uri | 请求uri | string | |
| &emsp;&emsp;url | 请求url | string | |
| &emsp;&emsp;vulnerableType | 漏洞类型 | string | |
| &emsp;&emsp;vulnerableTypeZH | 漏洞类型中文 | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": [
    {
      "agent": {
        "agentId": "",
        "cpuUsage": 0,
        "createTime": "",
        "detectionStatus": 0,
        "hostname": "",
        "jdkVersion": "",
        "lastActiveTime": "",
        "memoryUsage": 0,
        "os": "",
        "process": "",
        "project": {
          "id": 0,
          "level": 0,
          "name": "",
          "tag": "",
          "userId": 0
        },
        "serverPath": "",
        "state": 0,
        "version": ""
      },
      "duplicateKey": "",
      "findingData": "",
      "firstTimestamp": "",
      "id": 0,
      "lastTimestamp": "",
      "level": 0,
      "protocol": "",
      "requestBody": "",
      "requestHeader": {},
      "requestMethod": "",
      "responseBody": "",
      "responseHeader": {},
      "status": {
        "isDel": 0,
        "statusId": 0,
        "statusName": ""
      },
      "statusCode": 0,
      "uri": "",
      "url": "",
      "vulnerableType": "",
      "vulnerableTypeZH": ""
    }
  ],
  "flag": true,
  "message": ""
}
```

## 删除漏洞

**接口地址**: `/api/v1/report/delete/{id}`

**请求方式**: `POST`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| id | 漏洞ID | path | true | string | |

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

## 导出漏洞数据

**接口地址**: `/api/v1/report/export`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

暂无

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | |

### 响应参数

暂无

### 响应示例

```
```

## 导出对应id漏洞数据

**接口地址**: `/api/v1/report/export/{reportId}`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| reportId | 漏洞ID | path | true | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | |

### 响应参数

暂无

### 响应示例

```
```

## 最近一年的漏洞

**接口地址**: `/api/v1/report/last-12months`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

暂无

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«List«int»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | array | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": [],
  "flag": true,
  "message": ""
}
```

## 最近七天的漏洞

**接口地址**: `/api/v1/report/last-7days`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

暂无

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«List«int»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | array | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": [],
  "flag": true,
  "message": ""
}
```

## 获取所有level的对应的数量

**接口地址**: `/api/v1/report/level-counts`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentId | agent标识（可选） | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«List«ReportCountEntity»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | array | ReportCountEntity |
| &emsp;&emsp;count | | integer(int32) | |
| &emsp;&emsp;level | | string | |
| &emsp;&emsp;vulnerable_type_zh | | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": [
    {
      "count": 0,
      "level": "",
      "vulnerable_type_zh": ""
    }
  ],
  "flag": true,
  "message": ""
}
```

## 查询所有漏洞接口

**接口地址**: `/api/v1/report/list`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| firstTimestamp | firstTimestamp | query | false | string | |
| lastTimestamp | lastTimestamp | query | false | string | |
| level | level | query | false | integer(int32) | |
| url | url | query | false | string | |
| vulnerableType | vulnerableType | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«List«报告实体»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | array | 报告实体 |
| &emsp;&emsp;agent | | Agent实体 | Agent实体 |
| &emsp;&emsp;&emsp;&emsp;agentId | agentId | string | |
| &emsp;&emsp;&emsp;&emsp;cpuUsage | cpu使用率 | number | |
| &emsp;&emsp;&emsp;&emsp;createTime | 创建时间 | string | |
| &emsp;&emsp;&emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 | integer | |
| &emsp;&emsp;&emsp;&emsp;hostname | 主机名 | string | |
| &emsp;&emsp;&emsp;&emsp;jdkVersion | jdk版本 | string | |
| &emsp;&emsp;&emsp;&emsp;lastActiveTime | 最后活跃时间 | string | |
| &emsp;&emsp;&emsp;&emsp;memoryUsage | 内存使用率 | number | |
| &emsp;&emsp;&emsp;&emsp;os | 操作系统类型 | string | |
| &emsp;&emsp;&emsp;&emsp;process | 应用进程号 | string | |
| &emsp;&emsp;&emsp;&emsp;project | 所属项目信息 | 应用实体 | 应用实体 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id | id | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level | 应用重要性 | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name | 应用名 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag | 标签 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId | 用户ID | integer | |
| &emsp;&emsp;&emsp;&emsp;serverPath | 应用路径 | string | |
| &emsp;&emsp;&emsp;&emsp;state | 上线状态,1开启,2关闭 | integer | |
| &emsp;&emsp;&emsp;&emsp;version | agent版本 | string | |
| &emsp;&emsp;duplicateKey | 漏洞标识key | string | |
| &emsp;&emsp;findingData | 污染数据链 | string | |
| &emsp;&emsp;firstTimestamp | 首次报告时间 | string | |
| &emsp;&emsp;id | id | integer(int32) | |
| &emsp;&emsp;lastTimestamp | 上次报告时间 | string | |
| &emsp;&emsp;level | 漏洞等级 | integer(int32) | |
| &emsp;&emsp;protocol | 请求protocol | string | |
| &emsp;&emsp;requestBody | 请求body | string | |
| &emsp;&emsp;requestHeader | 请求header | object | |
| &emsp;&emsp;requestMethod | 请求方法 | string | |
| &emsp;&emsp;responseBody | 响应body | string | |
| &emsp;&emsp;responseHeader | 响应header | object | |
| &emsp;&emsp;status | | VulnerabilityStatusEntity | VulnerabilityStatusEntity |
| &emsp;&emsp;&emsp;&emsp;isDel | | integer | |
| &emsp;&emsp;&emsp;&emsp;statusId | | integer | |
| &emsp;&emsp;&emsp;&emsp;statusName | | string | |
| &emsp;&emsp;statusCode | 响应码 | integer(int32) | |
| &emsp;&emsp;uri | 请求uri | string | |
| &emsp;&emsp;url | 请求url | string | |
| &emsp;&emsp;vulnerableType | 漏洞类型 | string | |
| &emsp;&emsp;vulnerableTypeZH | 漏洞类型中文 | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": [
    {
      "agent": {
        "agentId": "",
        "cpuUsage": 0,
        "createTime": "",
        "detectionStatus": 0,
        "hostname": "",
        "jdkVersion": "",
        "lastActiveTime": "",
        "memoryUsage": 0,
        "os": "",
        "process": "",
        "project": {
          "id": 0,
          "level": 0,
          "name": "",
          "tag": "",
          "userId": 0
        },
        "serverPath": "",
        "state": 0,
        "version": ""
      },
      "duplicateKey": "",
      "findingData": "",
      "firstTimestamp": "",
      "id": 0,
      "lastTimestamp": "",
      "level": 0,
      "protocol": "",
      "requestBody": "",
      "requestHeader": {},
      "requestMethod": "",
      "responseBody": "",
      "responseHeader": {},
      "status": {
        "isDel": 0,
        "statusId": 0,
        "statusName": ""
      },
      "statusCode": 0,
      "uri": "",
      "url": "",
      "vulnerableType": "",
      "vulnerableTypeZH": ""
    }
  ],
  "flag": true,
  "message": ""
}
```

## 查询所有项目接口

**接口地址**: `/api/v1/report/page`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**:

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| firstTimestamp | firstTimestamp | query | false | string | |
| lastTimestamp | lastTimestamp | query | false | string | |
| level | level | query | false | integer(int32) | |
| pageNum | pageNum | query | false | integer(int32) | |
| pageSize | pageSize | query | false | integer(int32) | |
| url | url | query | false | string | |
| vulnerableType | vulnerableType | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«PageInfo«报告实体»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | PageInfo«报告实体» | PageInfo«报告实体» |
| &emsp;&emsp;endRow | | integer(int64) | |
| &emsp;&emsp;hasNextPage | | boolean | |
| &emsp;&emsp;hasPreviousPage | | boolean | |
| &emsp;&emsp;isFirstPage | | boolean | |
| &emsp;&emsp;isLastPage | | boolean | |
| &emsp;&emsp;list | | array | 报告实体 |
| &emsp;&emsp;&emsp;&emsp;agent | | Agent实体 | Agent实体 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;agentId | agentId | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;cpuUsage | cpu使用率 | number | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;createTime | 创建时间 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hostname | 主机名 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;jdkVersion | jdk版本 | string | |
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;lastActiveTime | 最后活跃时间 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;memoryUsage | 内存使用率 | number | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;os | 操作系统类型 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;process | 应用进程号 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;project | 所属项目信息 | 应用实体 | 应用实体 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id | id | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level | 应用重要性 | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name | 应用名 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag | 标签 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId | 用户ID | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;serverPath | 应用路径 | string | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;state | 上线状态,1开启,2关闭 | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;version | agent版本 | string | |
| &emsp;&emsp;&emsp;&emsp;duplicateKey | 漏洞标识key | string | |
| &emsp;&emsp;&emsp;&emsp;findingData | 污染数据链 | string | |
| &emsp;&emsp;&emsp;&emsp;firstTimestamp | 首次报告时间 | string | |
| &emsp;&emsp;&emsp;&emsp;id | id | integer | |
| &emsp;&emsp;&emsp;&emsp;lastTimestamp | 上次报告时间 | string | |
| &emsp;&emsp;&emsp;&emsp;level | 漏洞等级 | integer | |
| &emsp;&emsp;&emsp;&emsp;protocol | 请求protocol | string | |
| &emsp;&emsp;&emsp;&emsp;requestBody | 请求body | string | |
| &emsp;&emsp;&emsp;&emsp;requestHeader | 请求header | object | |
| &emsp;&emsp;&emsp;&emsp;requestMethod | 请求方法 | string | |
| &emsp;&emsp;&emsp;&emsp;responseBody | 响应body | string | |
| &emsp;&emsp;&emsp;&emsp;responseHeader | 响应header | object | |
| &emsp;&emsp;&emsp;&emsp;status | | VulnerabilityStatusEntity | VulnerabilityStatusEntity |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;isDel | | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;statusId | | integer | |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;statusName | | string | |
| &emsp;&emsp;&emsp;&emsp;statusCode | 响应码 | integer | |
| &emsp;&emsp;&emsp;&emsp;uri | 请求uri | string | |
| &emsp;&emsp;&emsp;&emsp;url | 请求url | string | |
| &emsp;&emsp;&emsp;&emsp;vulnerableType | 漏洞类型 | string | |
| &emsp;&emsp;&emsp;&emsp;vulnerableTypeZH | 漏洞类型中文 | string | |
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
    "hasNextPage": false,
    "hasPreviousPage": false,
    "isFirstPage": false,
    "isLastPage": false,
    "list": [
      {
        "agent": {
          "agentId": "",
          "cpuUsage": 0,
          "createTime": "",
          "detectionStatus": 0,
          "hostname": "",
          "jdkVersion": "",
          "lastActiveTime": "",
          "memoryUsage": 0,
          "os": "",
          "process": "",
          "project": {
            "id": 0,
            "level": 0,
            "name": "",
            "tag": "",
            "userId": 0
          },
          "serverPath": "",
          "state": 0,
          "version": ""
        },
        "duplicateKey": "",
        "findingData": "",
        "firstTimestamp": "",
        "id": 0,
        "lastTimestamp": "",
        "level": 0,
        "protocol": "",
        "requestBody": "",
        "requestHeader": {},
        "requestMethod": "",
        "responseBody": "",
        "responseHeader": {},
        "status": {
          "isDel": 0,
          "statusId": 0,
          "statusName": ""
        },
        "statusCode": 0,
        "uri": "",
        "url": "",
        "vulnerableType": "",
        "vulnerableTypeZH": ""
      }
    ]
  },
  "flag": true,
  "message": ""
}
```

## 修改漏洞状态

### 基本信息

**地址**：/api/v1/report/status
**请求方式**：POST
**响应数据结构**：R«string»

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| id | 是 | integer | 漏洞id |
| statusId | 是 | integer | 状态id |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | string | 成功消息 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": "操作成功",
  "flag": true,
  "message": ""
}
```

## 获取指定agent最近12个月的漏洞详情统计

### 基本信息

**地址**：/api/v1/report/trend/month/{agentId}
**请求方式**：GET
**响应数据结构**：R«List«VulnerabilityTrendEntity»»

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| agentId | 是 | string | 代理id |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | array | 漏洞趋势列表 |
| &emsp;&emsp;criticalCount | integer | 高危漏洞数量 |
| &emsp;&emsp;highCount | integer | 严重漏洞数量 |
| &emsp;&emsp;infoCount | integer | 信息漏洞数量 |
| &emsp;&emsp;lowCount | integer | 低危漏洞数量 |
| &emsp;&emsp;mediumCount | integer | 中危漏洞数量 |
| &emsp;&emsp;month | string | 月份 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": [
    {
      "criticalCount": 0,
      "highCount": 0,
      "infoCount": 0,
      "lowCount": 0,
      "mediumCount": 0,
      "month": ""
    }
  ],
  "flag": true,
  "message": ""
}
```

## 获取指定agent最近7天的漏洞详情统计

### 基本信息

**地址**：/api/v1/report/trend/week/{agentId}
**请求方式**：GET
**响应数据结构**：R«List«TimeReportCountEntity»»

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| agentId | 是 | string | agent标识 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | array | 漏洞趋势列表 |
| &emsp;&emsp;levelCounts | object | 漏洞等级统计 |
| &emsp;&emsp;time | string | 时间 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": [
    {
      "levelCounts": {},
      "time": ""
    }
  ],
  "flag": true,
  "message": ""
}
```

## 通过漏洞ID获取单一漏洞信息

### 基本信息

**地址**：/api/v1/report/{id}
**请求方式**：GET
**响应数据结构**：R«报告实体»

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| id | 是 | string | 漏洞ID |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | 报告实体 | 漏洞详情 |
| &emsp;&emsp;agent | Agent实体 | 代理信息 |
| &emsp;&emsp;&emsp;&emsp;agentId | string | agentId |
| &emsp;&emsp;&emsp;&emsp;cpuUsage | number | cpu使用率 |
| &emsp;&emsp;&emsp;&emsp;createTime | string | 创建时间 |
| &emsp;&emsp;&emsp;&emsp;detectionStatus | integer | 上线状态,1开启,2关闭 |
| &emsp;&emsp;&emsp;&emsp;hostname | string | 主机名 |
| &emsp;&emsp;&emsp;&emsp;jdkVersion | string | jdk版本 |
| &emsp;&emsp;&emsp;&emsp;lastActiveTime | string | 最后活跃时间 |
| &emsp;&emsp;&emsp;&emsp;memoryUsage | number | 内存使用率 |
| &emsp;&emsp;&emsp;&emsp;os | string | 操作系统类型 |
| &emsp;&emsp;&emsp;&emsp;process | string | 应用进程号 |
| &emsp;&emsp;&emsp;&emsp;project | 应用实体 | 所属项目信息 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id | integer | id |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level | integer | 应用重要性 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name | string | 应用名 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag | string | 标签 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId | integer | 用户ID |
| &emsp;&emsp;&emsp;&emsp;serverPath | string | 应用路径 |
| &emsp;&emsp;&emsp;&emsp;state | integer | 上线状态,1开启,2关闭 |
| &emsp;&emsp;&emsp;&emsp;version | string | agent版本 |
| &emsp;&emsp;duplicateKey | string | 漏洞标识key |
| &emsp;&emsp;findingData | string | 污染数据链 |
| &emsp;&emsp;firstTimestamp | string | 首次报告时间 |
| &emsp;&emsp;id | integer | id |
| &emsp;&emsp;lastTimestamp | string | 上次报告时间 |
| &emsp;&emsp;level | integer | 漏洞等级 |
| &emsp;&emsp;protocol | string | 请求protocol |
| &emsp;&emsp;requestBody | string | 请求body |
| &emsp;&emsp;requestHeader | object | 请求header |
| &emsp;&emsp;requestMethod | string | 请求方法 |
| &emsp;&emsp;responseBody | string | 响应body |
| &emsp;&emsp;responseHeader | object | 响应header |
| &emsp;&emsp;status | VulnerabilityStatusEntity | 漏洞状态 |
| &emsp;&emsp;&emsp;&emsp;isDel | integer | |
| &emsp;&emsp;&emsp;&emsp;statusId | integer | |
| &emsp;&emsp;&emsp;&emsp;statusName | string | |
| &emsp;&emsp;statusCode | integer | 响应码 |
| &emsp;&emsp;uri | string | 请求uri |
| &emsp;&emsp;url | string | 请求url |
| &emsp;&emsp;vulnerableType | string | 漏洞类型 |
| &emsp;&emsp;vulnerableTypeZH | string | 漏洞类型中文 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": {
    "agent": {
      "agentId": "",
      "cpuUsage": 0,
      "createTime": "",
      "detectionStatus": 0,
      "hostname": "",
      "jdkVersion": "",
      "lastActiveTime": "",
      "memoryUsage": 0,
      "os": "",
      "process": "",
      "project": {
        "id": 0,
        "level": 0,
        "name": "",
        "tag": "",
        "userId": 0
      },
      "serverPath": "",
      "state": 0,
      "version": ""
    },
    "duplicateKey": "",
    "findingData": "",
    "firstTimestamp": "",
    "id": 0,
    "lastTimestamp": "",
    "level": 0,
    "protocol": "",
    "requestBody": "",
    "requestHeader": {},
    "requestMethod": "",
    "responseBody": "",
    "responseHeader": {},
    "status": {
      "isDel": 0,
      "statusId": 0,
      "statusName": ""
    },
    "statusCode": 0,
    "uri": "",
    "url": "",
    "vulnerableType": "",
    "vulnerableTypeZH": ""
  },
  "flag": true,
  "message": ""
}
```

## 接收漏洞报告

### 基本信息

**地址**：/client/report/receive
**请求方式**：POST
**请求数据类型**：application/json
**响应数据结构**：R«string»

### 接口描述

接收漏洞报告的接口

### 请求参数

暂无

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
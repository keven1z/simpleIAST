# 通知接口

## 导出所有操作日志数据

### 基本信息

**地址**：/api/v1/log/exportAll
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«List«OperationLogEntity»»

### 接口描述

导出所有操作日志数据

### 请求参数

暂无

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | array | 操作日志列表 |
| &emsp;&emsp;appCode | string | 应用代码 |
| &emsp;&emsp;content | string | 操作内容 |
| &emsp;&emsp;deviceInfo | string | 设备信息 |
| &emsp;&emsp;id | integer | 日志ID |
| &emsp;&emsp;ipAddress | string | IP地址 |
| &emsp;&emsp;operationParam | string | 操作参数 |
| &emsp;&emsp;operationTime | Timestamp | 操作时间 |
| &emsp;&emsp;&emsp;&emsp;date | integer | 日期 |
| &emsp;&emsp;&emsp;&emsp;day | integer | 天 |
| &emsp;&emsp;&emsp;&emsp;hours | integer | 小时 |
| &emsp;&emsp;&emsp;&emsp;minutes | integer | 分钟 |
| &emsp;&emsp;&emsp;&emsp;month | integer | 月 |
| &emsp;&emsp;&emsp;&emsp;nanos | integer | 纳秒 |
| &emsp;&emsp;&emsp;&emsp;seconds | integer | 秒 |
| &emsp;&emsp;&emsp;&emsp;time | integer | 时间 |
| &emsp;&emsp;&emsp;&emsp;timezoneOffset | integer | 时区偏移 |
| &emsp;&emsp;&emsp;&emsp;year | integer | 年 |
| &emsp;&emsp;operationType | string | 操作类型 |
| &emsp;&emsp;operatorName | string | 操作人姓名 |
| flag | boolean | 请求是否成功 |
| message | string | 响应消息 |

### 响应示例

```json
{
  "data": [
    {
      "appCode": "",
      "content": "",
      "deviceInfo": "",
      "id": 0,
      "ipAddress": "",
      "operationParam": "",
      "operationTime": {
        "date": 0,
        "day": 0,
        "hours": 0,
        "minutes": 0,
        "month": 0,
        "nanos": 0,
        "seconds": 0,
        "time": 0,
        "timezoneOffset": 0,
        "year": 0
      },
      "operationType": "",
      "operatorName": ""
    }
  ],
  "flag": true,
  "message": ""
}
```

## 导出操作日志为Excel文件

### 基本信息

**地址**：/api/v1/log/exportExcel
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：

### 接口描述

导出操作日志为Excel文件

### 请求参数

暂无

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

暂无

### 响应示例

```json

```

## 查看操作日志

### 基本信息

**地址**：/api/v1/log/list
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«PageInfo«OperationLogEntity»»

### 接口描述

查看操作日志

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| pageNum | 是 | integer | 页码 |
| pageSize | 是 | integer | 每页数量 |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | PageInfo«OperationLogEntity» | 分页操作日志 |
| &emsp;&emsp;endRow | integer | 结束行 |
| &emsp;&emsp;hasNextPage | boolean | 是否有下一页 |
| &emsp;&emsp;hasPreviousPage | boolean | 是否有上一页 |
| &emsp;&emsp;isFirstPage | boolean | 是否是第一页 |
| &emsp;&emsp;isLastPage | boolean | 是否是最后一页 |
| &emsp;&emsp;list | array | 操作日志列表 |
| &emsp;&emsp;&emsp;&emsp;appCode | string | 应用代码 |
| &emsp;&emsp;&emsp;&emsp;content | string | 操作内容 |
| &emsp;&emsp;&emsp;&emsp;deviceInfo | string | 设备信息 |
| &emsp;&emsp;&emsp;&emsp;id | integer | 日志ID |
| &emsp;&emsp;&emsp;&emsp;ipAddress | string | IP地址 |
| &emsp;&emsp;&emsp;&emsp;operationParam | string | 操作参数 |
| &emsp;&emsp;&emsp;&emsp;operationTime | Timestamp | 操作时间 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;date | integer | 日期 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;day | integer | 天 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hours | integer | 小时 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minutes | integer | 分钟 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;month | integer | 月 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;nanos | integer | 纳秒 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;seconds | integer | 秒 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;time | integer | 时间 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;timezoneOffset | integer | 时区偏移 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;year | integer | 年 |
| &emsp;&emsp;&emsp;&emsp;operationType | string | 操作类型 |
| &emsp;&emsp;&emsp;&emsp;operatorName | string | 操作人姓名 |
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
        "appCode": "",
        "content": "",
        "deviceInfo": "",
        "id": 0,
        "ipAddress": "",
        "operationParam": "",
        "operationTime": {
          "date": 0,
          "day": 0,
          "hours": 0,
          "minutes": 0,
          "month": 0,
          "nanos": 0,
          "seconds": 0,
          "time": 0,
          "timezoneOffset": 0,
          "year": 0
        },
        "operationType": "",
        "operatorName": ""
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

## 按条件搜索操作日志

### 基本信息

**地址**：/api/v1/log/search
**请求方式**：GET
**请求数据类型**：application/x-www-form-urlencoded
**响应数据结构**：R«PageInfo«OperationLogEntity»»

### 接口描述

按条件搜索操作日志

### 请求参数

| 参数名 | 是否必填 | 类型 | 描述 |
| :--- | :--- | :--- | :--- |
| pageNum | 是 | integer | 页码 |
| pageSize | 是 | integer | 每页数量 |
| endTime | 否 | string | 结束时间（可选，格式：yyyy-MM-dd HH:mm:ss） |
| operatorName | 否 | string | 操作者账号（可选） |
| startTime | 否 | string | 开始时间（可选，格式：yyyy-MM-dd HH:mm:ss） |

### 响应状态

| 状态码 | 状态信息 | 描述 |
| :--- | :--- | :--- |
| 200 | OK | 接口请求成功 |

### 响应参数

| 参数名 | 类型 | 描述 |
| :--- | :--- | :--- |
| data | PageInfo«OperationLogEntity» | 分页操作日志 |
| &emsp;&emsp;endRow | integer | 结束行 |
| &emsp;&emsp;hasNextPage | boolean | 是否有下一页 |
| &emsp;&emsp;hasPreviousPage | boolean | 是否有上一页 |
| &emsp;&emsp;isFirstPage | boolean | 是否是第一页 |
| &emsp;&emsp;isLastPage | boolean | 是否是最后一页 |
| &emsp;&emsp;list | array | 操作日志列表 |
| &emsp;&emsp;&emsp;&emsp;appCode | string | 应用代码 |
| &emsp;&emsp;&emsp;&emsp;content | string | 操作内容 |
| &emsp;&emsp;&emsp;&emsp;deviceInfo | string | 设备信息 |
| &emsp;&emsp;&emsp;&emsp;id | integer | 日志ID |
| &emsp;&emsp;&emsp;&emsp;ipAddress | string | IP地址 |
| &emsp;&emsp;&emsp;&emsp;operationParam | string | 操作参数 |
| &emsp;&emsp;&emsp;&emsp;operationTime | Timestamp | 操作时间 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;date | integer | 日期 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;day | integer | 天 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hours | integer | 小时 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minutes | integer | 分钟 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;month | integer | 月 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;nanos | integer | 纳秒 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;seconds | integer | 秒 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;time | integer | 时间 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;timezoneOffset | integer | 时区偏移 |
| &emsp;&emsp;&emsp;&emsp;&emsp;&emsp;year | integer | 年 |
| &emsp;&emsp;&emsp;&emsp;operationType | string | 操作类型 |
| &emsp;&emsp;&emsp;&emsp;operatorName | string | 操作人姓名 |
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
        "appCode": "",
        "content": "",
        "deviceInfo": "",
        "id": 0,
        "ipAddress": "",
        "operationParam": "",
        "operationTime": {
          "date": 0,
          "day": 0,
          "hours": 0,
          "minutes": 0,
          "month": 0,
          "nanos": 0,
          "seconds": 0,
          "time": 0,
          "timezoneOffset": 0,
          "year": 0
        },
        "operationType": "",
        "operatorName": ""
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
# Agent接口

## 获取在线和不在线的agent数量

**接口地址**: `/api/v1/agent/count/status`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 获取Agent在线和离线数量统计

### 请求参数

暂无

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«Map«string,int»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | 统计数据对象 | object | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "online": 5,
    "offline": 2
  },
  "flag": true,
  "message": ""
}
```

## 通过agent id删除agent

**接口地址**: `/api/v1/agent/delete/{agentId}`

**请求方式**: `DELETE`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 仅支持单一删除Agent

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentId | agent ID | path | true | string | |

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
  "data": "Agent删除成功",
  "flag": true,
  "message": ""
}
```

## 下载Agent

**接口地址**: `/api/v1/agent/download`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**:`*/*,application/java-archive`

**接口描述**: 下载iast-agent.jar

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| fileName | 文件名(agent或engine) | query | true | string | |
| serverUrl | 服务器URL(下载engine时必需) | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | 文件流 |

### 响应参数

暂无

## 获取agent与服务器通信key

**接口地址**: `/api/v1/agent/get-agent-key`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 获取Agent与服务器通信的密钥

### 请求参数

暂无

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«string» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | 通信密钥 | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": "your-agent-key",
  "flag": true,
  "message": ""
}
```

## 查询所有agent列表

**接口地址**: `/api/v1/agent/list`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 支持主机名、state、agent版本筛选

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| hostname | 主机名 | query | false | string | |
| state | 状态(0=离线,1=在线) | query | false | string | |
| version | agent版本 | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«List«Agent实体»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | Agent列表 | array | Agent实体 |
| &emsp;&emsp;agentId | agentId | string | |
| &emsp;&emsp;cpuUsage | cpu使用率 | number(double) | |
| &emsp;&emsp;createTime | 创建时间 | string | |
| &emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 | integer(int32) | |
| &emsp;&emsp;hostname | 主机名 | string | |
| &emsp;&emsp;jdkVersion | jdk版本 | string | |
| &emsp;&emsp;lastActiveTime | 最后活跃时间 | string | |
| &emsp;&emsp;memoryUsage | 内存使用率 | number(double) | |
| &emsp;&emsp;os | 操作系统类型 | string | |
| &emsp;&emsp;process | 应用进程号 | string | |
| &emsp;&emsp;project | 所属项目信息 | 应用实体 | |
| &emsp;&emsp;&emsp;&emsp;id | id | integer | |
| &emsp;&emsp;&emsp;&emsp;level | 应用重要性 | integer | |
| &emsp;&emsp;&emsp;&emsp;name | 应用名 | string | |
| &emsp;&emsp;&emsp;&emsp;tag | 标签 | string | |
| &emsp;&emsp;&emsp;&emsp;userId | 用户ID | integer | |
| &emsp;&emsp;serverPath | 应用路径 | string | |
| &emsp;&emsp;state | 上线状态,1开启,2关闭 | integer(int32) | |
| &emsp;&emsp;version | agent版本 | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": [
    {
      "agentId": "agent-123",
      "cpuUsage": 0.15,
      "createTime": "2023-06-15T08:30:00Z",
      "detectionStatus": 1,
      "hostname": "server-01",
      "jdkVersion": "1.8.0_341",
      "lastActiveTime": "2023-06-15T10:45:00Z",
      "memoryUsage": 0.45,
      "os": "Linux",
      "process": "12345",
      "project": {
        "id": 1,
        "level": 1,
        "name": "Sample Project",
        "tag": "web",
        "userId": 1
      },
      "serverPath": "/opt/tomcat/webapps",
      "state": 1,
      "version": "1.0.0"
    }
  ],
  "flag": true,
  "message": ""
}
```

## 分页查询所有agent

**接口地址**: `/api/v1/agent/list/page`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 支持主机名、state、agent版本筛选的分页查询

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| pageNum | 页码 | query | true | integer(int32) | |
| pageSize | 每页数量 | query | true | integer(int32) | |
| hostname | 主机名 | query | false | string | |
| state | 状态(0=离线,1=在线) | query | false | string | |
| version | agent版本 | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«PageInfo«Agent实体»» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | 分页Agent数据 | PageInfo«Agent实体» | |
| &emsp;&emsp;endRow | 当前页最后一行 | integer(int64) | |
| &emsp;&emsp;hasNextPage | 是否有下一页 | boolean | |
| &emsp;&emsp;hasPreviousPage | 是否有上一页 | boolean | |
| &emsp;&emsp;isFirstPage | 是否为第一页 | boolean | |
| &emsp;&emsp;isLastPage | 是否为最后一页 | boolean | |
| &emsp;&emsp;list | Agent列表 | array | Agent实体 |
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
    "endRow": 10,
    "hasNextPage": true,
    "hasPreviousPage": false,
    "isFirstPage": true,
    "isLastPage": false,
    "list": [
      {
        "agentId": "agent-123",
        "cpuUsage": 0.15,
        "createTime": "2023-06-15T08:30:00Z",
        "detectionStatus": 1,
        "hostname": "server-01",
        "jdkVersion": "1.8.0_341",
        "lastActiveTime": "2023-06-15T10:45:00Z",
        "memoryUsage": 0.45,
        "os": "Linux",
        "process": "12345",
        "project": {
          "id": 1,
          "level": 1,
          "name": "Sample Project",
          "tag": "web",
          "userId": 1
        },
        "serverPath": "/opt/tomcat/webapps",
        "state": 1,
        "version": "1.0.0"
      }
    ],
    "navigateFirstPage": 1,
    "navigateLastPage": 5,
    "navigatePages": 5,
    "navigatepageNums": [1, 2, 3, 4, 5],
    "nextPage": 2,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 5,
    "prePage": 0,
    "size": 10,
    "startRow": 1,
    "total": 45
  },
  "flag": true,
  "message": ""
}
```

## 通过agent id更新检测状态

**接口地址**: `/api/v1/agent/update/detection-status`

**请求方式**: `PUT`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 更新Agent的检测状态

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentId | agent id | query | true | string | |
| detectionStatus | 检测状态(0=关闭,1=开启) | query | true | integer(int32) | |

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
  "data": "检测状态更新成功",
  "flag": true,
  "message": ""
}
```

## 通过agentId获取指定agent信息

**接口地址**: `/api/v1/agent/{agentId}`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 获取指定Agent的详细信息

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentId | agent ID | path | true | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«Agent实体» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | Agent信息 | Agent实体 | |
| &emsp;&emsp;agentId | agentId | string | |
| &emsp;&emsp;cpuUsage | cpu使用率 | number(double) | |
| &emsp;&emsp;createTime | 创建时间 | string | |
| &emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 | integer(int32) | |
| &emsp;&emsp;hostname | 主机名 | string | |
| &emsp;&emsp;jdkVersion | jdk版本 | string | |
| &emsp;&emsp;lastActiveTime | 最后活跃时间 | string | |
| &emsp;&emsp;memoryUsage | 内存使用率 | number(double) | |
| &emsp;&emsp;os | 操作系统类型 | string | |
| &emsp;&emsp;process | 应用进程号 | string | |
| &emsp;&emsp;project | 所属项目信息 | 应用实体 | |
| &emsp;&emsp;&emsp;&emsp;id | id | integer | |
| &emsp;&emsp;&emsp;&emsp;level | 应用重要性 | integer | |
| &emsp;&emsp;&emsp;&emsp;name | 应用名 | string | |
| &emsp;&emsp;&emsp;&emsp;tag | 标签 | string | |
| &emsp;&emsp;&emsp;&emsp;userId | 用户ID | integer | |
| &emsp;&emsp;serverPath | 应用路径 | string | |
| &emsp;&emsp;state | 上线状态,1开启,2关闭 | integer(int32) | |
| &emsp;&emsp;version | agent版本 | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "agentId": "agent-123",
    "cpuUsage": 0.15,
    "createTime": "2023-06-15T08:30:00Z",
    "detectionStatus": 1,
    "hostname": "server-01",
    "jdkVersion": "1.8.0_341",
    "lastActiveTime": "2023-06-15T10:45:00Z",
    "memoryUsage": 0.45,
    "os": "Linux",
    "process": "12345",
    "project": {
      "id": 1,
      "level": 1,
      "name": "Sample Project",
      "tag": "web",
      "userId": 1
    },
    "serverPath": "/opt/tomcat/webapps",
    "state": 1,
    "version": "1.0.0"
  },
  "flag": true,
  "message": ""
}
```

## 下线Agent

**接口地址**: `/client/agent/deregister`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 提供给agent的接口，当Agent绑定的应用退出，发送请求下线，服务端设置Agent状态为0

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentId | agentId | query | true | string | |

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
  "data": "Agent下线成功",
  "flag": true,
  "message": ""
}
```

## 心跳包

**接口地址**: `/client/agent/heartbeat`

**请求方式**: `POST`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: Agent向服务器发送心跳信息

### 请求示例

```json
{
  "agentId": "agent-123",
  "cpuUsage": 0.15,
  "createTime": "2023-06-15T08:30:00Z",
  "detectionStatus": 1,
  "hostname": "server-01",
  "jdkVersion": "1.8.0_341",
  "lastActiveTime": "2023-06-15T10:45:00Z",
  "memoryUsage": 0.45,
  "os": "Linux",
  "process": "12345",
  "project": {
    "id": 1,
    "level": 1,
    "name": "Sample Project",
    "tag": "web",
    "userId": 1
  },
  "serverPath": "/opt/tomcat/webapps",
  "state": 1,
  "version": "1.0.0"
}
```

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentEntity | agentEntity | body | true | Agent实体 | Agent实体 |
| &emsp;&emsp;agentId | agentId |  | false | string | |
| &emsp;&emsp;cpuUsage | cpu使用率 |  | false | number(double) | |
| &emsp;&emsp;createTime | 创建时间 |  | false | string | |
| &emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 |  | false | integer(int32) | |
| &emsp;&emsp;hostname | 主机名 |  | false | string | |
| &emsp;&emsp;jdkVersion | jdk版本 |  | false | string | |
| &emsp;&emsp;lastActiveTime | 最后活跃时间 |  | false | string | |
| &emsp;&emsp;memoryUsage | 内存使用率 |  | false | number(double) | |
| &emsp;&emsp;os | 操作系统类型 |  | false | string | |
| &emsp;&emsp;process | 应用进程号 |  | false | string | |
| &emsp;&emsp;project | 所属项目信息 |  | false | 应用实体 | 应用实体 |
| &emsp;&emsp;&emsp;&emsp;id | id |  | false | integer | |
| &emsp;&emsp;&emsp;&emsp;level | 应用重要性 |  | false | integer | |
| &emsp;&emsp;&emsp;&emsp;name | 应用名 |  | false | string | |
| &emsp;&emsp;&emsp;&emsp;tag | 标签 |  | false | string | |
| &emsp;&emsp;&emsp;&emsp;userId | 用户ID |  | false | integer | |
| &emsp;&emsp;serverPath | 应用路径 |  | false | string | |
| &emsp;&emsp;state | 上线状态,1开启,2关闭 |  | false | integer(int32) | |
| &emsp;&emsp;version | agent版本 |  | false | string | |

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

## 安装agent

**接口地址**: `/client/agent/install`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: Agent安装相关接口

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| projectKey | 项目密钥 | query | true | string | |
| appName | 应用名称 | query | true | string | |
| agentVersion | Agent版本 | query | false | string | |
| os | 操作系统类型 | query | false | string | |
| jdkVersion | JDK版本 | query | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«Agent实体» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | Agent信息 | Agent实体 | |
| &emsp;&emsp;agentId | agentId | string | |
| &emsp;&emsp;cpuUsage | cpu使用率 | number(double) | |
| &emsp;&emsp;createTime | 创建时间 | string | |
| &emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 | integer(int32) | |
| &emsp;&emsp;hostname | 主机名 | string | |
| &emsp;&emsp;jdkVersion | jdk版本 | string | |
| &emsp;&emsp;lastActiveTime | 最后活跃时间 | string | |
| &emsp;&emsp;memoryUsage | 内存使用率 | number(double) | |
| &emsp;&emsp;os | 操作系统类型 | string | |
| &emsp;&emsp;process | 应用进程号 | string | |
| &emsp;&emsp;project | 所属项目信息 | 应用实体 | |
| &emsp;&emsp;&emsp;&emsp;id | id | integer | |
| &emsp;&emsp;&emsp;&emsp;level | 应用重要性 | integer | |
| &emsp;&emsp;&emsp;&emsp;name | 应用名 | string | |
| &emsp;&emsp;&emsp;&emsp;tag | 标签 | string | |
| &emsp;&emsp;&emsp;&emsp;userId | 用户ID | integer | |
| &emsp;&emsp;serverPath | 应用路径 | string | |
| &emsp;&emsp;state | 上线状态,1开启,2关闭 | integer(int32) | |
| &emsp;&emsp;version | agent版本 | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

### 响应示例

```json
{
  "data": {
    "agentId": "agent-123",
    "cpuUsage": 0,
    "createTime": "2023-06-15T08:30:00Z",
    "detectionStatus": 1,
    "hostname": "server-01",
    "jdkVersion": "1.8.0_341",
    "lastActiveTime": "2023-06-15T08:30:00Z",
    "memoryUsage": 0,
    "os": "Linux",
    "process": "",
    "project": {
      "id": 1,
      "level": 1,
      "name": "Sample Project",
      "tag": "web",
      "userId": 1
    },
    "serverPath": "",
    "state": 1,
    "version": "1.0.0"
  },
  "flag": true,
  "message": "Agent安装成功"
}
```

## 下载install.sh

**接口地址**: `/client/agent/download`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 下载install.sh

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

## 注册Agent

**接口地址**: `/client/agent/register`

**请求方式**: `POST`

**请求数据类型**: `application/json`

**响应数据类型**: `*/*`

**接口描述**: 提供给agent端使用

### 请求示例

```json
{
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
}
```

### 请求参数

| 参数名称 | 参数说明 | 请求类型 | 是否必须 | 数据类型 | schema |
|----------|----------|----------|----------|----------|--------|
| agentEntity | agentEntity | body | true | Agent实体 | Agent实体 |
| &emsp;&emsp;agentId | agentId |  | false | string | |
| &emsp;&emsp;cpuUsage | cpu使用率 |  | false | number(double) | |
| &emsp;&emsp;createTime | 创建时间 |  | false | string | |
| &emsp;&emsp;detectionStatus | 上线状态,1开启,2关闭 |  | false | integer(int32) | |
| &emsp;&emsp;hostname | 主机名 |  | false | string | |
| &emsp;&emsp;jdkVersion | jdk版本 |  | false | string | |
| &emsp;&emsp;lastActiveTime | 最后活跃时间 |  | false | string | |
| &emsp;&emsp;memoryUsage | 内存使用率 |  | false | number(double) | |
| &emsp;&emsp;os | 操作系统类型 |  | false | string | |
| &emsp;&emsp;process | 应用进程号 |  | false | string | |
| &emsp;&emsp;project | 所属项目信息 |  | false | 应用实体 | 应用实体 |
| &emsp;&emsp;&emsp;&emsp;id | id |  | false | integer | |
| &emsp;&emsp;&emsp;&emsp;level | 应用重要性 |  | false | integer | |
| &emsp;&emsp;&emsp;&emsp;name | 应用名 |  | false | string | |
| &emsp;&emsp;&emsp;&emsp;tag | 标签 |  | false | string | |
| &emsp;&emsp;&emsp;&emsp;userId | 用户ID |  | false | integer | |
| &emsp;&emsp;serverPath | 应用路径 |  | false | string | |
| &emsp;&emsp;state | 上线状态,1开启,2关闭 |  | false | integer(int32) | |
| &emsp;&emsp;version | agent版本 |  | false | string | |

### 响应状态

| 状态码 | 说明 | schema |
|--------|------|--------|
| 200 | OK | R«AuthenticationDto» |

### 响应参数

| 参数名称 | 参数说明 | 类型 | schema |
|----------|----------|------|--------|
| data | | AuthenticationDto | |
| &emsp;&emsp;agentId | | string | |
| &emsp;&emsp;token | | string | |
| flag | 请求是否成功 | boolean | |
| message | 响应消息 | string | |

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

## 当前Agent版本

**接口地址**: `/client/agent/version`

**请求方式**: `GET`

**请求数据类型**: `application/x-www-form-urlencoded`

**响应数据类型**: `*/*`

**接口描述**: 获取当前Agent版本

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
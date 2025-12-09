# API测试文档


**简介**:API测试文档


**HOST**:localhost:8989


**联系人**:keven1z


**Version**:v0.1.0


**接口路径**:/v2/api-docs


[TOC]






# API密钥管理


## 创建API密钥


**接口地址**:`/api/v1/apikey/create`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|密钥名称|query|true|string||
|description|密钥描述|query|false|string||
|expiredDateStr|过期日期 (支持ISO 8601格式如2025-11-17T02:20:11.400Z或yyyy-MM-dd HH:mm:ss, 可选)|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«API密钥实体类»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||API密钥实体类|API密钥实体类|
|&emsp;&emsp;apiKey|API密钥|string||
|&emsp;&emsp;createdTime|创建时间|string(date-time)||
|&emsp;&emsp;description|密钥描述|string||
|&emsp;&emsp;expiredTime|过期时间|string(date-time)||
|&emsp;&emsp;id|主键ID|integer(int64)||
|&emsp;&emsp;name|密钥名称|string||
|&emsp;&emsp;status|状态：0-禁用，1-启用|integer(int32)||
|&emsp;&emsp;userId|关联的用户ID|integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/apikey/delete/{id}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|API密钥ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


## 获取API密钥列表


**接口地址**:`/api/v1/apikey/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页数量|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«API密钥实体类»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«API密钥实体类»|PageInfo«API密钥实体类»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|API密钥实体类|
|&emsp;&emsp;&emsp;&emsp;apiKey|API密钥|string||
|&emsp;&emsp;&emsp;&emsp;createdTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;description|密钥描述|string||
|&emsp;&emsp;&emsp;&emsp;expiredTime|过期时间|string||
|&emsp;&emsp;&emsp;&emsp;id|主键ID|integer||
|&emsp;&emsp;&emsp;&emsp;name|密钥名称|string||
|&emsp;&emsp;&emsp;&emsp;status|状态：0-禁用，1-启用|integer||
|&emsp;&emsp;&emsp;&emsp;userId|关联的用户ID|integer||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/apikey/search`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页数量|query|true|integer(int32)||
|name|密钥名称（模糊匹配）|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«API密钥实体类»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«API密钥实体类»|PageInfo«API密钥实体类»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|API密钥实体类|
|&emsp;&emsp;&emsp;&emsp;apiKey|API密钥|string||
|&emsp;&emsp;&emsp;&emsp;createdTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;description|密钥描述|string||
|&emsp;&emsp;&emsp;&emsp;expiredTime|过期时间|string||
|&emsp;&emsp;&emsp;&emsp;id|主键ID|integer||
|&emsp;&emsp;&emsp;&emsp;name|密钥名称|string||
|&emsp;&emsp;&emsp;&emsp;status|状态：0-禁用，1-启用|integer||
|&emsp;&emsp;&emsp;&emsp;userId|关联的用户ID|integer||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/apikey/updateStatus`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|密钥ID|query|true|integer(int64)||
|status|状态值：0-禁用，1-启用|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


# Agent接口


## 获取在线和不在线的agent数量


**接口地址**:`/api/v1/agent/count/status`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«Map«string,int»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||object||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {},
	"flag": true,
	"message": ""
}
```


## 通过agent id删除agent


**接口地址**:`/api/v1/agent/delete/{agentId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>仅支持单一删除</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agent ID|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 下载Agent


**接口地址**:`/api/v1/agent/download`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*,application/java-archive`


**接口描述**:<p>下载iast-agent.jar</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|fileName|文件名(agent或engine)|query|true|string||
|serverUrl|服务器URL(下载engine时必需)|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 获取agent与服务器通信key


**接口地址**:`/api/v1/agent/get-agent-key`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 查询所有agent列表


**接口地址**:`/api/v1/agent/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>支持主机名、state、agent版本筛选</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|hostname|主机名|query|false|string||
|state|状态(0=离线,1=在线)|query|false|string||
|version|agent版本|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«Agent实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|Agent实体|
|&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;cpuUsage|cpu使用率|number(double)||
|&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer(int32)||
|&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;memoryUsage|内存使用率|number(double)||
|&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;state|上线状态,1开启,2关闭|integer(int32)||
|&emsp;&emsp;version|agent版本|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": [
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
	],
	"flag": true,
	"message": ""
}
```


## 分页查询所有agent


**接口地址**:`/api/v1/agent/list/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>支持主机名、state、agent版本筛选</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页数量|query|true|integer(int32)||
|hostname|主机名|query|false|string||
|state|状态(0=离线,1=在线)|query|false|string||
|version|agent版本|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«Agent实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«Agent实体»|PageInfo«Agent实体»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|Agent实体|
|&emsp;&emsp;&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;&emsp;&emsp;cpuUsage|cpu使用率|number||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;&emsp;&emsp;memoryUsage|内存使用率|number||
|&emsp;&emsp;&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;&emsp;&emsp;state|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;version|agent版本|string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {
		"endRow": 0,
		"hasNextPage": true,
		"hasPreviousPage": true,
		"isFirstPage": true,
		"isLastPage": true,
		"list": [
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


## 通过agent id更新检测状态


**接口地址**:`/api/v1/agent/update/detection-status`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agent id|query|true|string||
|detectionStatus|检测状态(0=关闭,1=开启)|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 通过agentId获取指定agent信息


**接口地址**:`/api/v1/agent/{agentId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agent ID|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«Agent实体»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||Agent实体|Agent实体|
|&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;cpuUsage|cpu使用率|number(double)||
|&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer(int32)||
|&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;memoryUsage|内存使用率|number(double)||
|&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;state|上线状态,1开启,2关闭|integer(int32)||
|&emsp;&emsp;version|agent版本|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {
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
	"flag": true,
	"message": ""
}
```


## 下线Agent


**接口地址**:`/client/agent/deregister`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>提供给agent的接口，当Agent绑定的应用退出，发送请求下线，服务端设置Agent状态为0</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agentId|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 心跳包


**接口地址**:`/client/agent/heartbeat`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentEntity|agentEntity|body|true|Agent实体|Agent实体|
|&emsp;&emsp;agentId|agentId||false|string||
|&emsp;&emsp;cpuUsage|cpu使用率||false|number(double)||
|&emsp;&emsp;createTime|创建时间||false|string||
|&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭||false|integer(int32)||
|&emsp;&emsp;hostname|主机名||false|string||
|&emsp;&emsp;jdkVersion|jdk版本||false|string||
|&emsp;&emsp;lastActiveTime|最后活跃时间||false|string||
|&emsp;&emsp;memoryUsage|内存使用率||false|number(double)||
|&emsp;&emsp;os|操作系统类型||false|string||
|&emsp;&emsp;process|应用进程号||false|string||
|&emsp;&emsp;project|所属项目信息||false|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;id|id||false|integer||
|&emsp;&emsp;&emsp;&emsp;level|应用重要性||false|integer||
|&emsp;&emsp;&emsp;&emsp;name|应用名||false|string||
|&emsp;&emsp;&emsp;&emsp;tag|标签||false|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户ID||false|integer||
|&emsp;&emsp;serverPath|应用路径||false|string||
|&emsp;&emsp;state|上线状态,1开启,2关闭||false|integer(int32)||
|&emsp;&emsp;version|agent版本||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


## 安装agent


**接口地址**:`/client/agent/install`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>下载install.sh</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 注册Agent


**接口地址**:`/client/agent/register`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>提供给agent端使用</p>



**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentEntity|agentEntity|body|true|Agent实体|Agent实体|
|&emsp;&emsp;agentId|agentId||false|string||
|&emsp;&emsp;cpuUsage|cpu使用率||false|number(double)||
|&emsp;&emsp;createTime|创建时间||false|string||
|&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭||false|integer(int32)||
|&emsp;&emsp;hostname|主机名||false|string||
|&emsp;&emsp;jdkVersion|jdk版本||false|string||
|&emsp;&emsp;lastActiveTime|最后活跃时间||false|string||
|&emsp;&emsp;memoryUsage|内存使用率||false|number(double)||
|&emsp;&emsp;os|操作系统类型||false|string||
|&emsp;&emsp;process|应用进程号||false|string||
|&emsp;&emsp;project|所属项目信息||false|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;id|id||false|integer||
|&emsp;&emsp;&emsp;&emsp;level|应用重要性||false|integer||
|&emsp;&emsp;&emsp;&emsp;name|应用名||false|string||
|&emsp;&emsp;&emsp;&emsp;tag|标签||false|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户ID||false|integer||
|&emsp;&emsp;serverPath|应用路径||false|string||
|&emsp;&emsp;state|上线状态,1开启,2关闭||false|integer(int32)||
|&emsp;&emsp;version|agent版本||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«AuthenticationDto»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||AuthenticationDto|AuthenticationDto|
|&emsp;&emsp;agentId||string||
|&emsp;&emsp;token||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/client/agent/version`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


# Webhook管理


## 创建Webhook配置


**接口地址**:`/api/v1/webhook`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>创建新的Webhook配置，包括URL、请求方法、自定义头和事件类型等</p>



**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|webhookDTO|Webhook配置数据传输对象|body|true|WebhookDTO|WebhookDTO|
|&emsp;&emsp;description|备注||false|string||
|&emsp;&emsp;events|触发事件类型列表||false|array|string|
|&emsp;&emsp;headers|请求头配置||false|string||
|&emsp;&emsp;method|请求方法,可用值:GET,POST||false|string||
|&emsp;&emsp;name|Webhook名称||true|string||
|&emsp;&emsp;status|状态：0-禁用，1-启用||false|integer(int32)||
|&emsp;&emsp;url|Webhook URL||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«WebhookEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||WebhookEntity|WebhookEntity|
|&emsp;&emsp;createdTime|创建时间|string(date-time)||
|&emsp;&emsp;description|备注|string||
|&emsp;&emsp;events|触发事件类型列表|array|string|
|&emsp;&emsp;headers|请求头配置|string||
|&emsp;&emsp;id|Webhook ID|integer(int64)||
|&emsp;&emsp;method|请求方法,可用值:GET,POST|string||
|&emsp;&emsp;name|Webhook名称|string||
|&emsp;&emsp;status|状态：0-禁用，1-启用|integer(int32)||
|&emsp;&emsp;updatedTime|更新时间|string(date-time)||
|&emsp;&emsp;url|Webhook URL|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


## 分页查询Webhook列表


**接口地址**:`/api/v1/webhook/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>分页查询Webhook配置列表，支持按名称搜索</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页大小|query|true|integer(int32)||
|name|搜索关键词（名称）|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«WebhookEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«WebhookEntity»|PageInfo«WebhookEntity»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|WebhookEntity|
|&emsp;&emsp;&emsp;&emsp;createdTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;description|备注|string||
|&emsp;&emsp;&emsp;&emsp;events|触发事件类型列表|array|string|
|&emsp;&emsp;&emsp;&emsp;headers|请求头配置|string||
|&emsp;&emsp;&emsp;&emsp;id|Webhook ID|integer||
|&emsp;&emsp;&emsp;&emsp;method|请求方法,可用值:GET,POST|string||
|&emsp;&emsp;&emsp;&emsp;name|Webhook名称|string||
|&emsp;&emsp;&emsp;&emsp;status|状态：0-禁用，1-启用|integer||
|&emsp;&emsp;&emsp;&emsp;updatedTime|更新时间|string||
|&emsp;&emsp;&emsp;&emsp;url|Webhook URL|string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/webhook/test`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>测试Webhook配置的连接是否正常</p>



**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|webhookDTO|Webhook配置数据传输对象|body|true|WebhookDTO|WebhookDTO|
|&emsp;&emsp;description|备注||false|string||
|&emsp;&emsp;events|触发事件类型列表||false|array|string|
|&emsp;&emsp;headers|请求头配置||false|string||
|&emsp;&emsp;method|请求方法,可用值:GET,POST||false|string||
|&emsp;&emsp;name|Webhook名称||true|string||
|&emsp;&emsp;status|状态：0-禁用，1-启用||false|integer(int32)||
|&emsp;&emsp;url|Webhook URL||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 查询Webhook详情


**接口地址**:`/api/v1/webhook/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据ID查询Webhook详细配置信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|Webhook ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«WebhookEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||WebhookEntity|WebhookEntity|
|&emsp;&emsp;createdTime|创建时间|string(date-time)||
|&emsp;&emsp;description|备注|string||
|&emsp;&emsp;events|触发事件类型列表|array|string|
|&emsp;&emsp;headers|请求头配置|string||
|&emsp;&emsp;id|Webhook ID|integer(int64)||
|&emsp;&emsp;method|请求方法,可用值:GET,POST|string||
|&emsp;&emsp;name|Webhook名称|string||
|&emsp;&emsp;status|状态：0-禁用，1-启用|integer(int32)||
|&emsp;&emsp;updatedTime|更新时间|string(date-time)||
|&emsp;&emsp;url|Webhook URL|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/webhook/{id}`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>根据ID更新Webhook配置信息</p>



**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|Webhook ID|path|true|integer(int64)||
|webhookDTO|Webhook配置数据传输对象|body|true|WebhookDTO|WebhookDTO|
|&emsp;&emsp;description|备注||false|string||
|&emsp;&emsp;events|触发事件类型列表||false|array|string|
|&emsp;&emsp;headers|请求头配置||false|string||
|&emsp;&emsp;method|请求方法,可用值:GET,POST||false|string||
|&emsp;&emsp;name|Webhook名称||true|string||
|&emsp;&emsp;status|状态：0-禁用，1-启用||false|integer(int32)||
|&emsp;&emsp;url|Webhook URL||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«WebhookEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||WebhookEntity|WebhookEntity|
|&emsp;&emsp;createdTime|创建时间|string(date-time)||
|&emsp;&emsp;description|备注|string||
|&emsp;&emsp;events|触发事件类型列表|array|string|
|&emsp;&emsp;headers|请求头配置|string||
|&emsp;&emsp;id|Webhook ID|integer(int64)||
|&emsp;&emsp;method|请求方法,可用值:GET,POST|string||
|&emsp;&emsp;name|Webhook名称|string||
|&emsp;&emsp;status|状态：0-禁用，1-启用|integer(int32)||
|&emsp;&emsp;updatedTime|更新时间|string(date-time)||
|&emsp;&emsp;url|Webhook URL|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/webhook/{id}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据ID删除Webhook配置</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|Webhook ID|path|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


## 更新Webhook状态


**接口地址**:`/api/v1/webhook/{id}/status`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>根据ID更新Webhook状态（启用/禁用）</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|Webhook ID|path|true|integer(int64)||
|status|状态值（0:禁用, 1:启用）|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


# 下发指令接口


## 查询所有agent的下发指令


**接口地址**:`/client/policy/all`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«指令实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|指令实体|
|&emsp;&emsp;agentEnabled||boolean||
|&emsp;&emsp;agentId||string||
|&emsp;&emsp;debugMode||boolean||
|&emsp;&emsp;detectEnabled||boolean||
|&emsp;&emsp;excludeClasses||array|string|
|&emsp;&emsp;excludePackages||array|string|
|&emsp;&emsp;excludeUrls||array|string|
|&emsp;&emsp;forceDisabledRules||array|string|
|&emsp;&emsp;id||integer(int32)||
|&emsp;&emsp;logLevel||string||
|&emsp;&emsp;maxCpuUsage||integer(int32)||
|&emsp;&emsp;maxMemoryUsage||integer(int32)||
|&emsp;&emsp;modifiedTime||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/client/policy/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«指令实体»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||指令实体|指令实体|
|&emsp;&emsp;agentEnabled||boolean||
|&emsp;&emsp;agentId||string||
|&emsp;&emsp;debugMode||boolean||
|&emsp;&emsp;detectEnabled||boolean||
|&emsp;&emsp;excludeClasses||array|string|
|&emsp;&emsp;excludePackages||array|string|
|&emsp;&emsp;excludeUrls||array|string|
|&emsp;&emsp;forceDisabledRules||array|string|
|&emsp;&emsp;id||integer(int32)||
|&emsp;&emsp;logLevel||string||
|&emsp;&emsp;maxCpuUsage||integer(int32)||
|&emsp;&emsp;maxMemoryUsage||integer(int32)||
|&emsp;&emsp;modifiedTime||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


# 漏洞报告接口


## 通过agent id查询对应的漏洞


**接口地址**:`/api/v1/report/by-agent/{agentId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agent标识|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«报告实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|报告实体|
|&emsp;&emsp;agent||Agent实体|Agent实体|
|&emsp;&emsp;&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;&emsp;&emsp;cpuUsage|cpu使用率|number||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;&emsp;&emsp;memoryUsage|内存使用率|number||
|&emsp;&emsp;&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;&emsp;&emsp;state|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;version|agent版本|string||
|&emsp;&emsp;duplicateKey|漏洞标识key|string||
|&emsp;&emsp;findingData|污染数据链|string||
|&emsp;&emsp;firstTimestamp|首次报告时间|string||
|&emsp;&emsp;id|id|integer(int32)||
|&emsp;&emsp;lastTimestamp|上次报告时间|string||
|&emsp;&emsp;level|漏洞等级|integer(int32)||
|&emsp;&emsp;protocol|请求protocol|string||
|&emsp;&emsp;requestBody|请求body|string||
|&emsp;&emsp;requestHeader|请求header|object||
|&emsp;&emsp;requestMethod|请求方法|string||
|&emsp;&emsp;responseBody|响应body|string||
|&emsp;&emsp;responseHeader|响应header|object||
|&emsp;&emsp;status||VulnerabilityStatusEntity|VulnerabilityStatusEntity|
|&emsp;&emsp;&emsp;&emsp;isDel||integer||
|&emsp;&emsp;&emsp;&emsp;statusId||integer||
|&emsp;&emsp;&emsp;&emsp;statusName||string||
|&emsp;&emsp;statusCode|响应码|integer(int32)||
|&emsp;&emsp;uri|请求uri|string||
|&emsp;&emsp;url|请求url|string||
|&emsp;&emsp;vulnerableType|漏洞类型|string||
|&emsp;&emsp;vulnerableTypeZH|漏洞类型中文|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/report/delete/{id}`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|漏洞ID|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 导出漏洞数据


**接口地址**:`/api/v1/report/export`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 导出对应id漏洞数据


**接口地址**:`/api/v1/report/export/{reportId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|reportId|漏洞ID|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 最近一年的漏洞


**接口地址**:`/api/v1/report/last-12months`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«int»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": [],
	"flag": true,
	"message": ""
}
```


## 最近七天的漏洞


**接口地址**:`/api/v1/report/last-7days`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«int»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": [],
	"flag": true,
	"message": ""
}
```


## 获取所有level的对应的数量


**接口地址**:`/api/v1/report/level-counts`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agent标识（可选）|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«ReportCountEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|ReportCountEntity|
|&emsp;&emsp;count||integer(int32)||
|&emsp;&emsp;level||string||
|&emsp;&emsp;vulnerable_type_zh||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/report/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|firstTimestamp|firstTimestamp|query|false|string||
|lastTimestamp|lastTimestamp|query|false|string||
|level|level|query|false|integer(int32)||
|url|url|query|false|string||
|vulnerableType|vulnerableType|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«报告实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|报告实体|
|&emsp;&emsp;agent||Agent实体|Agent实体|
|&emsp;&emsp;&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;&emsp;&emsp;cpuUsage|cpu使用率|number||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;&emsp;&emsp;memoryUsage|内存使用率|number||
|&emsp;&emsp;&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;&emsp;&emsp;state|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;version|agent版本|string||
|&emsp;&emsp;duplicateKey|漏洞标识key|string||
|&emsp;&emsp;findingData|污染数据链|string||
|&emsp;&emsp;firstTimestamp|首次报告时间|string||
|&emsp;&emsp;id|id|integer(int32)||
|&emsp;&emsp;lastTimestamp|上次报告时间|string||
|&emsp;&emsp;level|漏洞等级|integer(int32)||
|&emsp;&emsp;protocol|请求protocol|string||
|&emsp;&emsp;requestBody|请求body|string||
|&emsp;&emsp;requestHeader|请求header|object||
|&emsp;&emsp;requestMethod|请求方法|string||
|&emsp;&emsp;responseBody|响应body|string||
|&emsp;&emsp;responseHeader|响应header|object||
|&emsp;&emsp;status||VulnerabilityStatusEntity|VulnerabilityStatusEntity|
|&emsp;&emsp;&emsp;&emsp;isDel||integer||
|&emsp;&emsp;&emsp;&emsp;statusId||integer||
|&emsp;&emsp;&emsp;&emsp;statusName||string||
|&emsp;&emsp;statusCode|响应码|integer(int32)||
|&emsp;&emsp;uri|请求uri|string||
|&emsp;&emsp;url|请求url|string||
|&emsp;&emsp;vulnerableType|漏洞类型|string||
|&emsp;&emsp;vulnerableTypeZH|漏洞类型中文|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/report/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|firstTimestamp|firstTimestamp|query|false|string||
|lastTimestamp|lastTimestamp|query|false|string||
|level|level|query|false|integer(int32)||
|pageNum|pageNum|query|false|integer(int32)||
|pageSize|pageSize|query|false|integer(int32)||
|url|url|query|false|string||
|vulnerableType|vulnerableType|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«报告实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«报告实体»|PageInfo«报告实体»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|报告实体|
|&emsp;&emsp;&emsp;&emsp;agent||Agent实体|Agent实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;cpuUsage|cpu使用率|number||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;memoryUsage|内存使用率|number||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;state|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;version|agent版本|string||
|&emsp;&emsp;&emsp;&emsp;duplicateKey|漏洞标识key|string||
|&emsp;&emsp;&emsp;&emsp;findingData|污染数据链|string||
|&emsp;&emsp;&emsp;&emsp;firstTimestamp|首次报告时间|string||
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;lastTimestamp|上次报告时间|string||
|&emsp;&emsp;&emsp;&emsp;level|漏洞等级|integer||
|&emsp;&emsp;&emsp;&emsp;protocol|请求protocol|string||
|&emsp;&emsp;&emsp;&emsp;requestBody|请求body|string||
|&emsp;&emsp;&emsp;&emsp;requestHeader|请求header|object||
|&emsp;&emsp;&emsp;&emsp;requestMethod|请求方法|string||
|&emsp;&emsp;&emsp;&emsp;responseBody|响应body|string||
|&emsp;&emsp;&emsp;&emsp;responseHeader|响应header|object||
|&emsp;&emsp;&emsp;&emsp;status||VulnerabilityStatusEntity|VulnerabilityStatusEntity|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;isDel||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;statusId||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;statusName||string||
|&emsp;&emsp;&emsp;&emsp;statusCode|响应码|integer||
|&emsp;&emsp;&emsp;&emsp;uri|请求uri|string||
|&emsp;&emsp;&emsp;&emsp;url|请求url|string||
|&emsp;&emsp;&emsp;&emsp;vulnerableType|漏洞类型|string||
|&emsp;&emsp;&emsp;&emsp;vulnerableTypeZH|漏洞类型中文|string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {
		"endRow": 0,
		"hasNextPage": true,
		"hasPreviousPage": true,
		"isFirstPage": true,
		"isLastPage": true,
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


## 修改漏洞状态


**接口地址**:`/api/v1/report/status`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|reportId|报告ID|query|true|integer(int32)||
|statusId|状态ID|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 获取指定agent最近12个月的漏洞详情统计


**接口地址**:`/api/v1/report/trend/month/{agentId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agent标识|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«TimeReportCountEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|TimeReportCountEntity|
|&emsp;&emsp;levelCounts||object||
|&emsp;&emsp;time||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


## 获取指定agent最近7天的漏洞详情统计


**接口地址**:`/api/v1/report/trend/week/{agentId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|agentId|agent标识|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«TimeReportCountEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|TimeReportCountEntity|
|&emsp;&emsp;levelCounts||object||
|&emsp;&emsp;time||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/report/{id}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|漏洞ID|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«报告实体»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||报告实体|报告实体|
|&emsp;&emsp;agent||Agent实体|Agent实体|
|&emsp;&emsp;&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;&emsp;&emsp;cpuUsage|cpu使用率|number||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;&emsp;&emsp;memoryUsage|内存使用率|number||
|&emsp;&emsp;&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;&emsp;&emsp;state|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;version|agent版本|string||
|&emsp;&emsp;duplicateKey|漏洞标识key|string||
|&emsp;&emsp;findingData|污染数据链|string||
|&emsp;&emsp;firstTimestamp|首次报告时间|string||
|&emsp;&emsp;id|id|integer(int32)||
|&emsp;&emsp;lastTimestamp|上次报告时间|string||
|&emsp;&emsp;level|漏洞等级|integer(int32)||
|&emsp;&emsp;protocol|请求protocol|string||
|&emsp;&emsp;requestBody|请求body|string||
|&emsp;&emsp;requestHeader|请求header|object||
|&emsp;&emsp;requestMethod|请求方法|string||
|&emsp;&emsp;responseBody|响应body|string||
|&emsp;&emsp;responseHeader|响应header|object||
|&emsp;&emsp;status||VulnerabilityStatusEntity|VulnerabilityStatusEntity|
|&emsp;&emsp;&emsp;&emsp;isDel||integer||
|&emsp;&emsp;&emsp;&emsp;statusId||integer||
|&emsp;&emsp;&emsp;&emsp;statusName||string||
|&emsp;&emsp;statusCode|响应码|integer(int32)||
|&emsp;&emsp;uri|请求uri|string||
|&emsp;&emsp;url|请求url|string||
|&emsp;&emsp;vulnerableType|漏洞类型|string||
|&emsp;&emsp;vulnerableTypeZH|漏洞类型中文|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/client/report/receive`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


# 漏洞状态接口


## 查询单个漏洞状态


**接口地址**:`/api/v1/vulnerability-status/get/{statusId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|statusId|状态ID|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«VulnerabilityStatusEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||VulnerabilityStatusEntity|VulnerabilityStatusEntity|
|&emsp;&emsp;isDel||integer(int32)||
|&emsp;&emsp;statusId||integer(int32)||
|&emsp;&emsp;statusName||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {
		"isDel": 0,
		"statusId": 0,
		"statusName": ""
	},
	"flag": true,
	"message": ""
}
```


## 所有的漏洞状态


**接口地址**:`/api/v1/vulnerability-status/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«VulnerabilityStatusEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|VulnerabilityStatusEntity|
|&emsp;&emsp;isDel||integer(int32)||
|&emsp;&emsp;statusId||integer(int32)||
|&emsp;&emsp;statusName||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": [
		{
			"isDel": 0,
			"statusId": 0,
			"statusName": ""
		}
	],
	"flag": true,
	"message": ""
}
```


# 漏洞评论接口


## 添加漏洞评论


**接口地址**:`/api/v1/comment`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentEntity|漏洞评论实体类|body|true|VulnerabilityCommentEntity|VulnerabilityCommentEntity|
|&emsp;&emsp;content|评论内容||true|string||
|&emsp;&emsp;createdAt|评论时间||false|string(date-time)||
|&emsp;&emsp;id|评论ID||false|integer(int32)||
|&emsp;&emsp;reportId|漏洞ID||true|integer(int32)||
|&emsp;&emsp;user|用户信息||false|UserEntity|UserEntity|
|&emsp;&emsp;&emsp;&emsp;created|账号创建时间||false|string||
|&emsp;&emsp;&emsp;&emsp;email|注册邮箱||false|string||
|&emsp;&emsp;&emsp;&emsp;id|用户ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;&emsp;&emsp;phone|注册手机号||false|string||
|&emsp;&emsp;&emsp;&emsp;state|账号状态||false|string||
|&emsp;&emsp;&emsp;&emsp;updated|账号更新时间||false|string||
|&emsp;&emsp;&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;userId|用户ID||true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 更新漏洞评论


**接口地址**:`/api/v1/comment`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentEntity|漏洞评论实体类|body|true|VulnerabilityCommentEntity|VulnerabilityCommentEntity|
|&emsp;&emsp;content|评论内容||true|string||
|&emsp;&emsp;createdAt|评论时间||false|string(date-time)||
|&emsp;&emsp;id|评论ID||false|integer(int32)||
|&emsp;&emsp;reportId|漏洞ID||true|integer(int32)||
|&emsp;&emsp;user|用户信息||false|UserEntity|UserEntity|
|&emsp;&emsp;&emsp;&emsp;created|账号创建时间||false|string||
|&emsp;&emsp;&emsp;&emsp;email|注册邮箱||false|string||
|&emsp;&emsp;&emsp;&emsp;id|用户ID||false|integer||
|&emsp;&emsp;&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;&emsp;&emsp;phone|注册手机号||false|string||
|&emsp;&emsp;&emsp;&emsp;state|账号状态||false|string||
|&emsp;&emsp;&emsp;&emsp;updated|账号更新时间||false|string||
|&emsp;&emsp;&emsp;&emsp;username|用户名||true|string||
|&emsp;&emsp;userId|用户ID||true|integer(int64)||
|userId|用户ID|query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 获取指定漏洞的评论列表


**接口地址**:`/api/v1/comment/list/{reportId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|reportId|漏洞ID|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«VulnerabilityCommentEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|VulnerabilityCommentEntity|
|&emsp;&emsp;content|评论内容|string||
|&emsp;&emsp;createdAt|评论时间|string(date-time)||
|&emsp;&emsp;id|评论ID|integer(int32)||
|&emsp;&emsp;reportId|漏洞ID|integer(int32)||
|&emsp;&emsp;user|用户信息|UserEntity|UserEntity|
|&emsp;&emsp;&emsp;&emsp;created|账号创建时间|string||
|&emsp;&emsp;&emsp;&emsp;email|注册邮箱|string||
|&emsp;&emsp;&emsp;&emsp;id|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;password|密码|string||
|&emsp;&emsp;&emsp;&emsp;phone|注册手机号|string||
|&emsp;&emsp;&emsp;&emsp;state|账号状态|string||
|&emsp;&emsp;&emsp;&emsp;updated|账号更新时间|string||
|&emsp;&emsp;&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;userId|用户ID|integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/comment/{commentId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|评论ID|path|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«VulnerabilityCommentEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||VulnerabilityCommentEntity|VulnerabilityCommentEntity|
|&emsp;&emsp;content|评论内容|string||
|&emsp;&emsp;createdAt|评论时间|string(date-time)||
|&emsp;&emsp;id|评论ID|integer(int32)||
|&emsp;&emsp;reportId|漏洞ID|integer(int32)||
|&emsp;&emsp;user|用户信息|UserEntity|UserEntity|
|&emsp;&emsp;&emsp;&emsp;created|账号创建时间|string||
|&emsp;&emsp;&emsp;&emsp;email|注册邮箱|string||
|&emsp;&emsp;&emsp;&emsp;id|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;password|密码|string||
|&emsp;&emsp;&emsp;&emsp;phone|注册手机号|string||
|&emsp;&emsp;&emsp;&emsp;state|账号状态|string||
|&emsp;&emsp;&emsp;&emsp;updated|账号更新时间|string||
|&emsp;&emsp;&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;userId|用户ID|integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


## 删除漏洞评论


**接口地址**:`/api/v1/comment/{commentId}`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|commentId|评论ID|path|true|integer(int32)||
|userId|用户ID|query|true|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


# 用户管理


## 添加用户


**接口地址**:`/api/v1/user/add`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>创建新用户</p>



**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userEntity|用户信息实体类|body|true|UserEntity|UserEntity|
|&emsp;&emsp;created|账号创建时间||false|string(date-time)||
|&emsp;&emsp;email|注册邮箱||false|string||
|&emsp;&emsp;id|用户ID||false|integer(int64)||
|&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;phone|注册手机号||false|string||
|&emsp;&emsp;state|账号状态||false|string||
|&emsp;&emsp;updated|账号更新时间||false|string(date-time)||
|&emsp;&emsp;username|用户名||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 删除用户


**接口地址**:`/api/v1/user/delete`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>删除指定用户，支持批量删除</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|用户ID，多个ID用逗号分隔|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 根据ID获取用户


**接口地址**:`/api/v1/user/get`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>通过用户ID查询用户信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|用户ID|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«UserEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||UserEntity|UserEntity|
|&emsp;&emsp;created|账号创建时间|string(date-time)||
|&emsp;&emsp;email|注册邮箱|string||
|&emsp;&emsp;id|用户ID|integer(int64)||
|&emsp;&emsp;password|密码|string||
|&emsp;&emsp;phone|注册手机号|string||
|&emsp;&emsp;state|账号状态|string||
|&emsp;&emsp;updated|账号更新时间|string(date-time)||
|&emsp;&emsp;username|用户名|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/user/getByName`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>通过用户名查询用户信息</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|用户名|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«UserEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||UserEntity|UserEntity|
|&emsp;&emsp;created|账号创建时间|string(date-time)||
|&emsp;&emsp;email|注册邮箱|string||
|&emsp;&emsp;id|用户ID|integer(int64)||
|&emsp;&emsp;password|密码|string||
|&emsp;&emsp;phone|注册手机号|string||
|&emsp;&emsp;state|账号状态|string||
|&emsp;&emsp;updated|账号更新时间|string(date-time)||
|&emsp;&emsp;username|用户名|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/user/info`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>获取当前登录用户的详细信息</p>



**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«UserEntity»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||UserEntity|UserEntity|
|&emsp;&emsp;created|账号创建时间|string(date-time)||
|&emsp;&emsp;email|注册邮箱|string||
|&emsp;&emsp;id|用户ID|integer(int64)||
|&emsp;&emsp;password|密码|string||
|&emsp;&emsp;phone|注册手机号|string||
|&emsp;&emsp;state|账号状态|string||
|&emsp;&emsp;updated|账号更新时间|string(date-time)||
|&emsp;&emsp;username|用户名|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/user/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>分页查询用户列表</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页大小|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«UserEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«UserEntity»|PageInfo«UserEntity»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|UserEntity|
|&emsp;&emsp;&emsp;&emsp;created|账号创建时间|string||
|&emsp;&emsp;&emsp;&emsp;email|注册邮箱|string||
|&emsp;&emsp;&emsp;&emsp;id|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;password|密码|string||
|&emsp;&emsp;&emsp;&emsp;phone|注册手机号|string||
|&emsp;&emsp;&emsp;&emsp;state|账号状态|string||
|&emsp;&emsp;&emsp;&emsp;updated|账号更新时间|string||
|&emsp;&emsp;&emsp;&emsp;username|用户名|string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/user/login`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>用户登录获取访问令牌</p>



**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userEntity|用户信息实体类|body|true|UserEntity|UserEntity|
|&emsp;&emsp;created|账号创建时间||false|string(date-time)||
|&emsp;&emsp;email|注册邮箱||false|string||
|&emsp;&emsp;id|用户ID||false|integer(int64)||
|&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;phone|注册手机号||false|string||
|&emsp;&emsp;state|账号状态||false|string||
|&emsp;&emsp;updated|账号更新时间||false|string(date-time)||
|&emsp;&emsp;username|用户名||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«AuthenticationDto»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||AuthenticationDto|AuthenticationDto|
|&emsp;&emsp;agentId||string||
|&emsp;&emsp;token||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/user/update`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>更新用户信息</p>



**请求示例**:


```javascript
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


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userEntity|用户信息实体类|body|true|UserEntity|UserEntity|
|&emsp;&emsp;created|账号创建时间||false|string(date-time)||
|&emsp;&emsp;email|注册邮箱||false|string||
|&emsp;&emsp;id|用户ID||false|integer(int64)||
|&emsp;&emsp;password|密码||true|string||
|&emsp;&emsp;phone|注册手机号||false|string||
|&emsp;&emsp;state|账号状态||false|string||
|&emsp;&emsp;updated|账号更新时间||false|string(date-time)||
|&emsp;&emsp;username|用户名||true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


# 通知接口


## 导出所有操作日志数据


**接口地址**:`/api/v1/log/exportAll`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«OperationLogEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|OperationLogEntity|
|&emsp;&emsp;appCode||string||
|&emsp;&emsp;content||string||
|&emsp;&emsp;deviceInfo||string||
|&emsp;&emsp;id||integer(int64)||
|&emsp;&emsp;ipAddress||string||
|&emsp;&emsp;operationParam||string||
|&emsp;&emsp;operationTime||Timestamp|Timestamp|
|&emsp;&emsp;&emsp;&emsp;date||integer||
|&emsp;&emsp;&emsp;&emsp;day||integer||
|&emsp;&emsp;&emsp;&emsp;hours||integer||
|&emsp;&emsp;&emsp;&emsp;minutes||integer||
|&emsp;&emsp;&emsp;&emsp;month||integer||
|&emsp;&emsp;&emsp;&emsp;nanos||integer||
|&emsp;&emsp;&emsp;&emsp;seconds||integer||
|&emsp;&emsp;&emsp;&emsp;time||integer||
|&emsp;&emsp;&emsp;&emsp;timezoneOffset||integer||
|&emsp;&emsp;&emsp;&emsp;year||integer||
|&emsp;&emsp;operationType||string||
|&emsp;&emsp;operatorName||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/log/exportExcel`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK||


**响应参数**:


暂无


**响应示例**:
```javascript

```


## 查看操作日志


**接口地址**:`/api/v1/log/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页数量|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«OperationLogEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«OperationLogEntity»|PageInfo«OperationLogEntity»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|OperationLogEntity|
|&emsp;&emsp;&emsp;&emsp;appCode||string||
|&emsp;&emsp;&emsp;&emsp;content||string||
|&emsp;&emsp;&emsp;&emsp;deviceInfo||string||
|&emsp;&emsp;&emsp;&emsp;id||integer||
|&emsp;&emsp;&emsp;&emsp;ipAddress||string||
|&emsp;&emsp;&emsp;&emsp;operationParam||string||
|&emsp;&emsp;&emsp;&emsp;operationTime||Timestamp|Timestamp|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;date||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;day||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hours||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minutes||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;month||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;nanos||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;seconds||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;time||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;timezoneOffset||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;year||integer||
|&emsp;&emsp;&emsp;&emsp;operationType||string||
|&emsp;&emsp;&emsp;&emsp;operatorName||string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


**接口地址**:`/api/v1/log/search`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页数量|query|true|integer(int32)||
|endTime|结束时间（可选，格式：yyyy-MM-dd HH:mm:ss）|query|false|string||
|operatorName|操作者账号（可选）|query|false|string||
|startTime|开始时间（可选，格式：yyyy-MM-dd HH:mm:ss）|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«OperationLogEntity»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«OperationLogEntity»|PageInfo«OperationLogEntity»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|OperationLogEntity|
|&emsp;&emsp;&emsp;&emsp;appCode||string||
|&emsp;&emsp;&emsp;&emsp;content||string||
|&emsp;&emsp;&emsp;&emsp;deviceInfo||string||
|&emsp;&emsp;&emsp;&emsp;id||integer||
|&emsp;&emsp;&emsp;&emsp;ipAddress||string||
|&emsp;&emsp;&emsp;&emsp;operationParam||string||
|&emsp;&emsp;&emsp;&emsp;operationTime||Timestamp|Timestamp|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;date||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;day||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hours||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;minutes||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;month||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;nanos||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;seconds||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;time||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;timezoneOffset||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;year||integer||
|&emsp;&emsp;&emsp;&emsp;operationType||string||
|&emsp;&emsp;&emsp;&emsp;operatorName||string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


## 查看通知历史


**接口地址**:`/api/v1/notify/all`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|pageNum|query|false|integer(int32)||
|pageSize|pageSize|query|false|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«报告实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«报告实体»|PageInfo«报告实体»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|报告实体|
|&emsp;&emsp;&emsp;&emsp;agent||Agent实体|Agent实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;cpuUsage|cpu使用率|number||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;memoryUsage|内存使用率|number||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;state|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;version|agent版本|string||
|&emsp;&emsp;&emsp;&emsp;duplicateKey|漏洞标识key|string||
|&emsp;&emsp;&emsp;&emsp;findingData|污染数据链|string||
|&emsp;&emsp;&emsp;&emsp;firstTimestamp|首次报告时间|string||
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;lastTimestamp|上次报告时间|string||
|&emsp;&emsp;&emsp;&emsp;level|漏洞等级|integer||
|&emsp;&emsp;&emsp;&emsp;protocol|请求protocol|string||
|&emsp;&emsp;&emsp;&emsp;requestBody|请求body|string||
|&emsp;&emsp;&emsp;&emsp;requestHeader|请求header|object||
|&emsp;&emsp;&emsp;&emsp;requestMethod|请求方法|string||
|&emsp;&emsp;&emsp;&emsp;responseBody|响应body|string||
|&emsp;&emsp;&emsp;&emsp;responseHeader|响应header|object||
|&emsp;&emsp;&emsp;&emsp;status||VulnerabilityStatusEntity|VulnerabilityStatusEntity|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;isDel||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;statusId||integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;statusName||string||
|&emsp;&emsp;&emsp;&emsp;statusCode|响应码|integer||
|&emsp;&emsp;&emsp;&emsp;uri|请求uri|string||
|&emsp;&emsp;&emsp;&emsp;url|请求url|string||
|&emsp;&emsp;&emsp;&emsp;vulnerableType|漏洞类型|string||
|&emsp;&emsp;&emsp;&emsp;vulnerableTypeZH|漏洞类型中文|string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {
		"endRow": 0,
		"hasNextPage": true,
		"hasPreviousPage": true,
		"isFirstPage": true,
		"isLastPage": true,
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


## 设置通知为已读


**接口地址**:`/api/v1/notify/read/set`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|通知ID|query|true|integer(int32)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## connect


**接口地址**:`/api/v1/notify/refresh/{userId}`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|userId|userId|path|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|SseEmitter|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|timeout||integer(int64)|integer(int64)|


**响应示例**:
```javascript
{
	"timeout": 0
}
```


## 未读通知数量


**接口地址**:`/api/v1/notify/unread/count`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 查看未读通知


**接口地址**:`/api/v1/notify/unread/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«报告实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|报告实体|
|&emsp;&emsp;agent||Agent实体|Agent实体|
|&emsp;&emsp;&emsp;&emsp;agentId|agentId|string||
|&emsp;&emsp;&emsp;&emsp;cpuUsage|cpu使用率|number||
|&emsp;&emsp;&emsp;&emsp;createTime|创建时间|string||
|&emsp;&emsp;&emsp;&emsp;detectionStatus|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;hostname|主机名|string||
|&emsp;&emsp;&emsp;&emsp;jdkVersion|jdk版本|string||
|&emsp;&emsp;&emsp;&emsp;lastActiveTime|最后活跃时间|string||
|&emsp;&emsp;&emsp;&emsp;memoryUsage|内存使用率|number||
|&emsp;&emsp;&emsp;&emsp;os|操作系统类型|string||
|&emsp;&emsp;&emsp;&emsp;process|应用进程号|string||
|&emsp;&emsp;&emsp;&emsp;project|所属项目信息|应用实体|应用实体|
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;&emsp;&emsp;serverPath|应用路径|string||
|&emsp;&emsp;&emsp;&emsp;state|上线状态,1开启,2关闭|integer||
|&emsp;&emsp;&emsp;&emsp;version|agent版本|string||
|&emsp;&emsp;duplicateKey|漏洞标识key|string||
|&emsp;&emsp;findingData|污染数据链|string||
|&emsp;&emsp;firstTimestamp|首次报告时间|string||
|&emsp;&emsp;id|id|integer(int32)||
|&emsp;&emsp;lastTimestamp|上次报告时间|string||
|&emsp;&emsp;level|漏洞等级|integer(int32)||
|&emsp;&emsp;protocol|请求protocol|string||
|&emsp;&emsp;requestBody|请求body|string||
|&emsp;&emsp;requestHeader|请求header|object||
|&emsp;&emsp;requestMethod|请求方法|string||
|&emsp;&emsp;responseBody|响应body|string||
|&emsp;&emsp;responseHeader|响应header|object||
|&emsp;&emsp;status||VulnerabilityStatusEntity|VulnerabilityStatusEntity|
|&emsp;&emsp;&emsp;&emsp;isDel||integer||
|&emsp;&emsp;&emsp;&emsp;statusId||integer||
|&emsp;&emsp;&emsp;&emsp;statusName||string||
|&emsp;&emsp;statusCode|响应码|integer(int32)||
|&emsp;&emsp;uri|请求uri|string||
|&emsp;&emsp;url|请求url|string||
|&emsp;&emsp;vulnerableType|漏洞类型|string||
|&emsp;&emsp;vulnerableTypeZH|漏洞类型中文|string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
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


# 项目管理


## 添加项目


**接口地址**:`/api/v1/project/add`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "level": 0,
  "name": "",
  "tag": "",
  "userId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|projectEntity|projectEntity|body|true|应用实体|应用实体|
|&emsp;&emsp;id|id||false|integer(int32)||
|&emsp;&emsp;level|应用重要性||false|integer(int32)||
|&emsp;&emsp;name|应用名||false|string||
|&emsp;&emsp;tag|标签||false|string||
|&emsp;&emsp;userId|用户ID||false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


## 批量删除项目


**接口地址**:`/api/v1/project/batch-delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
[]
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|ids|项目ID列表|body|true|array|string|


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


## 查询项目总数


**接口地址**:`/api/v1/project/count`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


暂无


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«int»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||integer(int32)|integer(int32)|
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": 0,
	"flag": true,
	"message": ""
}
```


## 删除项目


**接口地址**:`/api/v1/project/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|id|项目ID|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


## 查询所有项目列表


**接口地址**:`/api/v1/project/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|importance|应用重要性|query|false|integer(int32)||
|name|项目名称|query|false|string||
|tags|标签（逗号分隔）|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«应用实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|应用实体|
|&emsp;&emsp;id|id|integer(int32)||
|&emsp;&emsp;level|应用重要性|integer(int32)||
|&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;userId|用户ID|integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": [
		{
			"id": 0,
			"level": 0,
			"name": "",
			"tag": "",
			"userId": 0
		}
	],
	"flag": true,
	"message": ""
}
```


## 分页查询项目


**接口地址**:`/api/v1/project/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页数量|query|true|integer(int32)||
|importance|应用重要性|query|false|integer(int32)||
|name|项目名称|query|false|string||
|tags|标签（逗号分隔）|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«应用实体»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«应用实体»|PageInfo«应用实体»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|应用实体|
|&emsp;&emsp;&emsp;&emsp;id|id|integer||
|&emsp;&emsp;&emsp;&emsp;level|应用重要性|integer||
|&emsp;&emsp;&emsp;&emsp;name|应用名|string||
|&emsp;&emsp;&emsp;&emsp;tag|标签|string||
|&emsp;&emsp;&emsp;&emsp;userId|用户ID|integer||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {
		"endRow": 0,
		"hasNextPage": true,
		"hasPreviousPage": true,
		"isFirstPage": true,
		"isLastPage": true,
		"list": [
			{
				"id": 0,
				"level": 0,
				"name": "",
				"tag": "",
				"userId": 0
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


## 更新项目


**接口地址**:`/api/v1/project/update`


**请求方式**:`PUT`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:


**请求示例**:


```javascript
{
  "id": 0,
  "level": 0,
  "name": "",
  "tag": "",
  "userId": 0
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|projectEntity|projectEntity|body|true|应用实体|应用实体|
|&emsp;&emsp;id|id||false|integer(int32)||
|&emsp;&emsp;level|应用重要性||false|integer(int32)||
|&emsp;&emsp;name|应用名||false|string||
|&emsp;&emsp;tag|标签||false|string||
|&emsp;&emsp;userId|用户ID||false|integer(int64)||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«boolean»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||boolean||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": true,
	"flag": true,
	"message": ""
}
```


# 黑名单接口


## 添加黑名单


**接口地址**:`/api/v1/blacklist/add`


**请求方式**:`POST`


**请求数据类型**:`application/json`


**响应数据类型**:`*/*`


**接口描述**:<p>设置主机黑名单，禁止注册</p>



**请求示例**:


```javascript
{
  "id": 0,
  "name": ""
}
```


**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|blacklistEntity|blacklistEntity|body|true|Agent黑名单|Agent黑名单|
|&emsp;&emsp;id|||false|integer(int32)||
|&emsp;&emsp;name|||false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 删除黑名单


**接口地址**:`/api/v1/blacklist/delete`


**请求方式**:`DELETE`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>支持删除单个或多个黑名单（逗号分隔）</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|黑名单名称|query|true|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«string»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": "",
	"flag": true,
	"message": ""
}
```


## 搜索黑名单列表


**接口地址**:`/api/v1/blacklist/list`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据名称搜索黑名单，返回所有匹配结果</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|name|黑名单名称关键字|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«List«Agent黑名单»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||array|Agent黑名单|
|&emsp;&emsp;id||integer(int32)||
|&emsp;&emsp;name||string||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": [
		{
			"id": 0,
			"name": ""
		}
	],
	"flag": true,
	"message": ""
}
```


## 分页搜索黑名单列表


**接口地址**:`/api/v1/blacklist/list/page`


**请求方式**:`GET`


**请求数据类型**:`application/x-www-form-urlencoded`


**响应数据类型**:`*/*`


**接口描述**:<p>根据名称分页搜索黑名单</p>



**请求参数**:


| 参数名称 | 参数说明 | 请求类型    | 是否必须 | 数据类型 | schema |
| -------- | -------- | ----- | -------- | -------- | ------ |
|pageNum|页码|query|true|integer(int32)||
|pageSize|每页数量|query|true|integer(int32)||
|name|黑名单名称关键字|query|false|string||


**响应状态**:


| 状态码 | 说明 | schema |
| -------- | -------- | ----- | 
|200|OK|R«PageInfo«Agent黑名单»»|


**响应参数**:


| 参数名称 | 参数说明 | 类型 | schema |
| -------- | -------- | ----- |----- | 
|data||PageInfo«Agent黑名单»|PageInfo«Agent黑名单»|
|&emsp;&emsp;endRow||integer(int64)||
|&emsp;&emsp;hasNextPage||boolean||
|&emsp;&emsp;hasPreviousPage||boolean||
|&emsp;&emsp;isFirstPage||boolean||
|&emsp;&emsp;isLastPage||boolean||
|&emsp;&emsp;list||array|Agent黑名单|
|&emsp;&emsp;&emsp;&emsp;id||integer||
|&emsp;&emsp;&emsp;&emsp;name||string||
|&emsp;&emsp;navigateFirstPage||integer(int32)||
|&emsp;&emsp;navigateLastPage||integer(int32)||
|&emsp;&emsp;navigatePages||integer(int32)||
|&emsp;&emsp;navigatepageNums||array|integer|
|&emsp;&emsp;nextPage||integer(int32)||
|&emsp;&emsp;pageNum||integer(int32)||
|&emsp;&emsp;pageSize||integer(int32)||
|&emsp;&emsp;pages||integer(int32)||
|&emsp;&emsp;prePage||integer(int32)||
|&emsp;&emsp;size||integer(int32)||
|&emsp;&emsp;startRow||integer(int64)||
|&emsp;&emsp;total||integer(int64)||
|flag||boolean||
|message||string||


**响应示例**:
```javascript
{
	"data": {
		"endRow": 0,
		"hasNextPage": true,
		"hasPreviousPage": true,
		"isFirstPage": true,
		"isLastPage": true,
		"list": [
			{
				"id": 0,
				"name": ""
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
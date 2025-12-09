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
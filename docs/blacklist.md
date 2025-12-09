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
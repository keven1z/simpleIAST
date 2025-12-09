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
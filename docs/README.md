# simpleIAST API 文档

这是 simpleIAST 项目的 API 文档，详细介绍了系统的所有接口。

## 文档导航

- [API密钥管理](./apikey.md)
- [Agent接口](./agent.md)
- [黑名单接口](./blacklist.md)
- [漏洞评论接口](./comment.md)
- [操作日志接口](./log.md)
- [通知接口](./notify.md)
- [策略接口](./policy.md)
- [项目管理](./project.md)
- [漏洞报告接口](./report.md)
- [用户管理](./user.md)
- [漏洞状态接口](./vulnerability-status.md)
- [Webhook接口](./webhook.md)

## 基本信息

- **Host**: `localhost:8989`
- **Version**: `v0.1.0`
- **基础接口路径**: `/api/v1`

## 响应格式

所有API接口的响应格式统一为JSON，包含以下字段：

```json
{
  "data": {},
  "flag": true,
  "message": ""
}
```

- `data`: 响应数据，具体格式根据接口而定
- `flag`: 请求是否成功，`true`为成功，`false`为失败
- `message`: 响应消息，成功时为空或成功描述，失败时为错误信息

## 状态码

| 状态码 | 说明 |
|--------|------|
| 200    | 请求成功 |
| 400    | 请求参数错误 |
| 401    | 未授权 |
| 403    | 拒绝访问 |
| 404    | 资源不存在 |
| 500    | 服务器内部错误 |

## 认证方式

API使用API密钥进行认证，在请求头中添加以下信息：

```
Authorization: Bearer {apiKey}
```
<div align="center">

#  simpleIAST  ![0.1.0 (shields.io)](https://img.shields.io/badge/0.1.0-brightgreen.svg)

</div>


<p align="center">
simpleIAST是一种交互式应用程序安全测试工具。
</p>


## 快速开始

### 1. clone项目

```shell
git clone https://github.com/keven1z/simpleIAST.git
```
### 2. docker运行
```shell
cd ./simpleIAST/docker/
docker-compose up -d
```
### 3. 访问
访问地址: http://\[your_ip\]:8443/
默认用户名: admin
默认密码: 123456

> 前端端口:  8443
> 
> 后端端口: 81
> 
> 数据库端口: 33060
> 
> redis端口: 63790

## Agent启动
> 将iast-agent.jar和iast-engine.jar 放在同一目录
### 跟随应用启动运行
```shell
java -javaagent:iast-agent.jar -jar [app.jar] # 
```
### 应用启动完成attach方式运行
```shell
# attach方式安装agent
java -jar iast-engine.jar -m install -p [pid] 
# attach方式卸载agent
java -jar iast-engine.jar -m uninstall -p [pid] 
```
## 兼容
### 支持中间件

* Tomcat
* Springboot
* Jetty
* Weblogic
* glassfish
* WildFly
* TongWeb
* Resin
* Undertow

### 支持JDK
* jdk 1.8
* jdk 11

## 支持漏洞
* SQL注入
* 反序列化漏洞
* SSRF
* URL跳转漏洞
* XXE
* 命令注入
* 文件上传
* XSS
* Spring EL表达式注入
* 数据库弱口令
* XPATH注入
* 硬编码漏洞

## 漏洞详情展示
![img.png](img/detail.png)

## 使用文档
* [快速开始](https://github.com/keven1z/simpleIAST/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B)
* [应用](https://github.com/keven1z/simpleIAST/wiki/%E5%BA%94%E7%94%A8)
* [Agent](https://github.com/keven1z/simpleIAST/wiki/Agent)
* [漏洞](https://github.com/keven1z/simpleIAST/wiki/%E6%BC%8F%E6%B4%9E)
* [设置](https://github.com/keven1z/simpleIAST/wiki/%E8%AE%BE%E7%BD%AE)

## 计划
- [ ] 心跳包采用websocket
- [x] API改造
- [x] 漏洞检测数量，覆盖[ant-application-security-testing-benchmark](https://github.com/alipay/ant-application-security-testing-benchmark)
- [x] 服务端交互界面(11月底)
- [x] 支持多种中间件

## 鸣谢
> [IntelliJ IDEA](https://zh.wikipedia.org/zh-hans/IntelliJ_IDEA) 是一个在各个方面都最大程度地提高开发人员的生产力的 IDE，适用于 JVM 平台语言。

特别感谢 [JetBrains](https://www.jetbrains.com/?from=mirai) 为开源项目提供免费的 [IntelliJ IDEA](https://www.jetbrains.com/idea/?from=mirai)授权

![](https://resources.jetbrains.com/storage/products/company/brand/logos/jetbrains.svg)

## License
本项目采用 Apache License 2.0 开源授权许可证。

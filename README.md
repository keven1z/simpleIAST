<div align="center">

#  simpleIAST  ![1.0beta (shields.io)](https://img.shields.io/badge/1.0beta-brightgreen.svg)

</div>


<p align="center">
simpleIAST是一种交互式应用程序安全测试工具。
</p>

## 项目结构
```
/agent  # 负责agent的加载、卸载。

/engine # 污点追踪的引擎代码
```

## 快速开始

- **下载并自行打包**

```shell
# clone安装包
wget https://github.com/keven1z/simpleIAST/archive/refs/heads/master.zip

```

```shell
# 打包jar包,pom.xml存在导入tool.jar，若打包时提示不存在，手动配置tool.jar路径
mvn clean package
```
- **运行**
>将iast-agent.jar和iast-engine.jar 放在同一目录

1. 跟随应用启动运行
```shell

java -javaagent:iast-agent.jar -jar [app.jar] # 

```

2. 应用启动后attach方式运行
```shell

java -jar iast-engine.jar -l # 获取应用的pid，需要确认tool.jar是否存在于${java.home}/lib目录中
java -jar iast-engine.jar -p [PID] # attach方式运行

```

## 支持中间件

* Tomcat
* Springboot

## 支持JDK
* jdk 1.8
* jdk 11

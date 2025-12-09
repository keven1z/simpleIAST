# simpleIAST

[![Release](https://img.shields.io/github/v/release/keven1z/simpleIAST)](https://github.com/keven1z/simpleIAST/releases)  [![License](https://img.shields.io/github/license/keven1z/simpleIAST)](LICENSE)
#### [简体中文](README.md) / [English](README_EN.md)

A taint‑tracking based gray‑box vulnerability scanning tool for Java web applications, providing real‑time detection and reporting of common security vulnerabilities.

---

## Introduction

**simpleIAST** is a Java Agent‑based gray‑box vulnerability scanning tool that employs taint tracking to detect runtime security risks in Java web applications. It identifies vulnerabilities such as SQL Injection, Deserialization issues, XSS, XXE, and more. The tool is lightweight, easily integrable, and compatible with mainstream middleware and JDK versions—ideal for DevSecOps workflows.

---

## Quick Start

### Clone the Project

```bash
git clone https://github.com/keven1z/simpleIAST.git
cd simpleIAST
```

### Start with Docker

```bash
cd docker/
chmod +x ./deploy.sh
sudo ./deploy.sh
```

- **Frontend Access**: `http://[your_ip]:8443/`  
- **Default Login**: Username `admin`, Password `123456`  
- **Frontend Port**: 8443, **Backend Port**: 81  

### Agent Startup Methods

#### Inject agent during application start

```bash
java -javaagent:iast-agent.jar -jar [app.jar]
```

#### Attach agent after application launch

```bash
java -jar iast-engine.jar -m install -p [pid]   # Install agent  
java -jar iast-engine.jar -m uninstall -p [pid] # Uninstall agent
```

> Ensure that `iast-agent.jar` and `iast-engine.jar` are located in the same directory.

---

## Features & Compatibility

### Supported Middleware

- Tomcat  
- Spring Boot  
- Jetty  
- Weblogic  
- GlassFish  
- WildFly  
- TongWeb  
- Resin  
- Undertow  

### Supported JDK Versions

- JDK 1.8  
- JDK 11  

### Detected Vulnerability Types (with severity)

| No. | Vulnerability Type              | Severity |
|-----|----------------------------------|----------|
| 1   | SQL Injection                    | High     |
| 2   | Deserialization                  | Critical |
| 3   | SSRF (Server-Side Request Forgery) | Medium |
| 4   | URL Redirection                  | Medium   |
| 5   | XXE (XML External Entities)      | High     |
| 6   | Command Injection                | Critical |
| 7   | File Upload Vulnerability        | Medium   |
| 8   | XSS (Cross-Site Scripting)       | Medium   |
| 9   | Spring EL Expression Injection   | High     |
| 10  | Weak Database Credentials        | Medium   |
| 11  | XPATH Injection                  | High     |
| 12  | Hardcoded Credentials            | Medium   |
| 13  | Fastjson Deserialization         | Critical |

---

---
## benchmark
[benchmark](./benchmark/benchmark.md)

## Further Documentation

More guides and official documentation are available in the project Wiki:

- [Quick Start]((https://github.com/keven1z/simpleIAST/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B))
---

## License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](LICENSE) file for details.

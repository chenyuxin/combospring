# combospring (commonframework) 项目介绍

**combospint**（又名 **commonframework**）是一个基于 Java 和 Spring Boot 构建的模块化通用开发框架。该项目采用了高度模块化的设计，旨在为开发者提供一系列开箱即用的工具包和功能组件，涵盖了从基础工具、数据访问、Web 开发到特定领域（如加密、中文处理、FTP、XML 等）的广泛功能。

## 项目结构与模块功能

项目由多个功能独立的子模块组成，每个模块专注于解决特定的技术问题：

### 核心基础模块
*   **`commonframework-base`**: 提供框架中最基础的工具类和常量定义。
*   **`commonframework-util`**: 通用工具包，依赖于 `base` 模块，包含一些常见的 Java 逻辑实现（如 UUID 生成等）。
*   **`commonframework-dependencies`**: 核心依赖管理模块，定义了整个项目及所有子模块使用的第三方库版本（如 Spring Boot, Fastjson2, MySQL, Redis 等）。

### 数据访问与存储模块
*   **`commonframework-dao`**: 数据访问层抽象，集成了 JDBC、MySQL、PostgreSQL 和 Oracle 驱动，并集成了 Druid 连接池。
*   **`commonframework-dao-jpa`**: 为 Spring Data JPA 提供进一步的支持。
*   **`commonframework-redis`**: 提供了对 Redis 缓存的集成支持，包含了连接池（commons-pool2）的配置。

### Web 与 接口模块
*   **`commonframework-mvc`**: Web MVC 相关的增强工具，集成了 **Knife4j/Swagger** 用于自动生成 API 文档。
*   **`commonframework-webservice`**: 提供了对 Web Service 接口的支持。
*   **`commonframework-validator`**: 增强的参数校验模块，基于 Jakarta Validation API 和 Spring Boot Validation 实现。
*   **`commonframework-aop`**: 通用的 AOP（面向切面编程）框架，利用 AspectJ 实现。

### 特色功能模块
*   **`commonframework-chinese`**: 中文处理工具，集成了 `pinyin4j` 用于拼音转换等中文相关逻辑。
*   **`commonframework-cipher`**: 加密模块，基于 **BouncyCastle** 提供了强大的对称/非对称加密功能。
*   **`commonframework-ftp`**: FTP 客户端工具，基于 `commons-net` 实现，支持连接池管理。
*   **`commonframework-xml`**: XML 处理模块，集成了 `dom4j`、`xstream` 和 `woodstox`，用于复杂的 XML 解析与转换。
*   **`commonframework-xxx`**: 预留的扩展模块占位符。

### 测试模块
*   **`testcombospring`**: 专门用于集成测试和功能演示的示例项目。

## 技术栈分析
*   **核心语言**: Java (项目配置使用了较新的 Java 25)
*   **构建工具**: Maven
*   **核心框架**: Spring Boot (项目配置为 4.1.0 版本)
*   **JSON 处理**: Alibaba Fastjson2
*   **数据库/持久化**: MySQL, PostgreSQL, Oracle, Spring Data JDBC/JPA, Druid
*   **缓存**: Redis
*   **API 文档**: Swagger / Knife4j
*   **加密算法**: BouncyCastle
*   **日志**: SLF4J
*   **其他**: Lombok, JUnit 5, Apache Commons (lang3, text, collections4, net), XML (dom4j, xstream, etc.)

## 总结
**combospring** 是一个设计严谨、职责分离明确的 Java 框架。其优点在于：
1.  **高度模块化**: 开发者可以根据需求仅引入所需的模块，避免了臃肿依赖。
2.  **依赖管理规范**: 通过专门的 `dependencies` 模块统一版本，极大降低了版本冲突的风险。
3.  **功能覆盖面广**: 涵盖了企业级开发中常见的多种技术场景。
4.  **现代技术栈**: 使用了 Java 25 和 Spring Boot 的高版本，体现了对现代 Java 特性的支持。
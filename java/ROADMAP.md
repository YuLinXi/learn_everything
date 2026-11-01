# Java Web 后端学习路线

版本：2026-10-31 · 目标：从 TS / React 前端工程师成长为能接手公司 Java 后端小需求的全栈开发者。

## 如何使用

主教材为 [JavaGuide](https://github.com/Snailclimb/JavaGuide) 的公开站点文章。每节按“限定阅读 → 独立练习 → 验收 → 延迟复查”执行；练习是本路线围绕公司源码设计的任务，不是 JavaGuide 原文作业。源码位置与证据见 [PROJECT_MAP.md](PROJECT_MAP.md)，正式状态只记在 [PROGRESS.md](PROGRESS.md)。

各单元按 [作业设计与交付](practice/README.md#作业设计与交付) 分工：用户交付代码与必要理解，导师负责可观察结果的核验和学习记录。下文的运行、证据与验收要求不转换为通用日志填写表；独立操作及接口文档等属于目标能力的产出仍按题意完成。

已有 TS/React 能力用于类比和联调；Java、SQL、Spring 起点尚未测定。时间是规划估算，包含练习、测试和返工，不是完成承诺。先用验收题判定可跳过的内容，再调节节奏。

## 优先级与里程碑

- **P0：M00–M08，共 36 单元，78–110 小时。** 默认每周 10 小时约 8–11 周；每周 5 小时约 16–22 周。M03 结束开始独立写内存 API，M05 结束打通真实数据库，M08 完成接手验收。
- **P1：M09–M11，共 12 单元，20–28 小时。** 在实际需求涉及缓存、并发、性能、复杂报表或组件集成时进入。遇到这些需求，可将对应单元提前到交付前。
- **P2：暂缓系统研读。** JSP/JSTL、手写 Servlet 全套、JPA/Hibernate 第二套持久化栈、Netty/NIO 源码、JVM GC 算法细节、AQS/HashMap 源码背诵、分布式事务/共识、完整微服务治理、算法刷题、复杂 DDD、最新 Java 特性。遇到目标需求再补，Servlet 请求生命周期在 M03 了解即可。

先修顺序：`M00 → M01 → M02 → M03 → M04 → M05 → M06 → M07 → M08`。M07 的源码入口可提前浏览；SQL 熟练者可通过 M04 验收后跳过阅读。M09/M10 需先通过 M05；M11 需先通过 M07 和相关专项基础。

| 阶段 | 核心交付 | 通过条件 |
| --- | --- | --- |
| G1 · M00–M02 | Java 小业务方法与测试 | 无关键提示写出、运行、解释边界，能修复一次失败 |
| G2 · M03–M05 | Spring + MyBatis + 数据库 API | HTTP、持久化、事务回滚、SQL 与集成测试有当次证据 |
| G3 · M06–M08 | 可评审、可部署的小需求 | 权限/租户/幂等/联调/回归/回滚说明齐全，并完成公司测试环境验证与后端同事评审 |

本地模拟项目通过不自动代表 G3 通过。公司私服、运行环境、缺失源码或评审尚不可用时，记录具体阻塞，继续不依赖它的学习任务。

## 一个贯穿项目

在 `practice/` 逐步实现**字段分组管理**：entity + attribute_group、创建/详情/列表/修改、分页、名称校验、唯一性、租户归属、版本冲突、统一错误与 React 联调。M00–M02 写纯 Java，M03 接内存，M04–M05 接数据库，M06 补边界，M08 交付。其业务模型经过教学简化，公司 UDC 中空分组、虚拟分组、隐藏属性等规则另按源码学习。

数据库以 MySQL 练基础；公司实际数据库方言和部署版本需在 M00/M07 确认。Java 核心练习先保持 Java 8 兼容语法；Spring Boot 具体版本待取得公司父 POM/依赖树后固定。文章中的新版 API、`jakarta.*`、测试注解不能直接拷入公司 `javax.*` 工程。独立练习若采用不同版本，须在练习 README 记录差异。

## 模块总览

| 模块 | 主题 | 优先级 | 估算 | 产出 |
| --- | --- | --- | --- | --- |
| [M00](#m00) | 建立工程坐标与反馈循环 | P0 | 4–6 小时 | 能定位源码、运行 Java 和测试；建立起点评估。 |
| [M01](#m01) | 补齐 Java 与 TypeScript 的语义差异 | P0 | 8–12 小时 | 能独立写一个有边界检查和测试的小业务服务。 |
| [M02](#m02) | 写得出业务数据处理代码 | P0 | 8–10 小时 | 能处理 DTO 列表、泛型结果、金额和日期。 |
| [M03](#m03) | 从 Spring 到可调用的 Web API | P0 | 10–14 小时 | 提供内存版字段分组 API，具备参数与错误契约。 |
| [M04](#m04) | SQL 与关系型数据库的工作能力 | P0 | 10–14 小时 | 设计小表并写出正确、可解释的查询。 |
| [M05](#m05) | MyBatis 与事务性业务接口 | P0 | 10–14 小时 | 将 API 从内存迁到数据库，并证明失败时数据正确。 |
| [M06](#m06) | 补齐真实需求的边界 | P0 | 8–12 小时 | 能交付有权限、幂等、诊断和联调保障的功能。 |
| [M07](#m07) | 迁移到公司工程的开发方式 | P0 | 8–12 小时 | 读懂两条真实链路，在公司边界内完成最小改动演练。 |
| [M08](#m08) | 端到端交付与接手验收 | P0 | 12–16 小时 | 独立交付可评审的小需求；公司实测是接手验收的必要项。 |
| [M09](#m09) | Redis 与缓存专项 | P1 | 6–8 小时 | 遇到缓存需求时，能处理一致性、失效与降级。 |
| [M10](#m10) | 并发、线程池与 JVM 排障专项 | P1 | 6–8 小时 | 能维护并发请求下的代码，识别线程与内存问题。 |
| [M11](#m11) | 按实际业务进入框架和报表深水区 | P1 | 8–12 小时 | 能沿需求扩展知识，不因陌生组件把路线无限拉长。 |

## 详细大纲

每次学习优先取一个连续的 60–90 分钟学习块：20–30 分钟限定阅读、30–50 分钟独立实践、10 分钟验证。单元较大可拆为多块；每块按 [Markdown 课件规范](lessons/README.md) 保存原始课件，并在当前会话直接呈现内容。无需先生成后续所有课件再开始学习。

<a id="m00"></a>

## M00 · 建立工程坐标与反馈循环

优先级：P0；估算：4–6 小时。产出：能定位源码、运行 Java 和测试；建立起点评估。

<a id="m00-1"></a>

### M00.1 沿 import 找到接口与实现

- **阅读范围**：[Java 基础（中）](https://javaguide.cn/java/basis/java-basic-questions-02.html#⭐️-接口和抽象类有什么共同点和区别)：只读接口、实现、多态相关段落；暂缓抽象类设计题。
- **独立练习**：阅读导师根据 UDC 源码生成的 Controller → api 接口 → Impl 调用定位图，用简短回答解释声明类型、执行实现及参数和返回类型。
- **验收标准**：给出三个正确包名和方法名，指出同名 appservice 类不在此调用路径；口头解释声明类型与执行实现的区别。

<a id="m00-2"></a>

### M00.2 JDK、编译与调试

- **阅读范围**：[Java 基础（上）](https://javaguide.cn/java/basis/java-basic-questions-01.html#⭐️-jvm-vs-jdk-vs-jre)：只读 JDK/JRE/JVM、字节码、基本语法与方法；暂缓 AOT、移位细节。
- **独立练习**：在 practice 下手写一个参数校验方法；观察 java --version、javac --version、mvn --version 的差异；设断点查看调用栈。环境信息由导师核验。
- **验收标准**：从命令行运行成功，能定位一次编译错误与一次运行异常；区分项目语言级别、运行 JDK、Maven 使用的 JDK。

<a id="m00-3"></a>

### M00.3 Maven 的项目、依赖与生命周期

- **阅读范围**：[Maven 核心概念](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-坐标)：读坐标、依赖范围、仓库、生命周期、多模块；暂缓插件开发。
- **独立练习**：建立最小 Maven 练习工程；结合导师生成的 UDC POM 关系图与父 POM 定位结论，通过简短问答解释聚合、继承与实际依赖。
- **验收标准**：执行 compile/test/package 并解释差异；能区分编译错误、依赖下载失败和测试失败；未取得私服依赖时如实记录阻塞。

<a id="m00-4"></a>

### M00.4 第一次可重复的单元测试

- **阅读范围**：[单元测试](https://javaguide.cn/system-design/basis/unit-test.html#何谓单元测试)：读测试目的、单测与集成测试区别、框架和 Mock 选择；暂缓 TDD 流派。
- **独立练习**：给自己的方法写正常值、边界值、非法值测试；结合实际失败或导师在副本中构造的一次失败，解释被破坏的业务规则与正确修复方向。
- **验收标准**：导师检查真实断言、关键边界与适用的 Jupiter 导入，并运行测试确认结果；用户能解释断言保护了什么、失败为何发生。JUnit 4 与 Jupiter 的导入差异按具体疑问补讲。

<a id="m01"></a>

## M01 · 补齐 Java 与 TypeScript 的语义差异

优先级：P0；估算：8–12 小时。产出：能独立写一个有边界检查和测试的小业务服务。

<a id="m01-1"></a>

### M01.1 类型、null 与数值边界

- **阅读范围**：[Java 基础（上）](https://javaguide.cn/java/basis/java-basic-questions-01.html#⭐️-基本数据类型)：读基本类型、包装类型、拆装箱和变量；补 if/for/switch 基本用法，暂缓包装缓存面试题。
- **独立练习**：把 TS 的分组输入转换为 Java DTO；覆盖 Long/long、Boolean/boolean、null、整数除法、越界输入。
- **验收标准**：独立解释并修复一次空指针与一次数值截断；不把 JS number 的经验直接套给 long、金额。

<a id="m01-2"></a>

### M01.2 类、接口、构造器与访问范围

- **阅读范围**：[Java 基础（中）](https://javaguide.cn/java/basis/java-basic-questions-02.html#面向对象基础)：读对象/引用、构造器、多态、接口；配合基础（上）的重载与重写。
- **独立练习**：写接口及一个实现，通过构造器交给调用者；试验 public/private、static、final、重载和 @Override。
- **验收标准**：能解释接口实现为何不能缩小访问范围；替换实现时调用者业务逻辑无需改动；测试覆盖正常与拒绝输入。

<a id="m01-3"></a>

### M01.3 值传递、相等性与字符串

- **阅读范围**：[Java 值传递](https://javaguide.cn/java/basis/why-there-only-value-passing-in-java.html#为什么-java-只有值传递)：只做三个参数传递案例。 [Java 基础（中）](https://javaguide.cn/java/basis/java-basic-questions-02.html#和-equals-的区别)：读 ==/equals/hashCode 和 String，不读常量池对象计数题。
- **独立练习**：先预测修改对象、重绑引用、替换字符串的结果，再运行；写一个用 Set 去重的值对象。
- **验收标准**：闭卷解释三段结果；equals/hashCode 一致，能用测试暴露错误的 == 比较。

<a id="m01-4"></a>

### M01.4 异常、文件与资源关闭

- **阅读范围**：[Java 基础（下）](https://javaguide.cn/java/basis/java-basic-questions-03.html#异常)：读 checked/unchecked、抛出、捕获和 try-with-resources。 [IO 基础](https://javaguide.cn/java/io/io-basis.html)：只读字节流/字符流与文件读取；暂缓 NIO/Netty。
- **独立练习**：读取练习 JSON/文本配置，模拟路径不存在与内容错误；将错误向上层传播并保留 cause。
- **验收标准**：成功和失败路径都关闭资源；不吞异常、不返回伪成功；能从异常链定位自己的第一处相关代码。

<a id="m02"></a>

## M02 · 写得出业务数据处理代码

优先级：P0；估算：8–10 小时。产出：能处理 DTO 列表、泛型结果、金额和日期。

<a id="m02-1"></a>

### M02.1 List、Set、Map 与业务集合

- **阅读范围**：[集合（上）](https://javaguide.cn/java/collection/java-collection-questions-01.html#集合概述)：读四类集合的用途、ArrayList、Set 与 Comparator。 [集合使用注意事项](https://javaguide.cn/java/collection/java-collection-precautions-for-use.html#集合转-map)：读转 Map、遍历和去重；暂缓扩容和红黑树推导。
- **独立练习**：为分组列表实现按 ID 索引、稳定排序与去重；定义重复 ID、空列表和 null 元素的处理规则。
- **验收标准**：3 类边界用例通过；能说明有序性与唯一性要求；重复键处理是明确业务规则。

<a id="m02-2"></a>

### M02.2 泛型与请求响应包装

- **阅读范围**：[泛型与通配符](https://javaguide.cn/java/basis/generics-and-wildcards.html#)：读定义、类/接口/方法用法、擦除概念；通配符只读上下界用途，暂缓桥方法。
- **独立练习**：写最小 Result<T>，再阅读公司 SingleRequest<Long> 与 ListResponse<AttributeGroupDto> 的使用点。
- **验收标准**：不使用 raw type 或盲目强转；能区分 List<DTO> 与 ListResponse<DTO>；不推测缺失源码中的 JSON 字段。

<a id="m02-3"></a>

### M02.3 Lambda、Stream 与 Optional

- **阅读范围**：[Java 8 常用特性](https://javaguide.cn/java/new-features/java8-common-new-features.html#lambda-表达式)：读函数式接口、Lambda、Stream、Optional；暂缓并行流。
- **独立练习**：对相同分组列表分别用循环和 stream 实现筛选、排序、映射；模仿 UDC 的 containHidden 分支。
- **验收标准**：两种写法结果一致，覆盖空值与重复键；不在 Optional.get 前假定一定有值；能读懂方法引用。

<a id="m02-4"></a>

### M02.4 金额、时间与 JSON 契约

- **阅读范围**：[BigDecimal](https://javaguide.cn/java/basis/bigdecimal.html#bigdecimal-常见方法)：读构造、计算、比较、舍入。 [Java 8 常用特性](https://javaguide.cn/java/new-features/java8-common-new-features.html#date-time-api)：只读日期时间 API 的使用与日期/时刻差异。
- **独立练习**：实现一个金额汇总与日期范围查询参数转换；确定 long ID 的 JSON 表示和时区约定。
- **验收标准**：金额不由不精确 double 构造；明确舍入与比较规则；日期跨日边界、超出 JS 安全整数范围的 ID 有契约用例。

<a id="m03"></a>

## M03 · 从 Spring 到可调用的 Web API

优先级：P0；估算：10–14 小时。产出：提供内存版字段分组 API，具备参数与错误契约。

<a id="m03-1"></a>

### M03.1 IoC、Bean 与启动入口

- **阅读范围**：[IoC 与 AOP](https://javaguide.cn/system-design/framework/spring/ioc-and-aop.html#什么是-ioc)：读 IoC/DI 与 Bean 装配概念。 [Spring 常用注解](https://javaguide.cn/system-design/framework/spring/spring-common-annotations.html#spring-boot-基础注解)：只读启动、Bean 注册与依赖注入。
- **独立练习**：创建独立练习服务，Controller 调用构造器注入的 Service；对照 UdcApplication。
- **验收标准**：能启动并调用接口；说明 Spring/Spring MVC/Boot 各自用途；注入缺失时能定位原因。

<a id="m03-2"></a>

### M03.2 配置、环境与自动装配入门

- **阅读范围**：[Spring 常用注解](https://javaguide.cn/system-design/framework/spring/spring-common-annotations.html#配置)：读配置类、配置属性和配置文件。 [Spring Boot 自动装配](https://javaguide.cn/system-design/framework/spring/spring-boot-auto-assembly-principles.html#什么是-springboot-自动装配)：只读自动装配解决的问题，暂缓选择器源码。
- **独立练习**：为练习服务配置端口、分页上限和 dev/test 环境；在 M00 取得的版本基础上固定依赖。
- **验收标准**：通过测试或启动日志确认配置生效；数据库凭据由本地环境提供；记录真实 JDK/Boot/MyBatis 版本。

<a id="m03-3"></a>

### M03.3 MVC、DTO 与校验

- **阅读范围**：[Spring 常用注解](https://javaguide.cn/system-design/framework/spring/spring-common-annotations.html#)：读请求映射、参数绑定、数据校验与 JSON；跳过 JPA。 [数据校验](https://javaguide.cn/system-design/security/data-validation.html#后端校验)：读后端校验和权限校验的区别。
- **独立练习**：实现创建、详情、列表、修改四个内存接口；区分路径参数、查询参数、请求体与持久化对象。
- **验收标准**：请求正常、缺失必填字段、非法枚举各有断言；使用独立输入 DTO，客户端不能批量覆盖服务端管理字段。

<a id="m03-4"></a>

### M03.4 统一错误与 HTTP 边界

- **阅读范围**：[Spring 常用注解](https://javaguide.cn/system-design/framework/spring/spring-common-annotations.html#全局异常处理)：只读 ControllerAdvice/ExceptionHandler。 [HTTP 状态码](https://javaguide.cn/cs-basics/network/http-status-codes.html)：只读常见 2xx/4xx/5xx 含义。
- **独立练习**：把参数错误、记录不存在、业务冲突与意外异常映射成一致响应；运行 HTTP 层测试。
- **验收标准**：正常路径及 400/404/409/500 契约可验证；响应不暴露堆栈；对照公司包装器时先确认其实际状态码约定。

<a id="m04"></a>

## M04 · SQL 与关系型数据库的工作能力

优先级：P0；估算：10–14 小时。产出：设计小表并写出正确、可解释的查询。

<a id="m04-1"></a>

### M04.1 表结构、约束与 CRUD

- **阅读范围**：[SQL 语法速查](https://javaguide.cn/database/mysql/a-thousand-lines-of-mysql-study-notes.html#表的操作)：只读表操作、数据操作、列类型、列约束与建表规范；不通读全文。
- **独立练习**：建练习 entity、attribute_group 两张表，定义主键、租户、业务唯一键、时间和版本字段，准备种子数据。
- **验收标准**：建表与初始化脚本可重复用于全新练习库；非法数据由适当约束拒绝；能说明逻辑删除与唯一约束的冲突处理。

<a id="m04-2"></a>

### M04.2 JOIN、聚合与稳定分页

- **阅读范围**：[SQL 语法速查](https://javaguide.cn/database/mysql/a-thousand-lines-of-mysql-study-notes.html#select)：读 SELECT、GROUP BY/HAVING、连接查询、子查询；暂缓存储过程/触发器。
- **独立练习**：实现分组数统计、多条件列表、稳定排序分页；用空分组、一对多和重复排序值作为输入。
- **验收标准**：结果行数与人工预期一致；不会因 JOIN 多行而重复计数；分页排序有唯一键作为补充。

<a id="m04-3"></a>

### M04.3 索引与 EXPLAIN

- **阅读范围**：[MySQL 索引](https://javaguide.cn/database/mysql/mysql-index.html#覆盖索引和联合索引)：读索引用途、联合/覆盖索引与使用建议；暂缓 B+ 树证明。 [MySQL 执行计划](https://javaguide.cn/database/mysql/mysql-query-execution-plan.html#如何分析-explain-结果)：只分析 type、key、rows、Extra。
- **独立练习**：给租户内按 entity_id 与名称过滤的查询设计索引，在足量练习数据上比较执行计划。
- **验收标准**：提交 SQL、数据规模、前后 EXPLAIN 与理由；能识别全表扫描和索引失效，理解优化器可能不选索引。

<a id="m04-4"></a>

### M04.4 事务、隔离与并发写入

- **阅读范围**：[SQL 语法速查](https://javaguide.cn/database/mysql/a-thousand-lines-of-mysql-study-notes.html#事务-transaction)：只读事务操作。 [事务隔离级别](https://javaguide.cn/database/mysql/transaction-isolation-level.html#事务隔离级别总结)：读四级隔离和两个会话演示；暂缓 MVCC 源码。
- **独立练习**：用两个数据库会话复现未提交读取边界和竞争更新；演示唯一键冲突与回滚。
- **验收标准**：能区分原子性、隔离性、锁等待和业务幂等；失败后查询数据库确认无部分写入。

<a id="m05"></a>

## M05 · MyBatis 与事务性业务接口

优先级：P0；估算：10–14 小时。产出：将 API 从内存迁到数据库，并证明失败时数据正确。

<a id="m05-1"></a>

### M05.1 Mapper、参数与结果映射

- **阅读范围**：[MyBatis](https://javaguide.cn/system-design/framework/mybatis/mybatis-interview.html#mybatis-基础)：读 Mapper/SqlSession 基本职责、参数处理、resultType/resultMap；暂缓执行器源码。
- **独立练习**：为字段分组实现增查改 Mapper，处理列名映射、null、主键回填；比较 #{} 与 ${}。
- **验收标准**：真实数据库往返测试通过；恶意输入作为参数值处理；动态排序字段只接受白名单。

<a id="m05-2"></a>

### M05.2 动态 SQL、分页与批量操作

- **阅读范围**：[MyBatis](https://javaguide.cn/system-design/framework/mybatis/mybatis-interview.html#参数处理与动态-sql)：读 if/where/foreach、多参数与分页；补 N+1 查询段落。
- **独立练习**：实现可选过滤条件与分页，并处理空 ID 列表；对照公司 BaseMybatisDao/Wrapper 的调用方式。
- **验收标准**：无条件/组合条件/空集合/分页边界均正确；列表接口没有逐行追加查询；不假定公司必须手写 Mapper XML。

<a id="m05-3"></a>

### M05.3 服务层事务与失效场景

- **阅读范围**：[Spring 事务](https://javaguide.cn/system-design/framework/spring/spring-transaction.html#事务属性详解)：读 REQUIRED、隔离级别、rollbackFor 与 @Transactional 使用注意事项；暂缓复杂传播组合。
- **独立练习**：一个业务动作同时新增分组与操作记录；第二步故意失败，比较代理调用、自调用和捕获异常的结果。
- **验收标准**：在真实数据库集成测试中证明回滚；能解释 checked 异常规则、自调用和吞异常对事务的影响。

<a id="m05-4"></a>

### M05.4 数据库集成测试与回归

- **阅读范围**：[单元测试](https://javaguide.cn/system-design/basis/unit-test.html#为重构保驾护航)：聚焦单测无法覆盖配置/事务/数据库的边界。 [MyBatis](https://javaguide.cn/system-design/framework/mybatis/mybatis-interview.html#事务、性能与工程问题)：只读未提交、映射不完整和 SQL 性能排查。
- **独立练习**：建立可清理测试数据的集成测试；验证唯一约束、事务、分页和错误映射；记录数据库版本。
- **验收标准**：单测与数据库测试分别运行并记录数量；测试可重复且不依赖先后顺序；Mock 通过不代替数据库验收。

<a id="m06"></a>

## M06 · 补齐真实需求的边界

优先级：P0；估算：8–12 小时。产出：能交付有权限、幂等、诊断和联调保障的功能。

<a id="m06-1"></a>

### M06.1 认证、授权与数据归属

- **阅读范围**：[认证与授权基础](https://javaguide.cn/system-design/security/basis-of-authority-certification.html#认证-authentication-和授权-authorization-的区别是什么)：读认证/授权、RBAC、Session/Cookie、JWT 与 CSRF；不实现 SSO。 [权限系统设计](https://javaguide.cn/system-design/security/design-of-authority-system.html#系统-菜单-数据权限管理)：只读功能权限与数据权限的区别。
- **独立练习**：为两用户两租户准备测试；练习环境使用明确的测试身份入口，接公司时追踪真实身份来源与权限注解。
- **验收标准**：未登录、缺少角色、跨租户/越权访问各被拒绝；租户不取自可伪造请求字段；说明 Cookie/Token 方案下的 CSRF/CORS 边界。

<a id="m06-2"></a>

### M06.2 重复提交与并发修改

- **阅读范围**：[接口幂等性](https://javaguide.cn/high-availability/idempotency.html#如何保证接口幂等性)：读唯一索引、乐观锁、去重表与验证方法；暂缓分布式锁。
- **独立练习**：为创建设置业务唯一键；为修改设置版本检查；并发发送相同请求与过期版本请求。
- **验收标准**：并发重复创建不会产生两条业务记录；旧版本修改返回明确冲突；能说明幂等键的范围和有效期。

<a id="m06-3"></a>

### M06.3 日志、断点与故障定位

- **阅读范围**：[Linux 入门](https://javaguide.cn/cs-basics/operating-system/linux-intro.html#linux-常用命令)：只查文件/日志、进程、端口与环境变量相关命令。 [JDK 排障工具](https://javaguide.cn/java/jvm/jdk-monitoring-and-troubleshooting-tools.html#jps-查看所有-java-进程)：只读进程定位；jstack 按阻塞场景查阅。
- **独立练习**：分别制造参数错误、依赖缺失、SQL 错误和慢调用；记录 request/trace 标识、耗时与异常原因。
- **验收标准**：通过日志或断点定位到负责层，提交复现→原因→最小修复→回归证据；日志隐藏凭据和敏感字段。

<a id="m06-4"></a>

### M06.4 React 联调与接口兼容

- **阅读范围**：[REST API](https://javaguide.cn/system-design/basis/RESTfulAPI.html#restful-api-规范)：读路径、过滤和状态码；跳过 HATEOAS。 [Spring 常用注解](https://javaguide.cn/system-design/framework/spring/spring-common-annotations.html#json-数据处理)：只读序列化边界。
- **独立练习**：用已有 React 能力接入列表、表单与错误提示；写接口契约：ID、时间、空值、分页、错误、鉴权、兼容性。
- **验收标准**：浏览器与后端/数据库形成一条可复现链路；覆盖失败态；解释空列表、404、权限不足和冲突的 UI 行为。

<a id="m07"></a>

## M07 · 迁移到公司工程的开发方式

优先级：P0；估算：8–12 小时。产出：读懂两条真实链路，在公司边界内完成最小改动演练。

<a id="m07-1"></a>

### M07.1 UDC 字段分组读链路

- **阅读范围**：[Java 8 常用特性](https://javaguide.cn/java/new-features/java8-common-new-features.html#stream)：复习 filter/sorted/collect；配合 PROJECT_MAP 的 UDC 路径。
- **独立练习**：完整跟踪 selectListByEntityId：请求包装、接口/实现、属性查询、分组 DAO、隐藏字段过滤、空/虚拟分组和 DTO 返回。
- **验收标准**：根据导师生成且附源码位置的数据流图解释关键分支，说明它为何不等于一次简单表查询；没有运行时证据时只标记静态追踪。

<a id="m07-2"></a>

### M07.2 UDC 小需求演练

- **阅读范围**：[数据校验](https://javaguide.cn/system-design/security/data-validation.html#后端校验)：复习字段校验与权限；测试方法按单测章节复用。
- **独立练习**：先在 practice 实现“分组名称校验”或“新增可选过滤条件”；再写公司目标层、兼容影响、权限/事务和测试位置的改动清单。
- **验收标准**：有正常/边界/非法/兼容用例；能解释改哪一层及理由；公司实现仅在后续明确练习任务中进入独立分支。

<a id="m07-3"></a>

### M07.3 公司 starter、传输包装与测试约定

- **阅读范围**：[Spring Boot 自动装配](https://javaguide.cn/system-design/framework/spring/spring-boot-auto-assembly-principles.html#如何实现一个-starter)：只读 starter 注册和条件装配，结合 boot-starter-enums。 [Maven 核心概念](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-多模块管理)：复习 parent、依赖管理与 reactor。
- **独立练习**：阅读 spring.factories、EnumsAutoConfiguration、Properties、BaseMybatisDao 调用及 JUnit 样例；列出标准 Spring 与公司扩展。
- **验收标准**：能区分 @AppService/@HasRole/SingleResponse 与标准框架；缺失实现标为待核验；按实际模块选择测试框架。

<a id="m07-4"></a>

### M07.4 cdp-report 查询入口与职责分层

- **阅读范围**：[IoC 与 AOP](https://javaguide.cn/system-design/framework/spring/ioc-and-aop.html#ioc-解决了什么问题)：复习依赖边界；配合 PROJECT_MAP 的报表链路。
- **独立练习**：追踪 query → preparation → executor → gateway → queryEngine；先到引擎入口停止，标记参数、权限准备、短路与异常。
- **验收标准**：定位正确文件与方法，解释入口/编排/网关职责；给出一个空结果和一个权限失败的预期，不宣称已验证数据库执行。

<a id="m08"></a>

## M08 · 端到端交付与接手验收

优先级：P0；估算：12–16 小时。产出：独立交付可评审的小需求；公司实测是接手验收的必要项。

<a id="m08-1"></a>

### M08.1 需求、数据与验收设计

- **阅读范围**：[REST API](https://javaguide.cn/system-design/basis/RESTfulAPI.html#restful-api-规范)：按需复查契约。 [单元测试](https://javaguide.cn/system-design/basis/unit-test.html#何谓单元测试)：复查测试层次。
- **独立练习**：为“字段分组管理”写一页需求：角色、数据归属、接口、表、唯一性、失败分支、上线影响和验收矩阵。
- **验收标准**：在编码前列出关键行为和用例；范围可在一个小需求中完成，无新增不必要组件。

<a id="m08-2"></a>

### M08.2 独立实现与全链路验证

- **阅读范围**：[Spring 常用注解](https://javaguide.cn/system-design/framework/spring/spring-common-annotations.html#)：按需查绑定/校验。 [Spring 事务](https://javaguide.cn/system-design/framework/spring/spring-transaction.html#transactional-注解使用详解)：按需查回滚；不再全篇重读。
- **独立练习**：独立交付列表/详情/创建/修改、数据库持久化、权限、幂等/版本冲突、统一错误与 React 调用。
- **验收标准**：正常流、非法参数、跨租户、重复提交、回滚、兼容性均有证据；脱离参考答案解释每层责任。

<a id="m08-3"></a>

### M08.3 构建、数据库变更与回滚

- **阅读范围**：[Maven 实践](https://javaguide.cn/tools/maven/maven-best-practices.html#通过持续集成实现构建自动化)：读构建与环境配置。 [Docker 入门](https://javaguide.cn/tools/docker/docker-intro.html#docker-基本概念)：只读镜像/容器、运行/日志/端口、数据卷；已有环境可复用。
- **独立练习**：在本地练习环境演练建库→迁移→打包→启动→健康检查→回滚；写接口文档与运行说明。
- **验收标准**：新环境能按 README 复现；版本和配置明确；迁移向后兼容并说明数据回滚限制；公司部署流程仍由实际规范确认。

<a id="m08-4"></a>

### M08.4 公司小需求评审与迁移题

- **阅读范围**：[单元测试](https://javaguide.cn/system-design/basis/unit-test.html#为重构保驾护航)：复用回归思路；主要资料是实际需求和公司规范。
- **独立练习**：取得一个适合新接手者的真实小需求，在独立分支中实现，完成适用测试、测试环境联调及后端同事评审；再接受一次条件变化。
- **验收标准**：提交 diff/测试/联调/评审/上线与回滚说明；无关键提示完成变化题。若私服、环境或评审不可用，只通过本地演练，不标记“可独立接手”。

<a id="m09"></a>

## M09 · Redis 与缓存专项

优先级：P1；估算：6–8 小时。产出：遇到缓存需求时，能处理一致性、失效与降级。

<a id="m09-1"></a>

### M09.1 Redis 用途与键设计

- **阅读范围**：[Redis 基础](https://javaguide.cn/database/redis/redis-questions-01.html#⭐️redis-数据类型)：只读 String/Hash/Set/ZSet 场景与过期；暂缓底层编码。
- **独立练习**：对照 EnumCacheUtils 的调用，为租户内字典缓存设计 key、值、TTL 和序列化。
- **验收标准**：能解释为何需要缓存、过期和跨租户键隔离；实际命令或测试验证读写与过期。

<a id="m09-2"></a>

### M09.2 Cache Aside 与更新失效

- **阅读范围**：[缓存读写策略](https://javaguide.cn/database/redis/3-commonly-used-cache-read-and-write-strategies.html)：只读 Cache Aside 的读写路径和一致性边界。
- **独立练习**：为练习查询添加缓存，更新后失效；用时序图分析读写并发，不承诺严格一致。
- **验收标准**：数据库是事实源；更新与读取测试正确；列出允许的陈旧窗口与恢复办法。

<a id="m09-3"></a>

### M09.3 穿透、击穿、雪崩与故障

- **阅读范围**：[Redis 工程问题](https://javaguide.cn/database/redis/redis-questions-02.html#⭐️redis-生产问题-重要)：只读缓存穿透/击穿/雪崩及缓存故障；暂缓集群选举。
- **独立练习**：模拟空值热查询、批量到期和 Redis 不可用；选择一项合适的保护措施。
- **验收标准**：请求不会无界堆积；故障时行为和数据库负载预期明确；说明措施的副作用。

<a id="m09-4"></a>

### M09.4 缓存专项交付

- **阅读范围**：[Redis 工程问题](https://javaguide.cn/database/redis/redis-questions-02.html#redis-使用规范)：只读 key/TTL/大 key 等日常规范。
- **独立练习**：给一个真实热点查询提交有/无缓存的测量、失效测试和开关回退方案。
- **验收标准**：证明收益且覆盖错误路径；无热点或无收益则保留不加缓存的结论。

<a id="m10"></a>

## M10 · 并发、线程池与 JVM 排障专项

优先级：P1；估算：6–8 小时。产出：能维护并发请求下的代码，识别线程与内存问题。

<a id="m10-1"></a>

### M10.1 多线程与共享状态

- **阅读范围**：[Java 并发基础](https://javaguide.cn/java/concurrent/java-concurrent-questions-01.html#多线程)：读并发/并行、线程安全与死锁概念。 [Spring 核心概念](https://javaguide.cn/system-design/framework/spring/spring-knowledge-and-questions-summary.html#⭐️bean-是线程安全的吗)：只读单例 Bean 的共享状态问题。
- **独立练习**：复现一个单例服务共享可变字段的串数据问题；改为请求内局部状态或适当同步。
- **验收标准**：并发用例暴露并修复问题；说明 Java 请求线程与 JS 事件循环的差异，区分锁与数据库事务。

<a id="m10-2"></a>

### M10.2 有界线程池与任务失败

- **阅读范围**：[Java 线程池](https://javaguide.cn/java/concurrent/java-thread-pool-summary.html#threadpoolexecutor-类介绍-重要)：读核心参数、队列、拒绝、关闭与异常；暂缓源码逐行阅读。
- **独立练习**：实现受控批量任务，规定队列/并发上限、超时和失败结果；不用无限新增线程。
- **验收标准**：饱和、超时、任务异常和正常关闭有测试；能解释阻塞 IO 与 CPU 任务选择的依据。

<a id="m10-3"></a>

### M10.3 JVM 内存与线程诊断

- **阅读范围**：[JVM 入门](https://javaguide.cn/java/jvm/jvm-intro.html#三、运行时数据区)：只读栈、堆、类加载、GC 目的。 [JDK 排障工具](https://javaguide.cn/java/jvm/jdk-monitoring-and-troubleshooting-tools.html#jstack-生成虚拟机当前时刻的线程快照)：按需读线程快照与内存工具；按实际 JDK 确认可用命令。
- **独立练习**：在练习进程中分析一次线程阻塞和持续分配场景，保存观察与假设。
- **验收标准**：能区分 CPU 忙、锁等待、内存增长；用证据排除一个错误假设，不凭参数模板调 JVM。

<a id="m10-4"></a>

### M10.4 性能专项验收

- **阅读范围**：[MySQL 执行计划](https://javaguide.cn/database/mysql/mysql-query-execution-plan.html#如何分析-explain-结果)：复查数据库耗时。 [Java 线程池](https://javaguide.cn/java/concurrent/java-thread-pool-summary.html#线程池最佳实践)：复查容量与拒绝策略。
- **独立练习**：对一个慢接口拆分应用/SQL/外部调用耗时，选最主要瓶颈做最小改进。
- **验收标准**：同一数据量与负载下记录前后结果和正确性回归；说明测量条件与适用上限。

<a id="m11"></a>

## M11 · 按实际业务进入框架和报表深水区

优先级：P1；估算：8–12 小时。产出：能沿需求扩展知识，不因陌生组件把路线无限拉长。

<a id="m11-1"></a>

### M11.1 注解、代理与自动装配深入

- **阅读范围**：[Java 基础（下）](https://javaguide.cn/java/basis/java-basic-questions-03.html#⭐️-反射)：只读反射、代理、注解的用途。 [Spring Boot 自动装配](https://javaguide.cn/system-design/framework/spring/spring-boot-auto-assembly-principles.html#springboot-是如何实现自动装配的)：沿一次具体 starter 注册流程阅读。
- **独立练习**：为已有练习功能提取一个小配置模块，验证条件装配开/关；对照公司自定义注解的实际实现（取得后）。
- **验收标准**：解释 Bean 如何出现、代理在哪生效；明确版本差异；没有重复包装现有能力。

<a id="m11-2"></a>

### M11.2 报表查询完整链路

- **阅读范围**：[MyBatis](https://javaguide.cn/system-design/framework/mybatis/mybatis-interview.html#mybatis-sql-性能问题如何排查)：复习 SQL 与结果映射边界。 [SQL 语法速查](https://javaguide.cn/database/mysql/a-thousand-lines-of-mysql-study-notes.html#连接查询-join)：只复查当前查询所需语法。
- **独立练习**：沿 cdp-report 的明细或统计分支选一条，继续追到数据源/SQL 执行/结果适配与分页，准备测试数据。
- **验收标准**：记录真实 SQL 或执行证据、权限过滤、空结果和分页；理解该引擎不等同于普通 MyBatis CRUD。

<a id="m11-3"></a>

### M11.3 外部服务与中间件需求分流

- **阅读范围**：[Maven 核心概念](https://javaguide.cn/tools/maven/maven-core-concepts.html#传递依赖性)：按需定位调用所需组件。 [接口幂等性](https://javaguide.cn/high-availability/idempotency.html#如何验证幂等方案)：复用重试与重复请求检验。
- **独立练习**：从实际 ticket 确认是否涉及 Dubbo/HTTP、消息、Elasticsearch 或导入导出；只选其中一种追加官方版本文档与练习。
- **验收标准**：先证明实际用到，再确定超时/失败/重试/幂等/监控边界；未使用的组件保持未开始。

<a id="m11-4"></a>

### M11.4 第二个需求与延迟复查

- **阅读范围**：[单元测试](https://javaguide.cn/system-design/basis/unit-test.html#为重构保驾护航)：按需复查。 [权限系统设计](https://javaguide.cn/system-design/security/design-of-authority-system.html#系统-菜单-数据权限管理)：复查跨资源数据边界。
- **独立练习**：独立完成与首次不同的需求（如报表条件、字典缓存或批量导入），在一周后做不看笔记的变化题。
- **验收标准**：新需求有评审与回归证据；延迟复查通过；把暴露的弱点回写原单元而不重新整套刷课。

## 接手工作的最终判定

“读完路线”和“能接手需求”分别统计。G3 要求以下各项都有可复核证据：需求理解与影响范围、接口/数据设计、Java 实现、事务与权限、正常及异常测试、前后端联调、问题定位、公司测试环境验证、评审与回滚说明。一次练习出现关键提示时保留提示记录，换一个变体再独立验收。

掌握后的第 1、3、7、14 天安排短复查，这是初始排程，不是已经运行的提醒服务。单次只问一道闭卷题；错题回到原单元，保留历史证据。会用已有能力解决不同需求并在延迟复查中保持，才说明知识可迁移。

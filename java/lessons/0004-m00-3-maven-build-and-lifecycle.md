# M00.3 · Maven 项目、依赖与生命周期

所属单元：[M00.3](../ROADMAP.md#m00-3)。对应作业：[Q01 最小 Maven 工程](../practice/practice/m00_3/README.md#q01)、[Q04 失败分类](../practice/practice/m00_3/README.md#q04)。

回补说明：整理首次 Maven 构建、JAR 路径辨析和日志分类的讲解。UDC 多模块关系单独保存在下一份课件，避免把两个学习块混为一份操作清单。

## 学习目标与范围

把已完成的 Java 程序放入 Maven 标准目录，用 POM 管理构建，理解 compile、test、package 的先后关系，并区分编译、构件下载和测试失败。

本块覆盖最小 jar 工程、坐标、常见依赖范围、仓库和构建阶段。插件开发、依赖冲突的完整处理和公司多模块实践不在本块展开。

## 限定阅读

主资料：[JavaGuide · Maven 核心概念](https://javaguide.cn/tools/maven/maven-core-concepts.html)，阅读约 20 分钟。

| 入口 | 明确起止范围 |
| --- | --- |
| [Maven 介绍](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-介绍) | 完整阅读“Maven 介绍”和“Maven 坐标”，包含各坐标字段与 EasyExcel 示例；到“Maven 依赖”标题前停止。 |
| [依赖配置](https://javaguide.cn/tools/maven/maven-core-concepts.html#依赖配置) | 从“依赖配置”读到“依赖范围”结束，在“传递依赖性”前停止。重点辨认 dependencies 与常见 scope。 |
| [Maven 仓库](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-仓库) | 完整阅读该节，到“Maven 生命周期”前停止，区分本地缓存、中央仓库和公司私服。 |
| [Maven 生命周期](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-生命周期) | 读开头、default 和 clean 两小节，到 site 生命周期前停止；重点是 compile、test、package。 |

## 核心讲解与示例

### POM 是项目配置入口

上一节手动指定源码和编译参数。Maven 把项目坐标、依赖、插件和构建配置集中到 pom.xml，并采用约定的目录结构。可以借用 package.json 理解“配置入口”，同时记住 Maven 生命周期还规定了阶段顺序。

本练习的 [完整 POM](../practice/practice/m00_3/project/pom.xml) 中，关键字段为：

```xml
<groupId>com.example.learning</groupId>
<artifactId>group-name-demo</artifactId>
<version>1.0-SNAPSHOT</version>
<packaging>jar</packaging>
```

常用坐标写成 `groupId:artifactId:version`，本题为 `com.example.learning:group-name-demo:1.0-SNAPSHOT`。packaging 表示打包类型；这里生成 jar。

`maven.compiler.release=8` 延续 Java 8 编译兼容目标，运行 Maven 的 JDK 另看 mvn -version。compiler 插件负责执行编译；即使没有业务库依赖，首次构建仍可能下载插件及插件自身的依赖。

### 标准目录与依赖范围

```text
project/
├── pom.xml
├── src/main/java/m00_2/ValidationDemo.java
├── src/test/java/             # M00.4 再加入真实测试
└── target/
    ├── classes/m00_2/ValidationDemo.class
    └── group-name-demo-1.0-SNAPSHOT.jar
```

`src/main/java` 是主代码源码根；package m00_2 对应其下的 m00_2 目录。target 保存生成物，与源码分开。

| scope | 先理解的用途 |
| --- | --- |
| compile | 默认范围，主代码编译、测试及运行时需要 |
| test | 测试编译与执行使用，例如本工作区后续的 JUnit |
| provided | 编译、测试时可用，运行时预期由容器或运行环境提供 |
| runtime | 主代码编译时不要求，运行及测试时需要 |

本地仓库保存下载的构件并承担缓存作用。中央仓库与公司私服是远程构件来源；POM 声明了某个依赖，不等于本机已经下载成功。

### 后面的阶段会经过前面的必要阶段

对本课 jar 工程，关注这段 default 生命周期顺序：

```text
编译主代码 → 编译测试代码 → 执行测试 → 打包
  compile                       test     package
```

执行 package 会先经过前面的阶段；主代码已经是最新时，编译插件可能报告 Nothing to compile，但编译阶段仍被检查。clean 是另一条生命周期，`clean compile` 表示先清理，再执行编译流程。

当时在已核对 JDK 21 的练习终端执行：

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_3/project
mvn -version
mvn -B -ntp clean compile
mvn -B -ntp test
mvn -B -ntp package
```

`-B` 使用批处理模式，`-ntp` 隐藏下载进度条；它们不改变业务代码含义。观察对象分别是 target/classes 下的 class、实际测试数量及 target 下的 JAR。

### 三个容易混淆的名字

```sh
jar tf target/group-name-demo-1.0-SNAPSHOT.jar
java -cp target/group-name-demo-1.0-SNAPSHOT.jar m00_2.ValidationDemo '  基本信息  '
```

| 名称 | 本题对应值 |
| --- | --- |
| 磁盘上的 JAR 文件 | target/group-name-demo-1.0-SNAPSHOT.jar |
| JAR 内部的 class 路径 | m00_2/ValidationDemo.class |
| 运行时指定的入口类全名 | m00_2.ValidationDemo |

jar tf 查看 JAR 里有哪些文件；java -cp 用该 JAR 作为类路径，再显式指定入口类。若 package 没成功生成可用 JAR，后续类加载失败不能直接证明校验方法写错了。

### 根据有效错误行分类

| 现象 | 应作出的判断 |
| --- | --- |
| BUILD SUCCESS，加上 No tests to run | 构建到达的阶段成功，但实际没有测试可运行；不等于测试用例通过 |
| Compilation failure，随后指出源码位置 | 先处理 Java 编译问题 |
| Could not transfer artifact，加上 Connection timed out | 构件传输失败，结合坐标、仓库和连接原因排查 |
| Tests run: 3, Failures: 1, Errors: 0, Skipped: 0 | 教学例子中实际执行了测试，至少一个测试失败 |
| Nothing to compile - all classes are up to date | 现有 class 已是最新，可属于正常构建结果 |

`Could not resolve dependencies` 只说明依赖解析失败，具体是缺少构件、连接超时还是其他原因，要继续看下面的错误链。上表的测试数量是教学示意，不代表这个 M00.3 最小工程运行过 3 个测试。

首次 package 的旧记录缺少具体 ERROR 明细，后续重试成功且未复现原失败。因此保留“原失败根因未确定”的结论，不用成功重试反推之前必定是某种网络错误。

## 本次练习

- [Q01](../practice/practice/m00_3/README.md#q01)：将上一节源码放入标准目录，观察 compile/test/package，查看并运行普通 JAR。
- [Q04](../practice/practice/m00_3/README.md#q04)：用关键错误行区分编译失败、依赖下载失败和测试失败。

原命令输出、首次失败与重试记录保留在章节文件及其 evidence 目录。复习时重点解释现象；操作记录与导师核验按当前作业规范分工。

## 验收与复习

能够对应解释构建阶段、磁盘产物、包内路径和入口类名；能识别没有测试的成功构建，并根据有效错误行选择排查方向。

本课件补齐不产生新的构建或测试结论。原操作与整个单元的验收范围分别见 [构建记录](../learning-records/0006-m00-3-maven-build.md) 和 [M00.3 验收记录](../learning-records/0008-m00-3-acceptance.md)。

## 资料与关联

- [Maven 官方生命周期说明](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html)：导师补查阶段顺序与 clean 的区别，非新增必读材料。
- 上一课：[编译运行与调试](0003-m00-2-jdk-compile-and-debug.md)。
- 同单元下一块：[Maven 聚合、继承与模块依赖](0005-m00-3-maven-modules-and-dependencies.md)。

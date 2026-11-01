# M00.2 · JDK、编译运行与调试

所属单元：[M00.2](../ROADMAP.md#m00-2)。对应作业：[M00.2 章节文件](../practice/practice/m00_2/README.md)。

回补说明：整理当时的工具环境、名称校验、编译运行、错误定位及兼容目标补讲。以下命令用于回看操作方法，不代表本次重新执行过这些操作。

## 学习目标与范围

区分源码、字节码、编译器和运行环境；把一个 Java 方法编译运行起来，能根据编译错误、异常堆栈和断点观察定位问题。

范围包括名称校验所需的参数、返回值、异常和短路判断；JIT/AOT、虚拟机结构及复杂异常体系暂缓。

## 限定阅读

主资料为 JavaGuide 的 Java 基础（上），分两块阅读：

| 入口 | 起止范围与目的 |
| --- | --- |
| [JVM vs JDK vs JRE](https://javaguide.cn/java/basis/java-basic-questions-01.html#⭐️-jvm-vs-jdk-vs-jre) | 从该标题下“JVM”开始，读到“JDK 和 JRE”结束，在下一标题“什么是字节码？采用字节码的好处是什么？”前停止。辨认运行环境与开发工具；跳过产品枚举、规范外链、jlink 扩展。 |
| [什么是字节码？](https://javaguide.cn/java/basis/java-basic-questions-01.html#⭐️-什么是字节码-采用字节码的好处是什么) | 只读定义、源码执行流程图及紧随其后的解释，到第一个“拓展阅读”之前停止。理解源码先编译再运行；暂缓 JIT 深入与 AOT。 |

## 核心讲解与示例

### 开发工具与执行流程

| 名称 | 本节需要区分的职责 |
| --- | --- |
| JVM | 加载并执行 Java 字节码 |
| JRE | 运行 Java 程序所需的环境这一概念 |
| JDK | 提供 Java 开发工具，例如 javac，并包含运行程序所需的能力 |
| javac | 把 Java 源码编译成 class 文件 |
| java | 启动 Java 程序的运行环境，加载入口类 |
| Maven | 组织构建流程，自身也运行在某个 JDK 上 |

```text
ValidationDemo.java
       │ javac 编译
       ▼
ValidationDemo.class
       │ java 启动 JVM，加载入口类
       ▼
执行 main，再调用 requireGroupName
```

TS 开发可以借助“源码先交给工具处理，再交给运行环境执行”的经验理解这个流程；Java 的 class 保存的是供 JVM 使用的字节码，不能直接等同于编译后的 JavaScript。

### 方法签名、参数与返回值

```java
public static String requireGroupName(String name)
```

从左到右读：public 表示公开访问；static 表示属于类，可通过类名调用；String 是返回类型；requireGroupName 是方法名；括号里的 String name 是参数声明。

本练习的 main 入口从命令行取第一个参数。没有参数时传入 null，再调用校验方法。方法可以返回处理后的字符串，也可以因非法输入抛出异常；异常抛出后，这次调用不会按正常路径返回结果。

原练习源码见 [ValidationDemo.java](../practice/practice/m00_2/ValidationDemo.java)。业务约定是去掉首尾空格、保留内部空格，并拒绝 null、空串及仅含空格的名称。

### 空引用与短路判断

先看这个安全的条件：

```java
name == null || name.trim().isEmpty()
```

`||` 从左向右短路求值。name 为 null 时左侧已为 true，右侧不会执行；这样避免在空引用上调用 trim。把顺序反过来，就可能在还没判断 null 前触发 NullPointerException。

trim 返回处理后的字符串；isEmpty 判断长度是否为零。把清理字符串与判断拆成清晰步骤，有利于阅读和调试。原提交中“条件里赋值”的写法也可以合法执行，回补课件不改写原实现。

### 编译、输出目录与运行入口

以下是当时练习的操作方式，在使用 JDK 21 的练习终端执行：

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice
mkdir -p out/production/practice
javac --release 8 -encoding UTF-8 -g -d out/production/practice practice/m00_2/ValidationDemo.java
java -cp out/production/practice m00_2.ValidationDemo '  基本信息  '
```

| 参数或名称 | 含义 |
| --- | --- |
| --release 8 | 按 Java 8 的语言规则、对应标准 API 和目标字节码编译 |
| -encoding UTF-8 | 指定源码编码 |
| -g | 保存调试信息 |
| -d out/production/practice | 指定编译输出根目录 |
| -cp out/production/practice | 指定运行时查找类的根目录 |
| m00_2.ValidationDemo | 包名加类名，不是文件系统路径 |

源码里的 `package m00_2;` 对应输出目录下的 `m00_2/ValidationDemo.class`。每次修改源码后，先编译成功再运行，避免误把旧 class 的输出当作新代码结果。

### 编译目标不会切换运行 JDK

当时工具输出是 javac 21.0.11、Java 21.0.11、Maven 3.9.16；Maven 报告的运行 JDK 也是 21.0.11。`--release 8` 改变的是编译兼容约束，不会把这些工具切换为 Java 8。复习或换终端时，实际版本仍应以 java、javac 和 mvn 的版本输出为准。

Java 8 目标的 class 主版本号为 52。它与本机运行 JDK 的主版本号不是同一个指标。课上出现的 source/target 8 过时警告也不等于编译失败：应结合退出码、错误信息和产物判断。

### 编译错误、运行异常与断点

- 少分号等语法问题会在 javac 阶段报错，信息通常指出源文件和行号；先恢复正确源码并重新编译，再继续运行。
- 非法名称由程序主动抛出 IllegalArgumentException，是运行阶段的行为。异常堆栈能说明异常类型和调用位置，不表示编译失败。
- 在 requireGroupName 第一条可执行语句处打断点，使用 Debug 启动 main；参数留空时观察 name 为 null，并查看 main → requireGroupName 的调用关系，再单步到异常位置。

## 本次练习

- [Q01 工具环境](../practice/practice/m00_2/README.md#q01)：区分 java、javac 和 Maven 自身及其运行 JDK。
- [Q02 编译运行与调试](../practice/practice/m00_2/README.md#q02)：名称校验、四类输入、编译错误和断点观察。
- [Q03 兼容目标](../practice/practice/m00_2/README.md#q03)：解释 --release 8 为什么不会切换运行环境。

已有代码与原始操作记录继续保存在章节目录。回补课件不要求为补齐历史表格重跑已经确认的操作；按当前交付规范保存必要代码，由导师核验可观察结果。

## 验收与复习

复习围绕三个判断：当前失败发生在编译还是运行阶段、class 路径与入口类名怎样对应、Java 8 兼容目标约束了什么。具体题目一次检查一道。

原验收区分用户提交的调试观察、导师复核运行，以及导师补充的语法/API 兼容细节，见 [M00.2 验收记录](../learning-records/0005-m00-2-acceptance.md)。本课件不扩大这些证据范围。

## 资料与关联

- [javac 官方文档](https://docs.oracle.com/en/java/javase/21/docs/specs/man/javac.html)：导师补查编译选项与 --release 的含义，非额外必读资料。
- [名称校验与运行记录](../learning-records/0004-m00-2-validation-practice.md)：原方法与当时核验结果。
- 上一课：[接口与实现](0002-m00-1-interface-and-implementation.md)；下一课：[Maven 构建与生命周期](0004-m00-3-maven-build-and-lifecycle.md)。

# M00.2 · JDK、编译与调试

## 本章作业要求

目标：区分开发工具、编译与运行环境，亲手编译运行 Java 方法并定位错误。对应 [ROADMAP M00.2](../../../ROADMAP.md#m00-2)。本文件按题号逐步追加，正式状态见 [PROGRESS](../../../PROGRESS.md)。

## 当前限定阅读

- 资料：[JavaGuide · JVM vs JDK vs JRE](https://javaguide.cn/java/basis/java-basic-questions-01.html#⭐️-jvm-vs-jdk-vs-jre)。
- 起点：该标题下的“JVM”小节。
- 终点：“JDK 和 JRE”小节结束，在下一标题“什么是字节码？采用字节码的好处是什么？”之前停下。
- 本次重点：JVM 负责运行字节码，JRE 表示运行所需环境，JDK 提供编译和开发工具；辨认 `java` 与 `javac` 的作用。
- 暂时跳过：不同 JVM 产品枚举、规范链接，以及模块化、jlink 的扩展介绍。字节码执行细节和编写方法在后续学习块展开。

## 题目索引

- [Q01 · 记录本机 Java 工具环境](#q01)
- [Q02 · 名称校验、编译运行与调试](#q02)
- [Q03 · 编译兼容目标与运行 JDK](#q03)

<a id="q01"></a>

## Q01 · 记录本机 Java 工具环境

### 作业要求

这是操作练习，可以查资料。先完成上面的限定阅读，再在终端进入练习工程，逐条执行：

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice
java -version
javac -version
mvn -version
```

把每条命令的完整输出分别粘贴到下方。若有命令报错或不存在，保留原始错误，由导师据此定位；报错也属于本题有效提交。本题只收集当前环境，不预填版本答案。

验收标准：三条命令的当次输出可核对，能够识别已可用的工具及具体阻塞；仅完成本题不代表整个 M00.2 通过。

### 我的解答

执行时间：

执行目录：

`java -version` 输出：

```text
openjdk version "21.0.11" 2026-04-21
OpenJDK Runtime Environment Homebrew (build 21.0.11)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.11, mixed mode, sharing)
```

`javac --version` 输出：

```text
javac 21.0.11
```

`mvn -version` 输出：

```text
Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)
Maven home: /opt/homebrew/Cellar/maven/3.9.16/libexec
Java version: 21.0.11, vendor: Homebrew, runtime: /opt/homebrew/Cellar/openjdk@21/21.0.11/libexec/openjdk.jdk/Contents/Home
Default locale: zh_CN_#Hans, platform encoding: UTF-8
OS name: "mac os x", version: "26.3.1", arch: "aarch64", family: "mac"
```

### 验收反馈

2026-11-01 更新：已读取用户更正后的编译器输出 `javac 21.0.11`。本次用户说明实际使用的是 `javac --version`，已同步该输出栏的命令标注。

核对结果：Java 运行环境为 21.0.11，javac 编译器为 21.0.11，Maven 3.9.16 报告运行于 Java 21.0.11，版本一致。Q01 的工具环境检查通过。

本机 JDK 21 的 `javac --help` 同时列出 `--version, -version`，两种写法均支持；先前待核对的是重复粘贴的输出内容，该问题现已解决。

执行时间、执行目录仍未填写，按未记录保留；不据此要求重跑已确认的版本命令，后续编译运行记录再填写实际时间与目录。本题通过不代表整个 M00.2 已验收。

<a id="q02"></a>

## Q02 · 名称校验、编译运行与调试

### 作业要求

学习块约 60–90 分钟，可以查资料、运行和调试。主资料为 [JavaGuide · 什么是字节码？采用字节码的好处是什么？](https://javaguide.cn/java/basis/java-basic-questions-01.html#⭐️-什么是字节码-采用字节码的好处是什么)。从该标题开始，只读字节码定义、源码转为运行的流程图与紧随其后的解释段，读到第一个“拓展阅读”之前停下；暂缓 JIT 深入、AOT 与虚拟机结构。阅读约 5–10 分钟。

本次掌握的流程：修改 `.java` 源码 → `javac` 编译出 `.class` → `java` 启动 JVM，加载并执行类的入口。每次修改源码后都要重新编译；编译失败时先修复并重新编译成功，再运行，避免把上一次的 `.class` 输出当作本次结果。

#### 1. 补齐一个校验方法

源码文件：[ValidationDemo.java](ValidationDemo.java)。已提供包名、类、方法签名和 `main` 入口；方法目前仅返回原参数，尚未实现校验。请修改 `requireGroupName` 的方法体，满足以下规则：


| 输入           | 要求                                                               |
| ------------ | ---------------------------------------------------------------- |
| `null`       | 抛出 `IllegalArgumentException`，消息为 `group name must not be blank` |
| 空字符串或只有空格    | 抛出相同异常和消息                                                        |
| 有效名称，可能带前后空格 | 去掉前后空格后返回；保留名称中间的空格                                              |


可用语法与 API：`name == null` 判断空引用；`String.trim()` 返回去掉前后空格的字符串；`String.isEmpty()` 判断字符串长度是否为零。异常语句的写法是 `throw new IllegalArgumentException("group name must not be blank");`。如何安排判断顺序和返回值由你完成。

方法签名 `public static String requireGroupName(String name)` 表示：可公开调用、属于类、返回 `String`、方法名是 `requireGroupName`、接收一个 `String` 参数。`main` 是程序入口，它从命令行取第一个参数，未提供参数时传入 `null`，再调用你的校验方法。

AI 辅助边界：入口、签名、参考语法与命令由导师提供；用户编写的校验逻辑及实际操作单独记录，不把整个模板计为用户独立代码。

#### 2. 编译并运行四种输入

在练习工程根目录逐条执行。先用 `date`、`pwd` 记录当次时间和目录。

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice
date '+%F %T %Z'
pwd
mkdir -p out/production/practice
javac --release 8 -encoding UTF-8 -g -d out/production/practice practice/m00_2/ValidationDemo.java
```

编译成功通常没有输出；编译后确认生成 `out/production/practice/m00_2/ValidationDemo.class`。`--release 8` 按 Java 8 的语言、目标字节码及对应标准 API 编译；编译器仍是本机 JDK 21。`-d` 指定输出根目录，`-encoding UTF-8` 指定源码编码，`-g` 保留调试信息。

编译成功后，逐条运行并保留正常输出或异常堆栈：

```sh
java -cp out/production/practice m00_2.ValidationDemo '  基本信息  '
java -cp out/production/practice m00_2.ValidationDemo ''
java -cp out/production/practice m00_2.ValidationDemo '   '
java -cp out/production/practice m00_2.ValidationDemo
```

`-cp` 指定查找类的根目录；`m00_2.ValidationDemo` 是包名加类名。引号内的文字作为一个参数传入；最后一条没有名称参数，入口会把 `null` 交给你的方法。本题要求非法输入由你主动抛出指定异常，不需要在 `main` 中捕获，保留堆栈用于观察运行异常。

#### 3. 定位一次编译错误

在首次正确编译后，暂时删掉 `main` 中打印语句末尾的分号，再执行同一条 `javac` 命令。保存错误信息并记录它指出的源文件和行号；恢复分号并重新编译成功。本阶段先看编译结果，再继续运行。

#### 4. 观察断点和调用栈

在 IDEA 中打开源码，在你实现的 `requireGroupName` 方法第一条可执行语句处打断点，使用 Debug 运行 `main`，程序参数留空。暂停时观察 `name` 的值以及调用栈中的 `main → requireGroupName`；单步到抛出异常，记录观察结果。

验收标准：源码满足三类输入规则；有四次当次运行输出、一份编译错误与修复后结果，以及断点和调用栈的实际观察。保留遇到的问题和已使用提示；未完成项直接写未完成。完成后保存源码与下方记录，告诉导师“已保存”，由导师读取源码与结果验证。

### 我的解答

执行时间：2026-11-01 13:12:57 CST

执行目录：/Users/yu/Desktop/yumengyuan/learn_everything/java/practice

我修改的源码范围 / 使用的额外提示：/Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_2/ValidationDemo.java. 6-9行

首次编译命令与结果（包含生成的 class 路径）：  
警告: [options] 源值 8 已过时，将在未来发行版中删除

警告: [options] 目标值 8 已过时，将在未来发行版中删除

警告: [options] 要隐藏有关已过时选项的警告, 请使用 -Xlint:-options。

3 个警告。  
  
/Users/yu/Desktop/yumengyuan/learn\_everything/java/practice/out/production/practice/m00\_2/ValidationDemo.class  


正常名称输入的输出：基本信息

空字符串输入的输出：Exception in thread "main" java.lang.IllegalArgumentException: group name must not be blank

只有空格输入的输出：Exception in thread "main" java.lang.IllegalArgumentException: group name must not be blank

无参数输入的输出：Exception in thread "main" java.lang.IllegalArgumentException: group name must not be blank

编译错误原文与定位到的行号：

practice/m00_2/ValidationDemo.java:16: 错误: 需要';'

        System.out.println(requireGroupName(name))



恢复分号后重新编译的结果：

警告: [options] 源值 8 已过时，将在未来发行版中删除

警告: [options] 目标值 8 已过时，将在未来发行版中删除

警告: [options] 要隐藏有关已过时选项的警告, 请使用 -Xlint:-options。

3 个警告

断点处参数值、调用栈和单步观察：  
参数值name:null  
at m00\_2.ValidationDemo.requireGroupName(ValidationDemo.java:6)
at m00\_2.ValidationDemo.main(ValidationDemo.java:16)


未完成项或卡点：

### 验收反馈

2026-11-01：Q02 实践部分通过。当前源码能处理正常名称、空字符串、只有空格和 `null`，并保留名称中间的空格。导师以 JDK 21、`--release 8` 重新编译当前源码，在临时目录运行 5 组输入，全部符合要求；class 主版本号为 52，对应 Java 8。详见 [本次复核记录](evidence/2026-11-01-q02-verification.json)。

用户已记录缺分号时的编译错误与第 16 行位置、恢复后的编译结果，以及断点处 `name = null` 和 `requireGroupName / main` 调用栈。断点部分按用户提交的实际观察记录；导师本次复核的是命令行行为。

三条 source/target 8 过时警告本次也可复现，编译退出码为 0，属于成功编译时的警告。用户首次编译记录未逐字贴出命令，本次导师复核日志保留了完整命令与输出。

校验逻辑正确：`||` 按从左到右短路求值，`name == null` 成立时，后面的 `isEmpty()` 和 `trim()` 不会执行。条件中同时赋值与判断可正常工作；以后可将清理字符串单独写成一步，便于阅读。本次保留用户源码。

<a id="q03"></a>

## Q03 · 编译兼容目标与运行 JDK

### 作业要求

这是 M00.2 最后一项概念检查，可以查看本章 Q01 的实际输出和 Q02 的命令，先不回看导师讲解，不需要执行新命令。用 2–4 句中文在下方作答。

> 使用 `javac --release 8` 后，运行程序和运行 Maven 的 JDK 会变成 Java 8 吗？请结合 Q01 的输出，用 2–4 句说明该选项约束的对象，以及两个运行环境各使用的版本。

验收标准：能区分源码及目标字节码的兼容要求、编译器版本、程序运行 JDK 和 Maven 使用的 JDK；未回答时保持未评分。

### 我的解答

不会变成Java8，只是编辑成Java8版本对应的字节码

### 验收反馈

2026-11-01：核心判断通过。用户正确指出兼容目标不会将实际运行环境切换为 Java 8；“编辑成”在这里应称为“编译成”。

导师补充：`--release 8` 同时约束源码语言规则、可使用的对应版本标准 API 和目标字节码。根据 Q01 的实际输出，本机编译器、运行程序的 Java 以及 Maven 使用的 JDK 都是 21.0.11；Maven 自身版本为 3.9.16。

以上语法/API 约束和具体版本的完整陈述为导师补充，不改写用户原答案，也不作为用户已独立陈述这些细节的证据。结合 Q01、Q02 的工具与实践证据，M00.2 本节验收通过；这些补充细节在后续复查中再检查。正式记录见 [M00.2 验收记录](../../../learning-records/0005-m00-2-acceptance.md)。

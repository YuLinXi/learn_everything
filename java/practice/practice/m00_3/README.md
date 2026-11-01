# M00.3 · Maven 的项目、依赖与生命周期

## 本章作业要求

目标：用 Maven 组织已完成的 Java 程序，区分编译、测试阶段与打包；之后再定位公司多模块 POM。对应 [ROADMAP M00.3](../../../ROADMAP.md#m00-3)，正式状态见 [PROGRESS](../../../PROGRESS.md)。

## Q01 限定阅读

主资料：[JavaGuide · Maven 核心概念](https://javaguide.cn/tools/maven/maven-core-concepts.html)。本块约 60–90 分钟，阅读约 20 分钟，其余用于操作与记录。


| 章节入口                                                                               | 起止范围与重点                                                                                                                                           |
| ---------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| [Maven 介绍、坐标](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-介绍)  | **完整阅读“Maven 介绍”和“Maven 坐标”两节**，包含坐标各字段的说明及 EasyExcel 依赖示例。看到下一个二级标题“Maven 依赖”时停止；该标题下的正文不属于这一段阅读范围。重点是 POM、groupId、artifactId、version、packaging。 |
| [依赖配置与范围](https://javaguide.cn/tools/maven/maven-core-concepts.html#依赖配置)          | 从“依赖配置”读到“依赖范围”结束，在“传递依赖性”前停下。先理解 dependencies 和 compile/test/provided/runtime；冲突与排除后续再展开。                                                        |
| [Maven 仓库](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-仓库)     | 从该标题读到“Maven 生命周期”前停下，区分本地缓存、中央仓库和私服。                                                                                                             |
| [Maven 生命周期](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-生命周期) | 读开头与 default、clean 两小节，到 site 生命周期前停下。重点定位 compile、test、package 及先后关系，暂不背全部阶段。                                                                    |


插件开发、多模块管理及公司 POM 放到后续学习块。本题不新增运行库依赖；测试框架和测试代码在 M00.4 建立。

## 题目索引

- [Q01 · 建立最小 Maven 工程并观察三个阶段](#q01)
- [Q02 · UDC 多模块关系讲解](#q02)
- [Q03 · 聚合与直接依赖（口头检查）](#q03)
- [Q04 · 根据日志区分失败类型（口头检查）](#q04)

<a id="q01"></a>

## Q01 · 建立最小 Maven 工程并观察三个阶段

### 作业要求

本题为可以查资料、运行命令的操作练习。已提供 [project/pom.xml](project/pom.xml)，构建配置属于 AI 提供的脚手架；由用户完成源码组织、实际执行与结果观察。复用 M00.2 的校验方法，保留其原学习证据。

#### 1. 看懂配置与目录

- `pom.xml` 是 Maven 项目的配置入口，坐标为 `com.example.learning:group-name-demo:1.0-SNAPSHOT`，打包类型为 jar。
- `maven.compiler.release=8` 延续上一节的 Java 8 兼容目标；运行 Maven 使用当前已核对的 JDK 21。
- `maven-compiler-plugin` 为构建插件，固定 3.16.0；项目虽然没有业务依赖，首次构建仍可能下载插件。插件配置与运行库依赖的职责不同。
- 本项目没有公司父 POM，先用于学习独立构建。后续公司依赖分析不依据本项目猜测。

在终端执行以下目录与复制操作：

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_3/project
mkdir -p src/main/java/m00_2
cp ../../m00_2/ValidationDemo.java src/main/java/m00_2/ValidationDemo.java
```

保留源码里的 `package m00_2;`。Maven 项目的源码根为 `src/main/java`，包名对应其下的 `m00_2/`；本题复制你的上一节实现，不改写原文件。

```text
project/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── m00_2/
                └── ValidationDemo.java
```

如果使用 IDEA，打开本题 `project/pom.xml`，作为独立 Maven 工程在新窗口导入，以 `src/main/java` 为源码根。

#### 2. 按顺序执行构建阶段

在同一个 project 目录中逐条执行并保存完整日志或失败信息：

```sh
date '+%F %T %Z'
pwd
mvn -version
mvn -B -ntp clean compile
mvn -B -ntp test
mvn -B -ntp package
```

- `compile`：编译主代码，观察 `target/classes/m00_2/ValidationDemo.class`。
- `test`：进入测试阶段，观察是否发现测试、是否真正运行了测试；只看到 BUILD SUCCESS 时，还需要查看测试相关日志。
- `package`：完成前面的必要阶段后，按 jar 配置生成 `target/group-name-demo-1.0-SNAPSHOT.jar`。
- `clean` 属于单独的生命周期，清理既有构建产物；上面的第一条构建命令先清理再编译。

若某一步失败，先记录实际命令和第一处有意义的错误，暂不运行依赖其产物的后续步骤。遇到下载失败时保留构件坐标和错误摘要，不将它记为 Java 编译错误。

#### 3. 查看并运行打包产物

构建成功后执行：

```sh
jar tf target/group-name-demo-1.0-SNAPSHOT.jar
java -cp target/group-name-demo-1.0-SNAPSHOT.jar m00_2.ValidationDemo '  基本信息  '
```

在 JAR 列表中找到 `m00_2/ValidationDemo.class`，记录实际程序输出。这是普通 JAR，本题通过 `-cp` 指定它并显式提供入口类。

验收标准：源码目录与包名正确；有 compile/test/package 的当次结果和对应产物；能从测试日志判断是否运行了测试，不能把没有测试的成功构建当成测试用例通过；打包产物可通过指定类运行。命令失败时保留具体阻塞，不预填成功结论。

### 我的解答

执行时间：2026-11-01 14:13:42 CST

执行目录：/Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_3/project

使用的提示 / AI 辅助范围：无

从 POM 读出的项目坐标与打包类型：com.example.learning:group-name-demo:1.0-SNAPSHOT 打包类型：jar

迁移后的 Java 文件路径及 package：  
/Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_3/project/src/main/java/m00_2/[ValidationDemo.java](http://ValidationDemo.java)  
package m00_2

`mvn -version` 的输出：Apache Maven 3.9.16 (2bdd9fddda4b155ebf8000e807eb73fd829a51d5)

`mvn -B -ntp clean compile` 的关键日志、结果与 class 路径：  
关键日志

[INFO] --- clean:3.2.0:clean (default-clean) @ group-name-demo ---

[INFO] Artifact org.apache.maven.shared:maven-shared-utils:jar:3.3.4 is present in the local repository, but cached from a remote repository ID that is unavailable in current build context, verifying that is downloadable from [central ([https://repo.maven.apache.org/maven2](https://repo.maven.apache.org/maven2), default, releases), apache.snapshots ([https://repository.apache.org/snapshots](https://repository.apache.org/snapshots), default, snapshots)]

[INFO] Artifact org.apache.maven.shared:maven-shared-utils:jar:3.3.4 is present in the local repository, but cached from a remote repository ID that is unavailable in current build context, verifying that is downloadable from [central ([https://repo.maven.apache.org/maven2](https://repo.maven.apache.org/maven2), default, releases), apache.snapshots ([https://repository.apache.org/snapshots](https://repository.apache.org/snapshots), default, snapshots)]



[INFO] --- compiler:3.16.0:compile (default-compile) @ group-name-demo ---

[INFO] Artifact org.codehaus.plexus:plexus:pom:25 is present in the local repository, but cached from a remote repository ID that is unavailable in current build context, verifying that is downloadable from [central ([https://repo.maven.apache.org/maven2](https://repo.maven.apache.org/maven2), default, releases)]

[INFO] Artifact org.codehaus.plexus:plexus:pom:25 is present in the local repository, but cached from a remote repository ID that is unavailable in current build context, verifying that is downloadable from [central ([https://repo.maven.apache.org/maven2](https://repo.maven.apache.org/maven2), default, releases)]

[INFO] Recompiling the module because of changed source code.

[INFO] Compiling 1 source file with javac [debug release 8] to target/classes



结果  
[INFO] BUILD SUCCESS  

class 路径  
/Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_3/project/target/classes/m00_2/ValidationDemo.class



`mvn -B -ntp test` 的关键日志、实际运行测试数量及判断依据：

关键日志：

[INFO] --- surefire:3.5.4:test (default-test) @ group-name-demo ---

[INFO] No tests to run.  

实际运行测试数量

--- surefire:3.5.4:test (default-test) @ group-name-demo ---

No tests to run.



`mvn -B -ntp package` 的关键日志、结果与 JAR 路径：

关键日志：  

结果：BUILD FAILURE



JAR路径：不明白



`jar tf` 中找到的类路径：target/group-name-demo-1.0-SNAPSHOT.jar

通过 JAR 运行程序的实际输出：  
错误: 找不到或无法加载主类 m00_2.ValidationDemo

原因: java.lang.ClassNotFoundException: m00_2.ValidationDemo

遇到的失败 / 未完成项：

#### 重试记录（保留上方首次失败）

重试执行时间：2026-11-01 14:36:39 CST

`mvn -B -ntp package` 的结果与关键日志（失败时包括 `[ERROR]` 行）：

[INFO] Scanning for projects...

[INFO] 

[INFO] ----------------&lt; com.example.learning:group-name-demo &gt;----------------

[INFO] Building group-name-demo 1.0-SNAPSHOT

[INFO]   from pom.xml

[INFO] --------------------------------[ jar ]---------------------------------

[INFO] 

[INFO] --- resources:3.4.0:resources (default-resources) @ group-name-demo ---

[INFO] skip non existing resourceDirectory /Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_3/project/src/main/resources

[INFO] 

[INFO] --- compiler:3.16.0:compile (default-compile) @ group-name-demo ---

[INFO] Nothing to compile - all classes are up to date.

[INFO] 

[INFO] --- resources:3.4.0:testResources (default-testResources) @ group-name-demo ---

[INFO] skip non existing resourceDirectory /Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_3/project/src/test/resources

[INFO] 

[INFO] --- compiler:3.16.0:testCompile (default-testCompile) @ group-name-demo ---

[INFO] No sources to compile

[INFO] 

[INFO] --- surefire:3.5.4:test (default-test) @ group-name-demo ---

[INFO] No tests to run.

[INFO] 

[INFO] --- jar:3.5.0:jar (default-jar) @ group-name-demo ---

[INFO] ------------------------------------------------------------------------

[INFO] BUILD SUCCESS

[INFO] ------------------------------------------------------------------------

[INFO] Total time:  0.484 s

[INFO] Finished at: 2026-11-01T14:36:50+08:00

[INFO] ------------------------------------------------------------------------

`jar tf` 输出中实际出现的 class 路径：

META-INF/

META-INF/MANIFEST.MF

m00_2/

META-INF/maven/

META-INF/maven/com.example.learning/

META-INF/maven/com.example.learning/group-name-demo/

m00_2/ValidationDemo.class

META-INF/maven/com.example.learning/group-name-demo/pom.xml

META-INF/maven/com.example.learning/group-name-demo/[pom.properties](http://pom.properties)

通过 JAR 运行程序的实际输出：

基本信息

### 验收反馈

2026-11-01 首次查验：项目坐标、jar 打包类型、源码迁移与 package 正确；有成功编译日志和对应 class。`No tests to run.` 表示本次没有测试可运行，实际测试数量为 0，不代表测试用例通过。

用户首次记录的 `package` 为 BUILD FAILURE，未提供具体 `[ERROR]` 行；导师首次检查时 target 中确实没有 JAR。后续类加载失败可由缺少可用 JAR 解释，不能据此判断校验代码有误。

导师在本项目中执行同一条 `mvn -B -ntp package`，本次退出码 0，生成 JAR；核对包内存在 `m00_2/ValidationDemo.class`，通过该 JAR 运行得到 `基本信息`。没有修改 POM 或源码。记录分别见 [打包诊断日志](evidence/2026-11-01-q01-package-diagnosis.log) 与 [JAR 复核记录](evidence/2026-11-01-q01-jar-verification.json)，均属于导师操作，不代替用户重试记录。

原失败本次未复现，原始错误摘要又未提交，因此根因尚未确定，不推定为网络、仓库或源码问题。已有 INFO 日志中的缓存来源验证信息本身不表示构建失败。

三个名称要区分：


| 名称                            | 本题实际值                                     |
| ----------------------------- | ----------------------------------------- |
| JAR 文件路径（相对 project 目录）       | `target/group-name-demo-1.0-SNAPSHOT.jar` |
| JAR 内部的 class 路径（由 jar tf 查看） | `m00_2/ValidationDemo.class`              |
| 运行时指定的入口类全名                   | `m00_2.ValidationDemo`                    |


重试查验：用户已提交 2026-11-01 14:36:39 CST 的操作记录，package 日志以 BUILD SUCCESS 结束，JAR 中列出 `m00_2/ValidationDemo.class`，通过 JAR 运行输出 `基本信息`。Q01 操作验收通过，保留首次失败与后续重试记录，原失败原因仍未查明。

`Nothing to compile - all classes are up to date.` 表示编译阶段检查后发现现有 class 已是最新，属于正常结果。执行 package 的日志仍能看到编译、测试及打包的相关阶段。进入下一块 [Q02](#q02)，无需重做 Q01。

<a id="q02"></a>

## Q02 · UDC 多模块关系讲解

### 教学方式调整

2026-11-01 用户反馈手工画图费时，改为导师核对源码、绘图并给结论，直接在终端讲解。原先的画图、逐边抄录和批量填表要求撤销，不要求补齐；下方原提交保留。图表属于导师材料，用户理解通过单道抽问检查。

参考资料：[JavaGuide · Maven 多模块管理](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-多模块管理)，按需完整阅读该节，包含 Dubbo 图，看到“文章推荐”标题停止。

### 导师核对与讲解

源码基线：`projects/udc`，分支 `test`，提交 `a182384764a0d50c196dd8ff5ae36411b7ba59a7`；本次工作树干净，以下是静态 POM 关系。

#### 配置继承与模块聚合

```text
udc-parent（公共父 POM）
└── udc（根 POM，packaging=pom）
    ├── udc-biz
    ├── udc-web
    ├── udc-test
    ├── udc-assemble
    ├── udc-cicd
    └── udc-upgrade
```

根 `udc` 的 parent 是 `com.seeyon:udc-parent:6.0.0-TEST-SNAPSHOT`。六个子模块的 parent 均指向 `com.seeyon:udc:${udc.version}`；`${udc.version}` 是属性引用，本次根 properties 中没有该定义，保留占位符。

根 POM 的 `modules` 聚合了图中六个子模块。这个工程中根 `udc` 既是聚合者，也是子模块的 parent；两种作用分别来自 `modules` 和 `parent`，概念不同。子模块放进 modules，不会自动产生子模块之间的直接依赖。

#### 四个指定子模块的实际直接依赖

每条箭头 A → B 表示 A 的 `dependencies` 中声明了 B；本图范围仅含 web、biz、test、assemble 四个子模块。

```text
udc-assemble ──→ udc-web ──→ udc-biz
udc-test     ──→ udc-web
udc-test     ──→ udc-biz
```

| 直接依赖 | 源码依据 | scope |
| --- | --- | --- |
| web → biz | [udc-web/pom.xml](../../../projects/udc/udc-web/pom.xml)，第 15 行 | 未显式写，默认 compile |
| test → web | [udc-test/pom.xml](../../../projects/udc/udc-test/pom.xml)，第 15 行 | 未显式写，默认 compile |
| test → biz | 同上，第 19 行 | 未显式写，默认 compile |
| assemble → web | [udc-assemble/pom.xml](../../../projects/udc/udc-assemble/pom.xml)，第 18 行 | 未显式写，默认 compile |

`udc-assemble` 可以经 web 形成到 biz 的传递路径，但这不等于 assemble 的 POM 直接声明了 biz。模块名叫 `udc-test`，也不意味着其所有依赖的 scope 都是 test，要以具体声明为准。

#### 父 POM 源码位置

本次扫描工作区 `projects/` 下 37 份非 target 目录的 POM，未找到 artifactId 为 `udc-parent` 的项目源码。结论限定为本工作区未见该父项目源码；不能据此认定 Maven 无法获得其 POM，因为构件还可能在本地或远程仓库中。本题没有运行公司构建或验证仓库解析。

### 用户原提交（保留，不要求补齐）

执行时间 / 实际分支 / 提交：

使用的提示或辅助：


| POM                  | artifactId            | packaging | parent 坐标（groupId:artifactId:version）     |
| -------------------- | --------------------- | --------- | ----------------------------------------- |
| 根 pom.xml            | ```bash<br>udc<br>``` | pom       | com.seeyon.udc-parent.6.0.0-TEST-SNAPSHOT |
| udc-web/pom.xml      | udc-web               | jar       | com.seeyon.udc.${udc.version}             |
| udc-biz/pom.xml      | udc-biz               | jar       | com.seeyon.udc.${udc.version}             |
| udc-test/pom.xml     | udc-test              | jar       | com.seeyon.udc.${udc.version}             |
| udc-assemble/pom.xml | udc-assemble          | jar       | com.seeyon.udc.${udc.version}             |


根 POM 的 modules 缩进列表：  
&lt;modules&gt;

   &lt;module&gt;udc-biz&lt;/module&gt;  
   &lt;module&gt;udc-web&lt;/module&gt;  
   &lt;module&gt;udc-test&lt;/module&gt;  
   &lt;module&gt;udc-assemble&lt;/module&gt;  
   &lt;module&gt;udc-cicd&lt;/module&gt;  
   &lt;module&gt;udc-upgrade&lt;/module&gt;  
&lt;/modules&gt;

四个子模块的直接依赖图：直接依赖读取其对应的pom.xml 文件中的 dependencies 定义即可清楚，我这里单独写了。

每条依赖边的 POM 路径、行号与 scope：不知道怎么表述

根 parent 的源码查找范围与结果：不知道怎么表述

仓库解析状态：本题未验证

不确定或待查的信息：

### 验收反馈

已提交的 artifactId、packaging、parent 目标与根 modules 列表基本正确。坐标应使用冒号分隔三个字段，例如 `com.seeyon:udc-parent:6.0.0-TEST-SNAPSHOT`，子模块 parent 为 `com.seeyon:udc:${udc.version}`。不要求重新抄表。

原绘图与逐边抄录要求已撤销，未完成这些材料不记为答错。依赖图、scope 与父项目查找结论由导师补充，属于讲解，尚不作为用户独立掌握证据。后续由 Q03 检查关键关系。

<a id="q03"></a>

## Q03 · 聚合与直接依赖（口头检查）

### 作业要求

直接在对话中回答一句话即可，由导师归档到本题；无需画图或填写关系表。

> 只根据根 POM 的 `<modules>` 同时包含 `udc-web` 和 `udc-biz`，能否认定 `udc-web` 已经直接依赖 `udc-biz`？用一句话说明。

验收标准：区分 modules 的聚合作用和 dependencies 的直接依赖声明，依据明确。未回答时保持未评分。

### 我的解答

不能，要查看udc-web时候直接依赖uidc-biz 要看对应 udc-web包下的pom定义。

这里的 modules 不是描述依赖关系，而是表示将多个模块聚合起来，统一组织构建。

### 验收反馈

2026-11-01：通过。用户明确区分了 modules 的聚合作用与直接依赖声明，并指出应查看 udc-web 的 POM。更精确的定位是该 POM 的 `dependencies`。原回答中的笔误保留；理解结论正确。

<a id="q04"></a>

## Q04 · 根据日志区分失败类型（口头检查）

### 作业要求

这是 M00.3 最后一项检查。以下为教学用简化日志；不需要运行命令，直接在对话中按 `A=类型+依据；B=类型+依据；C=类型+依据` 回答即可。

**A**

```text
[ERROR] Compilation failure
[ERROR] ValidationDemo.java:[16,52] ';' expected
```

**B**

```text
[ERROR] Could not resolve dependencies for project com.example:demo:jar:1.0
[ERROR] Could not transfer artifact com.example:sample-lib:jar:1.0
[ERROR] Connection timed out
```

**C**

```text
[ERROR] Tests run: 3, Failures: 1, Errors: 0, Skipped: 0
[ERROR] There are test failures.
```

> 将 A、B、C 分别归类为“编译错误”“依赖下载失败”或“测试失败”，并从每段日志指出一个判断依据。

验收标准：三类匹配正确，依据来自错误日志；不把它们等同于笼统的 BUILD FAILURE。本题日志不作为本项目真实构建或测试记录。

### 我的解答

```text
A = 编译错误 + [ERROR] Compilation failure
B= 依赖下载失败 + [ERROR] Could not resolve dependencies for project com.example:demo:jar:1.0
C= 测试失败 + [ERROR] Tests run: 3, Failures: 1, Errors: 0, Skipped: 0
```

### 验收反馈

2026-11-01：通过，三项分类正确。A 为编译错误，进一步可从文件行列与 `';' expected` 定位语法问题。B 在本题完整日志中为依赖下载失败；用户引用的 resolve dependencies 是总括信息，具体下载依据还应继续看 `Could not transfer artifact` 和 `Connection timed out`。仅凭 resolve dependencies 不能将所有依赖解析问题都归因于下载超时。C 明确运行了 3 个测试，失败 1 个，属于测试失败。

本题为教学日志分类，不把示例中的 3 个测试记为本工程的实际测试数量。结合 Q01 实践及 Q03 关系解释，M00.3 本次验收通过，详见 [完整验收记录](../../../learning-records/0008-m00-3-acceptance.md)。

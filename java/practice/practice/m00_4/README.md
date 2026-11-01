# M00.4 · 第一次可重复的单元测试

## 本章作业要求

目标：将名称校验规则写成可重复运行的 JUnit 测试，理解断言保护的业务约定。对应 [ROADMAP M00.4](../../../ROADMAP.md#m00-4)，正式状态见 [PROGRESS](../../../PROGRESS.md)。

交付：保存测试代码并告知“已完成”即可；导师读取源码并运行核验。理解检查在续学时一次问一道，可直接在对话回答。

本章使用独立 [Maven 工程](project/pom.xml)，复用 M00.3 的校验源码；新增 JUnit Jupiter 5.13.4，依赖范围 test，Surefire 3.5.4，编译兼容目标仍为 Java 8。JUnit 5.13.4 支持 Java 8 及以上运行环境，本练习沿用 JDK 21。业务方法直接处理参数，无外部服务依赖，先使用真实方法调用与断言。

## 限定阅读

主资料：[JavaGuide · 单元测试到底是什么？应该怎么做？](https://javaguide.cn/system-design/basis/unit-test.html)，阅读约 10–15 分钟。

1. **完整阅读“何谓单元测试？”**，到“为什么需要单元测试？”标题前停止；只理解测试单元与依赖隔离概念，外链无需展开。
2. **完整阅读“为什么需要单元测试？”及其五个小节**，到“谁逼你写单元测试？”标题前停止。包含“为重构保驾护航”中单元测试与集成测试边界的说明。
3. 跳到 **“单测框架和 Mock 工具如何选择？”**，只读开头两段，辨认 JUnit/Spock 与 Mock 工具的职责，到开始比较两套方案的提问前停止。本题不要求使用 Mock。
4. “谁逼你写单元测试？”、TDD 章节、框架对比细节暂时跳过。

## 题目索引

- [Q01 · 编写断言并解释保护的规则](#q01)
- [Q02 · 判断测试失败的修复方向](#q02)

<a id="q01"></a>

## Q01 · 编写断言并解释保护的规则

### 作业要求

可查资料并运行。本题业务实现已存在，核心任务是为正常输入和非法输入写出有效断言，再解释这些断言如何发现错误。

#### 1. 认识测试代码

源码：[ValidationDemo.java](project/src/main/java/m00_2/ValidationDemo.java)。测试：[ValidationDemoTest.java](project/src/test/java/m00_2/ValidationDemoTest.java)。Maven 将主代码放在 src/main/java，测试代码放在 src/test/java。

`@Test` 标记测试方法；本题导入 `org.junit.jupiter.api.Test`，对应 Jupiter。`org.junit.Test` 属于 JUnit 4，两套导入不能混用。本题不编写测试 main，由测试框架发现并执行测试方法。

`assertEquals(期望值, 实际值)` 检查结果；期望值来自业务规则，实际值来自真实方法调用。已提供一个完整示例：

```java
@Test
void trimsLeadingAndTrailingSpaces() {
    assertEquals("基本信息", ValidationDemo.requireGroupName("  基本信息  "));
}
```

异常断言用 `assertThrows`。下面只用一个熟悉的字符串转整数场景演示语法；作业中要调用你自己的校验方法：

```java
NumberFormatException error = assertThrows(
    NumberFormatException.class,
    () -> Integer.parseInt("abc")
);
```

`异常类型.class` 表示要检查的异常类型；`() -> ...` 是把待执行动作交给断言，框架执行它并检查异常。`assertThrows` 会返回异常对象，可用 `error.getMessage()` 取得消息，再用 assertEquals 检查业务约定消息。

#### 2. 补齐四个测试

保留导师提供的正常输入示例，完成另外四个方法：

- `preservesInternalSpaces`：输入 `"  基本 信息  "`，断言返回值为 `"基本 信息"`。
- `rejectsNullName`：输入 null，检查 IllegalArgumentException 及消息 `group name must not be blank`。
- `rejectsEmptyName`：输入空字符串，检查相同异常及消息。
- `rejectsSpacesOnlyName`：输入只有空格的字符串，检查相同异常及消息。

初始模板中的四处 `fail("TODO...")` 是失败占位提示，需替换为真实断言。完成后应有 5 个测试，其中 1 个为导师示例、4 个为用户补充。仅删除 fail 留空方法不能证明规则正确。

#### 3. 运行与提交

用 IDEA 时将本章 project/pom.xml 作为独立 Maven 工程打开。在终端中：

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_4/project
mvn -B -ntp clean test
```

写好后运行测试，观察是否通过；卡住时直接说明问题。已运行过就无需为了补交材料再做一遍。保存代码后告知完成，测试方法、运行结果和报告由导师自行核验。

#### 4. 理解检查（续学时进行）

导师优先使用练习中出现的真实失败；若没有，便在临时副本构造一次违反业务规则的改动，展示关键现象。用户只需解释断言保护的规则及正确修复方向；这一步训练判断，不要求提交演示过程或抄写 expected/actual。

验收标准：导师确认 5 个测试实际执行并通过，4 个补充方法具有真实业务断言，三个非法输入同时检查异常类型与约定消息；用户能针对一次失败作出正确解释。已有充分依据的部分不重复提问。

辅助范围：配置、业务源码复用、测试类结构和一个示例由导师准备；用户补充四个测试方法。导师核验、演示与用户理解分别记录。

### 代码交付

代码直接保存在 [ValidationDemoTest.java](project/src/test/java/m00_2/ValidationDemoTest.java)。本题无需填写过程表；后续理解题的对话答案由导师归档。

### 验收反馈

导师模板预检：在临时副本中执行 Maven test，确认发现 5 个测试；正常输入示例通过，4 处 fail TODO 按预期失败，Errors=0、Skipped=0。配置与测试发现链路可工作，记录见 [模板预检](evidence/2026-11-01-scaffold-check.json)。这不是用户完成测试的证据。

2026-11-01 作业交付调整：撤销原日志与过程填写表，以及为提交材料反复改错、恢复的要求；源码核验和运行记录由导师负责。原表尚无用户填写内容，测试源码保留原样。理解检查尚未出题、未评分。

代码核验：已保存的四个补充测试均有有效断言，三个异常用例同时验证类型和消息，Jupiter 导入正确。导师在当前工程的临时副本中执行 `mvn -B -ntp clean test`，5 个测试全部通过，失败、错误、跳过均为 0；用户源码和原工程报告未改动。代码部分通过，下一步只需检查断言保护规则的理解，无需补填原表。

<a id="q02"></a>

## Q02 · 判断测试失败的修复方向

### 作业要求

导师在临时副本中改动名称处理逻辑，保留你的原有测试：

```java
assertEquals("基本 信息", ValidationDemo.requireGroupName("  基本 信息  "));
```

实际运行后，`preservesInternalSpaces` 失败：

```text
expected: <基本 信息> but was: <基本信息>
```

问题：为了修复这个失败，应该修改业务实现，还是把测试期望从“基本 信息”改为“基本信息”？用 1–2 句话说明依据。

直接在对话回答即可，无需查资料、运行或修改文件。本题检查能否依据业务规则判断修复方向。

### 我的解答

> 应该改业务实现，测试用例执行失败应该回到业务代码中去修改，而不是修改测试的期望来掩盖错误。

### 验收反馈

修复方向判断正确，本题通过。原业务约定为去掉首尾空格并保留名称中间的空格，因此此处应修正实现，保持测试期望“基本 信息”。用户明确反对为通过测试而掩盖错误；具体业务规则与以下边界由导师在反馈中补充，不记为用户原话：测试失败时先对照业务约定，如果测试本身写错或需求已经确认变更，应相应修正测试，并非一律修改业务实现。

演示来源：导师将临时副本中的 `name.trim()` 改为 `name.replace(" ", "")`，执行 `mvn -B -ntp clean test`：5 个测试中 1 个失败、0 个错误、0 个跳过。原工程源码与测试未修改；该运行只作为题面依据。结合 Q01 代码核验与本题回答，M00.4 本次验收通过，详见 [完整记录](../../../learning-records/0009-m00-4-acceptance.md)。

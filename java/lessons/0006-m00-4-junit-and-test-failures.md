# M00.4 · JUnit、断言与测试失败判断

所属单元：[M00.4](../ROADMAP.md#m00-4)。对应作业：[M00.4 章节文件](../practice/practice/m00_4/README.md)。

回补说明：整理当时的 JUnit 入门、异常断言、真实失败辨析和导师补充的判断边界。课件是复习材料，代码提交与运行证据仍保存在原章节和学习记录中。

## 学习目标与范围

把手动运行名称校验程序的检查变成可重复执行的测试，分别断言返回值、异常类型与异常消息；出现测试失败时依据业务规则判断修复方向。

本课使用无外部依赖的真实方法调用。Spring、数据库、Mock、复杂参数化测试及 TDD 方法论后续按需学习。

## 限定阅读

主资料：[JavaGuide · 单元测试到底是什么？应该怎么做？](https://javaguide.cn/system-design/basis/unit-test.html)，阅读约 10–15 分钟。

1. 从 [“何谓单元测试？”](https://javaguide.cn/system-design/basis/unit-test.html#何谓单元测试) 开始，完整阅读，到“为什么需要单元测试？”标题前停止。理解被测试的单元与依赖隔离，外链不展开。
2. 完整阅读 [“为什么需要单元测试？”](https://javaguide.cn/system-design/basis/unit-test.html#为什么需要单元测试) 及其五个小节，到“谁逼你写单元测试？”标题前停止，注意单测与集成测试的边界。
3. 跳到 [“单测框架和 Mock 工具如何选择？”](https://javaguide.cn/system-design/basis/unit-test.html#单测框架和-mock-工具如何选择)，只读开头两段，到比较两套方案的提问前停止。本题先不用 Mock。
4. 跳过“谁逼你写单元测试？”、TDD 章节与框架对比细节。

## 核心讲解与示例

### 测试直接调用方法，再验证业务规则

手动运行时，由人看输出是否合理。单元测试把输入和期望写成断言，交给框架反复执行；规则被后续改动破坏时，可以快速发现。

单测只保护它实际覆盖的局部行为。一个字符串校验方法通过测试，不等于已经验证多个组件的配合、配置、HTTP、数据库或事务。

本章的 [Maven 工程](../practice/practice/m00_4/project/pom.xml) 使用 JUnit Jupiter 5.13.4、test 范围依赖和 Surefire 3.5.4；主代码编译兼容目标为 Java 8，练习使用 JDK 21。src/main/java 保存业务代码，src/test/java 保存测试代码。

### Jupiter 的 Test 注解与普通断言

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
```

`@Test` 标记测试方法，由测试框架发现和执行；本题无需自己编写测试 main。JUnit 4 的 `org.junit.Test` 与 Jupiter 的导入不同，应和当前工程的依赖及测试引擎匹配。

当时提供的正常输入示例为：

```java
@Test
void trimsLeadingAndTrailingSpaces() {
    assertEquals("基本信息", ValidationDemo.requireGroupName("  基本信息  "));
}
```

`assertEquals(期望值, 实际值)`：期望来自业务约定，实际来自真实方法调用。只调用方法、不写有效断言，无法证明返回值符合规则。

### 用 assertThrows 检查约定异常

原始讲解用字符串转整数演示异常断言语法：

```java
NumberFormatException error = assertThrows(
    NumberFormatException.class,
    () -> Integer.parseInt("abc")
);
```

`() -> ...` 把待执行动作交给断言；框架执行这个动作并检查异常，返回的异常对象还能用 getMessage 读取消息。

这个 NumberFormatException 对应 Integer.parseInt 的示例。迁移到名称校验时，要按该方法的业务约定选择 IllegalArgumentException，同时让接收变量的类型匹配：

```java
IllegalArgumentException error = assertThrows(
    IllegalArgumentException.class,
    () -> ValidationDemo.requireGroupName(null)
);
assertEquals("group name must not be blank", error.getMessage());
```

不能只改 assertThrows 中的 class，却继续用不匹配的更具体异常类型接收结果。选错断言期望类型，可能在测试执行时失败；变量类型不匹配，则可能先在测试代码编译时失败。这是不同阶段的问题。

非法输入抛出约定异常时，异常测试可以通过，因为这正是测试要验证的行为。初始模板中的 fail TODO 会主动令测试失败；仅删除它并留下空方法，也不构成有效测试。

### 从业务规则选测试边界

本章规则是去掉名称首尾空格、保留内部空格，并拒绝非法名称：

| 场景 | 要保护的行为 |
| --- | --- |
| 有效名称带首尾空格 | 返回清理后的名称 |
| 名称内部有空格 | 内部空格仍保留 |
| null | 抛出约定类型与消息 |
| 空字符串 | 抛出约定类型与消息 |
| 只有空格 | 抛出约定类型与消息 |

其中一个正常用例由导师提供，其余四个测试由用户补充；具体代码见 [ValidationDemoTest.java](../practice/practice/m00_4/project/src/test/java/m00_2/ValidationDemoTest.java)。覆盖范围要结合真实断言判断，不能只看方法数量。

### 测试运行与失败判断

在使用本章练习 JDK 的终端中运行：

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m00_4/project
mvn -B -ntp clean test
```

本章预期发现 5 个测试。需要结合 Tests run、Failures、Errors、Skipped 以及具体失败方法判断结果；没有测试可运行与 5 个测试全部通过是不同情况。

当时导师在临时副本把首尾空格处理改为删除所有空格，保留原测试，得到：

```text
preservesInternalSpaces
expected: <基本 信息> but was: <基本信息>
```

这个失败暴露了实现违反“保留内部空格”的约定，应修正实现。把测试期望改成“基本信息”只是掩盖这次错误。

判断边界是先对照业务约定，而不是一律修改业务代码：测试本身写错，或需求已经明确变更时，测试也需要相应修正。导师补充的这个边界与用户原回答分别记录，不能混成同一份独立掌握证据。

## 本次练习

- [Q01](../practice/practice/m00_4/README.md#q01)：编写四个有效测试，覆盖内部空格和三种非法输入；完整题目和反馈保留在原文件。
- [Q02](../practice/practice/m00_4/README.md#q02)：根据业务规则解释失败，判断应修实现还是改期望。

按现有规范保存代码并告知完成，由导师读取与运行核验；不要求为整理课件反复改错、恢复或补贴过程日志。

## 验收与复习

能够解释每条断言保护的规则、期望值与实际值的来源，以及异常断言为什么可以因“正确抛出异常”而通过；看到失败后先回到规则判断修复方向。

历史验收与各方操作范围见 [M00.4 学习记录](../learning-records/0009-m00-4-acceptance.md)。本次补课件没有重新执行 Maven，也不产生新的测试通过或独立操作证据。

## 资料与关联

- [JUnit 5.13.4 官方指南](https://docs.junit.org/5.13.4/user-guide/#writing-tests-assertions)：导师补查断言；[异常断言](https://docs.junit.org/5.13.4/user-guide/#writing-tests-exceptions-expected-assertThrows) 用于核对 assertThrows，非额外必读课程。
- 上一课：[Maven 聚合与模块依赖](0005-m00-3-maven-modules-and-dependencies.md)。
- 下一课：[M01.1 可空字段、拆装箱与默认值](0001-m01-1-null-and-unboxing.md)。

# M01.1 · 类型、null 与数值边界

## 本章目标与交付

将前端输入中的可空字段转换为后端可使用的数据，处理拆箱空指针，并理解整数运算的边界。对应 [ROADMAP](../../../ROADMAP.md#m01-1)，正式状态见 [PROGRESS](../../../PROGRESS.md)。

本章分块进行。本次只做 Q01 的可空字段转换；整数除法、数值截断和越界将在后续练习中展开。保存代码并告知完成即可，核验与学习记录由导师处理。

## 本次课件与阅读

原始课件：[M01.1 · 可空字段、拆装箱与默认值](../../../lessons/0001-m01-1-null-and-unboxing.md)。

请先完成课件中的限定阅读与概念示例，再进行下方 Q01。阅读起止标题、TS 对照、拆装箱示例和补查依据统一保存在课件中；导师也会直接在当前会话呈现本次课件内容。

## 工程与索引

本章独立 [Maven 工程](project/pom.xml) 沿用 M00.4 的配置：JDK 21、Java 8 编译目标、JUnit Jupiter 5.13.4、Surefire 3.5.4。配置及测试由导师提供，不计学生实现。

- [Q01 · 修复 DTO 转换中的空值与默认值](#q01)
- [Q02 · 调换校验顺序会发生什么](#q02)

<a id="q01"></a>

## Q01 · 修复 DTO 转换中的空值与默认值

### 作业要求（约 25–40 分钟）

DTO 是 Data Transfer Object，即传输数据的对象。本题用最小数据类承接输入，暂用 public 字段避免引入 getter/setter 等下一节内容；尚未接入 HTTP、JSON 或 Spring。

- [GroupInput.java](project/src/main/java/m01_1/GroupInput.java)：输入字段为 Long entityId、Boolean enabled，可以表达缺失值。
- [GroupSettings.java](project/src/main/java/m01_1/GroupSettings.java)：转换后的字段为 long entityId、boolean enabled，要求值已确定。
- **需要修改：[GroupNormalizer.java](project/src/main/java/m01_1/GroupNormalizer.java) 的 normalize 方法。**
- [GroupNormalizerTest.java](project/src/test/java/m01_1/GroupNormalizerTest.java)：导师提供的业务约定测试，可阅读和运行。

当前 normalize 直接把包装类型字段赋给基本类型字段，且没有校验。请按以下约定修复：

1. input 为 null：抛 IllegalArgumentException，消息为 `input must not be null`。
2. entityId 为 null、0 或负数：抛 IllegalArgumentException，消息为 `entityId must be positive`；合法正数保持原值，不转换为 int。
3. enabled 为 null：转换为 true；明确提交 true 或 false 时保持原值。
4. 返回新的 GroupSettings 对象，保持输入对象不变。

可以查阅读材料并运行测试。类、方法签名与业务约定保持稳定，完成核心逻辑即可。当前版本的测试失败是练习起点，不需要为它补交错误记录。

```sh
cd /Users/yu/Desktop/yumengyuan/learn_everything/java/practice/practice/m01_1/project
mvn -B -ntp test
```

用 IDEA 时将本章 project/pom.xml 作为独立 Maven 工程打开。本题使用 `new GroupSettings()` 创建结果对象，再用 `对象.字段 = 值` 设置字段；构造器设计后续再讲。

### 交付与验收

保存 normalize 的实现后告诉导师“已完成”即可。有卡点时直接提问。导师读取代码，检查空值判断发生在拆箱之前、false 不被默认值覆盖、合法 long 值不丢失，并运行 6 个测试核验；无需填写方法列表、时间、目录或输出日志。

Q01 通过后再做一次有针对性的理解检查及数值边界练习；本题全部通过不等于本章全部通过。

### 验收反馈

Q01 代码验收通过。用户已保存 input 判空、entityId 校验与 enabled 默认值处理；明确的 false 被保留，合法 long 值未缩窄，返回新的结果对象且没有修改输入。实现按已提交代码记录，类型结构、初始错误实现和测试为导师提供。

导师模板预检：临时副本中执行 `mvn -B -ntp test`，发现 6 个测试，2 个通过、3 个断言失败、1 个空指针错误，跳过数为 0，符合初始版本缺少校验与默认值处理的预期。工程可编译、测试可发现；该结果不计用户练习证据。

本次提交核验：导师以 JDK 21 在当前工程的临时副本执行 `mvn -B -ntp clean test`，退出码 0，6 个测试全部通过，Failures=0、Errors=0、Skipped=0。用户源码与测试未修改；这是导师运行证据。Q02 理解检查及后续数值边界尚未完成，本章保持学习中。

<a id="q02"></a>

## Q02 · 调换校验顺序会发生什么

### 作业要求

保留前面的 input 判空逻辑，只把 entityId 的条件改成：

```java
if (input.entityId <= 0 || input.entityId == null) {
    throw new IllegalArgumentException("entityId must be positive");
}
```

假设 input 本身不为 null，但 input.entityId 为 null，这段代码会抛出这里写的 IllegalArgumentException 吗？用 1–2 句话说明执行过程。

只看题面判断，直接在对话回答；无需运行或修改原文件。

### 我的解答

> 不会，代码是自上往下同步执行的，这里会直接报空指针的错误

### 验收反馈

Q02 结果判断正确，通过。用户独立判断不会执行题面中的 IllegalArgumentException，而会先出现空指针异常。

导师补充更准确的机制：“自上往下同步执行”不足以描述同一表达式内的顺序；`||` 先求值左侧。`input.entityId <= 0` 会将 Long 拆箱为 long，可按 `input.entityId.longValue() <= 0` 理解。entityId 为 null 时，此处抛 NullPointerException，右侧判空及 if 分支体均不会执行。原实现把判空放在左边，null 时左侧为 true，`||` 短路跳过右侧比较。拆箱细节与短路解释为导师补充，不记为用户原话。

第一学习块的代码与结果判断通过；本章后续仍需整数除法、数值截断与边界练习。

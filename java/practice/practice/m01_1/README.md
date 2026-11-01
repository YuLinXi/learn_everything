# M01.1 · 类型、null 与数值边界

## 本章目标与交付

将前端输入中的可空字段转换为后端可使用的数据，处理拆箱空指针，并理解整数运算的边界。对应 [ROADMAP](../../../ROADMAP.md#m01-1)，正式状态见 [PROGRESS](../../../PROGRESS.md)。

本章分块进行。本次只做 Q01 的可空字段转换；整数除法、数值截断和越界将在后续练习中展开。保存代码并告知完成即可，核验与学习记录由导师处理。

## 本次限定阅读（15–20 分钟）

主资料：[JavaGuide · Java 基础面试题（上）](https://javaguide.cn/java/basis/java-basic-questions-01.html)。按下列顺序跳读，不从文章开头通读：

1. 从 **“Java 中的几种基本数据类型了解么？”** 开始，读完 **“基本类型和包装类型的区别？”**，到 **“包装类型的缓存机制了解么？”** 标题前停止。了解八种类型，重点辨认 int/Integer、long/Long、boolean/Boolean；无需背完整范围表。
2. 跳过缓存机制，完整阅读 **“自动装箱与拆箱了解吗？原理是什么？”**，到 **“为什么浮点数运算的时候会有精度丢失的风险？”** 标题前停止。重点看赋值触发转换和 null 拆箱风险，字节码指令只需浏览。
3. 跳到 **“⭐️ 成员变量与局部变量的区别？”**，读完这一节，到 **“静态变量有什么作用？”** 标题前停止。重点区分字段默认值与局部变量必须先赋值。

本次跳过包装缓存、浮点精度、BigDecimal、超过 long 范围的数据，以及后续方法章节；不展开文中外链。下一块再补整数运算。

## 先理解这几个区别

| Java 类型 | 可以取的值 | 能否为 null |
| --- | --- | --- |
| long | 64 位整数值 | 不能 |
| Long | long 的包装对象引用 | 可以 |
| boolean | true 或 false | 不能 |
| Boolean | true、false 或 null | 可以 |

对照 TypeScript：本题的输入可以近似理解为 `{ entityId: number | null; enabled?: boolean | null }`，但这只类比可空契约；Java long 的范围与 JS number 的整数精度不同。Java 没有 undefined，本题在手动创建输入对象时用 null 表示未提供；真实 JSON 如何映射留到 Web 章节验证。

```java
Long wrapped = 42L;   // long -> Long，装箱；L 表示 long 字面量
long value = wrapped; // Long -> long，拆箱

Boolean flag = null;
// boolean enabled = flag; // 若执行，会在拆箱时抛 NullPointerException
```

Boolean 的 null 与 false 含义不同，默认值处理应保留用户明确提交的 false。字段如 `public Boolean enabled;` 默认是 null；方法里的局部变量必须先赋值才能使用，不能套用字段的默认初始化规则。

导师核对依据：[JLS 5.1.8 拆箱](https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.8)、[JLS 4.12.5 初始值](https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.12.5)，无需另读规范。

## 工程与索引

本章独立 [Maven 工程](project/pom.xml) 沿用 M00.4 的配置：JDK 21、Java 8 编译目标、JUnit Jupiter 5.13.4、Surefire 3.5.4。配置及测试由导师提供，不计学生实现。

- [Q01 · 修复 DTO 转换中的空值与默认值](#q01)

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

待用户修改代码，尚未评分。类型结构、初始错误实现和测试均为导师提供，不作为用户独立代码证据。

导师模板预检：临时副本中执行 `mvn -B -ntp test`，发现 6 个测试，2 个通过、3 个断言失败、1 个空指针错误，跳过数为 0，符合初始版本缺少校验与默认值处理的预期。工程可编译、测试可发现；该结果不计用户练习证据。

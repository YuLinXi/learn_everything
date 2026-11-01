# M01.1 · 可空字段、拆装箱与默认值

所属单元：[M01.1 类型、null 与数值边界](../ROADMAP.md#m01-1)。
对应作业：[Q01 修复 DTO 转换中的空值与默认值](../practice/practice/m01_1/README.md#q01)。

## 学习目标与范围

将前端输入中的可空字段转换为后端可使用的数据，辨认拆箱空指针，并保留用户明确提交的布尔值。

本学习块只覆盖 Q01 的可空字段转换；整数除法、数值截断和越界将在本单元后续学习块展开。

## 限定阅读

主资料：[JavaGuide · Java 基础面试题（上）](https://javaguide.cn/java/basis/java-basic-questions-01.html)。预计 15–20 分钟，按下列顺序跳读，不从文章开头通读：

1. 从 **“Java 中的几种基本数据类型了解么？”** 开始，读完 **“基本类型和包装类型的区别？”**，到 **“包装类型的缓存机制了解么？”** 标题前停止。了解八种类型，重点辨认 int/Integer、long/Long、boolean/Boolean；无需背完整范围表。
2. 跳过缓存机制，完整阅读 **“自动装箱与拆箱了解吗？原理是什么？”**，到 **“为什么浮点数运算的时候会有精度丢失的风险？”** 标题前停止。重点看赋值触发转换和 null 拆箱风险，字节码指令只需浏览。
3. 跳到 **“⭐️ 成员变量与局部变量的区别？”**，读完这一节，到 **“静态变量有什么作用？”** 标题前停止。重点区分字段默认值与局部变量必须先赋值。

本次跳过包装缓存、浮点精度、BigDecimal、超过 long 范围的数据，以及后续方法章节；不展开文中外链。

## 核心讲解与示例

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

## 本次练习

完成 [M01.1 Q01](../practice/practice/m01_1/README.md#q01)：阅读题目中的输入、输出类型和业务约定，修复 normalize 方法的输入校验与默认值处理。

实现入口、测试和运行命令统一见章节作业；保存代码并告知完成即可，核验与记录由导师处理。

## 验收与复习

- 在拆箱前识别并处理可空输入，区分引用类型与基本类型。
- 默认值处理保留用户明确提交的 false，合法 long 值不因转换丢失。
- 能解释字段默认值与局部变量初始化要求的区别。

具体验收按 Q01 的要求执行；通过当前练习后，再由导师提出一道必要的理解检查。当前学习块不覆盖整个 M01.1 的验收范围。

## 资料与关联

导师补查依据：[JLS 5.1.8 拆箱](https://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.1.8)、[JLS 4.12.5 初始值](https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.12.5)，无需另读规范。

下一学习块补整数除法、数值截断和越界，具体范围按当前练习的验收结果确定。正式学习状态见 [PROGRESS.md](../PROGRESS.md)。

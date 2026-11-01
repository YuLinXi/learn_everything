# M00.1 · 沿 import 找到接口与实现

## 本章作业要求

目标：从 Controller 中的一次方法调用，找到它依赖的接口和对应实现，区分变量的声明类型与实现方法所在的位置。对应 [ROADMAP M00.1](../../../ROADMAP.md#m00-1)。

阅读范围：JavaGuide 的[多态](https://javaguide.cn/java/basis/java-basic-questions-02.html#多态)与[接口和抽象类](https://javaguide.cn/java/basis/java-basic-questions-02.html#⭐️-接口和抽象类有什么共同点和区别)，只看接口、实现关系和多态；暂缓抽象类设计题。

每题先读作业要求，再在该题的“我的解答”中填写并保存。讲解在终端进行；只作答导师当前指定的一道检查题。正式进度及当前待答题见 [PROGRESS](../../../PROGRESS.md)，组织规则见 [章节作业规范](../../README.md#章节作业规范)。

## 题目索引

- [Q01 · 源码定位图](#q01)
- [Q02 · 讲解后整理](#q02)
- [Q03 · 接口调用变式](#q03)
- [Q04 · 同名类型判断](#q04)

<a id="q01"></a>

## Q01 · 源码定位图

### 作业要求

1. 从 [AttributeGroupController.java](../../../projects/udc/udc-web/src/main/java/com/seeyon/udc/controller/AttributeGroupController.java) 的 `selectListByEntityId` 开始，只看相关 `import`、注入字段和这个方法。
2. 根据字段声明与 `import` 找到接口，再通过 `implements` 找到实现类中的同名方法。读到实现方法调用 Service 的位置即可。
3. 按“Controller → 接口 → 实现类”画文字定位图。每个节点写出完整包名、类或接口名、方法名、参数类型与返回类型；泛型参数写完整。

箭头表示源码定位顺序，接口声明不是额外执行的一层。本节不要求展开请求/响应包装类、Service 业务、SQL 或 Spring 运行时装配。

验收标准：三个节点的完整名称、方法名、参数类型与返回类型准确。

### 我的解答

① Controller：com.seeyon.udc.controller.AttributeGroupController

   方法：selectListByEntityId(Long entityId) → ListResponse&lt;AttributeGroupDto&gt;

   │

   ↓ 根据字段声明和 import 找到接口

② 接口：com.seeyon.udc.api.AttributeGroupAppService;

   方法：selectListByEntityId(SingleRequest&lt;Long&gt; entityId) → ListResponse&lt;AttributeGroupDto&gt;

   │

   ↓ 查找 implements 该接口的实现类

③ 实现类：com.seeyon.udc.appservice.AttributeGroupAppServiceImpl

   方法：selectListByEntityId(SingleRequest&lt;Long&gt; entityId) → ListResponse&lt;AttributeGroupDto&gt;

### 验收反馈

2026-11-01：已核对三个节点及类型；先前的方法名和括号笔误已由用户修正。

<a id="q02"></a>

## Q02 · 讲解后整理

### 作业要求

2026-11-01，用户表示不了解本题涉及的概念，先由导师在终端讲解。本题转为讲解后整理，可以参考讲解与源码，再用 2–4 句中文写下自己的理解。不要求默写完整包名、画新图或编写 Java 代码。

回答填写在本题的“我的解答”中，按讲解后的理解整理记录，不作为独立闭卷掌握证据。接续的独立理解检查见 [M00.1 变式作业](#q03)。

> 针对 `selectListByEntityId`，`AttributeGroupAppService` 接口与 `AttributeGroupAppServiceImpl` 实现类分别负责什么？请用 2–4 句中文说明两者的分工与实现关系。

可以按“接口负责……；实现类负责……；两者的关系是……”组织答案。不确定的地方直接写出来即可。

验收标准：接口约定与实现方法体的分工表述准确；本题按讲解后整理记录。

### 我的解答

<!-- 阅读讲解后，在这里用自己的话整理；本次已有导师讲解，不计独立闭卷回答。 -->

AttributeGroupAppService接口负责：声明有哪些方法，各自方法需要什么参数、返回什么类型。  
AttributeGroupAppServiceImpl实现类负责：实现接口的具体逻辑函数体。

### 验收反馈

2026-11-01：两句整理正确；已有完整讲解，按提示后回答记录。

<a id="q03"></a>

## Q03 · 接口调用变式

### 作业要求

目标：根据给定代码判断接口变量的一次方法调用，检查对接口与实现类关系的理解。对应 [ROADMAP M00.1](../../../ROADMAP.md#m00-1)，接续 [源码定位与讲解后整理](#q02)。本题不增加阅读材料。

- 可以看本题全部代码，先不运行代码、不回看之前的讲解。
- 这是一个独立示例，按本题给出的类型和方法定义判断。
- 只回答下方这一题，在“我的解答”填写一行输出和一句判断理由。
- 保存文件后告诉导师“已完成”，导师重新读取验证；若需要提示，可以直接说明。

#### 题目代码

```java
interface MessageSender {
    String send(String message);
}

class EmailSender implements MessageSender {
    @Override
    public String send(String message) {
        return "邮件：" + message;
    }
}

class SmsSender implements MessageSender {
    @Override
    public String send(String message) {
        return "短信：" + message;
    }
}

public class SenderDemo {
    public static void main(String[] args) {
        MessageSender sender = new EmailSender();
        sender = new SmsSender();
        System.out.println(sender.send("你好"));
    }
}
```

> 运行 `SenderDemo` 后，控制台打印什么？请用一句话说明你根据哪一处代码判断。

验收标准：输出与代码一致，判断理由能准确对应题面中的代码；未回答时保持未评分。

### 我的解答

输出：短信: 你好

判断理由：最终执行的是SmsSender实现类的对象，运行时最终调用的SmsSender中实现的send方法。

### 验收反馈

2026-11-01：方法调用的判断理由正确，概念检查通过。精确输出是 `短信：你好`，原答案中的冒号和空格差异已反馈；详见 [学习记录](../../../learning-records/0002-interface-dispatch.md)。

<a id="q04"></a>

## Q04 · 同名类型判断

### 作业要求

目标：根据源码确定一个字段声明使用的具体类型，完成 [M00.1](../../../ROADMAP.md#m00-1) 的最后一项检查。本题不增加阅读材料。

- 可以看下面全部题面，不需要背包名，也不需要运行代码。
- 只回答这一题，在下方填写完整类型名和判断依据。
- 保存后告诉导师“已保存”，由导师重新读取验证。

#### 题目

工程中同时存在以下两个类型，它们的简单名称都叫 `AttributeGroupAppService`：

```text
① com.seeyon.udc.api.AttributeGroupAppService
② com.seeyon.udc.appservice.AttributeGroupAppService
```

下面是 Controller 的相关代码，已省略与本题无关的注解和方法：

```java
package com.seeyon.udc.controller;

import com.seeyon.udc.api.AttributeGroupAppService;

public class AttributeGroupController {
    private AttributeGroupAppService attributeGroupAppService;
}
```

> 这里的 `attributeGroupAppService` 字段使用的是哪个完整类型名？请引用题面中的一行代码，并用一句话说明为什么能排除另一个同名类型。

验收标准：完整类型名正确，理由明确对应源码，能够排除另一个同名类型；未回答时保持未评分。

### 我的解答

完整类型名：com.seeyon.udc.api.AttributeGroupAppService

判断依据（代码行与一句说明）：import com.seeyon.udc.api.AttributeGroupAppService; 明确引用了包名

### 验收反馈

2026-11-01：通过。完整类型名与 `import` 依据正确。这里显式导入的是 `com.seeyon.udc.api.AttributeGroupAppService`，另一个类型属于不同包，不是该字段使用的导入类型。M00.1 本节验收通过，正式记录见 [PROGRESS](../../../PROGRESS.md)。

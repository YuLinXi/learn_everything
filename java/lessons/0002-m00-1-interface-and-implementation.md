# M00.1 · 沿 import 找到接口与实现

所属单元：[M00.1](../ROADMAP.md#m00-1)。对应作业：[M00.1 章节文件](../practice/practice/m00_1/README.md)。

回补说明：根据此前会话讲解、源码定位和讲解后的补充整理为 Markdown 课件；用户答案与验收结论仍保存在原作业及学习记录中。

## 学习目标与范围

看到 Controller 中的一次方法调用，能根据字段声明、import 和 implements，找到具体接口与实现方法，分清声明类型和实际对象。

本课只追到实现类调用 Service 的位置；Spring 如何选择 Bean、权限拦截、SQL 和请求包装类内部实现留到后续单元。

## 限定阅读

主资料：[JavaGuide · Java 基础（中）](https://javaguide.cn/java/basis/java-basic-questions-02.html)。

- 从 [“多态”](https://javaguide.cn/java/basis/java-basic-questions-02.html#多态) 开始，只读多态的含义及父类型引用具体对象的例子；在下一主题前停止，不展开外链。
- 从 [“接口和抽象类有什么共同点和区别？”](https://javaguide.cn/java/basis/java-basic-questions-02.html#⭐️-接口和抽象类有什么共同点和区别) 开始，读完该问答，到下一问答前停止。重点看接口约定与实现关系，暂缓抽象类设计题。
- 阅读目的：能够解释“变量写的是接口名，为什么运行时执行实现类的方法”。

## 核心讲解与示例

### 接口约定能力，实现类提供方法体

先约定一种“发送消息”的能力：

```java
interface MessageSender {
    void send(String message);
}
```

本例的接口方法声明以分号结束：方法名是 send，参数是 String，返回类型 void 表示不返回结果。声明约定如何调用，没有给出发送消息的具体步骤。

```java
class ConsoleSender implements MessageSender {
    @Override
    public void send(String message) {
        System.out.println(message);
    }
}
```

`implements MessageSender` 明确声明实现关系；方法的大括号里才是实际执行的代码。`@Override` 让编译器检查方法是否正确覆盖了父类型中的方法。

在 main 等方法中使用：

```java
MessageSender sender = new ConsoleSender();
sender.send("你好");
```

| 代码部分 | 含义 |
| --- | --- |
| MessageSender | 变量的声明类型，编译器据此检查允许调用的方法 |
| sender | 引用变量名 |
| new ConsoleSender() | 创建具体对象，交给变量引用 |
| sender.send(...) | 在这个例子中执行 ConsoleSender 提供的方法体 |

接口变量的声明类型不会因为重新赋值而改变，但它当前引用的对象可以改变。判断调用哪个实例方法实现，要同时看声明关系和最后引用的对象，不能只看变量名。

可以借用 TS 的 interface 理解“能力约定”。差异是 Java 的类需要显式声明 implements；一个无关类即使恰好有同名方法，也不会自动成为该接口的实现。

### 公司源码中的定位顺序

Controller 的相关声明是：

```java
import com.seeyon.udc.api.AttributeGroupAppService;

private AttributeGroupAppService attributeGroupAppService;
```

前面的 AttributeGroupAppService 是类型，后面的 attributeGroupAppService 是字段名。这条字段声明本身不创建对象；项目中的注入机制以后再展开。

本次查询的三个位置为：

| 位置 | 完整名称与方法契约 |
| --- | --- |
| [Controller](../projects/udc/udc-web/src/main/java/com/seeyon/udc/controller/AttributeGroupController.java) | com.seeyon.udc.controller.AttributeGroupController；selectListByEntityId(Long) → ListResponse&lt;AttributeGroupDto&gt; |
| [接口](../projects/udc/udc-biz/src/main/java/com/seeyon/udc/api/AttributeGroupAppService.java) | com.seeyon.udc.api.AttributeGroupAppService；selectListByEntityId(SingleRequest&lt;Long&gt;) → ListResponse&lt;AttributeGroupDto&gt; |
| [实现类](../projects/udc/udc-biz/src/main/java/com/seeyon/udc/appservice/AttributeGroupAppServiceImpl.java) | com.seeyon.udc.appservice.AttributeGroupAppServiceImpl；implements 上述接口，同名方法提供方法体 |

```text
Controller
  │ 根据字段声明和 import 定位
  ▼
api.AttributeGroupAppService 接口
  │ 根据 implements 查找实现
  ▼
AttributeGroupAppServiceImpl
  └─ 取出请求中的 ID，调用 attributeGroupService，包装返回结果
```

箭头表示阅读源码的定位顺序。接口声明不是一次额外执行的业务调用。Controller 把 Long 包装为 SingleRequest，接口与实现使用相同的请求和返回契约。

### 同名类型要看完整包名

工程还存在 `com.seeyon.udc.appservice.AttributeGroupAppService`。它与 api 包下的接口简单名称相同，但属于不同类型。判断这里使用哪一个，依据是 Controller 的显式 import，而不是全局搜索时首先看到的文件。

本课源码基线为 UDC 提交 `a182384764a0d50c196dd8ff5ae36411b7ba59a7`；回补时已核对上述 import、字段、接口签名和实现关系。

## 本次练习

历史练习与原答案保留在章节文件：

- [Q01 源码定位](../practice/practice/m00_1/README.md#q01)：根据源码识别三个位置及方法契约。
- [Q02 讲解后整理](../practice/practice/m00_1/README.md#q02)：用自己的话说明接口与实现的分工。
- [Q03 接口调用变式](../practice/practice/m00_1/README.md#q03)：根据变量最后引用的对象判断调用实现。
- [Q04 同名类型判断](../practice/practice/m00_1/README.md#q04)：根据 import 排除另一个同名类型。

关系图由导师生成并讲解，复习时通过简短判断或代码实践检查理解；无需重新手画定位图。

## 验收与复习

复习时检查三个关键点：声明类型约束什么、实际对象决定本例的哪个方法体、同名类型根据哪行源码区分。题目一次检查一道，原题与反馈以章节文件为准。

本课是静态源码与概念学习，不据此推定已经验证 Spring 运行时装配。正式验收范围见 [M00.1 学习记录](../learning-records/0003-m00-1-acceptance.md)。

## 资料与关联

- [公司工程地图](../PROJECT_MAP.md#链路-audc-字段分组)：继续追踪 Service 与 DAO 时使用。
- [接口调用变式记录](../learning-records/0002-interface-dispatch.md)：区分独立变式回答与讲解后整理。
- 下一课：[JDK、编译运行与调试](0003-m00-2-jdk-compile-and-debug.md)。

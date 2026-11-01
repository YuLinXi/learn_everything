# M00.1：源码定位、接口调用与同名类型判断通过

2026-11-01，用户提交 [Q04 同名类型判断](../practice/practice/m00_1/README.md#q04)：

> 完整类型名：com.seeyon.udc.api.AttributeGroupAppService
>
> 判断依据（代码行与一句说明）：import com.seeyon.udc.api.AttributeGroupAppService; 明确引用了包名

类型名和显式导入的依据正确，能够排除另一个包中的同名类型。Q04 按允许查看题面的独立源码判断记录，未要求闭卷默写包名。

**Evidence**：本章 [Q01](../practice/practice/m00_1/README.md#q01) 已完成三节点源码定位，完整名称、方法参数和返回类型已核对，书写笔误已修正；[Q02](../practice/practice/m00_1/README.md#q02) 为完整讲解后整理；[Q03](../practice/practice/m00_1/README.md#q03) 在另一段代码上正确判断调用的实现，详见 [独立变式记录](0002-interface-dispatch.md)；Q04 正确根据 import 判断同名类型。

结论：达到 ROADMAP M00.1 的静态源码定位与概念解释验收要求，本节通过。保留先讲解再完成变式的学习过程，不把 Q02 改记为独立闭卷回答；本节不包含编译运行验收，未声称执行过公司构建或验证 Spring 运行时装配。

复查初始安排：2026-11-02、2026-11-04、2026-11-08、2026-11-15。尚未复查，后续根据结果调整。

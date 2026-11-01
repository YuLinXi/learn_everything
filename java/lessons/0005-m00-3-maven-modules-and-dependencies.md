# M00.3 · Maven 聚合、继承与模块依赖

所属单元：[M00.3](../ROADMAP.md#m00-3)。对应作业：[Q02 UDC 多模块讲解](../practice/practice/m00_3/README.md#q02)、[Q03 聚合与依赖判断](../practice/practice/m00_3/README.md#q03)。

回补说明：根据当时的终端讲解和静态 POM 关系整理；图表由导师生成，原提交和口头回答仍保存在章节文件中。

## 学习目标与范围

能分别回答“继承谁的配置”“聚合哪些模块构建”“当前模块实际依赖谁”，用具体 POM 判断关系。

本块只读 UDC 的配置关系，重点看根 POM、web、biz、test、assemble；不展开全部插件、私服认证或运行时服务装配。

## 限定阅读

主资料：[JavaGuide · Maven 多模块管理](https://javaguide.cn/tools/maven/maven-core-concepts.html#maven-多模块管理)。

从“Maven 多模块管理”标题开始，完整阅读该节，包含 Dubbo 示例图；看到下一标题“文章推荐”时停止。阅读目的是把聚合构建与项目之间的依赖区分开，图不要求手工重画。

## 核心讲解与示例

### 三个配置分别回答三个问题

| 配置 | 回答的问题 | 阅读入口 |
| --- | --- | --- |
| parent | 当前项目继承谁的公共配置？ | 当前模块的 parent 元素 |
| modules | 当前项目把哪些子模块聚合起来构建？ | 聚合项目的 modules 列表 |
| dependencies | 当前模块直接声明了哪些依赖？ | 当前模块的 dependencies 列表 |

一个根 POM 可以同时充当聚合者和子模块的父项目，但这两种作用来自不同配置。被放进同一个 modules 列表，不会自动让兄弟模块互相产生直接依赖。

### UDC 的聚合与继承

源码基线：`projects/udc` 的 `test` 分支，提交 `a182384764a0d50c196dd8ff5ae36411b7ba59a7`。回补时静态核对仍是这个提交。

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

[根 POM](../projects/udc/pom.xml) 的 modules 包含这六个子模块；它自己的 parent 为 `com.seeyon:udc-parent:6.0.0-TEST-SNAPSHOT`。

子模块的 parent 指向 `com.seeyon:udc:${udc.version}`。`${udc.version}` 是属性引用，坐标字段之间用冒号分隔；定位声明时保留这个属性表达式，不凭目录名猜版本值。

这个工程里，根 udc 既聚合这些模块，也被它们声明为 parent。目录树看起来相同，不意味着聚合与继承是同一个概念。

### 四个指定子模块之间的直接依赖

下图 A → B 表示 A 的 dependencies 中直接声明 B，范围限定为 web、biz、test、assemble 之间的关系：

```text
udc-assemble ──→ udc-web ──→ udc-biz
udc-test     ──→ udc-web
udc-test     ──→ udc-biz
```

| 直接依赖 | 配置依据 | scope |
| --- | --- | --- |
| web → biz | [udc-web/pom.xml](../projects/udc/udc-web/pom.xml) 的 dependencies | 未显式写，使用默认 compile |
| test → web | [udc-test/pom.xml](../projects/udc/udc-test/pom.xml) 的 dependencies | 未显式写，使用默认 compile |
| test → biz | 同一份 udc-test POM 的另一条 dependency | 未显式写，使用默认 compile |
| assemble → web | [udc-assemble/pom.xml](../projects/udc/udc-assemble/pom.xml) 的 dependencies | 未显式写，使用默认 compile |

例如，要判断 web 是否直接依赖 biz，查看 web 的 POM，找到 groupId 为 com.seeyon、artifactId 为 udc-biz 的 dependency。仅看到根 modules 同时列出两者，信息不够。

assemble 可以经过 web 形成到 biz 的传递路径；这不等于 assemble 自己直接声明了 biz。模块名叫 udc-test，也不能据此断定其所有依赖的 scope 都是 test。

### 父项目源码与父 POM 构件

当时在当前工作区没有找到 udc-parent 的项目源码。这个事实只限定源码目录的覆盖范围；Maven 仍可能从本地或远程仓库获得其 POM。

因此，“没找到源码仓库”“无法解析父 POM”“公司工程构建失败”是需要分别取证的结论。本块静态读图不新增仓库访问、编译或运行成功的证据。

## 本次练习

- [Q02](../practice/practice/m00_3/README.md#q02)：看导师给出的聚合、继承与直接依赖图，结合 POM 定位理解配置。
- [Q03](../practice/practice/m00_3/README.md#q03)：说明为什么 modules 不能证明 web 对 biz 的直接依赖，并指出应查看的配置。

关系图、源码查询和逐边核对由导师完成；用户通过简短判断或代码实践证明理解。历史中已撤销的画图和批量填表要求不作为新的作业要求。

## 验收与复习

能够根据问题选择 parent、modules 或 dependencies，并区分直接声明与传递路径；复习时用一条具体关系做判断即可。

原口头回答及证据范围见 [聚合与依赖学习记录](../learning-records/0007-maven-aggregation-dependency.md)，整个单元结论见 [M00.3 验收记录](../learning-records/0008-m00-3-acceptance.md)。

## 资料与关联

- [公司工程地图](../PROJECT_MAP.md)：跨仓库定位时的已知路径与边界。
- 本单元前一块：[Maven 构建与生命周期](0004-m00-3-maven-build-and-lifecycle.md)。
- 下一课：[JUnit 与测试失败判断](0006-m00-4-junit-and-test-failures.md)。

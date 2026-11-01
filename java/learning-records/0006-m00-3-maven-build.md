# M00.3：最小 Maven 工程构建与打包运行

2026-11-01，用户完成 [Q01](../practice/practice/m00_3/README.md#q01) 的源码迁移、编译、测试阶段观察与打包运行。POM 由导师提供，源码复用用户 M00.2 实现；操作及日志由用户提交，按脚手架上的实际实践记录。

**Evidence**：

- 首次操作时间 2026-11-01 14:13:42 CST，坐标与 jar 类型正确；compile 成功，test 报告 No tests to run，当前实际未运行测试。
- 首次 package 记录为 BUILD FAILURE，未保存 ERROR 明细；当时未生成 JAR，后续 java 报 ClassNotFoundException。此失败记录保留，根因未查明。
- 导师重试同一 package 命令成功，检查 JAR 和运行成功，未修改源码、POM 或设置；日志与 JSON 为导师证据，单独存放在章节 evidence/。
- 用户随后提交 2026-11-01 14:36:39 CST 的重试记录，package 成功；JAR 列表中存在 `m00_2/ValidationDemo.class`；运行输出为 `基本信息`。这是用户重新提交的操作证据。

反馈区分了 JAR 文件路径、JAR 内部 class 路径和入口类全名，并说明 Nothing to compile 属于已是最新的正常结果、No tests to run 不是测试用例通过。后者曾由导师讲解，不把只粘贴日志升级为独立概念掌握证明。

结论：Q01 操作验收通过。原打包失败本轮未复现，未推定为网络、仓库或源码问题。M00.3 仍需完成公司多模块 POM 定位及失败类型的概念检查，整单元尚未通过。

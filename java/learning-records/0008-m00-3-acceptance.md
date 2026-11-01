# M00.3：Maven 构建与失败类型验收

2026-11-01，用户对 [Q04 教学日志分类](../practice/practice/m00_3/README.md#q04) 回答：

```text
A = 编译错误 + [ERROR] Compilation failure
B= 依赖下载失败 + [ERROR] Could not resolve dependencies for project com.example:demo:jar:1.0
C= 测试失败 + [ERROR] Tests run: 3, Failures: 1, Errors: 0, Skipped: 0
```

三项分类正确。导师补充 B 的更具体依据是 transfer artifact 和 Connection timed out，resolve dependencies 本身只说明解析失败，并不单独证明一定是下载超时。

**Evidence**：

- [Q01 实践](0006-m00-3-maven-build.md)：用户在 AI POM 与上一节源码基础上组织 Maven 工程，提交 compile、test、package 及 JAR 运行记录。首次打包失败与后续成功都保留；真实项目没有测试可运行，尚未编写测试用例。
- Q02：用户填出的模块基本信息与 modules 列表正确。按用户新要求，关系图、逐边依据和父 POM 源码查询结论由导师生成讲解，未把这部分材料计为用户独立调查或制图证据。
- [Q03 口头回答](0007-maven-aggregation-dependency.md)：用户准确说明 modules 是聚合，判断 web 对 biz 的直接依赖应看 web 的 POM。
- Q04：用户将三类失败日志分类正确；本题答案未提前提供，具体定位提示在作答后补充。教学日志中的 Tests run: 3 不代表本工程真实运行了 3 个测试。

结论：结合实际构建、关系解释与失败分类，M00.3 本次验收通过。公司父 POM 的源码与仓库可解析性只作边界说明，本次没有执行公司构建或验证私服。原打包失败未复现且缺少 ERROR 明细，根因仍未查明。

首次复查为 2026-11-02，后续初始排程为 2026-11-04、2026-11-08、2026-11-15，尚未进行。下一节 M00.4 将建立真实、可重复的单元测试。

# 智火星传

红色文化学习与交流平台的开源原型。项目采用 Vue 3 + TypeScript 前端与 Java 17 + Spring Boot 3.4 后端，基于芋道开源框架进行功能开发。

本仓库提供整理后的源代码、空库表结构和配置说明。历史服务地址、真实业务记录、个人资料、密钥和未经核实授权的展示媒体不随代码发布。公开版界面的部分图片使用中性占位图。

## 演示

[![虚拟展厅演示](docs/images/red-culture-gallery-demo.jpg)](https://github.com/myheart521/zhihuoxingchuan/releases/download/v1.0.0-source/red-culture-gallery-demo.mp4)

[项目与 PPT 材料整理](docs/PROJECT.md) · [获奖记录](docs/AWARDS.md) · [下载演示视频](https://github.com/myheart521/zhihuoxingchuan/releases/download/v1.0.0-source/red-culture-gallery-demo.mp4)

## 获奖记录

以下是项目历次参赛中已核实的获奖记录，年份、赛段和等级按保存的证书及参赛材料整理。

| 年份 | 赛事与赛道 | 赛段 / 赛区 | 奖项 |
| --- | --- | --- | --- |
| 2025 | 第二十七届中国机器人及人工智能大赛 · 智能文化创意创新赛 | 全国总决赛 | 一等奖 |
| 2025 | 第十八届中国大学生计算机设计大赛 · 微课与 AI 辅助教学 | 河南省级赛 | 三等奖 |
| 2025 | 第二十七届中国机器人及人工智能大赛 · 新质生产力创新设计专项赛 | 省级层次选拔赛 | 优秀奖 |

公开记录不列参赛作品题名、团队名单和证书编号，原始证书也不上传。各条记录的赛段说明见 [AWARDS.md](docs/AWARDS.md)。

## 功能与实现范围

| 模块 | 仓库中的实现 |
| --- | --- |
| 内容学习 | 红色文章、分类标签、评论、点赞、轮播与实践投稿相关页面和后端接口 |
| 知识测评 | 题目、答题结果、历史记录与基于成绩的基础建议 |
| 展示交流 | 虚拟展厅页面、实时群聊、历史消息管理 |
| 智能扩展 | AI 学习建议调用层，以及继承的模型管理、知识库与文档处理代码 |

原型包含外部三维展厅入口；公开版已移除原部署链接，展厅内容及资源需自行配置。AI 个性化建议默认关闭，测评服务保留原有基础建议回退。

这些功能描述以可见代码为依据。继承的后台框架还包含权限、系统管理、基础设施和报表等模块，前端也保留部分可选业务页面；页面存在不表示所有可选后端模块均已启用。RAG、向量检索和多模型接入需要额外服务、模型与配置，不能直接视为开箱即用的完整应用。

## 目录

```text
frontend/       Vue 3、Vite、Element Plus 前端
backend/        Spring Boot Maven 多模块工程
database/       仅含 CREATE TABLE 的空库表结构
docs/           配置、功能边界与发布说明
licenses/       保留的上游许可证
```

后端保留本原型启用的 blog、system、infra、report、ai 和 framework 模块。代码包名统一为 `com.example.project`，Maven 坐标使用 `com.example.boot`，以移除原有个人或机构标识。

## 开发准备

需要 Java 17、Maven 3.8+、Node.js 18+、pnpm 8+、MySQL 8 和 Redis。向量数据库、对象存储、邮件、短信与模型服务均为按需配置项。

1. 创建新的本地 MySQL 数据库 `zhihuoxingchuan`，审核并导入 `database/schema.sql`。
2. 按 [配置文档](docs/CONFIGURATION.md)设置后端环境变量。后端不会自动读取 `.env` 文件。
3. 在 `frontend` 内复制 `.env.example` 为 `.env.local`，确认 API 指向自己的后端。
4. 安装前端依赖并编译；编译后端。整个配置过程中不需要任何原作者的服务账号。

```sh
cd frontend
cp .env.example .env.local
pnpm install --frozen-lockfile
pnpm build
pnpm dev
```

PowerShell 使用 `Copy-Item .env.example .env.local`。后端另开终端运行：

```sh
cd backend
mvn -DskipTests package
java -jar project-server/target/project-server.jar
```

实际 jar 文件名以 Maven 构建产物为准。后端默认端口为 `48080`，前端为 `5173`。完整启动还取决于本地数据库初始化与可选模块配置。

**初始化限制：** 表结构不含历史数据、账户、角色授权、菜单种子、字典、题库或存储配置。需要自行建立授权账户及必要的系统配置，并核对原型后续新增实体与表结构是否一致。仓库没有内置管理员密码，也没有提供已经完成端到端验收的初始化向导。

## AI 与外部资源

浏览器中的原模型密钥和直接供应商调用已移除。`VITE_AI_ENABLED=false` 为默认值，建议服务在未配置时使用页面已有的基础建议或给出明确错误。启用前需自行实现具备登录鉴权、配额和输入校验的后端适配器，接口约定见 [配置文档](docs/CONFIGURATION.md#ai-适配器约定)。供应商密钥只能保存在服务端。

文件上传默认经过后端。对象存储桶、地域、访问密钥与外部媒体链接需要由部署者重新配置；仓库中的 `example.invalid` 为无效占位域名。

## 验证与限制

发布整理使用离线 Maven 工程校验、静态资源/配置检查与密钥扫描；具体构建结果和已知问题见 [验证记录](docs/VERIFICATION.md)。未连接原数据库、Redis、OSS 或模型服务进行端到端测试。请在自己的隔离环境中完成数据库初始化、权限、上传、聊天和检测场景验证后再部署。

## 许可证与来源

上游 `ruoyi-vue-pro` 和 `yudao-ui-admin-vue3` 的 MIT 许可证完整保留在 `licenses/`。依赖仍遵循各自许可证，详见 [第三方说明](THIRD_PARTY_NOTICES.md)。本仓库不附带商业服务使用权、第三方媒体授权或原业务数据使用权。

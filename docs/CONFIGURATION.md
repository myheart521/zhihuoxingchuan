# 配置与本地初始化

## 后端环境

`backend/project-server/src/main/resources/application.yaml` 保留框架默认设置，`application-local.yaml` 提供本地数据服务配置。配置项通过 Spring 的 `${ENV_VAR:default}` 语法读取进程环境。`backend/.env.example` 仅为清单，不会由 Spring Boot 自动加载。

| 变量 | 用途 | 默认或要求 |
| --- | --- | --- |
| `DB_URL` | JDBC 连接串 | 本机 MySQL、数据库 `zhihuoxingchuan` |
| `DB_USER` / `DB_PASSWORD` | 数据库认证 | 必须自行设置；无默认管理员密码 |
| `REDIS_HOST` / `REDIS_PORT` | Redis 地址 | `127.0.0.1` / `6379` |
| `REDIS_DATABASE` | Redis 数据库编号 | `0` |
| `REDIS_PASSWORD` | Redis 认证 | 空；按自己的 Redis 配置设置 |
| `SERVER_PORT` | HTTP 端口 | `48080` |
| `SPRING_PROFILES_ACTIVE` | Spring 配置分组 | `local` |
| `LOG_FILE` | 本地日志输出 | `./logs/application.log` |

例如在 PowerShell 中设置 `$env:DB_USER='local_app'`，并从本地安全配置或终端输入提供 `DB_PASSWORD`。不要把实际值写入 Git 跟踪文件。后端示例的 `AI_API_KEY` 与 `OSS_*` 是部署清单项，现有代码不会自动把这些变量映射到全部第三方适配器；应在自己的 Spring 配置或后台受控配置中完成映射。

## 数据库

`database/schema.sql` 从原 SQL 中仅提取建表语句，并移除原数据、表注释、数据库主机、账户与自增计数。包含 88 个表定义。不要将原 SQL dump 或用户上传目录复制进仓库。

建表之后，需要根据业务选择初始化系统账户、角色、权限、菜单、字典、题目与对象存储配置。框架和原型版本之间可能存在表结构差异，导入前应对照相关 `dal/dataobject` 实体核对。当前仓库不提供默认账号，也没有声称建表后即可完整登录。

## 前端环境

所有 `VITE_*` 都可能进入浏览器构建产物，因此只能设置公开配置。

| 变量 | 用途 |
| --- | --- |
| `VITE_APP_TITLE` | 页面标题 |
| `VITE_BASE_URL` | 自己的后端 HTTP 地址 |
| `VITE_API_URL` | API 前缀，默认 `/admin-api` |
| `VITE_UPLOAD_URL` | 自己的后端上传接口 |
| `VITE_UPLOAD_TYPE` | 默认 `server`，由后端处理上传 |
| `VITE_PORT` | 开发端口 |
| `VITE_AI_ENABLED` | 默认 `false`；仅在适配器完成后开启 |
| `VITE_AI_PROXY_PATH` | 相对 API 路径；默认 `/admin-api/ai/publication/chat` |

前端不存储记住登录密码；记住登录只保留账户信息。原第三方统计标识和默认账号已清空。

## AI 适配器约定

公开版改造的是前端调用层，**没有新增实现 `publication/chat` 的后端控制器**。集成方可以复用已有后端模型服务实现适配器，也可以替换前端调用层以匹配自己的安全接口。

客户端通过现有 `request` 封装发送 POST：

```json
{"messages":[{"role":"user","content":"请提供学习建议"}]}
```

服务端应按照本项目 `CommonResult` 协议返回成功状态及 `data.content` 字符串。当前客户端配置的成功码为 `200`，部署者需与自己的后端约定保持一致。例如：

```json
{"code":200,"data":{"content":"这里是模型生成的建议"},"msg":""}
```

`request` 已负责应用登录令牌；模型 API key 不能出现在请求体、浏览器环境变量或前端源码。适配器应在服务端校验登录状态、限制请求长度与频率、控制模型选择和配额，并处理超时。禁止根据客户端任意 URL 直接转发请求。

基础建议与规则并不需要供应商密钥。AI 服务不可用时应保持错误提示或回退，避免把模拟输出标为真实模型结果。

## Redis、OSS 与其他服务

Redis 只连接部署者配置的实例。向量索引的自动初始化在公开配置中关闭；启用 Qdrant、Milvus 或 Redis 向量功能时，应自行创建隔离集合并配置连接。

对象存储通过后台文件配置与后端客户端接入。`OSS_ENDPOINT`、`OSS_BUCKET`、`OSS_ACCESS_KEY`、`OSS_SECRET_KEY` 需映射到自己的存储配置；公开仓库不包含桶名、访问凭据或历史对象路径。默认上传使用后端接口。

短信、邮件、社交登录、报表以及其他可选框架能力需要分别配置。默认配置关闭部分外部注册、定时任务和模拟登录能力。相关页面的保留不表示已连接相应服务。

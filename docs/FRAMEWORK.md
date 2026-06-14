# 电商购物平台 — 项目框架说明

本文档说明仓库目录与各文件职责，便于对照 `zhi dao` 与 `requirements` 维护代码。

## 一、目录总览

```
电商购物平台/
├── pom.xml                          # Maven 依赖与构建
├── requirements                     # 业务与技术需求
├── zhi dao                          # Cursor 开发指令（生成顺序与规范）
├── FRAMEWORK.md                     # 本文件：框架说明
├── src/main/java/com/ecommerce/    # 后端 Java 源码
├── src/main/resources/              # 配置、SQL、MyBatis XML
└── frontend/                        # Vue3 前端
```

## 二、后端核心文件

| 文件 | 作用 |
|------|------|
| `EcommerceApplication.java` | Spring Boot 启动入口；`@MapperScan` 扫描 Mapper；开启事务 |
| `application.yml` | 端口、MyBatis、JWT、AI 开关 |
| `application-sqlite.yml` | 默认 SQLite 数据源与 `schema.sql` 初始化 |
| `application-mysql.yml` | MySQL 切换配置（改 `spring.profiles.active`） |
| `schema.sql` / `schema-mysql.sql` | 建表：用户、商品、购物车、订单、评价 |
| `common/Result.java` | 统一 API 返回 `{code, message, data}` |
| `common/PageResult.java` | 分页结构 |
| `exception/BusinessException.java` | 业务异常 |
| `exception/GlobalExceptionHandler.java` | 全局转 JSON 错误 |
| `interceptor/RequestLoggingInterceptor.java` | 请求/响应日志与耗时 |
| `interceptor/CorsInterceptor.java` | 跨域头（开发联调） |
| `config/WebMvcConfig.java` | 注册拦截器、静态资源 `/uploads/**` |
| `config/ShiroConfig.java` | Shiro 过滤器链：公开路径 + JWT |
| `config/DataInitializer.java` | 首次启动创建 admin/admin123 |
| `shiro/JwtFilter.java` | 从 Header 解析 Bearer Token |
| `shiro/JwtRealm.java` | 校验 JWT 并写入角色 |
| `util/JwtUtils.java` | 生成/解析 JWT |
| `util/UserContext.java` | 获取当前用户 ID、是否管理员 |
| `util/AiDescriptionHelper.java` | AI 描述**假实现**（模板文案） |
| `entity/*` | 与表对应的 POJO |
| `mapper/*` + `resources/mapper/*.xml` | MyBatis 数据访问（SQL 兼容 SQLite） |
| `service/*` + `service/impl/*` | 业务接口与实现 |
| `controller/*` | REST API，全部返回 `Result` |

## 三、API 模块对照

| 模块 | 路径前缀 | 说明 |
|------|----------|------|
| 认证 | `/api/auth` | 注册、登录（返回 JWT） |
| 商品 | `/api/products` | 列表/详情公开；增删改需管理员 Token |
| 购物车 | `/api/cart` | 需登录 |
| 订单 | `/api/orders` | 提交、我的订单；`/admin` 管理员列表 |
| 评价 | `/api/reviews` | 发表需登录；按商品查询公开 |
| 上传 | `/api/upload/image` | 管理员上传图片 |

## 四、前端核心文件

| 文件 | 作用 |
|------|------|
| `frontend/src/main.js` | 挂载 Vue、Pinia、Router |
| `frontend/src/api/request.js` | Axios：带 Token、处理 401、解析 Result |
| `frontend/src/stores/user.js` | Pinia：token、角色 |
| `frontend/src/stores/cart.js` | Pinia：购物车数量 |
| `frontend/src/router/index.js` | 路由与登录/管理员守卫 |
| `frontend/src/views/*` | 页面：商品、购物车、订单、登录注册 |
| `frontend/src/views/admin/*` | 管理员：商品管理、订单管理 |

## 五、启动方式

```bash
# 后端（项目根目录）
mvn spring-boot:run

# 前端
cd frontend
npm install
npm run dev
```

- 后端：http://localhost:8080  
- 前端：http://localhost:5173（代理 `/api` 到后端）  
- 默认管理员：**admin / admin123**

## 六、数据库切换

- 默认：`spring.profiles.active=sqlite`（库文件 `./data/ecommerce.db`）
- MySQL：在 `application.yml` 中设置 `spring.profiles.active: mysql`，并修改 `application-mysql.yml` 中的账号密码；Mapper XML 中部分 `datetime('now')` 需改为 MySQL 语法或改为 Java 赋值（生产迁移时注意）。

## 七、生成顺序（与 zhi dao 一致）

1. 项目初始化 → 2. 表+实体+Mapper → 3. 通用组件 → 4. 商品 → 5. 认证 → 6. 购物车 → 7. 订单 → 8. 评价 → 前端

后续扩展 OpenAI 真实调用：替换 `AiDescriptionHelper` 即可，勿改 Controller 契约。

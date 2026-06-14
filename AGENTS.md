# AGENTS.md — 电商购物平台编码规范

> 本规范综合自 ECC-main_skills 规则集（rules/java/、rules/common/、rules/web/），
> 针对 Spring Boot 3 + MyBatis Plus + Vue 3 项目做了适配。

---

## 1. 核心原则

### KISS — 保持简单
- 优先选择能工作的最简单方案
- 避免过度抽象和过早优化
- 一个类只做一件事

### DRY — 不重复
- 公共逻辑抽取为工具方法
- 避免复制粘贴导致的行为漂移

### YAGNI — 不用就别加
- 不构建尚未需要的功能
- 不引入尚未需要的依赖

---

## 2. 不可变性（CRITICAL）

**永远创建新对象，不要修改现有对象：**

```java
// WRONG — 修改原对象
user.setName("新名称");

// CORRECT — 返回新对象
User updated = user.withName("新名称");
```

---

## 3. 文件组织

**多小文件 > 少大文件**
- 每个文件 200-400 行，最大不超过 800 行
- 按功能/领域组织，不按类型组织
- 前端按页面组件分目录

---

## 4. 项目分层架构

```
Controller → Service接口 → ServiceImpl → Mapper（BaseMapper）
     ↓            ↓
   DTO/Request  Entity（@TableName）
```

**严格分层调用，不可跨层。**

---

## 5. Java 编码规范

### 5.1 命名
- 类/接口：`PascalCase`（`OrderService`、`ProductController`）
- 方法/字段：`camelCase`（`findById`、`totalAmount`）
- 常量：`UPPER_SNAKE_CASE`（`ALLOWED_STATUS`）
- 包名：全小写（`com.ecommerce.service`）

### 5.2 依赖注入
**始终使用构造器注入，禁止字段注入：**

```java
// CORRECT — 构造器注入
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    public OrderServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }
}

// WRONG — 字段注入
@Autowired
private OrderMapper orderMapper;
```

### 5.3 DTO
使用 Java `record`（Java 16+）作为 DTO：

```java
public record OrderResponse(Long id, String status, BigDecimal total) {
    public static OrderResponse from(ShopOrder order) {
        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount());
    }
}
```

### 5.4 异常处理
- 创建领域异常类继承 `RuntimeException`
- 全局异常处理器统一转换为 `Result<T>` JSON
- 永远不在 API 响应中暴露堆栈/内部路径

```java
// 日志记录详情，返回通用消息
} catch (Exception e) {
    log.error("订单处理失败 id={}", id, e);
    return Result.fail("服务器内部错误");
}
```

### 5.5 输入校验
- DTO 使用 `@NotNull`、`@NotBlank`、`@Min`、`@Max` 校验
- Controller 使用 `@Validated` 触发校验
- Service 层对关键参数再次断言

---

## 6. MyBatis Plus 规范

### 6.1 Entity
```java
@Data
@TableName("shop_order")
public class ShopOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("user_id")
    private Long userId;
}
```

### 6.2 Mapper
```java
@Mapper
public interface ShopOrderMapper extends BaseMapper<ShopOrder> {
    // 复杂查询写在 XML 或使用 @Select
}
```

### 6.3 查询
- 简单 CRUD 使用 `BaseMapper` 内置方法
- 复杂条件使用 `LambdaQueryWrapper`
- 分页使用 `Page<T>` + `page()` 方法

---

## 7. 安全规范

### 7.1 密钥管理
- 绝对不硬编码 API 密钥、Token、密码
- 使用 `application.yml` + 环境变量
- JWT secret 必须在生产环境覆盖

### 7.2 SQL 注入防护
- 始终使用参数化查询（MyBatis `#{param}`）
- 禁用字符串拼接 SQL
- XML 中所有参数用 `#{}` 而非 `${}`

### 7.3 认证与授权
- 密码使用 BCrypt 加密存储
- JWT 验证每个请求（无状态）
- Service 层强制校验权限

---

## 8. 测试规范

### 8.1 框架
- JUnit 5 + AssertJ + Mockito
- `@DisplayName` 描述测试意图

### 8.2 测试组织
```
src/test/java/com/ecommerce/
  service/     — Service 层单元测试
  controller/  — Controller 层测试
```

### 8.3 命名
- 方法名：`methodName_scenario_expectedBehavior()`
- 示例：`findById_existingOrder_returnsOrder()`

### 8.4 覆盖率
- 目标 80%+ 行覆盖
- 重点测试 Service 和领域逻辑

---

## 9. 前端规范（Vue 3）

### 9.1 目录结构
```
frontend/src/
  views/       — 页面组件
  stores/      — Pinia 状态管理
  api/         — Axios 封装
  router/      — 路由配置
```

### 9.2 错误处理
- 每个请求必须 try-catch
- 使用 `loading`/`error` 状态变量
- 不使用 `alert()` 做错误提示

---

## 10. 代码质量检查清单

- [ ] 代码可读，命名有意义
- [ ] 方法 < 50 行
- [ ] 文件 < 800 行
- [ ] 嵌套层级 < 4
- [ ] 有适当的错误处理
- [ ] 无硬编码值（使用常量或配置）
- [ ] 遵循不可变模式
- [ ] 输入已校验

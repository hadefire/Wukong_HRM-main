# 悟空HRM数据库初始化指南

本文档提供完整的数据库初始化步骤，适用于首次部署或重置数据库。

---

## 📋 前置要求

- MySQL 5.7+ 或 MySQL 8.0+
- 数据库管理工具（Navicat、MySQL Workbench、DBeaver 或命令行）
- 确保 MySQL 服务正在运行

---

## 🚀 快速初始化（推荐）

### 方式一：使用命令行（最快）

```bash
# 1. 创建数据库并导入基础数据
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS hrms CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
mysql -u root -p hrms < DB/wk_hrm_open.sql

# 2. 修复 SQL Mode 问题
mysql -u root -p hrms < DB/fix_sql_mode.sql

# 3. 添加密码字段
mysql -u root -p hrms < DB/add_password_field.sql

# 4. 初始化本地测试用户
mysql -u root -p hrms < DB/init_local_user.sql

# 5. 配置完整的 HRM 权限
mysql -u root -p hrms < DB/init_full_hrm_permissions.sql

# 6. （可选）验证权限配置
mysql -u root -p hrms < DB/check_permissions.sql
```

### 方式二：使用图形化工具（推荐新手）

按照下面的 "详细步骤说明" 操作。

---

## 📖 详细步骤说明

### 步骤 1：创建数据库

```sql
CREATE DATABASE IF NOT EXISTS hrms 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_general_ci;

USE hrms;
```

**说明**：创建名为 `wk_hrm_open` 的数据库，使用 UTF-8 字符集。

---

### 步骤 2：导入基础表结构和数据

**执行文件**：`wk_hrm_open.sql`

**方式 A - 使用 Navicat/DBeaver**：
1. 右键数据库 → 运行 SQL 文件
2. 选择 `DB/wk_hrm_open.sql`
3. 点击开始执行

**方式 B - 使用命令行**：
```bash
mysql -u root -p hrms < DB/wk_hrm_open.sql
```

**说明**：
- 创建所有数据表（84个表）
- 导入基础配置数据（菜单、字段配置等）
- 预计耗时：10-30秒

**包含内容**：
- ✅ 员工管理表
- ✅ 组织架构表
- ✅ 角色权限表
- ✅ 菜单配置表
- ✅ 字段配置表
- ✅ 薪资、绩效、招聘等业务表

---

### 步骤 3：修复 SQL Mode 问题

**执行文件**：`fix_sql_mode.sql`

```sql
-- 执行该文件或手动执行以下命令
SET GLOBAL sql_mode=(SELECT REPLACE(@@GLOBAL.sql_mode,'ONLY_FULL_GROUP_BY',''));
```

**说明**：
- 解决 MySQL 5.7+ 的 `ONLY_FULL_GROUP_BY` 严格模式问题
- 避免某些 GROUP BY 查询报错
- **重要**：执行后需要重启应用或重新连接数据库

**验证**：
```sql
SELECT @@GLOBAL.sql_mode;  -- 应该不包含 ONLY_FULL_GROUP_BY
```

---

### 步骤 4：添加员工密码字段

**执行文件**：`add_password_field.sql`

**作用**：
- 在 `wk_hrm_employee` 表中添加 `password` 字段
- 为所有员工设置默认密码：`123456`（BCrypt 加密）
- 支持本地登录功能

**密码说明**：
- 默认密码：`123456`
- 加密方式：BCrypt
- 安全性高，不可逆

**重置密码**（如需）：
```sql
-- 重置指定员工密码为 123456
UPDATE wk_hrm_employee 
SET password = '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW' 
WHERE employee_id = xxx;
```

---

### 步骤 5：初始化本地测试用户

**执行文件**：`init_local_user.sql`

**创建内容**：

#### 5.1 测试部门
- 部门名称：本地测试企业
- 部门编码：LOCAL-001

#### 5.2 测试员工（超级管理员）
- **员工ID**：`1559019593470803969`（重要：与 `application.yml` 中配置一致）
- **姓名**：本地管理员
- **手机号**：`13800000000`（重要：登录用户名）
- **默认密码**：`123456`
- **职位**：系统管理员
- **状态**：在职

#### 5.3 角色创建
创建两个角色：
1. **超级管理员角色**（role_id: `1559019593879896074`）
   - 拥有系统管理权限
   - 数据权限：全部

2. **HRM管理员角色**（role_id: `1559019593879896076`）
   - 拥有人事管理权限
   - 数据权限：全部

#### 5.4 用户角色关联
- 将测试员工分配为"超级管理员"
- 将测试员工分配为"HRM管理员"

**验证查询**：
```sql
-- 查看创建的员工
SELECT employee_id, employee_name, mobile, post, status 
FROM wk_hrm_employee 
WHERE mobile = '13800000000';

-- 查看角色分配
SELECT u.employee_name, r.role_name, r.role_type
FROM wk_admin_user_role ur
LEFT JOIN wk_hrm_employee u ON ur.user_id = u.employee_id
LEFT JOIN wk_admin_role r ON ur.role_id = r.role_id
WHERE ur.user_id = 1559019593470803969;
```

---

### 步骤 6：配置完整的菜单权限

**执行文件**：`init_full_hrm_permissions.sql`（推荐）或 `init_admin_permissions.sql`

**说明**：
- 为超级管理员角色分配所有菜单权限
- 包括：HRM模块、管理后台、组织管理等所有功能
- 确保管理员能访问所有功能页面

**权限范围**：
- ✅ 员工管理（查看、新建、编辑、删除等）
- ✅ 组织架构（部门管理、调岗、异动等）
- ✅ 薪资管理
- ✅ 绩效管理
- ✅ 招聘管理
- ✅ 考勤管理
- ✅ 系统设置（角色管理、菜单管理等）

**验证查询**：
```sql
-- 查看权限数量
SELECT COUNT(*) AS total_permissions 
FROM wk_admin_role_menu 
WHERE role_id = 1559019593879896074;  -- 应该 > 50

-- 查看部分权限明细
SELECT m.menu_name, m.realm, m.menu_type
FROM wk_admin_role_menu rm
LEFT JOIN wk_admin_menu m ON rm.menu_id = m.menu_id
WHERE rm.role_id = 1559019593879896074
ORDER BY m.menu_id
LIMIT 30;
```

---

### 步骤 7：验证配置（可选）

**执行文件**：`check_permissions.sql`

**检查内容**：
1. ✅ 超级管理员角色是否存在
2. ✅ 用户角色关联是否正确
3. ✅ 菜单权限数量是否足够
4. ✅ 组织管理等关键权限是否配置

**预期结果**：
- 角色存在且状态为启用
- 用户关联了 2 个角色（超级管理员 + HRM管理员）
- 菜单权限数量 > 50 条
- 包含组织管理、员工管理等核心权限

---

## ✅ 初始化完成检查清单

完成所有步骤后，请检查：

- [ ] 数据库 `wk_hrm_open` 创建成功
- [ ] 共有 84 个数据表
- [ ] SQL Mode 已修复（不含 ONLY_FULL_GROUP_BY）
- [ ] 员工表有 `password` 字段
- [ ] 测试员工创建成功（手机号：13800000000）
- [ ] 创建了 2 个角色（超级管理员、HRM管理员）
- [ ] 超级管理员拥有 > 50 条菜单权限
- [ ] 可以用 13800000000 / 123456 登录系统

---

## 🔐 默认登录信息

初始化完成后，使用以下账号登录：

```
登录地址：http://localhost:44311
用户名：13800000000
密码：123456
```

**安全建议**：
- ⚠️ 首次登录后请立即修改密码
- ⚠️ 生产环境请删除测试账号或修改其密码
- ⚠️ 定期更换管理员密码

---

## 🔧 常见问题

### Q1: 导入 wk_hrm_open.sql 报错？
**A**: 检查 MySQL 版本是否 >= 5.7，字符集是否为 utf8mb4。

### Q2: 登录时提示"密码错误"？
**A**: 确保执行了 `add_password_field.sql`，检查 password 字段是否存在。

### Q3: 登录后看不到菜单或功能？
**A**: 
1. 确保执行了 `init_full_hrm_permissions.sql`
2. 检查权限数量：`SELECT COUNT(*) FROM wk_admin_role_menu WHERE role_id = 1559019593879896074;`
3. 清除浏览器缓存或使用隐私模式重新登录

### Q4: 组织管理功能无法使用？
**A**: 执行 `check_permissions.sql` 检查权限配置，确保包含 menu_id 830-835。

### Q5: SQL Mode 修改后仍然报错？
**A**: 
1. 执行 `SET GLOBAL sql_mode=...` 后需要重启应用
2. 或重新建立数据库连接
3. 验证：`SELECT @@GLOBAL.sql_mode;`

### Q6: 想要重新初始化数据库？
**A**: 
```sql
-- 方式1：删除数据库重建
DROP DATABASE wk_hrm_open;
-- 然后重新执行步骤 1-6

-- 方式2：只清空数据保留结构
-- 在 Navicat 等工具中选择"清空数据库"
```

---

## 📝 SQL 文件说明

| 文件名 | 大小 | 作用 | 执行顺序 | 必需 |
|--------|------|------|----------|------|
| `wk_hrm_open.sql` | ~396KB | 基础表结构和配置数据 | 1 | ✅ 必需 |
| `fix_sql_mode.sql` | ~892B | 修复 SQL 严格模式 | 2 | ✅ 推荐 |
| `add_password_field.sql` | ~1.1KB | 添加密码字段 | 3 | ✅ 必需 |
| `init_local_user.sql` | ~6.5KB | 创建测试用户和角色 | 4 | ✅ 必需 |
| `init_full_hrm_permissions.sql` | ~3KB | 完整HRM权限配置 | 5 | ✅ 推荐 |
| `init_admin_permissions.sql` | ~2.9KB | 基础权限配置（替代方案） | 5 | ⚪ 可选 |
| `check_permissions.sql` | ~1.9KB | 验证权限配置 | 6 | ⚪ 可选 |

**执行建议**：
- 推荐使用 `init_full_hrm_permissions.sql`（更完整）
- `init_admin_permissions.sql` 仅在需要精简权限时使用
- `check_permissions.sql` 用于排查权限问题

---

## 🎯 配置文件对应关系

数据库初始化后，需要确保以下配置文件的值与数据库一致：

### application.yml
```yaml
wukong:
  auth:
    local:
      enabled: true
      user-id: 1559019593470803969      # ← 对应 employee_id
      mobile: "13800000000"              # ← 对应 mobile
```

**重要**：如果修改了测试员工的 ID 或手机号，必须同步修改配置文件！

---

## 📞 技术支持

如遇到问题，请检查：
1. MySQL 错误日志
2. 应用程序日志（logs/hrm.log）
3. 浏览器控制台（F12）

---

## 📅 更新记录

- **2026-02-06**: 创建初始化指南
- 汇总了 7 个 SQL 文件的完整执行流程
- 添加了详细的检查和验证步骤

---

**祝使用愉快！** 🎉

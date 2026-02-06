# Ever HRM 前后端分离开发指南

## 快速开始

### 1. 启动后端（Terminal 1）

```bash
cd C:\Users\hadef\Project\Wukong_HRM-main

# 设置 Java 环境（如果需要）
$env:JAVA_HOME="C:\Users\hadef\Tools\Java\OpenJDK\jdk-17.0.2"
$env:Path="$env:JAVA_HOME\bin;$env:Path"

# 直接运行（无需前端构建）
java -jar hrm\hrm-web\target\hrm-web-0.0.1-SNAPSHOT.jar
```

后端启动后访问: http://localhost:28080

### 2. 启动前端开发服务器（Terminal 2）

```bash
cd C:\Users\hadef\Project\Wukong_HRM-main\ux

# 安装依赖（首次）
npm install

# 启动开发服务器（带热重载）
npm run dev
```

前端开发服务器访问: http://localhost:8090

---

## 分别构建

### 仅构建后端（Java 代码变更后）

```bash
cd C:\Users\hadef\Project\Wukong_HRM-main
$env:JAVA_HOME="C:\Users\hadef\Tools\Java\OpenJDK\jdk-17.0.2"
$env:Path="$env:JAVA_HOME\bin;$env:Path"
$env:MAVEN_OPTS="-Xmx2048m"

# 只编译后端（不包含前端）
mvn -DskipTests -pl hrm/hrm-web -am package -Dmaven.resources.skip=true

# 或者完整构建（包含复制前端到静态资源）
mvn -DskipTests -pl hrm/hrm-web -am package
```

### 仅构建前端（Vue 代码变更后）

```bash
cd C:\Users\hadef\Project\Wukong_HRM-main\ux

# 构建生产版本
npm run build
```

构建结果在 `ux/dist` 目录

---

## 开发模式说明

### 前端开发模式 (`npm run dev`)
- 端口: 8090
- 热重载: 代码修改后自动刷新
- API 代理: 自动转发到后端 http://localhost:28080
- 登录: 不会强制每次刷新重新登录

### 生产模式
- 前端构建后复制到后端 static 目录
- 后端同时提供 API 和静态资源
- 登录: 每次刷新需重新登录

---

## 常用命令汇总

| 操作 | 命令 |
|------|------|
| 启动后端 | `java -jar hrm\hrm-web\target\hrm-web-0.0.1-SNAPSHOT.jar` |
| 启动前端开发 | `cd ux && npm run dev` |
| 构建前端 | `cd ux && npm run build` |
| 构建后端 | `mvn -DskipTests -pl hrm/hrm-web -am package` |
| 清理构建 | `mvn clean -pl hrm/hrm-web -am` |

---

## 本地认证配置

后端配置文件 `hrm/hrm-web/src/main/resources/application.yml`:

```yaml
wukong:
  auth:
    local:
      enabled: true          # 启用本地认证
      user-id: 1559019593470803969
      dept-id: 1
      company-id: 1
      username: local-admin
      mobile: 13800000000    # EmployeeAspect 需要
      admin: true
```

本地认证模式下，任意用户名/密码即可登录。

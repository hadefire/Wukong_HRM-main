# 启动后端开发模式（支持热重载，无需打包JAR）

# 强制设置 JDK 17（Lombok 不兼容 JDK 21）
$env:JAVA_HOME = "C:\Users\hadef\Tools\Java\OpenJDK\jdk-17.0.2"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

# 切换到项目目录
Set-Location "$PSScriptRoot\hrm\hrm-web"

# 尝试终止占用端口的进程（忽略错误）
Get-NetTCPConnection -LocalPort 44311 2>$null | ForEach-Object { Stop-Process -Id $_.OwningProcess -Force 2>$null }

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  HRM Backend - Development Mode" -ForegroundColor Cyan
Write-Host "  (with Spring Boot DevTools)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Server URL: http://localhost:44311" -ForegroundColor Green
Write-Host "JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Yellow
& "$env:JAVA_HOME\bin\java.exe" -version
Write-Host ""
Write-Host "Hot Reload Tips:" -ForegroundColor Magenta
Write-Host "  - Java code changes: Auto restart (DevTools)" -ForegroundColor White
Write-Host "  - Static resources: Auto reload (no restart)" -ForegroundColor White
Write-Host "  - Config changes: Manual restart required" -ForegroundColor White
Write-Host "  - Press Ctrl+C to stop the server" -ForegroundColor White
Write-Host ""
Write-Host "Starting server with DevTools..." -ForegroundColor Green
Write-Host ""

# 切换到 hrm-web 目录并运行 Maven
Set-Location "$PSScriptRoot\hrm\hrm-web"
mvn spring-boot:run "-DskipTests"

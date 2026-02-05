# 启动前端开发服务器
Set-Location "$PSScriptRoot\ux"

Write-Host "Starting HRM Frontend Dev Server on http://localhost:8090 ..." -ForegroundColor Green
Write-Host "API requests will be proxied to http://localhost:44311" -ForegroundColor Yellow
npm run dev

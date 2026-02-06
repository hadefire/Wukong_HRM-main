# 启动后端服务
$env:JAVA_HOME = "C:\Users\hadef\Tools\Java\OpenJDK\jdk-17.0.2"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "Starting HRM Backend on http://localhost:28080 ..." -ForegroundColor Green
java -jar "$PSScriptRoot\hrm\hrm-web\target\hrm-web-0.0.1-SNAPSHOT.jar"

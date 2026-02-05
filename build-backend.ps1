# 仅构建后端
$env:JAVA_HOME = "C:\Users\hadef\Tools\Java\OpenJDK\jdk-17.0.2"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
$env:MAVEN_OPTS = "-Xmx2048m"

Set-Location $PSScriptRoot

Write-Host "Building HRM Backend..." -ForegroundColor Green
mvn -DskipTests -pl hrm/hrm-web -am package

if ($LASTEXITCODE -eq 0) {
    Write-Host "Build SUCCESS! JAR located at: hrm\hrm-web\target\hrm-web-0.0.1-SNAPSHOT.jar" -ForegroundColor Green
} else {
    Write-Host "Build FAILED!" -ForegroundColor Red
}

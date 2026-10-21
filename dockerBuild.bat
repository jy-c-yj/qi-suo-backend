@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

REM ===== 配置 =====
set "ROOT=%~dp0"
set "TAG=latest"
set "STOP_ON_ERROR=0"

REM 真实服务目录，相对根目录
REM 注意：目录名带 Service 后缀
set "SERVICES=qi-suo-gateway qi-suo-service\qi-suo-authService qi-suo-service\qi-suo-userService qi-suo-service\qi-suo-merchantService qi-suo-service\qi-suo-adminService qi-suo-service\qi-suo-fileService"

REM ===== 参数处理 =====
if not "%~1"=="" (
    if "%~1"=="--tag" (
        set "TAG=%~2"
        shift
        shift
    ) else if "%~1"=="--stop" (
        set "STOP_ON_ERROR=1"
        shift
    ) else (
        set "SERVICES=%~1"
    )
)

echo ========================================
echo 项目根目录: %ROOT%
echo 镜像标签:   %TAG%
echo 服务列表:   %SERVICES%
echo ========================================
echo.

set "SUCCESS_COUNT=0"
set "FAIL_COUNT=0"
set "FAIL_LIST="

for %%S in (%SERVICES%) do (
    call :build_one "%%S"
)

echo.
echo ========================================
echo 构建完成: 成功 %SUCCESS_COUNT% 个, 失败 %FAIL_COUNT% 个
if not "%FAIL_LIST%"=="" (
    echo 失败服务: %FAIL_LIST%
)
echo ========================================

if %FAIL_COUNT% GTR 0 (
    exit /b 1
)
exit /b 0


:build_one
set "SVC_REL=%~1"
set "SVC_DIR=%ROOT%%SVC_REL%\target"

REM 取服务目录名，例如 qi-suo-authService
REM 注意：必须从 SVC_REL 取，SVC_DIR 以 \target 结尾，%%~nxA 会得到 "target"
for %%A in ("%SVC_REL%") do set "SVC_NAME=%%~nxA"

REM 如果目录名以 Service 结尾，去掉它
REM 例如 qi-suo-authService -> qi-suo-auth
if /i "!SVC_NAME:~-7!"=="Service" (
    set "SVC_NAME=!SVC_NAME:~0,-7!"
)

set "IMAGE_NAME=!SVC_NAME!"

echo.
echo ----------------------------------------
echo [构建] !SVC_NAME!
echo 目录: %SVC_DIR%
echo 镜像: !IMAGE_NAME!:%TAG%
echo ----------------------------------------

if not exist "%SVC_DIR%" (
    echo [跳过] 目录不存在: %SVC_DIR%
    set /a FAIL_COUNT+=1
    set "FAIL_LIST=!FAIL_LIST! !SVC_NAME!"
    if "%STOP_ON_ERROR%"=="1" exit /b 1
    goto :eof
)

if not exist "%SVC_DIR%\Dockerfile" (
    echo [跳过] Dockerfile 不存在: %SVC_DIR%\Dockerfile
    set /a FAIL_COUNT+=1
    set "FAIL_LIST=!FAIL_LIST! !SVC_NAME!"
    if "%STOP_ON_ERROR%"=="1" exit /b 1
    goto :eof
)

pushd "%SVC_DIR%"
docker build -t "!IMAGE_NAME!:%TAG%" .
set "BUILD_RESULT=%ERRORLEVEL%"
popd

if %BUILD_RESULT% NEQ 0 (
    echo [失败] !SVC_NAME!
    set /a FAIL_COUNT+=1
    set "FAIL_LIST=!FAIL_LIST! !SVC_NAME!"
    if "%STOP_ON_ERROR%"=="1" exit /b 1
) else (
    echo [成功] !IMAGE_NAME!
    set /a SUCCESS_COUNT+=1
)
goto :eof
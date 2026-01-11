@echo off
chcp 65001 >nul
echo ========================================
echo    补充后端 Jar 包到交付包
echo ========================================
echo.

if not exist "target\cloudnote-1.0.0.jar" (
    echo ❌ 错误：找不到 target\cloudnote-1.0.0.jar
    echo.
    echo 请先打包后端：
    echo   1. 打开 IDEA
    echo   2. 右侧 Maven 面板
    echo   3. 双击 clean，再双击 package
    echo.
    pause
    exit
)

echo 复制后端 Jar 包...
copy "target\cloudnote-1.0.0.jar" "交付包\部署包\" >nul

if exist "交付包\部署包\cloudnote-1.0.0.jar" (
    echo ✅ 后端 Jar 包复制成功！
    echo.
    echo 文件位置：交付包\部署包\cloudnote-1.0.0.jar
) else (
    echo ❌ 复制失败
)

echo.
pause

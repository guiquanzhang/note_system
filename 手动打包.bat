@echo off
chcp 65001 >nul
echo ========================================
echo    手动打包后端 Jar
echo ========================================
echo.

echo 正在查找 Java...
java -version
if errorlevel 1 (
    echo ❌ 找不到 Java，请确保 JDK 已安装
    pause
    exit
)

echo.
echo 正在创建 Jar 包...

cd target\classes

:: 创建 MANIFEST.MF
echo Manifest-Version: 1.0 > MANIFEST.MF
echo Main-Class: com.cloudnote.CloudnoteApplication >> MANIFEST.MF
echo.>> MANIFEST.MF

:: 打包
jar cvfm ..\cloudnote-1.0.0.jar MANIFEST.MF *

cd ..\..

if exist "target\cloudnote-1.0.0.jar" (
    echo.
    echo ✅ Jar 包创建成功！
    echo.
    echo 正在复制到交付包...
    copy "target\cloudnote-1.0.0.jar" "交付包\部署包\" >nul
    
    if exist "交付包\部署包\cloudnote-1.0.0.jar" (
        echo ✅ 复制成功！
        echo.
        echo 文件位置：交付包\部署包\cloudnote-1.0.0.jar
    )
) else (
    echo ❌ 打包失败
)

echo.
pause

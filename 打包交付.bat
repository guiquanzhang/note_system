@echo off
chcp 65001 >nul
echo ========================================
echo    CloudNote 云笔记系统 - 一键打包
echo ========================================
echo.

:: 创建交付目录
echo [1/5] 创建交付目录...
if exist "交付包" rmdir /s /q "交付包"
mkdir "交付包\源代码"
mkdir "交付包\部署包"
mkdir "交付包\文档"

:: 打包后端
echo [2/5] 打包后端 Jar...
call mvn clean package -DskipTests -q
if exist "target\cloudnote-1.0.0.jar" (
    copy "target\cloudnote-1.0.0.jar" "交付包\部署包\" >nul
    echo       √ 后端打包成功
) else (
    echo       × 后端打包失败，请检查 Maven 配置
)

:: 打包前端
echo [3/5] 打包前端...
cd front_end
call npm run build >nul 2>&1
if exist "dist" (
    xcopy /E /I /Q "dist" "..\交付包\部署包\front_end_dist" >nul
    echo       √ 前端打包成功
) else (
    echo       × 前端打包失败，请检查 npm 配置
)
cd ..

:: 复制文档
echo [4/5] 复制文档...
copy "README.md" "交付包\文档\" >nul 2>&1
copy "API接口文档.md" "交付包\文档\" >nul 2>&1
copy "系统设计文档.md" "交付包\文档\" >nul 2>&1
copy "数据库ER图.md" "交付包\文档\" >nul 2>&1
copy "部署手册.md" "交付包\文档\" >nul 2>&1
copy "项目演示PPT.md" "交付包\文档\" >nul 2>&1
copy "CloudNote项目演示.html" "交付包\文档\" >nul 2>&1
copy "交付清单.md" "交付包\文档\" >nul 2>&1
copy "src\main\resources\db\schema.sql" "交付包\文档\" >nul 2>&1
echo       √ 文档复制完成

:: 复制源代码（排除不需要的目录）
echo [5/5] 复制源代码...
xcopy /E /I /Q "src" "交付包\源代码\src" >nul
xcopy /E /I /Q "front_end" "交付包\源代码\front_end" /EXCLUDE:exclude_list.txt >nul 2>&1
copy "pom.xml" "交付包\源代码\" >nul
copy "README.md" "交付包\源代码\" >nul
copy ".gitignore" "交付包\源代码\" >nul 2>&1

:: 删除源代码中的 node_modules
if exist "交付包\源代码\front_end\node_modules" rmdir /s /q "交付包\源代码\front_end\node_modules"
echo       √ 源代码复制完成

echo.
echo ========================================
echo    打包完成！
echo ========================================
echo.
echo 交付包目录结构：
echo   交付包\
echo   ├── 源代码\        (前后端源代码)
echo   ├── 部署包\        (Jar包 + 前端dist)
echo   └── 文档\          (所有文档)
echo.
echo 请将 "交付包" 文件夹压缩后提交给老师
echo.
pause

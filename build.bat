@echo off
chcp 65001 >nul
echo ============================================================
echo  错题库 -- 一键打包为 exe
echo ============================================================

cd /d "%~dp0"

echo [1/3] 安装 PyInstaller...
uv add --dev pyinstaller

echo [2/3] 开始打包...
uv run pyinstaller ^
    --onefile ^
    --windowed ^
    --name "错题库" ^
    --add-data "src;src" ^
    src/mistake_notebook/main.py

echo [3/3] 打包完成！
echo 可执行文件位于: dist\错题库.exe
pause

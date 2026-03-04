#!/bin/bash
# ============================================================
# WSL2 Ubuntu 一键打包 APK 脚本
# 用法：在 WSL2 Ubuntu 终端中执行：
#   bash build_apk.sh
# ============================================================
set -e

echo "============================================"
echo "  错题库 Android APK 构建脚本"
echo "============================================"

# ── 1. 检查并安装系统依赖 ────────────────────────────────────────────────────
echo "[1/5] 安装系统依赖..."
sudo apt-get update -qq
sudo apt-get install -y -qq \
    python3-pip python3-venv \
    git zip unzip \
    autoconf libtool pkg-config \
    zlib1g-dev libncurses5-dev libncursesw5-dev \
    libtinfo5 cmake libffi-dev libssl-dev \
    openjdk-17-jdk \
    adb

# 设置 JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# ── 2. 安装 buildozer ────────────────────────────────────────────────────────
echo "[2/5] 安装 buildozer..."
pip3 install --user --upgrade buildozer cython

# ── 3. 切换到项目目录 ────────────────────────────────────────────────────────
echo "[3/5] 进入项目目录..."
# WSL2 中访问 Windows 路径：/mnt/d/github/mistake-notebook-android
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"
echo "当前目录: $(pwd)"

# ── 4. 下载字体（如果不存在）────────────────────────────────────────────────
echo "[4/5] 检查字体资源..."
FONT_DIR="assets/fonts"
FONT_FILE="$FONT_DIR/NotoSansSC.ttf"
mkdir -p "$FONT_DIR"
if [ ! -f "$FONT_FILE" ]; then
    echo "  下载 Noto Sans SC 字体..."
    # 使用 Google Fonts 静态字体下载（Medium 400 weight）
    FONT_URL="https://github.com/googlefonts/noto-cjk/raw/main/Sans/OTF/SimplifiedChinese/NotoSansCJKsc-Regular.otf"
    # 降级方案：使用 Noto Sans SC VF 的子集
    pip3 install --quiet fonttools 2>/dev/null || true
    wget -q -O /tmp/NotoSansSC.ttf \
        "https://fonts.gstatic.com/s/notosanssc/v26/k3kCo84MPvpLmixcA63oeALhLOCT-7hQkHXMfA.ttf" \
        && mv /tmp/NotoSansSC.ttf "$FONT_FILE" \
        || echo "  字体下载失败，请手动放置 NotoSansSC.ttf 到 assets/fonts/"
fi

# ── 5. 执行 buildozer 打包 ───────────────────────────────────────────────────
echo "[5/5] 开始打包 APK（首次运行需下载 SDK/NDK，约需 20-40 分钟）..."
~/.local/bin/buildozer android debug

echo ""
echo "============================================"
echo "  ✓ 打包完成！APK 文件位于："
echo "  $(pwd)/bin/*.apk"
echo "============================================"

# ── 可选：列出生成的 APK ────────────────────────────────────────────────────
ls -lh bin/*.apk 2>/dev/null || echo "  未找到 APK 文件，请检查上方构建日志"

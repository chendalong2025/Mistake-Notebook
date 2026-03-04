[app]

# APK 显示名称
title = 错题库

# 包名（唯一标识符，只能用英文）
package.name = mistakenotebook
package.domain = org.mistakenotebook

# 源码目录
source.dir = .
source.main = main.py

# 包含的文件扩展名
source.include_exts = py,png,jpg,jpeg,gif,ttf,json,kv

# 包含的目录（字体资源）
source.include_patterns = assets/*,assets/fonts/*

# 版本
version = 1.0.0

# ── Python 依赖 ──────────────────────────────────────────────────────────────
# 注意：buildozer 使用 pip 格式，不是 uv 格式
requirements = python3==3.11,kivy==2.3.0,pillow

# ── 界面 & 方向 ───────────────────────────────────────────────────────────────
orientation = portrait
fullscreen = 0

# ── 图标（可选，放 assets/icon.png）──────────────────────────────────────────
#icon.filename = %(source.dir)s/assets/icon.png

# ── Android 配置 ──────────────────────────────────────────────────────────────
[android]

# 目标 API：33 = Android 13
android.api = 33

# 最低支持 API：21 = Android 5.0（覆盖 99%+ 设备）
android.minapi = 21

# NDK 版本（buildozer 会自动下载）
android.ndk = 25b

# SDK 版本
android.sdk = 24

# 权限
android.permissions = WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE

# ABI：arm64-v8a 覆盖现代手机；armeabi-v7a 覆盖旧机
android.archs = arm64-v8a, armeabi-v7a

# 允许备份
android.allow_backup = True

# ── 构建配置 ──────────────────────────────────────────────────────────────────
[buildozer]

# 构建日志级别：2 = verbose（调试用）
log_level = 2

# 警告导致失败：0 = 关闭（宽松模式）
warn_on_root = 1

"""应用程序常量定义"""

import os
from pathlib import Path

# ─── 路径 ────────────────────────────────────────────────────────────────────
# 如果是打包后的exe，数据存储在exe同级目录；否则存储在项目根目录
if getattr(__import__('sys'), 'frozen', False):
    APP_DIR = Path(os.path.dirname(__import__('sys').executable))
else:
    APP_DIR = Path(__file__).parent.parent.parent  # 项目根目录

DATA_DIR = APP_DIR / "data"
DATA_FILE = DATA_DIR / "questions.json"

# ─── 做题参数 ─────────────────────────────────────────────────────────────────
QUIZ_TIMEOUT_SECONDS = 10                        # 每题倒计时秒数（默认）
QUIZ_TIMEOUT_OPTIONS = [5, 10, 15, 20, 30, 60]  # 可选时间列表（秒）
QUIZ_COUNTDOWN_INTERVAL_MS = 1000               # 倒计时刷新间隔（毫秒）

# ─── 选项 ─────────────────────────────────────────────────────────────────────
OPTION_KEYS = ["A", "B", "C", "D"]

# ─── 界面尺寸 ─────────────────────────────────────────────────────────────────
WINDOW_WIDTH = 860
WINDOW_HEIGHT = 660
WINDOW_TITLE = "错题库"

# ─── 颜色主题 ─────────────────────────────────────────────────────────────────
COLOR_BG = "#1E1E2E"          # 主背景（深紫蓝）
COLOR_SURFACE = "#2A2A3E"     # 卡片/面板背景
COLOR_PRIMARY = "#7C5CFC"     # 主色（紫色）
COLOR_PRIMARY_HOVER = "#9B7EFD"
COLOR_SUCCESS = "#4ADE80"     # 正确（绿）
COLOR_ERROR = "#F87171"       # 错误（红）
COLOR_WARNING = "#FBBF24"     # 超时（黄）
COLOR_TEXT = "#E2E8F0"        # 主文字
COLOR_TEXT_MUTED = "#94A3B8"  # 次要文字
COLOR_BORDER = "#3D3D5C"      # 边框
COLOR_BTN_OPTION = "#2D2D44"  # 选项按钮背景

# ─── 字体 ─────────────────────────────────────────────────────────────────────
FONT_TITLE = ("Microsoft YaHei UI", 18, "bold")
FONT_QUESTION = ("Microsoft YaHei UI", 14)
FONT_OPTION = ("Microsoft YaHei UI", 12)
FONT_LABEL = ("Microsoft YaHei UI", 11)
FONT_SMALL = ("Microsoft YaHei UI", 10)
FONT_COUNTDOWN = ("Microsoft YaHei UI", 36, "bold")
FONT_RESULT_BIG = ("Microsoft YaHei UI", 48, "bold")

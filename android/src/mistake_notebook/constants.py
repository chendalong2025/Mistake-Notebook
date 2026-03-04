"""应用程序常量定义（适配 Kivy / Android）"""

# ─── 做题参数 ─────────────────────────────────────────────────────────────────
QUIZ_TIMEOUT_SECONDS: int = 10                       # 每题倒计时秒数（默认）
QUIZ_TIMEOUT_OPTIONS: list[int] = [5, 10, 15, 20, 30, 60]
QUIZ_COUNTDOWN_INTERVAL: float = 1.0                 # 倒计时刷新间隔（秒，Kivy Clock 用）

# ─── 选项 ─────────────────────────────────────────────────────────────────────
OPTION_KEYS: list[str] = ["A", "B", "C", "D"]

# ─── 界面标题 ─────────────────────────────────────────────────────────────────
APP_TITLE: str = "错题库"

# ─── 颜色主题（十六进制，Kivy 用 rgba 转换工具，直接用 hex string）────────────
COLOR_BG: str = "#1E1E2E"
COLOR_SURFACE: str = "#2A2A3E"
COLOR_PRIMARY: str = "#7C5CFC"
COLOR_PRIMARY_HOVER: str = "#9B7EFD"
COLOR_SUCCESS: str = "#4ADE80"
COLOR_ERROR: str = "#F87171"
COLOR_WARNING: str = "#FBBF24"
COLOR_TEXT: str = "#E2E8F0"
COLOR_TEXT_MUTED: str = "#94A3B8"
COLOR_BORDER: str = "#3D3D5C"
COLOR_BTN_OPTION: str = "#2D2D44"
COLOR_WHITE: str = "#FFFFFF"
COLOR_DARK_TEXT: str = "#1E1E2E"

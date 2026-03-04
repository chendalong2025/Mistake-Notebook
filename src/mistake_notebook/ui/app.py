"""主窗口 - 导航两个Tab页"""

from __future__ import annotations

import sys
import tkinter as tk
from pathlib import Path

from mistake_notebook.constants import (
    COLOR_BG,
    COLOR_PRIMARY,
    COLOR_SURFACE,
    COLOR_TEXT,
    COLOR_TEXT_MUTED,
    FONT_LABEL,
    FONT_TITLE,
    WINDOW_HEIGHT,
    WINDOW_TITLE,
    WINDOW_WIDTH,
)
from mistake_notebook.models import QuizSession
from mistake_notebook.ui.add_question import AddQuestionFrame
from mistake_notebook.ui.quiz import QuizFrame
from mistake_notebook.ui.result import ResultFrame

# ─── 图标路径解析（兼容开发环境与打包后 exe）─────────────────────────────────
def _resolve_asset(relative: str) -> Path:
    """返回 assets 目录下文件的绝对路径，打包后也能正确定位"""
    if getattr(sys, "frozen", False):
        # PyInstaller 打包后，资源文件在 sys._MEIPASS 临时目录
        base = Path(getattr(sys, "_MEIPASS", Path(sys.executable).parent))
    else:
        # 开发环境：项目根目录
        base = Path(__file__).parent.parent.parent.parent
    return base / relative


ICON_ICO = _resolve_asset("assets/icon.ico")
ICON_PNG = _resolve_asset("Mistake-Notebook.png")


class MainApp(tk.Tk):
    """主窗口，单例模式"""

    def __init__(self) -> None:
        super().__init__()
        self.title(WINDOW_TITLE)
        self.geometry(f"{WINDOW_WIDTH}x{WINDOW_HEIGHT}")
        self.minsize(700, 540)
        self.configure(bg=COLOR_BG)
        self.resizable(True, True)

        self._set_icon()
        self._center_window()
        self._build_ui()

    def _set_icon(self) -> None:
        """设置窗口左上角和任务栏图标"""
        # 优先用 ICO（支持多尺寸，任务栏显示最佳）
        if ICON_ICO.exists():
            try:
                self.iconbitmap(str(ICON_ICO))
                return
            except Exception:
                pass
        # fallback：用 PNG 通过 PhotoImage 设置
        if ICON_PNG.exists():
            try:
                img = tk.PhotoImage(file=str(ICON_PNG))
                self.iconphoto(True, img)
                self._icon_img = img  # 防止被 GC
            except Exception:
                pass

    def _center_window(self) -> None:
        self.update_idletasks()
        sw = self.winfo_screenwidth()
        sh = self.winfo_screenheight()
        x = (sw - WINDOW_WIDTH) // 2
        y = (sh - WINDOW_HEIGHT) // 2
        self.geometry(f"{WINDOW_WIDTH}x{WINDOW_HEIGHT}+{x}+{y}")

    def _build_ui(self) -> None:
        self.rowconfigure(1, weight=1)
        self.columnconfigure(0, weight=1)

        self._build_nav()

        self._content = tk.Frame(self, bg=COLOR_BG)
        self._content.grid(row=1, column=0, sticky="nsew")
        self._content.rowconfigure(0, weight=1)
        self._content.columnconfigure(0, weight=1)

        self._add_frame = AddQuestionFrame(self._content)
        self._add_frame.grid(row=0, column=0, sticky="nsew")

        self._quiz_frame = QuizFrame(self._content, on_finish=self._on_quiz_finish)
        self._quiz_frame.grid(row=0, column=0, sticky="nsew")

        self._result_frame = ResultFrame(
            self._content,
            on_retry=self._on_retry,
            on_back=self._on_back_to_home,
        )
        self._result_frame.grid(row=0, column=0, sticky="nsew")

        self._show_tab("add")

    def _build_nav(self) -> None:
        nav = tk.Frame(self, bg=COLOR_SURFACE, height=54)
        nav.grid(row=0, column=0, sticky="ew")
        nav.grid_propagate(False)
        nav.columnconfigure(2, weight=1)

        tk.Label(nav, text="📚  错题库",
                 font=FONT_TITLE, bg=COLOR_SURFACE, fg=COLOR_TEXT,
                 padx=20).grid(row=0, column=0, sticky="w")

        self._tab_buttons: dict[str, tk.Label] = {}
        tabs = [("add", "📝  录入题目"), ("quiz", "🎯  开始做题")]
        for col, (tab_id, label) in enumerate(tabs, start=1):
            btn = tk.Label(
                nav, text=label, font=FONT_LABEL,
                bg=COLOR_SURFACE, fg=COLOR_TEXT_MUTED,
                padx=18, pady=16, cursor="hand2",
            )
            btn.grid(row=0, column=col, sticky="ns")
            btn.bind("<Button-1>", lambda e, t=tab_id: self._show_tab(t))
            btn.bind("<Enter>", lambda e, b=btn: b.config(fg=COLOR_TEXT))
            btn.bind("<Leave>", lambda e, b=btn, t=tab_id: b.config(
                fg=COLOR_TEXT if self._active_tab == t else COLOR_TEXT_MUTED))
            self._tab_buttons[tab_id] = btn

        self._active_tab = ""

    def _show_tab(self, tab_id: str) -> None:
        self._active_tab = tab_id
        for tid, btn in self._tab_buttons.items():
            btn.config(fg=COLOR_TEXT if tid == tab_id else COLOR_TEXT_MUTED)
        if tab_id == "add":
            self._add_frame.tkraise()
            self._add_frame.on_tab_shown()
        elif tab_id == "quiz":
            self._quiz_frame.tkraise()
            self._quiz_frame.on_tab_shown()

    def _on_quiz_finish(self, session: QuizSession) -> None:
        self._result_frame.show_result(session)
        self._result_frame.tkraise()

    def _on_retry(self) -> None:
        self._quiz_frame.tkraise()
        self._quiz_frame.on_tab_shown()
        self._quiz_frame._start_quiz()

    def _on_back_to_home(self) -> None:
        self._show_tab("add")

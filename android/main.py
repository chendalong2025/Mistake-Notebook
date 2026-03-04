"""Kivy App 入口 + ScreenManager"""

from __future__ import annotations

import os
from pathlib import Path

# ── Kivy 配置（必须在 import kivy 之前）──────────────────────────────────────
os.environ.setdefault("KIVY_NO_ENV_CONFIG", "1")
# 针对 Android 关闭窗口尺寸限制；桌面保持合理尺寸
from kivy.config import Config
Config.set("graphics", "width", "480")
Config.set("graphics", "height", "854")
Config.set("graphics", "resizable", "1")

from kivy.app import App
from kivy.core.text import LabelBase
from kivy.core.window import Window
from kivy.metrics import dp
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.screenmanager import NoTransition, Screen, ScreenManager

from mistake_notebook.constants import (
    APP_TITLE,
    COLOR_BG,
    COLOR_PRIMARY,
    COLOR_SURFACE,
    COLOR_TEXT,
    COLOR_TEXT_MUTED,
)
from mistake_notebook.models import QuizSession
from mistake_notebook.storage import init_storage
from mistake_notebook.ui.add_screen import AddScreen
from mistake_notebook.ui.quiz_screen import QuizScreen
from mistake_notebook.ui.result_screen import ResultScreen

# 注册中文字体
_FONT_PATH = Path(__file__).parent / "assets" / "fonts" / "NotoSansSC.ttf"
import kivy as _kivy_module
_KIVY_FONT_DIR = Path(_kivy_module.__file__).parent / "data" / "fonts"
_FALLBACK_FONT = str(_KIVY_FONT_DIR / "Roboto-Regular.ttf")

if _FONT_PATH.exists():
    LabelBase.register(name="assets/fonts/NotoSansSC.ttf", fn_regular=str(_FONT_PATH))
else:
    # 降级：使用 Kivy 内置 DroidSans（中文可能显示方框，但不会崩溃）
    LabelBase.register(name="assets/fonts/NotoSansSC.ttf", fn_regular=_FALLBACK_FONT)


def _hex(color: str) -> list[float]:
    c = color.lstrip("#")
    r, g, b = int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16)
    return [r / 255, g / 255, b / 255, 1]


class MistakeNotebookApp(App):
    """主 App（单例模式）"""

    _instance: "MistakeNotebookApp | None" = None

    def __new__(cls, **kwargs):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    def build(self) -> BoxLayout:
        # ── 初始化数据目录（Android 用 user_data_dir）──────────────────────
        data_dir = Path(self.user_data_dir) / "data"
        init_storage(data_dir)

        # ── 窗口背景色 ─────────────────────────────────────────────────────
        Window.clearcolor = _hex(COLOR_BG)

        # ── 根布局（导航栏 + 内容区）──────────────────────────────────────
        root = BoxLayout(orientation="vertical")

        # ── ScreenManager ─────────────────────────────────────────────────
        self._sm = ScreenManager(transition=NoTransition())

        self._add_screen = AddScreen(name="add")
        self._quiz_screen = QuizScreen(name="quiz", on_finish=self._on_quiz_finish)
        self._result_screen = ResultScreen(
            name="result",
            on_retry=self._on_retry,
            on_back=self._on_back_to_home,
        )

        self._sm.add_widget(self._add_screen)
        self._sm.add_widget(self._quiz_screen)
        self._sm.add_widget(self._result_screen)

        # ── 顶部导航栏 ─────────────────────────────────────────────────────
        nav = self._build_nav()
        root.add_widget(nav)
        root.add_widget(self._sm)

        self._switch_tab("add")
        return root

    def _build_nav(self) -> BoxLayout:
        from kivy.graphics import Color, Rectangle

        nav = BoxLayout(
            orientation="horizontal",
            size_hint_y=None, height=dp(54),
            padding=(dp(16), 0),
            spacing=dp(4),
        )
        with nav.canvas.before:
            Color(*_hex(COLOR_SURFACE))
            rect = Rectangle(size=nav.size, pos=nav.pos)
        nav.bind(size=lambda w, v: setattr(rect, "size", v),
                 pos=lambda w, v: setattr(rect, "pos", v))

        # 标题
        nav.add_widget(Label(
            text="📚  错题库",
            size_hint_x=None, width=dp(120),
            font_name="assets/fonts/NotoSansSC.ttf",
            font_size=dp(18), bold=True,
            color=_hex(COLOR_TEXT),
        ))

        # Tab 按钮
        self._nav_btns: dict[str, Button] = {}
        for tab_id, tab_text in [("add", "📝  录入题目"), ("quiz", "🎯  开始做题")]:
            btn = Button(
                text=tab_text,
                background_normal="",
                background_color=_hex(COLOR_SURFACE),
                color=_hex(COLOR_TEXT_MUTED),
                font_name="assets/fonts/NotoSansSC.ttf",
                font_size=dp(15),
            )
            btn.bind(on_release=lambda b, t=tab_id: self._switch_tab(t))
            self._nav_btns[tab_id] = btn
            nav.add_widget(btn)

        return nav

    def _switch_tab(self, tab_id: str) -> None:
        self._sm.current = tab_id
        for tid, btn in self._nav_btns.items():
            if tid == tab_id:
                btn.color = _hex(COLOR_TEXT)
                btn.bold = True
            else:
                btn.color = _hex(COLOR_TEXT_MUTED)
                btn.bold = False

    def _on_quiz_finish(self, session: QuizSession) -> None:
        self._result_screen.show_result(session)
        self._sm.current = "result"

    def _on_retry(self) -> None:
        self._sm.current = "quiz"
        self._quiz_screen._start_quiz()

    def _on_back_to_home(self) -> None:
        self._switch_tab("add")

    def get_application_name(self) -> str:
        return APP_TITLE


def main() -> None:
    MistakeNotebookApp().run()


if __name__ == "__main__":
    main()

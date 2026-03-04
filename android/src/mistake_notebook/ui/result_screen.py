"""结果统计界面 Screen"""

from __future__ import annotations

from typing import Callable

from kivy.metrics import dp
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.gridlayout import GridLayout
from kivy.uix.label import Label
from kivy.uix.screenmanager import Screen
from kivy.uix.scrollview import ScrollView
from kivy.uix.widget import Widget

from mistake_notebook.constants import (
    COLOR_BG,
    COLOR_BTN_OPTION,
    COLOR_ERROR,
    COLOR_PRIMARY,
    COLOR_SUCCESS,
    COLOR_SURFACE,
    COLOR_TEXT,
    COLOR_TEXT_MUTED,
    COLOR_WARNING,
)
from mistake_notebook.models import QuizSession


def _hex(color: str) -> list[float]:
    c = color.lstrip("#")
    r, g, b = int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16)
    return [r / 255, g / 255, b / 255, 1]


class ResultScreen(Screen):
    """做题结果统计界面"""

    def __init__(self, on_retry: Callable, on_back: Callable, **kwargs) -> None:
        super().__init__(**kwargs)
        self._on_retry = on_retry
        self._on_back = on_back
        self._build_ui()

    def _build_ui(self) -> None:
        from kivy.graphics import Color, Rectangle

        root = BoxLayout(orientation="vertical", padding=dp(20), spacing=dp(12))
        with root.canvas.before:
            Color(*_hex(COLOR_BG))
            rect = Rectangle(size=root.size, pos=root.pos)
        root.bind(size=lambda w, v: setattr(rect, "size", v),
                  pos=lambda w, v: setattr(rect, "pos", v))

        # 正确率大字
        self._score_label = Label(
            text="0%",
            font_size=dp(64), bold=True,
            size_hint_y=None, height=dp(80),
            color=_hex(COLOR_SUCCESS),
            font_name="assets/fonts/NotoSansSC.ttf",
        )
        root.add_widget(self._score_label)

        root.add_widget(Label(
            text="正确率",
            font_size=dp(18),
            size_hint_y=None, height=dp(30),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
        ))

        # 统计卡片行
        self._stats_row = GridLayout(
            cols=4, size_hint_y=None, height=dp(80),
            spacing=dp(8),
        )
        root.add_widget(self._stats_row)

        # 答题明细（ScrollView）
        detail_label = Label(
            text="答题明细",
            size_hint_y=None, height=dp(30),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
            halign="left", text_size=(None, None),
        )
        root.add_widget(detail_label)

        scroll = ScrollView(size_hint=(1, 1))
        self._detail_box = BoxLayout(
            orientation="vertical",
            size_hint_y=None,
            spacing=dp(4), padding=dp(8),
        )
        self._detail_box.bind(minimum_height=self._detail_box.setter("height"))
        scroll.add_widget(self._detail_box)
        root.add_widget(scroll)

        # 按钮行
        btn_row = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(56),
                            spacing=dp(12))
        btn_row.add_widget(Widget())
        retry_btn = Button(
            text="🔄  再来一次",
            size_hint_x=None, width=dp(150),
            background_normal="",
            background_color=_hex(COLOR_PRIMARY),
            color=_hex("#FFFFFF"),
            font_name="assets/fonts/NotoSansSC.ttf",
            font_size=dp(16),
        )
        retry_btn.bind(on_release=lambda b: self._on_retry())
        back_btn = Button(
            text="回到主页",
            size_hint_x=None, width=dp(120),
            background_normal="",
            background_color=_hex(COLOR_BTN_OPTION),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
            font_size=dp(15),
        )
        back_btn.bind(on_release=lambda b: self._on_back())
        btn_row.add_widget(retry_btn)
        btn_row.add_widget(back_btn)
        btn_row.add_widget(Widget())
        root.add_widget(btn_row)

        self.add_widget(root)

    def show_result(self, session: QuizSession) -> None:
        """更新并显示统计结果"""
        acc = session.accuracy
        score_color = COLOR_SUCCESS if acc >= 80 else (COLOR_WARNING if acc >= 60 else COLOR_ERROR)
        self._score_label.text = f"{acc:.0f}%"
        self._score_label.color = _hex(score_color)

        # 统计卡片
        self._stats_row.clear_widgets()
        for value, label, color in [
            (str(session.total), "总题数", COLOR_TEXT),
            (str(session.correct), "答对", COLOR_SUCCESS),
            (str(session.wrong), "答错", COLOR_ERROR),
            (str(session.timeout_count), "超时", COLOR_WARNING),
        ]:
            self._stats_row.add_widget(self._make_stat_card(value, label, color))

        # 答题明细
        self._detail_box.clear_widgets()
        for i, rec in enumerate(session.records, 1):
            if rec.is_timeout:
                icon, color, user_ans = "⏰", COLOR_WARNING, "超时"
            elif rec.is_correct:
                icon, color, user_ans = "✓", COLOR_SUCCESS, (rec.user_answer or "")
            else:
                icon, color, user_ans = "✗", COLOR_ERROR, (rec.user_answer or "")

            from kivy.graphics import Color, Rectangle
            row = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(36),
                            padding=(dp(8), 0))
            with row.canvas.before:
                Color(*_hex(COLOR_SURFACE))
                r = Rectangle(size=row.size, pos=row.pos)
            row.bind(size=lambda w, v: setattr(r, "size", v),
                     pos=lambda w, v: setattr(r, "pos", v))

            preview = rec.question_content[:28] + ("..." if len(rec.question_content) > 28 else "")
            row.add_widget(Label(
                text=f"{i}. {preview}",
                color=_hex(COLOR_TEXT),
                font_name="assets/fonts/NotoSansSC.ttf",
                font_size=dp(13),
                halign="left", valign="middle",
            ))
            row.add_widget(Label(
                text=f"{icon} {user_ans} / 正确:{rec.correct_answer}",
                color=_hex(color),
                font_name="assets/fonts/NotoSansSC.ttf",
                font_size=dp(13),
                size_hint_x=None, width=dp(130),
                halign="right", valign="middle",
                text_size=(dp(130), None),
            ))
            self._detail_box.add_widget(row)

    def _make_stat_card(self, value: str, label: str, color: str) -> BoxLayout:
        from kivy.graphics import Color, Rectangle
        card = BoxLayout(orientation="vertical", padding=dp(8))
        with card.canvas.before:
            Color(*_hex(COLOR_SURFACE))
            rect = Rectangle(size=card.size, pos=card.pos)
        card.bind(size=lambda w, v: setattr(rect, "size", v),
                  pos=lambda w, v: setattr(rect, "pos", v))
        card.add_widget(Label(
            text=value, font_size=dp(22), bold=True,
            color=_hex(color),
            font_name="assets/fonts/NotoSansSC.ttf",
        ))
        card.add_widget(Label(
            text=label, font_size=dp(12),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
        ))
        return card

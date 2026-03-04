"""做题界面 Screen（倒计时 + 选项按钮 + 进度条）"""

from __future__ import annotations

import random
from typing import Callable, Optional

from kivy.clock import Clock
from kivy.metrics import dp
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.progressbar import ProgressBar
from kivy.uix.screenmanager import Screen
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
    OPTION_KEYS,
    QUIZ_COUNTDOWN_INTERVAL,
    QUIZ_TIMEOUT_OPTIONS,
    QUIZ_TIMEOUT_SECONDS,
)
from mistake_notebook.models import Question, QuizRecord, QuizSession
from mistake_notebook.storage import QuestionStorage


def _hex(color: str) -> list[float]:
    c = color.lstrip("#")
    r, g, b = int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16)
    return [r / 255, g / 255, b / 255, 1]


class QuizScreen(Screen):
    """做题界面"""

    def __init__(self, on_finish: Callable[[QuizSession], None], **kwargs) -> None:
        super().__init__(**kwargs)
        self._storage = QuestionStorage()
        self._on_finish = on_finish
        self._questions: list[Question] = []
        self._session = QuizSession()
        self._current_index = 0
        self._timeout_seconds = QUIZ_TIMEOUT_SECONDS
        self._countdown = self._timeout_seconds
        self._timer_event: Optional[object] = None
        self._answered = False
        self._build_ui()

    # ─── 构建 UI ─────────────────────────────────────────────────────────────

    def _build_ui(self) -> None:
        from kivy.graphics import Color, Rectangle

        root = BoxLayout(orientation="vertical")
        with root.canvas.before:
            Color(*_hex(COLOR_BG))
            rect = Rectangle(size=root.size, pos=root.pos)
        root.bind(size=lambda w, v: setattr(rect, "size", v),
                  pos=lambda w, v: setattr(rect, "pos", v))

        self._start_panel = self._build_start_panel()
        self._quiz_panel = self._build_quiz_panel()

        root.add_widget(self._start_panel)
        root.add_widget(self._quiz_panel)

        self._show_start()
        self.add_widget(root)

    def _show_start(self) -> None:
        self._start_panel.opacity = 1
        self._start_panel.disabled = False
        self._quiz_panel.opacity = 0
        self._quiz_panel.disabled = True

    def _show_quiz(self) -> None:
        self._start_panel.opacity = 0
        self._start_panel.disabled = True
        self._quiz_panel.opacity = 1
        self._quiz_panel.disabled = False

    # ── 开始界面 ─────────────────────────────────────────────────────────────

    def _build_start_panel(self) -> BoxLayout:
        panel = BoxLayout(orientation="vertical", padding=dp(40), spacing=dp(16))

        panel.add_widget(Widget())  # spacer

        panel.add_widget(Label(
            text="🎯", font_size=dp(56),
            size_hint_y=None, height=dp(72),
            color=_hex(COLOR_TEXT),
        ))
        panel.add_widget(Label(
            text="开始做题", font_size=dp(24),
            size_hint_y=None, height=dp(40),
            color=_hex(COLOR_TEXT),
            font_name="assets/fonts/NotoSansSC.ttf",
        ))

        self._ready_label = Label(
            text="", font_size=dp(14),
            size_hint_y=None, height=dp(30),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
        )
        panel.add_widget(self._ready_label)

        # 时间选择行
        time_label = Label(
            text="每题时间：", font_size=dp(14),
            size_hint_y=None, height=dp(30),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
        )
        panel.add_widget(time_label)

        time_row = BoxLayout(
            orientation="horizontal",
            size_hint_y=None, height=dp(48),
            spacing=dp(8),
        )
        self._time_btns: dict[int, Button] = {}
        for sec in QUIZ_TIMEOUT_OPTIONS:
            btn = Button(
                text=f"{sec}s",
                background_normal="",
                font_name="assets/fonts/NotoSansSC.ttf",
                font_size=dp(14),
            )
            btn.bind(on_release=lambda b, s=sec: self._select_time(s))
            self._time_btns[sec] = btn
            time_row.add_widget(btn)
        self._select_time(QUIZ_TIMEOUT_SECONDS)
        panel.add_widget(time_row)

        panel.add_widget(Label(
            text="超时自动跳题",
            font_size=dp(12),
            size_hint_y=None, height=dp(24),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
        ))

        self._start_btn = Button(
            text="▶  开始",
            size_hint=(None, None), size=(dp(160), dp(52)),
            pos_hint={"center_x": 0.5},
            background_normal="",
            background_color=_hex(COLOR_PRIMARY),
            color=_hex("#FFFFFF"),
            font_name="assets/fonts/NotoSansSC.ttf",
            font_size=dp(18),
        )
        self._start_btn.bind(on_release=lambda b: self._start_quiz())
        panel.add_widget(self._start_btn)

        panel.add_widget(Widget())  # spacer
        return panel

    def _select_time(self, seconds: int) -> None:
        self._timeout_seconds = seconds
        for sec, btn in self._time_btns.items():
            if sec == seconds:
                btn.background_color = _hex(COLOR_PRIMARY)
                btn.color = _hex("#FFFFFF")
            else:
                btn.background_color = _hex(COLOR_BTN_OPTION)
                btn.color = _hex(COLOR_TEXT_MUTED)

    # ── 做题界面 ─────────────────────────────────────────────────────────────

    def _build_quiz_panel(self) -> BoxLayout:
        panel = BoxLayout(orientation="vertical", padding=dp(16), spacing=dp(10))

        # 进度行
        top = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(40))
        self._progress_label = Label(
            text="", color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf", font_size=dp(14),
            halign="left", text_size=(None, None),
        )
        self._countdown_label = Label(
            text="", color=_hex(COLOR_SUCCESS),
            font_name="assets/fonts/NotoSansSC.ttf", font_size=dp(32),
            bold=True, halign="right", text_size=(None, None),
        )
        top.add_widget(self._progress_label)
        top.add_widget(self._countdown_label)
        panel.add_widget(top)

        # 进度条
        self._progress_bar = ProgressBar(
            max=100, value=0,
            size_hint_y=None, height=dp(6),
        )
        panel.add_widget(self._progress_bar)

        # 题目卡片
        from kivy.graphics import Color, Rectangle
        question_card = BoxLayout(
            orientation="vertical",
            size_hint_y=None, height=dp(100),
            padding=dp(16),
        )
        with question_card.canvas.before:
            Color(*_hex(COLOR_SURFACE))
            card_rect = Rectangle(size=question_card.size, pos=question_card.pos)
        question_card.bind(size=lambda w, v: setattr(card_rect, "size", v),
                           pos=lambda w, v: setattr(card_rect, "pos", v))

        self._question_label = Label(
            text="", color=_hex(COLOR_TEXT),
            font_name="assets/fonts/NotoSansSC.ttf", font_size=dp(16),
            halign="left", valign="top",
        )
        self._question_label.bind(size=self._question_label.setter("text_size"))
        question_card.add_widget(self._question_label)
        panel.add_widget(question_card)

        # 选项按钮
        self._option_btns: dict[str, Button] = {}
        for key in OPTION_KEYS:
            btn = Button(
                text="",
                size_hint_y=None, height=dp(56),
                background_normal="",
                background_color=_hex(COLOR_BTN_OPTION),
                color=_hex(COLOR_TEXT),
                font_name="assets/fonts/NotoSansSC.ttf",
                font_size=dp(15),
                halign="left",
            )
            btn.bind(on_release=lambda b, k=key: self._on_answer(k))
            self._option_btns[key] = btn
            panel.add_widget(btn)

        # 反馈标签
        self._feedback_label = Label(
            text="", color=_hex(COLOR_SUCCESS),
            font_name="assets/fonts/NotoSansSC.ttf", font_size=dp(15),
            size_hint_y=None, height=dp(36),
        )
        panel.add_widget(self._feedback_label)

        panel.add_widget(Widget())
        return panel

    # ─── 做题逻辑 ────────────────────────────────────────────────────────────

    def _start_quiz(self) -> None:
        questions = self._storage.get_all()
        if not questions:
            return
        self._questions = questions[:]
        random.shuffle(self._questions)
        self._session = QuizSession()
        self._current_index = 0
        self._show_quiz()
        self._load_question()

    def _load_question(self) -> None:
        self._answered = False
        self._feedback_label.text = ""
        q = self._questions[self._current_index]
        total = len(self._questions)

        self._progress_label.text = f"第 {self._current_index + 1} / {total} 题"
        self._progress_bar.value = int(self._current_index / max(total, 1) * 100)
        self._question_label.text = q.content

        for key in OPTION_KEYS:
            btn = self._option_btns[key]
            btn.text = f"  {key}.  {q.options.get(key, '')}"
            btn.background_color = _hex(COLOR_BTN_OPTION)
            btn.color = _hex(COLOR_TEXT)
            btn.disabled = False

        self._stop_timer()
        self._countdown = self._timeout_seconds
        self._update_countdown_display()
        self._timer_event = Clock.schedule_interval(self._tick, QUIZ_COUNTDOWN_INTERVAL)

    def _tick(self, dt: float) -> None:
        self._countdown -= 1
        self._update_countdown_display()
        if self._countdown <= 0:
            self._stop_timer()
            self._on_timeout()

    def _stop_timer(self) -> None:
        if self._timer_event:
            self._timer_event.cancel()
            self._timer_event = None

    def _update_countdown_display(self) -> None:
        t = self._countdown
        total = self._timeout_seconds
        if t > total * 0.2:
            color = COLOR_SUCCESS
        elif t > total * 0.1:
            color = COLOR_WARNING
        else:
            color = COLOR_ERROR
        self._countdown_label.text = str(t)
        self._countdown_label.color = _hex(color)

    def _on_timeout(self) -> None:
        if self._answered:
            return
        self._answered = True
        q = self._questions[self._current_index]
        self._session.records.append(QuizRecord(
            question_id=q.id, question_content=q.content,
            correct_answer=q.answer, user_answer=None,
            is_correct=False, is_timeout=True,
        ))
        self._option_btns[q.answer].background_color = _hex(COLOR_SUCCESS)
        self._option_btns[q.answer].color = _hex("#FFFFFF")
        self._feedback_label.text = f"⏰  超时！正确答案是 {q.answer}"
        self._feedback_label.color = _hex(COLOR_WARNING)
        for btn in self._option_btns.values():
            btn.disabled = True
        Clock.schedule_once(lambda dt: self._next_question(), 1.5)

    def _on_answer(self, selected: str) -> None:
        if self._answered:
            return
        self._answered = True
        self._stop_timer()
        q = self._questions[self._current_index]
        is_correct = selected == q.answer
        self._session.records.append(QuizRecord(
            question_id=q.id, question_content=q.content,
            correct_answer=q.answer, user_answer=selected,
            is_correct=is_correct, is_timeout=False,
        ))
        for btn in self._option_btns.values():
            btn.disabled = True
        if is_correct:
            self._option_btns[selected].background_color = _hex(COLOR_SUCCESS)
            self._option_btns[selected].color = _hex("#FFFFFF")
            self._feedback_label.text = "✓  回答正确！"
            self._feedback_label.color = _hex(COLOR_SUCCESS)
        else:
            self._option_btns[selected].background_color = _hex(COLOR_ERROR)
            self._option_btns[selected].color = _hex("#FFFFFF")
            self._option_btns[q.answer].background_color = _hex(COLOR_SUCCESS)
            self._option_btns[q.answer].color = _hex("#FFFFFF")
            self._feedback_label.text = f"✗  答错了，正确答案是 {q.answer}"
            self._feedback_label.color = _hex(COLOR_ERROR)
        Clock.schedule_once(lambda dt: self._next_question(), 1.2)

    def _next_question(self) -> None:
        self._current_index += 1
        if self._current_index >= len(self._questions):
            self._finish()
        else:
            self._load_question()

    def _finish(self) -> None:
        self._stop_timer()
        self._on_finish(self._session)

    def on_enter(self) -> None:
        """每次进入 Screen 时刷新状态"""
        self._stop_timer()
        self._show_start()
        questions = self._storage.get_all()
        count = len(questions)
        if count == 0:
            self._ready_label.text = "题库为空，请先录入题目"
            self._ready_label.color = _hex(COLOR_ERROR)
            self._start_btn.disabled = True
        else:
            self._ready_label.text = f"题库中共有 {count} 道题"
            self._ready_label.color = _hex(COLOR_TEXT_MUTED)
            self._start_btn.disabled = False

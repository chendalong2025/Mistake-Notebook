"""做题界面（含可配置倒计时）"""

from __future__ import annotations

import random
import tkinter as tk
from tkinter import ttk
from typing import Callable, Optional

from mistake_notebook.constants import (
    COLOR_BG,
    COLOR_BORDER,
    COLOR_BTN_OPTION,
    COLOR_ERROR,
    COLOR_PRIMARY,
    COLOR_SUCCESS,
    COLOR_SURFACE,
    COLOR_TEXT,
    COLOR_TEXT_MUTED,
    COLOR_WARNING,
    FONT_COUNTDOWN,
    FONT_LABEL,
    FONT_OPTION,
    FONT_QUESTION,
    FONT_SMALL,
    FONT_TITLE,
    OPTION_KEYS,
    QUIZ_COUNTDOWN_INTERVAL_MS,
    QUIZ_TIMEOUT_OPTIONS,
    QUIZ_TIMEOUT_SECONDS,
)
from mistake_notebook.models import Question, QuizRecord, QuizSession
from mistake_notebook.storage import QuestionStorage


class QuizFrame(tk.Frame):
    """做题界面"""

    def __init__(self, parent: tk.Widget, on_finish: Callable[[QuizSession], None]) -> None:
        super().__init__(parent, bg=COLOR_BG)
        self._storage = QuestionStorage()
        self._on_finish = on_finish
        self._questions: list[Question] = []
        self._session = QuizSession()
        self._current_index = 0
        self._timeout_seconds = QUIZ_TIMEOUT_SECONDS  # 当前选择的时间
        self._countdown = self._timeout_seconds
        self._timer_id: Optional[str] = None
        self._answered = False
        self._build_ui()
        self._show_start_screen()

    # ─── 构建UI ──────────────────────────────────────────────────────────────

    def _build_ui(self) -> None:
        self.columnconfigure(0, weight=1)
        self.rowconfigure(0, weight=1)
        self._start_screen = self._build_start_screen()
        self._quiz_screen = self._build_quiz_screen()

    def _build_start_screen(self) -> tk.Frame:
        frame = tk.Frame(self, bg=COLOR_BG)
        frame.place(relx=0.5, rely=0.5, anchor="center")

        tk.Label(frame, text="🎯", font=("", 56), bg=COLOR_BG).pack(pady=(0, 8))
        tk.Label(frame, text="开始做题", font=FONT_TITLE, bg=COLOR_BG, fg=COLOR_TEXT).pack()

        self._ready_hint = tk.Label(frame, text="", font=FONT_LABEL,
                                    bg=COLOR_BG, fg=COLOR_TEXT_MUTED)
        self._ready_hint.pack(pady=8)

        # ── 时间设置行 ────────────────────────────────────────────────────────
        time_row = tk.Frame(frame, bg=COLOR_BG)
        time_row.pack(pady=(4, 0))

        tk.Label(time_row, text="每题时间：", font=FONT_LABEL,
                 bg=COLOR_BG, fg=COLOR_TEXT_MUTED).pack(side="left")

        # 时间选择按钮组（替代下拉框，更直观）
        self._time_var = tk.IntVar(value=QUIZ_TIMEOUT_SECONDS)
        self._time_btns: dict[int, tk.Label] = {}
        for sec in QUIZ_TIMEOUT_OPTIONS:
            btn = tk.Label(
                time_row,
                text=f"{sec}s",
                font=FONT_SMALL,
                bg=COLOR_BTN_OPTION, fg=COLOR_TEXT_MUTED,
                padx=10, pady=5,
                cursor="hand2",
            )
            btn.pack(side="left", padx=3)
            btn.bind("<Button-1>", lambda e, s=sec: self._select_time(s))
            self._time_btns[sec] = btn

        self._select_time(QUIZ_TIMEOUT_SECONDS)  # 默认高亮

        tk.Label(frame, text="超时自动跳题", font=FONT_SMALL,
                 bg=COLOR_BG, fg=COLOR_TEXT_MUTED).pack(pady=(4, 0))

        self._start_btn = tk.Button(
            frame, text="▶  开始",
            font=FONT_LABEL, fg="white", bg=COLOR_PRIMARY,
            relief="flat", padx=32, pady=12,
            cursor="hand2",
            command=self._start_quiz,
        )
        self._start_btn.pack(pady=20)
        return frame

    def _select_time(self, seconds: int) -> None:
        """切换时间选择高亮"""
        self._timeout_seconds = seconds
        self._time_var.set(seconds)
        for sec, btn in self._time_btns.items():
            if sec == seconds:
                btn.config(bg=COLOR_PRIMARY, fg="white")
            else:
                btn.config(bg=COLOR_BTN_OPTION, fg=COLOR_TEXT_MUTED)

    def _build_quiz_screen(self) -> tk.Frame:
        frame = tk.Frame(self, bg=COLOR_BG)
        frame.columnconfigure(0, weight=1)

        # 顶部进度 + 倒计时
        top = tk.Frame(frame, bg=COLOR_BG, padx=24, pady=12)
        top.grid(row=0, column=0, sticky="ew")
        self._progress_label = tk.Label(top, text="", font=FONT_LABEL,
                                        bg=COLOR_BG, fg=COLOR_TEXT_MUTED)
        self._progress_label.pack(side="left")
        self._countdown_label = tk.Label(top, text="",
                                         font=FONT_COUNTDOWN,
                                         bg=COLOR_BG, fg=COLOR_SUCCESS)
        self._countdown_label.pack(side="right")

        # 进度条
        self._progress_bar_canvas = tk.Canvas(frame, height=4, bg=COLOR_BORDER,
                                              highlightthickness=0)
        self._progress_bar_canvas.grid(row=1, column=0, sticky="ew", padx=24)

        # 题目卡片
        question_card = tk.Frame(frame, bg=COLOR_SURFACE, padx=24, pady=20)
        question_card.grid(row=2, column=0, sticky="ew", padx=24, pady=12)
        question_card.columnconfigure(0, weight=1)

        self._question_label = tk.Label(
            question_card, text="", font=FONT_QUESTION,
            bg=COLOR_SURFACE, fg=COLOR_TEXT,
            wraplength=680, justify="left", anchor="w",
        )
        self._question_label.grid(row=0, column=0, sticky="ew")

        # 选项按钮
        self._option_buttons: dict[str, tk.Button] = {}
        options_frame = tk.Frame(frame, bg=COLOR_BG)
        options_frame.grid(row=3, column=0, sticky="ew", padx=24, pady=4)
        options_frame.columnconfigure(0, weight=1)

        for i, key in enumerate(OPTION_KEYS):
            btn = tk.Button(
                options_frame, text="",
                font=FONT_OPTION,
                fg=COLOR_TEXT, bg=COLOR_BTN_OPTION,
                activebackground=COLOR_PRIMARY, activeforeground="white",
                relief="flat", anchor="w", padx=20, pady=12,
                cursor="hand2", wraplength=680, justify="left",
                command=lambda k=key: self._on_answer(k),
            )
            btn.grid(row=i, column=0, sticky="ew", pady=4)
            self._option_buttons[key] = btn

        # 反馈标签
        self._feedback_label = tk.Label(frame, text="", font=FONT_LABEL,
                                        bg=COLOR_BG, fg=COLOR_TEXT)
        self._feedback_label.grid(row=4, column=0, pady=8)

        return frame

    # ─── 开始逻辑 ─────────────────────────────────────────────────────────────

    def _show_start_screen(self) -> None:
        self._quiz_screen.place_forget()
        questions = self._storage.get_all()
        count = len(questions)
        if count == 0:
            self._ready_hint.config(text="题库为空，请先录入题目", fg=COLOR_ERROR)
            self._start_btn.config(state="disabled")
        else:
            self._ready_hint.config(text=f"题库中共有 {count} 道题", fg=COLOR_TEXT_MUTED)
            self._start_btn.config(state="normal")
        self._start_screen.place(relx=0.5, rely=0.5, anchor="center")

    def _start_quiz(self) -> None:
        questions = self._storage.get_all()
        if not questions:
            return
        self._questions = questions[:]
        random.shuffle(self._questions)
        self._session = QuizSession()
        self._current_index = 0
        self._start_screen.place_forget()
        self._quiz_screen.place(relx=0, rely=0, relwidth=1, relheight=1)
        self._load_question()

    # ─── 做题核心 ─────────────────────────────────────────────────────────────

    def _load_question(self) -> None:
        self._answered = False
        self._feedback_label.config(text="")
        q = self._questions[self._current_index]

        total = len(self._questions)
        self._progress_label.config(text=f"第 {self._current_index + 1} / {total} 题")
        self._question_label.config(text=q.content)

        for key in OPTION_KEYS:
            self._option_buttons[key].config(
                text=f" {key}.  {q.options.get(key, '')}",
                bg=COLOR_BTN_OPTION, fg=COLOR_TEXT, state="normal",
            )

        self._update_progress_bar()
        self._stop_timer()
        self._countdown = self._timeout_seconds
        self._update_countdown_display()
        self._start_timer()

    def _update_progress_bar(self) -> None:
        self.update_idletasks()
        w = self._progress_bar_canvas.winfo_width()
        if w <= 1:
            w = 600
        filled = int(w * (self._current_index / max(len(self._questions), 1)))
        self._progress_bar_canvas.delete("all")
        self._progress_bar_canvas.create_rectangle(0, 0, filled, 4, fill=COLOR_PRIMARY, width=0)

    def _start_timer(self) -> None:
        self._timer_id = self.after(QUIZ_COUNTDOWN_INTERVAL_MS, self._tick)

    def _stop_timer(self) -> None:
        if self._timer_id:
            self.after_cancel(self._timer_id)
            self._timer_id = None

    def _tick(self) -> None:
        self._countdown -= 1
        self._update_countdown_display()
        if self._countdown <= 0:
            self._on_timeout()
        else:
            self._timer_id = self.after(QUIZ_COUNTDOWN_INTERVAL_MS, self._tick)

    def _update_countdown_display(self) -> None:
        t = self._countdown
        total = self._timeout_seconds
        # 颜色阈值自适应：最后20%变黄，最后10%变红
        if t > total * 0.2:
            color = COLOR_SUCCESS
        elif t > total * 0.1:
            color = COLOR_WARNING
        else:
            color = COLOR_ERROR
        self._countdown_label.config(text=str(t), fg=color)

    def _on_timeout(self) -> None:
        if self._answered:
            return
        self._answered = True
        self._stop_timer()
        q = self._questions[self._current_index]
        self._session.records.append(QuizRecord(
            question_id=q.id, question_content=q.content,
            correct_answer=q.answer, user_answer=None,
            is_correct=False, is_timeout=True,
        ))
        self._option_buttons[q.answer].config(bg=COLOR_SUCCESS, fg="white")
        self._feedback_label.config(text=f"⏰  超时！正确答案是 {q.answer}", fg=COLOR_WARNING)
        for key in OPTION_KEYS:
            self._option_buttons[key].config(state="disabled")
        self.after(1500, self._next_question)

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
        for key in OPTION_KEYS:
            self._option_buttons[key].config(state="disabled")
        if is_correct:
            self._option_buttons[selected].config(bg=COLOR_SUCCESS, fg="white")
            self._feedback_label.config(text="✓  回答正确！", fg=COLOR_SUCCESS)
        else:
            self._option_buttons[selected].config(bg=COLOR_ERROR, fg="white")
            self._option_buttons[q.answer].config(bg=COLOR_SUCCESS, fg="white")
            self._feedback_label.config(text=f"✗  答错了，正确答案是 {q.answer}", fg=COLOR_ERROR)
        self.after(1200, self._next_question)

    def _next_question(self) -> None:
        self._current_index += 1
        if self._current_index >= len(self._questions):
            self._finish()
        else:
            self._load_question()

    def _finish(self) -> None:
        self._stop_timer()
        self._quiz_screen.place_forget()
        self._on_finish(self._session)

    def on_tab_shown(self) -> None:
        self._stop_timer()
        self._show_start_screen()

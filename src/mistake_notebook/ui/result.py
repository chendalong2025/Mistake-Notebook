"""结果统计界面"""

from __future__ import annotations

import tkinter as tk
from typing import Callable

from mistake_notebook.constants import (
    COLOR_BG,
    COLOR_ERROR,
    COLOR_PRIMARY,
    COLOR_SUCCESS,
    COLOR_SURFACE,
    COLOR_TEXT,
    COLOR_TEXT_MUTED,
    COLOR_WARNING,
    FONT_LABEL,
    FONT_OPTION,
    FONT_RESULT_BIG,
    FONT_SMALL,
    FONT_TITLE,
)
from mistake_notebook.models import QuizSession


class ResultFrame(tk.Frame):
    """做题结果统计界面"""

    def __init__(self, parent: tk.Widget, on_retry: Callable, on_back: Callable) -> None:
        super().__init__(parent, bg=COLOR_BG)
        self._on_retry = on_retry
        self._on_back = on_back
        self._session: QuizSession | None = None
        self._build_ui()

    def _build_ui(self) -> None:
        self.columnconfigure(0, weight=1)

        wrapper = tk.Frame(self, bg=COLOR_BG)
        wrapper.place(relx=0.5, rely=0.5, anchor="center")

        # 大图数字
        self._score_label = tk.Label(
            wrapper, text="0%",
            font=FONT_RESULT_BIG,
            bg=COLOR_BG, fg=COLOR_SUCCESS,
        )
        self._score_label.pack(pady=(0, 4))

        tk.Label(wrapper, text="正确率", font=FONT_TITLE,
                 bg=COLOR_BG, fg=COLOR_TEXT_MUTED).pack()

        # 统计卡片行
        self._stats_frame = tk.Frame(wrapper, bg=COLOR_BG)
        self._stats_frame.pack(pady=24)

        # 错题明细（Scrollable）
        detail_outer = tk.Frame(wrapper, bg=COLOR_SURFACE, padx=0, pady=0)
        detail_outer.pack(fill="x", ipadx=0)

        detail_header = tk.Frame(detail_outer, bg=COLOR_SURFACE)
        detail_header.pack(fill="x", padx=16, pady=(12, 4))
        tk.Label(detail_header, text="答题明细", font=FONT_LABEL,
                 bg=COLOR_SURFACE, fg=COLOR_TEXT_MUTED).pack(side="left")

        self._detail_canvas = tk.Canvas(detail_outer, bg=COLOR_SURFACE, height=180,
                                        highlightthickness=0)
        self._detail_canvas.pack(fill="x", padx=16, pady=(0, 12))

        self._detail_frame = tk.Frame(self._detail_canvas, bg=COLOR_SURFACE)
        self._detail_canvas.create_window((0, 0), window=self._detail_frame, anchor="nw")
        self._detail_frame.bind("<Configure>",
                                lambda e: self._detail_canvas.configure(
                                    scrollregion=self._detail_canvas.bbox("all")))

        # 按钮行
        btn_row = tk.Frame(wrapper, bg=COLOR_BG)
        btn_row.pack(pady=20)

        tk.Button(
            btn_row, text="🔄  再来一次",
            font=FONT_LABEL, fg="white", bg=COLOR_PRIMARY,
            relief="flat", padx=20, pady=10,
            cursor="hand2",
            command=self._on_retry,
        ).pack(side="left", padx=6)

        tk.Button(
            btn_row, text="回到主页",
            font=FONT_LABEL, fg=COLOR_TEXT_MUTED, bg=COLOR_SURFACE,
            relief="flat", padx=20, pady=10,
            cursor="hand2",
            command=self._on_back,
        ).pack(side="left", padx=6)

    def _make_stat_card(self, parent: tk.Frame, value: str, label: str, color: str) -> None:
        card = tk.Frame(parent, bg=COLOR_SURFACE, padx=24, pady=12)
        card.pack(side="left", padx=8)
        tk.Label(card, text=value, font=FONT_TITLE, bg=COLOR_SURFACE, fg=color).pack()
        tk.Label(card, text=label, font=FONT_SMALL, bg=COLOR_SURFACE, fg=COLOR_TEXT_MUTED).pack()

    def show_result(self, session: QuizSession) -> None:
        """更新并显示统计结果"""
        self._session = session

        # 正确率颜色
        acc = session.accuracy
        score_color = COLOR_SUCCESS if acc >= 80 else (COLOR_WARNING if acc >= 60 else COLOR_ERROR)
        self._score_label.config(text=f"{acc:.0f}%", fg=score_color)

        # 统计卡片
        for w in self._stats_frame.winfo_children():
            w.destroy()
        self._make_stat_card(self._stats_frame, str(session.total), "总题数", COLOR_TEXT)
        self._make_stat_card(self._stats_frame, str(session.correct), "答对", COLOR_SUCCESS)
        self._make_stat_card(self._stats_frame, str(session.wrong), "答错", COLOR_ERROR)
        self._make_stat_card(self._stats_frame, str(session.timeout_count), "超时", COLOR_WARNING)

        # 明细
        for w in self._detail_frame.winfo_children():
            w.destroy()
        for i, rec in enumerate(session.records, 1):
            if rec.is_timeout:
                icon, color = "⏰", COLOR_WARNING
                user_ans = "超时"
            elif rec.is_correct:
                icon, color = "✓", COLOR_SUCCESS
                user_ans = rec.user_answer or ""
            else:
                icon, color = "✗", COLOR_ERROR
                user_ans = rec.user_answer or ""

            row = tk.Frame(self._detail_frame, bg=COLOR_SURFACE)
            row.pack(fill="x", pady=2)
            preview = rec.question_content[:30] + ("..." if len(rec.question_content) > 30 else "")
            tk.Label(row, text=f"{i}. {preview}", font=FONT_SMALL,
                     bg=COLOR_SURFACE, fg=COLOR_TEXT, anchor="w").pack(side="left", fill="x", expand=True)
            detail_txt = f"{icon} {user_ans} / 正确:{rec.correct_answer}"
            tk.Label(row, text=detail_txt, font=FONT_SMALL,
                     bg=COLOR_SURFACE, fg=color, width=18, anchor="e").pack(side="right")

"""录题界面（单题录入 + 批量录入）"""

from __future__ import annotations

import re
import tkinter as tk
from tkinter import messagebox, ttk

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
    FONT_LABEL,
    FONT_OPTION,
    FONT_SMALL,
    FONT_TITLE,
    OPTION_KEYS,
)
from mistake_notebook.models import Question
from mistake_notebook.storage import QuestionStorage

# 批量导入格式说明
BATCH_FORMAT_HELP = (
    "每道题格式（空行分隔）：\n"
    "题目内容\n"
    "A. 选项A\n"
    "B. 选项B\n"
    "C. 选项C\n"
    "D. 选项D\n"
    "答案: A\n"
    "\n"
    "示例：\n"
    "中国首都是哪个城市？\n"
    "A. 上海\n"
    "B. 北京\n"
    "C. 广州\n"
    "D. 深圳\n"
    "答案: B\n"
)


class _AnswerSelector(tk.Frame):
    """卡片式正确答案选择器（白色框 + 绿色对勾）"""

    def __init__(self, parent: tk.Widget, variable: tk.StringVar) -> None:
        super().__init__(parent, bg=COLOR_SURFACE)
        self._var = variable
        self._cards: dict[str, tk.Frame] = {}
        self._var.trace_add("write", lambda *_: self._refresh())
        self._build()

    def _build(self) -> None:
        for key in OPTION_KEYS:
            card = tk.Frame(
                self, bg=COLOR_BTN_OPTION,
                padx=14, pady=8,
                cursor="hand2",
                highlightthickness=2,
                highlightbackground=COLOR_BORDER,
            )
            card.pack(side="left", padx=6)
            card.bind("<Button-1>", lambda e, k=key: self._var.set(k))

            # 字母标签
            lbl = tk.Label(card, text=key, font=("Microsoft YaHei UI", 13, "bold"),
                           bg=COLOR_BTN_OPTION, fg=COLOR_TEXT, cursor="hand2")
            lbl.pack(side="left")
            lbl.bind("<Button-1>", lambda e, k=key: self._var.set(k))

            # 对勾标签（初始隐藏）
            check = tk.Label(card, text=" ✓", font=("Microsoft YaHei UI", 13, "bold"),
                             bg=COLOR_BTN_OPTION, fg=COLOR_SUCCESS, cursor="hand2")
            check.pack(side="left")
            check.bind("<Button-1>", lambda e, k=key: self._var.set(k))

            self._cards[key] = card
            card._lbl = lbl      # type: ignore[attr-defined]
            card._check = check  # type: ignore[attr-defined]

        self._refresh()

    def _refresh(self) -> None:
        selected = self._var.get()
        for key, card in self._cards.items():
            if key == selected:
                card.config(
                    bg="#FFFFFF",
                    highlightbackground=COLOR_SUCCESS,
                    highlightthickness=2,
                )
                card._lbl.config(bg="#FFFFFF", fg="#1E1E2E")    # type: ignore[attr-defined]
                card._check.config(bg="#FFFFFF", fg=COLOR_SUCCESS)  # type: ignore[attr-defined]
            else:
                card.config(
                    bg=COLOR_BTN_OPTION,
                    highlightbackground=COLOR_BORDER,
                    highlightthickness=2,
                )
                card._lbl.config(bg=COLOR_BTN_OPTION, fg=COLOR_TEXT_MUTED)  # type: ignore[attr-defined]
                card._check.config(bg=COLOR_BTN_OPTION, fg=COLOR_BTN_OPTION)  # 隐藏勾（同背景色）  # type: ignore[attr-defined]


class AddQuestionFrame(tk.Frame):
    """录题界面（含单题 / 批量 两个子Tab）"""

    def __init__(self, parent: tk.Widget) -> None:
        super().__init__(parent, bg=COLOR_BG)
        self._storage = QuestionStorage()
        self._selected_answer = tk.StringVar(value="A")
        self._build_ui()

    # ─── 构建UI ──────────────────────────────────────────────────────────────

    def _build_ui(self) -> None:
        self.columnconfigure(0, weight=1)
        self.rowconfigure(1, weight=1)
        self._build_sub_tabs()
        self._build_list()

    def _build_sub_tabs(self) -> None:
        """顶部子Tab：单题录入 / 批量录入"""
        tab_bar = tk.Frame(self, bg=COLOR_BG, padx=24, pady=0)
        tab_bar.grid(row=0, column=0, sticky="ew")

        self._sub_tab_var = tk.StringVar(value="single")
        self._tab_labels: dict[str, tk.Label] = {}

        for tab_id, tab_text in [("single", "单题录入"), ("batch", "批量录入")]:
            lbl = tk.Label(
                tab_bar, text=tab_text,
                font=FONT_LABEL, bg=COLOR_BG,
                padx=14, pady=10, cursor="hand2",
            )
            lbl.pack(side="left")
            lbl.bind("<Button-1>", lambda e, t=tab_id: self._switch_sub_tab(t))
            self._tab_labels[tab_id] = lbl

        # 内容容器
        self._single_frame = self._build_single_form()
        self._batch_frame = self._build_batch_form()
        self._switch_sub_tab("single")

    def _switch_sub_tab(self, tab_id: str) -> None:
        for tid, lbl in self._tab_labels.items():
            if tid == tab_id:
                lbl.config(fg=COLOR_TEXT,
                           font=("Microsoft YaHei UI", 11, "bold"))
            else:
                lbl.config(fg=COLOR_TEXT_MUTED,
                           font=FONT_LABEL)
        if tab_id == "single":
            self._batch_frame.grid_remove()
            self._single_frame.grid(row=0, column=0, sticky="ew")
        else:
            self._single_frame.grid_remove()
            self._batch_frame.grid(row=0, column=0, sticky="ew")

    # ── 单题录入表单 ──────────────────────────────────────────────────────────

    def _build_single_form(self) -> tk.Frame:
        outer = tk.Frame(self, bg=COLOR_BG, padx=24, pady=8)
        outer.columnconfigure(0, weight=1)

        card = tk.Frame(outer, bg=COLOR_SURFACE, padx=20, pady=16)
        card.grid(sticky="ew")
        card.columnconfigure(0, weight=1)

        # 题目
        tk.Label(card, text="题目内容", font=FONT_LABEL,
                 bg=COLOR_SURFACE, fg=COLOR_TEXT_MUTED).grid(sticky="w")
        self._content_text = tk.Text(
            card, height=3, font=FONT_OPTION,
            bg=COLOR_BTN_OPTION, fg=COLOR_TEXT,
            insertbackground=COLOR_TEXT,
            relief="flat", padx=8, pady=6, wrap="word",
        )
        self._content_text.grid(sticky="ew", pady=(4, 12))

        # 选项 2x2
        self._option_entries: dict[str, tk.Entry] = {}
        opt_frame = tk.Frame(card, bg=COLOR_SURFACE)
        opt_frame.grid(sticky="ew", pady=(0, 12))
        opt_frame.columnconfigure(1, weight=1)
        opt_frame.columnconfigure(3, weight=1)

        positions = [("A", 0, 0), ("B", 0, 2), ("C", 1, 0), ("D", 1, 2)]
        for key, row, col in positions:
            tk.Label(opt_frame, text=f"选项 {key}", font=FONT_LABEL,
                     bg=COLOR_SURFACE, fg=COLOR_TEXT_MUTED,
                     width=6, anchor="w").grid(row=row, column=col, sticky="w",
                                               padx=(0, 4), pady=4)
            entry = tk.Entry(opt_frame, font=FONT_OPTION,
                             bg=COLOR_BTN_OPTION, fg=COLOR_TEXT,
                             insertbackground=COLOR_TEXT, relief="flat")
            entry.grid(row=row, column=col + 1, sticky="ew",
                       padx=(0, 16), pady=4, ipady=4)
            self._option_entries[key] = entry

        # 正确答案（卡片式）
        ans_row = tk.Frame(card, bg=COLOR_SURFACE)
        ans_row.grid(sticky="w", pady=(0, 14))
        tk.Label(ans_row, text="正确答案：", font=FONT_LABEL,
                 bg=COLOR_SURFACE, fg=COLOR_TEXT_MUTED).pack(side="left")
        _AnswerSelector(ans_row, self._selected_answer).pack(side="left", padx=(8, 0))

        # 按钮行
        btn_row = tk.Frame(card, bg=COLOR_SURFACE)
        btn_row.grid(sticky="ew")
        tk.Button(
            btn_row, text="💾  保存题目",
            font=FONT_LABEL, fg="white", bg=COLOR_PRIMARY,
            relief="flat", padx=20, pady=8,
            activebackground=COLOR_PRIMARY, activeforeground="white",
            cursor="hand2", command=self._on_save,
        ).pack(side="left")
        tk.Button(
            btn_row, text="清空",
            font=FONT_SMALL, fg=COLOR_TEXT_MUTED, bg=COLOR_BG,
            relief="flat", padx=12, pady=8,
            activebackground=COLOR_BG, cursor="hand2",
            command=self._clear_form,
        ).pack(side="left", padx=8)
        self._status_label = tk.Label(btn_row, text="", font=FONT_SMALL, bg=COLOR_SURFACE)
        self._status_label.pack(side="left", padx=8)

        return outer

    # ── 批量录入表单 ──────────────────────────────────────────────────────────

    def _build_batch_form(self) -> tk.Frame:
        outer = tk.Frame(self, bg=COLOR_BG, padx=24, pady=8)
        outer.columnconfigure(0, weight=1)

        card = tk.Frame(outer, bg=COLOR_SURFACE, padx=20, pady=16)
        card.grid(sticky="ew")
        card.columnconfigure(0, weight=1)

        # 说明
        help_row = tk.Frame(card, bg=COLOR_SURFACE)
        help_row.grid(sticky="ew", pady=(0, 8))
        tk.Label(help_row, text="📋 批量录入", font=FONT_LABEL,
                 bg=COLOR_SURFACE, fg=COLOR_TEXT).pack(side="left")
        tk.Label(help_row,
                 text="格式：题目 → A./B./C./D. 选项 → 答案: X（空行分隔多题）",
                 font=FONT_SMALL, bg=COLOR_SURFACE, fg=COLOR_TEXT_MUTED).pack(side="left", padx=12)

        # 文本框
        self._batch_text = tk.Text(
            card, height=10, font=("Consolas", 11),
            bg=COLOR_BTN_OPTION, fg=COLOR_TEXT,
            insertbackground=COLOR_TEXT,
            relief="flat", padx=8, pady=6, wrap="word",
        )
        self._batch_text.grid(sticky="ew", pady=(0, 8))
        # 插入格式提示占位
        self._batch_text.insert("1.0", BATCH_FORMAT_HELP)
        self._batch_text.config(fg=COLOR_TEXT_MUTED)
        self._batch_text.bind("<FocusIn>", self._on_batch_focus_in)
        self._batch_text.bind("<FocusOut>", self._on_batch_focus_out)
        self._batch_placeholder_active = True

        # 按钮行
        btn_row = tk.Frame(card, bg=COLOR_SURFACE)
        btn_row.grid(sticky="ew")
        tk.Button(
            btn_row, text="📥  批量导入",
            font=FONT_LABEL, fg="white", bg=COLOR_PRIMARY,
            relief="flat", padx=20, pady=8,
            activebackground=COLOR_PRIMARY, activeforeground="white",
            cursor="hand2", command=self._on_batch_import,
        ).pack(side="left")
        tk.Button(
            btn_row, text="清空",
            font=FONT_SMALL, fg=COLOR_TEXT_MUTED, bg=COLOR_BG,
            relief="flat", padx=12, pady=8,
            activebackground=COLOR_BG, cursor="hand2",
            command=self._clear_batch,
        ).pack(side="left", padx=8)
        self._batch_status = tk.Label(btn_row, text="", font=FONT_SMALL, bg=COLOR_SURFACE)
        self._batch_status.pack(side="left", padx=8)

        return outer

    def _on_batch_focus_in(self, _: object) -> None:
        if self._batch_placeholder_active:
            self._batch_text.delete("1.0", "end")
            self._batch_text.config(fg=COLOR_TEXT)
            self._batch_placeholder_active = False

    def _on_batch_focus_out(self, _: object) -> None:
        content = self._batch_text.get("1.0", "end").strip()
        if not content:
            self._batch_text.insert("1.0", BATCH_FORMAT_HELP)
            self._batch_text.config(fg=COLOR_TEXT_MUTED)
            self._batch_placeholder_active = True

    # ─── 题库列表区域 ─────────────────────────────────────────────────────────

    def _build_list(self) -> None:
        list_outer = tk.Frame(self, bg=COLOR_BG, padx=24)
        list_outer.grid(row=1, column=0, sticky="nsew", pady=(0, 16))

        header = tk.Frame(list_outer, bg=COLOR_BG)
        header.pack(fill="x", pady=(0, 8))
        self._list_title = tk.Label(header, text="", font=FONT_LABEL,
                                    bg=COLOR_BG, fg=COLOR_TEXT_MUTED)
        self._list_title.pack(side="left")

        # Treeview
        style = ttk.Style()
        style.theme_use("clam")
        style.configure("Dark.Treeview",
                        background=COLOR_SURFACE, foreground=COLOR_TEXT,
                        fieldbackground=COLOR_SURFACE, borderwidth=0, rowheight=30)
        style.configure("Dark.Treeview.Heading",
                        background=COLOR_BTN_OPTION, foreground=COLOR_TEXT_MUTED,
                        relief="flat", font=FONT_SMALL)
        style.map("Dark.Treeview", background=[("selected", COLOR_PRIMARY)])

        tree_frame = tk.Frame(list_outer, bg=COLOR_SURFACE)
        tree_frame.pack(fill="both", expand=True)

        self._tree = ttk.Treeview(
            tree_frame, style="Dark.Treeview",
            columns=("no", "content", "answer"), show="headings", selectmode="browse",
        )
        self._tree.heading("no", text="#")
        self._tree.heading("content", text="题目")
        self._tree.heading("answer", text="答案")
        self._tree.column("no", width=40, anchor="center", stretch=False)
        self._tree.column("content", anchor="w")
        self._tree.column("answer", width=60, anchor="center", stretch=False)

        scrollbar = ttk.Scrollbar(tree_frame, orient="vertical", command=self._tree.yview)
        self._tree.configure(yscrollcommand=scrollbar.set)
        self._tree.pack(side="left", fill="both", expand=True)
        scrollbar.pack(side="right", fill="y")

        tk.Button(
            list_outer, text="🗑  删除选中",
            font=FONT_SMALL, fg=COLOR_TEXT_MUTED, bg=COLOR_BG,
            relief="flat", padx=10, pady=4, cursor="hand2",
            command=self._on_delete,
        ).pack(anchor="e", pady=(6, 0))

        self._refresh_list()

    # ─── 事件处理 ─────────────────────────────────────────────────────────────

    def _on_save(self) -> None:
        content = self._content_text.get("1.0", "end").strip()
        options = {k: self._option_entries[k].get().strip() for k in OPTION_KEYS}
        answer = self._selected_answer.get()
        if not content:
            self._show_status("题目内容不能为空", COLOR_WARNING)
            return
        if any(not v for v in options.values()):
            self._show_status("请填写所有选项(ABCD)", COLOR_WARNING)
            return
        self._storage.add(Question(content=content, options=options, answer=answer))
        self._show_status("✓ 保存成功！", COLOR_SUCCESS)
        self._clear_form()
        self._refresh_list()

    def _on_batch_import(self) -> None:
        if self._batch_placeholder_active:
            self._show_batch_status("请先填写题目内容", COLOR_WARNING)
            return
        raw = self._batch_text.get("1.0", "end").strip()
        if not raw:
            self._show_batch_status("内容为空", COLOR_WARNING)
            return

        questions, errors = self._parse_batch(raw)
        if not questions and errors:
            self._show_batch_status(f"解析失败：{errors[0]}", COLOR_ERROR)
            return

        for q in questions:
            self._storage.add(q)

        msg = f"✓ 成功导入 {len(questions)} 道题"
        if errors:
            msg += f"，{len(errors)} 道格式有误已跳过"
        self._show_batch_status(msg, COLOR_SUCCESS)
        self._clear_batch()
        self._refresh_list()

    def _parse_batch(self, raw: str) -> tuple[list[Question], list[str]]:
        """解析批量导入文本，返回(题目列表, 错误列表)"""
        questions: list[Question] = []
        errors: list[str] = []

        # 按空行分割每道题
        blocks = re.split(r"\n\s*\n", raw.strip())
        for block in blocks:
            block = block.strip()
            if not block:
                continue
            lines = [ln.strip() for ln in block.splitlines() if ln.strip()]
            if len(lines) < 6:
                errors.append(f"题目不完整：{lines[0][:20] if lines else '(空)'}")
                continue

            content = lines[0]
            options: dict[str, str] = {}
            answer = ""

            for line in lines[1:]:
                # 选项行：A. / A、/ A) / A：
                m = re.match(r"^([ABCD])[.、\)：:]\s*(.+)$", line, re.IGNORECASE)
                if m:
                    options[m.group(1).upper()] = m.group(2).strip()
                    continue
                # 答案行：答案: A / 答案：A / Answer: A
                m2 = re.match(r"^(?:答案|answer)[：:]\s*([ABCD])\s*$", line, re.IGNORECASE)
                if m2:
                    answer = m2.group(1).upper()

            if len(options) != 4:
                errors.append(f"选项不完整（需ABCD）：{content[:20]}")
                continue
            if answer not in OPTION_KEYS:
                errors.append(f"答案缺失或无效：{content[:20]}")
                continue

            questions.append(Question(content=content, options=options, answer=answer))

        return questions, errors

    def _on_delete(self) -> None:
        selected = self._tree.selection()
        if not selected:
            return
        tags = self._tree.item(selected[0], "tags")
        if tags:
            if messagebox.askyesno("确认删除", "确定要删除这道题吗？"):
                self._storage.delete(tags[0])
                self._refresh_list()

    def _clear_form(self) -> None:
        self._content_text.delete("1.0", "end")
        for entry in self._option_entries.values():
            entry.delete(0, "end")
        self._selected_answer.set("A")

    def _clear_batch(self) -> None:
        self._batch_text.delete("1.0", "end")
        self._batch_text.insert("1.0", BATCH_FORMAT_HELP)
        self._batch_text.config(fg=COLOR_TEXT_MUTED)
        self._batch_placeholder_active = True

    def _show_status(self, msg: str, color: str = COLOR_TEXT) -> None:
        self._status_label.config(text=msg, fg=color)
        self.after(3000, lambda: self._status_label.config(text=""))

    def _show_batch_status(self, msg: str, color: str = COLOR_TEXT) -> None:
        self._batch_status.config(text=msg, fg=color)
        self.after(4000, lambda: self._batch_status.config(text=""))

    def _refresh_list(self) -> None:
        for item in self._tree.get_children():
            self._tree.delete(item)
        questions = self._storage.get_all()
        self._list_title.config(text=f"题库共 {len(questions)} 道题")
        for i, q in enumerate(questions, 1):
            preview = q.content[:42] + ("..." if len(q.content) > 42 else "")
            self._tree.insert("", "end", values=(i, preview, q.answer), tags=(q.id,))

    def on_tab_shown(self) -> None:
        self._refresh_list()

"""录题界面 Screen（单题录入 + 批量录入 + 题库列表）"""

from __future__ import annotations

import re

from kivy.clock import Clock
from kivy.metrics import dp
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.gridlayout import GridLayout
from kivy.uix.label import Label
from kivy.uix.recycleview import RecycleView
from kivy.uix.recycleview.views import RecycleDataViewBehavior
from kivy.uix.screenmanager import Screen
from kivy.uix.scrollview import ScrollView
from kivy.uix.textinput import TextInput

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
)
from mistake_notebook.models import Question
from mistake_notebook.storage import QuestionStorage

# 批量录入格式提示
BATCH_PLACEHOLDER = (
    "每道题格式（空行分隔）：\n"
    "题目内容\n"
    "A. 选项A\n"
    "B. 选项B\n"
    "C. 选项C\n"
    "D. 选项D\n"
    "答案: A\n\n"
    "示例：\n"
    "中国首都是哪个城市？\n"
    "A. 上海\n"
    "B. 北京\n"
    "C. 广州\n"
    "D. 深圳\n"
    "答案: B"
)


def _hex(color: str) -> list[float]:
    """将 '#RRGGBB' 转换为 Kivy rgba [r,g,b,1]"""
    c = color.lstrip("#")
    r, g, b = int(c[0:2], 16), int(c[2:4], 16), int(c[4:6], 16)
    return [r / 255, g / 255, b / 255, 1]


class AddScreen(Screen):
    """录题界面"""

    def __init__(self, **kwargs) -> None:
        super().__init__(**kwargs)
        self._storage = QuestionStorage()
        self._selected_answer = "A"
        self._current_tab = "single"  # 'single' | 'batch'
        self._status_event = None
        self._batch_focused = False
        self._build_ui()

    # ─── 构建 UI ─────────────────────────────────────────────────────────────

    def _build_ui(self) -> None:
        from kivy.graphics import Color, Rectangle

        root = BoxLayout(orientation="vertical", spacing=0)
        with root.canvas.before:
            Color(*_hex(COLOR_BG))
            self._bg_rect = Rectangle(size=root.size, pos=root.pos)
        root.bind(size=lambda w, v: setattr(self._bg_rect, "size", v),
                  pos=lambda w, v: setattr(self._bg_rect, "pos", v))

        # ── 子Tab栏 ──
        root.add_widget(self._build_sub_tab_bar())

        # ── 表单区（单题/批量）──
        self._single_form = self._build_single_form()
        self._batch_form = self._build_batch_form()
        root.add_widget(self._single_form)
        root.add_widget(self._batch_form)
        self._switch_sub_tab("single")

        # ── 题库列表 ──
        root.add_widget(self._build_list_section())

        self.add_widget(root)

    def _build_sub_tab_bar(self) -> BoxLayout:
        from kivy.graphics import Color, Rectangle

        bar = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(48), padding=dp(8))
        with bar.canvas.before:
            Color(*_hex(COLOR_BG))
            rect = Rectangle(size=bar.size, pos=bar.pos)
        bar.bind(size=lambda w, v: setattr(rect, "size", v),
                 pos=lambda w, v: setattr(rect, "pos", v))

        self._tab_btns: dict[str, Button] = {}
        for tab_id, tab_text in [("single", "单题录入"), ("batch", "批量录入")]:
            btn = Button(
                text=tab_text,
                size_hint_x=None, width=dp(110),
                background_color=_hex(COLOR_BG),
                color=_hex(COLOR_TEXT_MUTED),
                font_name="assets/fonts/NotoSansSC.ttf",
                font_size=dp(15),
                background_normal="",
            )
            btn.bind(on_release=lambda b, t=tab_id: self._switch_sub_tab(t))
            self._tab_btns[tab_id] = btn
            bar.add_widget(btn)

        return bar

    def _switch_sub_tab(self, tab_id: str) -> None:
        self._current_tab = tab_id
        for tid, btn in self._tab_btns.items():
            if tid == tab_id:
                btn.bold = True
                btn.color = _hex(COLOR_TEXT)
            else:
                btn.bold = False
                btn.color = _hex(COLOR_TEXT_MUTED)

        self._single_form.opacity = 1 if tab_id == "single" else 0
        self._single_form.disabled = tab_id != "single"
        self._batch_form.opacity = 1 if tab_id == "batch" else 0
        self._batch_form.disabled = tab_id != "batch"

    # ── 单题录入表单 ─────────────────────────────────────────────────────────

    def _build_single_form(self) -> BoxLayout:
        from kivy.graphics import Color, Rectangle

        outer = BoxLayout(orientation="vertical", size_hint_y=None, height=dp(340),
                          padding=dp(16), spacing=dp(8))
        with outer.canvas.before:
            Color(*_hex(COLOR_SURFACE))
            rect = Rectangle(size=outer.size, pos=outer.pos)
        outer.bind(size=lambda w, v: setattr(rect, "size", v),
                   pos=lambda w, v: setattr(rect, "pos", v))

        # 题目
        outer.add_widget(Label(text="题目内容", color=_hex(COLOR_TEXT_MUTED),
                               size_hint_y=None, height=dp(24),
                               font_name="assets/fonts/NotoSansSC.ttf",
                               halign="left", text_size=(None, None)))
        self._content_input = TextInput(
            hint_text="请输入题目内容...",
            size_hint_y=None, height=dp(72),
            background_color=_hex(COLOR_BTN_OPTION),
            foreground_color=_hex(COLOR_TEXT),
            font_name="assets/fonts/NotoSansSC.ttf",
            multiline=True, font_size=dp(15),
        )
        outer.add_widget(self._content_input)

        # 选项 2x2
        self._option_inputs: dict[str, TextInput] = {}
        grid = GridLayout(cols=2, size_hint_y=None, height=dp(100), spacing=dp(8))
        for key in OPTION_KEYS:
            row = BoxLayout(orientation="horizontal", spacing=dp(6))
            row.add_widget(Label(text=f"{key}.", size_hint_x=None, width=dp(24),
                                 color=_hex(COLOR_TEXT_MUTED),
                                 font_name="assets/fonts/NotoSansSC.ttf"))
            ti = TextInput(
                hint_text=f"选项{key}",
                size_hint_y=None, height=dp(40),
                background_color=_hex(COLOR_BTN_OPTION),
                foreground_color=_hex(COLOR_TEXT),
                font_name="assets/fonts/NotoSansSC.ttf",
                multiline=False, font_size=dp(14),
            )
            self._option_inputs[key] = ti
            row.add_widget(ti)
            grid.add_widget(row)
        outer.add_widget(grid)

        # 答案选择
        ans_row = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(48),
                            spacing=dp(8))
        ans_row.add_widget(Label(text="正确答案：", size_hint_x=None, width=dp(80),
                                 color=_hex(COLOR_TEXT_MUTED),
                                 font_name="assets/fonts/NotoSansSC.ttf"))
        self._ans_btns: dict[str, Button] = {}
        for key in OPTION_KEYS:
            btn = Button(
                text=f"{key} ✓",
                size_hint_x=None, width=dp(56),
                background_normal="",
                font_name="assets/fonts/NotoSansSC.ttf",
                font_size=dp(14),
            )
            btn.bind(on_release=lambda b, k=key: self._select_answer(k))
            self._ans_btns[key] = btn
            ans_row.add_widget(btn)
        self._select_answer("A")
        outer.add_widget(ans_row)

        # 按钮行
        btn_row = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(48), spacing=dp(8))
        save_btn = Button(
            text="💾  保存题目",
            size_hint_x=None, width=dp(130),
            background_normal="",
            background_color=_hex(COLOR_PRIMARY),
            color=_hex("#FFFFFF"),
            font_name="assets/fonts/NotoSansSC.ttf",
            font_size=dp(15),
        )
        save_btn.bind(on_release=lambda b: self._on_save())
        clear_btn = Button(
            text="清空",
            size_hint_x=None, width=dp(70),
            background_normal="",
            background_color=_hex(COLOR_BTN_OPTION),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
        )
        clear_btn.bind(on_release=lambda b: self._clear_form())
        self._status_label = Label(text="", color=_hex(COLOR_SUCCESS),
                                   font_name="assets/fonts/NotoSansSC.ttf")
        btn_row.add_widget(save_btn)
        btn_row.add_widget(clear_btn)
        btn_row.add_widget(self._status_label)
        outer.add_widget(btn_row)

        return outer

    def _select_answer(self, key: str) -> None:
        self._selected_answer = key
        for k, btn in self._ans_btns.items():
            if k == key:
                btn.background_color = _hex(COLOR_SUCCESS)
                btn.color = _hex("#1E1E2E")
            else:
                btn.background_color = _hex(COLOR_BTN_OPTION)
                btn.color = _hex(COLOR_TEXT_MUTED)

    # ── 批量录入表单 ─────────────────────────────────────────────────────────

    def _build_batch_form(self) -> BoxLayout:
        from kivy.graphics import Color, Rectangle

        outer = BoxLayout(orientation="vertical", size_hint_y=None, height=dp(340),
                          padding=dp(16), spacing=dp(8))
        with outer.canvas.before:
            Color(*_hex(COLOR_SURFACE))
            rect = Rectangle(size=outer.size, pos=outer.pos)
        outer.bind(size=lambda w, v: setattr(rect, "size", v),
                   pos=lambda w, v: setattr(rect, "pos", v))

        outer.add_widget(Label(
            text="批量录入  |  格式：题目 → A./B./C./D. → 答案: X（空行分隔）",
            color=_hex(COLOR_TEXT_MUTED),
            size_hint_y=None, height=dp(24),
            font_name="assets/fonts/NotoSansSC.ttf",
            halign="left", text_size=(None, None),
        ))

        self._batch_input = TextInput(
            text=BATCH_PLACEHOLDER,
            size_hint_y=1,
            background_color=_hex(COLOR_BTN_OPTION),
            foreground_color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
            multiline=True, font_size=dp(13),
        )
        self._batch_input.bind(focus=self._on_batch_focus)
        outer.add_widget(self._batch_input)

        btn_row = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(48), spacing=dp(8))
        import_btn = Button(
            text="📥  批量导入",
            size_hint_x=None, width=dp(130),
            background_normal="",
            background_color=_hex(COLOR_PRIMARY),
            color=_hex("#FFFFFF"),
            font_name="assets/fonts/NotoSansSC.ttf",
        )
        import_btn.bind(on_release=lambda b: self._on_batch_import())
        clear_btn = Button(
            text="清空",
            size_hint_x=None, width=dp(70),
            background_normal="",
            background_color=_hex(COLOR_BTN_OPTION),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
        )
        clear_btn.bind(on_release=lambda b: self._clear_batch())
        self._batch_status = Label(text="", color=_hex(COLOR_SUCCESS),
                                   font_name="assets/fonts/NotoSansSC.ttf")
        btn_row.add_widget(import_btn)
        btn_row.add_widget(clear_btn)
        btn_row.add_widget(self._batch_status)
        outer.add_widget(btn_row)

        return outer

    def _on_batch_focus(self, instance: TextInput, focused: bool) -> None:
        if focused and not self._batch_focused:
            self._batch_focused = True
            instance.text = ""
            instance.foreground_color = _hex(COLOR_TEXT)
        elif not focused and not instance.text.strip():
            self._batch_focused = False
            instance.text = BATCH_PLACEHOLDER
            instance.foreground_color = _hex(COLOR_TEXT_MUTED)

    # ── 题库列表 ─────────────────────────────────────────────────────────────

    def _build_list_section(self) -> BoxLayout:
        from kivy.graphics import Color, Rectangle

        section = BoxLayout(orientation="vertical", padding=dp(12), spacing=dp(8))
        with section.canvas.before:
            Color(*_hex(COLOR_BG))
            rect = Rectangle(size=section.size, pos=section.pos)
        section.bind(size=lambda w, v: setattr(rect, "size", v),
                     pos=lambda w, v: setattr(rect, "pos", v))

        header = BoxLayout(orientation="horizontal", size_hint_y=None, height=dp(32))
        self._list_title = Label(text="题库共 0 道题", color=_hex(COLOR_TEXT_MUTED),
                                 font_name="assets/fonts/NotoSansSC.ttf",
                                 halign="left", text_size=(None, None))
        del_btn = Button(
            text="🗑  删除选中",
            size_hint_x=None, width=dp(110),
            background_normal="",
            background_color=_hex(COLOR_BTN_OPTION),
            color=_hex(COLOR_TEXT_MUTED),
            font_name="assets/fonts/NotoSansSC.ttf",
            font_size=dp(13),
        )
        del_btn.bind(on_release=lambda b: self._on_delete())
        header.add_widget(self._list_title)
        header.add_widget(del_btn)
        section.add_widget(header)

        # RecycleView 列表
        self._rv = QuestionListView(size_hint=(1, 1))
        section.add_widget(self._rv)

        return section

    # ─── 事件处理 ────────────────────────────────────────────────────────────

    def _on_save(self) -> None:
        content = self._content_input.text.strip()
        options = {k: self._option_inputs[k].text.strip() for k in OPTION_KEYS}
        answer = self._selected_answer
        if not content:
            self._show_status(self._status_label, "题目内容不能为空", COLOR_WARNING)
            return
        if any(not v for v in options.values()):
            self._show_status(self._status_label, "请填写所有选项(ABCD)", COLOR_WARNING)
            return
        self._storage.add(Question(content=content, options=options, answer=answer))
        self._show_status(self._status_label, "✓ 保存成功！", COLOR_SUCCESS)
        self._clear_form()
        self._refresh_list()

    def _on_batch_import(self) -> None:
        if not self._batch_focused:
            self._show_status(self._batch_status, "请先填写题目内容", COLOR_WARNING)
            return
        raw = self._batch_input.text.strip()
        if not raw:
            self._show_status(self._batch_status, "内容为空", COLOR_WARNING)
            return
        questions, errors = self._parse_batch(raw)
        if not questions and errors:
            self._show_status(self._batch_status, f"解析失败：{errors[0]}", COLOR_ERROR)
            return
        for q in questions:
            self._storage.add(q)
        msg = f"✓ 成功导入 {len(questions)} 道题"
        if errors:
            msg += f"，{len(errors)} 道跳过"
        self._show_status(self._batch_status, msg, COLOR_SUCCESS)
        self._clear_batch()
        self._refresh_list()

    def _parse_batch(self, raw: str) -> tuple[list[Question], list[str]]:
        questions: list[Question] = []
        errors: list[str] = []
        blocks = re.split(r"\n\s*\n", raw.strip())
        for block in blocks:
            block = block.strip()
            if not block:
                continue
            lines = [ln.strip() for ln in block.splitlines() if ln.strip()]
            if len(lines) < 6:
                errors.append(f"不完整：{lines[0][:20] if lines else '(空)'}")
                continue
            content = lines[0]
            options: dict[str, str] = {}
            answer = ""
            for line in lines[1:]:
                m = re.match(r"^([ABCD])[.、\)：:]\s*(.+)$", line, re.IGNORECASE)
                if m:
                    options[m.group(1).upper()] = m.group(2).strip()
                    continue
                m2 = re.match(r"^(?:答案|answer)[：:]\s*([ABCD])\s*$", line, re.IGNORECASE)
                if m2:
                    answer = m2.group(1).upper()
            if len(options) != 4:
                errors.append(f"选项不完整：{content[:20]}")
                continue
            if answer not in OPTION_KEYS:
                errors.append(f"答案缺失：{content[:20]}")
                continue
            questions.append(Question(content=content, options=options, answer=answer))
        return questions, errors

    def _on_delete(self) -> None:
        selected = self._rv.get_selected()
        if selected:
            self._storage.delete(selected)
            self._refresh_list()

    def _clear_form(self) -> None:
        self._content_input.text = ""
        for ti in self._option_inputs.values():
            ti.text = ""
        self._select_answer("A")

    def _clear_batch(self) -> None:
        self._batch_input.text = BATCH_PLACEHOLDER
        self._batch_input.foreground_color = _hex(COLOR_TEXT_MUTED)
        self._batch_focused = False

    def _show_status(self, label: Label, msg: str, color: str) -> None:
        label.text = msg
        label.color = _hex(color)
        if self._status_event:
            self._status_event.cancel()
        self._status_event = Clock.schedule_once(
            lambda dt: setattr(label, "text", ""), 3
        )

    def _refresh_list(self) -> None:
        questions = self._storage.get_all()
        self._list_title.text = f"题库共 {len(questions)} 道题"
        self._rv.update_data(questions)

    def on_enter(self) -> None:
        """Screen 进入时刷新列表"""
        self._refresh_list()


# ─── RecycleView 题库列表 ─────────────────────────────────────────────────────

class QuestionListView(RecycleView):
    """题库 RecycleView"""

    def __init__(self, **kwargs) -> None:
        super().__init__(**kwargs)
        self._selected_id: str | None = None
        from kivy.uix.recycleboxlayout import RecycleBoxLayout
        layout = RecycleBoxLayout(
            default_size=(None, dp(44)),
            default_size_hint=(1, None),
            size_hint_y=None,
            orientation="vertical",
        )
        layout.bind(minimum_height=layout.setter("height"))
        self.add_widget(layout)
        self.viewclass = "QuestionRow"
        self.data = []

    def update_data(self, questions: list) -> None:
        self._selected_id = None
        self.data = [
            {
                "idx": i + 1,
                "content": q.content[:40] + ("..." if len(q.content) > 40 else ""),
                "answer": q.answer,
                "qid": q.id,
                "rv": self,
            }
            for i, q in enumerate(questions)
        ]

    def select(self, qid: str) -> None:
        self._selected_id = qid

    def get_selected(self) -> str | None:
        return self._selected_id


class QuestionRow(RecycleDataViewBehavior, BoxLayout):
    """题库列表行"""

    def __init__(self, **kwargs) -> None:
        super().__init__(orientation="horizontal", size_hint_y=None, height=dp(44),
                         padding=(dp(8), 0), spacing=dp(8), **kwargs)
        from kivy.graphics import Color, Rectangle
        with self.canvas.before:
            self._bg_color = Color(*_hex(COLOR_SURFACE))
            self._bg_rect = Rectangle(size=self.size, pos=self.pos)
        self.bind(size=lambda w, v: setattr(self._bg_rect, "size", v),
                  pos=lambda w, v: setattr(self._bg_rect, "pos", v))

        self._no_lbl = Label(size_hint_x=None, width=dp(36),
                             color=_hex(COLOR_TEXT_MUTED),
                             font_name="assets/fonts/NotoSansSC.ttf", font_size=dp(13))
        self._content_lbl = Label(halign="left", valign="middle",
                                   color=_hex(COLOR_TEXT),
                                   font_name="assets/fonts/NotoSansSC.ttf", font_size=dp(13))
        self._content_lbl.bind(size=self._content_lbl.setter("text_size"))
        self._ans_lbl = Label(size_hint_x=None, width=dp(40),
                               color=_hex(COLOR_PRIMARY),
                               font_name="assets/fonts/NotoSansSC.ttf", font_size=dp(13))
        self.add_widget(self._no_lbl)
        self.add_widget(self._content_lbl)
        self.add_widget(self._ans_lbl)

        self._qid: str = ""
        self._rv: QuestionListView | None = None
        self._selected = False

    def refresh_view_attrs(self, rv, index, data):
        self._qid = data["qid"]
        self._rv = data["rv"]
        self._no_lbl.text = str(data["idx"])
        self._content_lbl.text = data["content"]
        self._ans_lbl.text = data["answer"]
        self._set_selected(False)
        return super().refresh_view_attrs(rv, index, data)

    def on_touch_down(self, touch):
        if self.collide_point(*touch.pos) and self._rv:
            self._rv.select(self._qid)
            self._set_selected(True)
            return True
        return super().on_touch_down(touch)

    def _set_selected(self, selected: bool) -> None:
        self._selected = selected
        color = _hex("#3D3D6C") if selected else _hex(COLOR_SURFACE)
        self._bg_color.rgba = color

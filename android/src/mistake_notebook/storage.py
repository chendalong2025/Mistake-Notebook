"""数据持久化 - JSON存储（单例模式），适配 Android 路径"""

from __future__ import annotations

import json
from pathlib import Path
from typing import List, Optional

from mistake_notebook.models import Question

# 数据目录：运行时由 App 注入（Android 用 user_data_dir，PC 用项目目录）
_data_dir: Optional[Path] = None
_data_file: Optional[Path] = None


def init_storage(data_dir: Path) -> None:
    """由 App 启动时调用，设置数据目录（适配 Android / PC 均可）"""
    global _data_dir, _data_file
    _data_dir = data_dir
    _data_file = data_dir / "questions.json"


class QuestionStorage:
    """题目存储，单例模式"""

    _instance: Optional["QuestionStorage"] = None
    _questions: List[Question] = []

    def __new__(cls) -> "QuestionStorage":
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._loaded = False
        return cls._instance

    def _get_data_file(self) -> Path:
        if _data_file is None:
            # 回退：使用当前工作目录下的 data 目录
            fallback = Path(".") / "data"
            fallback.mkdir(parents=True, exist_ok=True)
            return fallback / "questions.json"
        return _data_file

    def _ensure_loaded(self) -> None:
        if not self._loaded:
            self.load()
            self._loaded = True

    def load(self) -> None:
        """从磁盘加载题目"""
        data_file = self._get_data_file()
        data_file.parent.mkdir(parents=True, exist_ok=True)
        if data_file.exists():
            try:
                with open(data_file, encoding="utf-8") as f:
                    data = json.load(f)
                self._questions = [Question.from_dict(d) for d in data]
            except (json.JSONDecodeError, KeyError):
                self._questions = []
        else:
            self._questions = []

    def save(self) -> None:
        """持久化到磁盘"""
        data_file = self._get_data_file()
        data_file.parent.mkdir(parents=True, exist_ok=True)
        with open(data_file, "w", encoding="utf-8") as f:
            json.dump([q.to_dict() for q in self._questions], f, ensure_ascii=False, indent=2)

    def add(self, question: Question) -> None:
        self._ensure_loaded()
        self._questions.append(question)
        self.save()

    def get_all(self) -> List[Question]:
        self._ensure_loaded()
        return list(self._questions)

    def delete(self, question_id: str) -> bool:
        self._ensure_loaded()
        before = len(self._questions)
        self._questions = [q for q in self._questions if q.id != question_id]
        if len(self._questions) < before:
            self.save()
            return True
        return False

    def count(self) -> int:
        self._ensure_loaded()
        return len(self._questions)

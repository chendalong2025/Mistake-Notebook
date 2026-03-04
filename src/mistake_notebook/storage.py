"""数据持久化 - JSON存储（单例模式）"""

from __future__ import annotations

import json
from pathlib import Path
from typing import List, Optional

from mistake_notebook.constants import DATA_DIR, DATA_FILE
from mistake_notebook.models import Question


class QuestionStorage:
    """题目存储，单例模式"""

    _instance: Optional[QuestionStorage] = None
    _questions: List[Question] = []

    def __new__(cls) -> QuestionStorage:
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance._loaded = False
        return cls._instance

    def _ensure_loaded(self) -> None:
        if not self._loaded:
            self.load()
            self._loaded = True

    def load(self) -> None:
        """从磁盘加载题目"""
        DATA_DIR.mkdir(parents=True, exist_ok=True)
        if DATA_FILE.exists():
            try:
                with open(DATA_FILE, encoding="utf-8") as f:
                    data = json.load(f)
                self._questions = [Question.from_dict(d) for d in data]
            except (json.JSONDecodeError, KeyError):
                self._questions = []
        else:
            self._questions = []

    def save(self) -> None:
        """持久化到磁盘"""
        DATA_DIR.mkdir(parents=True, exist_ok=True)
        with open(DATA_FILE, "w", encoding="utf-8") as f:
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

"""数据模型定义"""

from __future__ import annotations

import uuid
from dataclasses import dataclass, field
from datetime import datetime
from typing import Optional


@dataclass
class Question:
    """题目模型"""

    content: str                    # 题目内容
    options: dict[str, str]         # {'A': '...', 'B': '...', 'C': '...', 'D': '...'}
    answer: str                     # 正确答案 'A'/'B'/'C'/'D'
    id: str = field(default_factory=lambda: str(uuid.uuid4()))
    created_at: str = field(default_factory=lambda: datetime.now().isoformat())

    def to_dict(self) -> dict:
        return {
            "id": self.id,
            "content": self.content,
            "options": self.options,
            "answer": self.answer,
            "created_at": self.created_at,
        }

    @classmethod
    def from_dict(cls, data: dict) -> Question:
        return cls(
            id=data["id"],
            content=data["content"],
            options=data["options"],
            answer=data["answer"],
            created_at=data.get("created_at", ""),
        )


@dataclass
class QuizRecord:
    """单题答题记录"""

    question_id: str
    question_content: str
    correct_answer: str
    user_answer: Optional[str]   # None 表示超时未作答
    is_correct: bool
    is_timeout: bool


@dataclass
class QuizSession:
    """一次做题会话统计"""

    records: list[QuizRecord] = field(default_factory=list)

    @property
    def total(self) -> int:
        return len(self.records)

    @property
    def correct(self) -> int:
        return sum(1 for r in self.records if r.is_correct)

    @property
    def timeout_count(self) -> int:
        return sum(1 for r in self.records if r.is_timeout)

    @property
    def wrong(self) -> int:
        return self.total - self.correct - self.timeout_count

    @property
    def accuracy(self) -> float:
        if self.total == 0:
            return 0.0
        return self.correct / self.total * 100

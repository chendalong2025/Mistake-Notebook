package com.mistakenotebook.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mistakenotebook.data.model.QuizRecord
import com.mistakenotebook.data.model.QuizSession
import com.mistakenotebook.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class QuizViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = QuestionRepository.getInstance(app)

    var session = QuizSession()
        private set

    // 做题参数（通过 start() 传入）
    private var questions = listOf<com.mistakenotebook.data.model.Question>()
    var currentIndex = 0
        private set

    val currentQuestion get() = questions.getOrNull(currentIndex)
    val totalCount get() = questions.size
    var answered = false

    /** 开始做题：从数据库取题，随机洗牌，按数量截取 */
    fun start(count: Int?, onReady: () -> Unit) = viewModelScope.launch {
        val all = repo.getAll().shuffled()
        questions = if (count != null && count < all.size) all.take(count) else all
        session = QuizSession()
        currentIndex = 0
        answered = false
        onReady()
    }

    /** 提交答案，返回是否正确 */
    fun submitAnswer(selected: String?): Boolean {
        val q = currentQuestion ?: return false
        val isCorrect = selected == q.answer
        session.records.add(
            QuizRecord(
                questionId = q.id,
                questionContent = q.content,
                options = q.optionsMap(),
                correctAnswer = q.answer,
                userAnswer = selected,
                isCorrect = isCorrect,
                isTimeout = selected == null
            )
        )
        answered = true
        return isCorrect
    }

    /** 下一题，返回 true 表示还有题，false 表示结束 */
    fun nextQuestion(): Boolean {
        currentIndex++
        answered = false
        return currentIndex < questions.size
    }
}

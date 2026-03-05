package com.mistakenotebook.ui.add

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mistakenotebook.data.model.Question
import com.mistakenotebook.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class AddViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = QuestionRepository.getInstance(app)
    val questions: LiveData<List<Question>> = repo.getAllFlow().asLiveData()

    /** 保存单题 */
    fun save(q: Question, onDone: (Boolean) -> Unit) = viewModelScope.launch {
        repo.add(q)
        onDone(true)
    }

    /** 批量导入 */
    fun importBatch(raw: String, onDone: (Int, List<String>) -> Unit) = viewModelScope.launch {
        val (added, errors) = repo.parseBatch(raw)
        if (added.isNotEmpty()) repo.addAll(added)
        onDone(added.size, errors)
    }

    /** 删除 */
    fun delete(id: String) = viewModelScope.launch { repo.delete(id) }

    /** 更新 */
    fun update(q: Question, onDone: (Boolean) -> Unit) = viewModelScope.launch {
        repo.update(q)
        onDone(true)
    }
}

package com.mistakenotebook.data.repository

import android.content.Context
import com.mistakenotebook.data.db.AppDatabase
import com.mistakenotebook.data.model.Question
import kotlinx.coroutines.flow.Flow

/**
 * 题目仓库 — 单例模式。
 * 包含批量文本解析逻辑（对应 Python 版 _parse_batch()）
 */
class QuestionRepository private constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).questionDao()

    // ── 基础 CRUD ──────────────────────────────────────────────────────────────

    fun getAllFlow(): Flow<List<Question>> = dao.getAllFlow()

    suspend fun getAll(): List<Question> = dao.getAll()

    suspend fun getById(id: String): Question? = dao.getById(id)

    suspend fun add(question: Question) = dao.insert(question)

    suspend fun update(question: Question) = dao.update(question)

    suspend fun delete(id: String) = dao.deleteById(id)

    suspend fun count(): Int = dao.count()

    // ── 批量文本解析 ───────────────────────────────────────────────────────────

    /**
     * 解析批量录入文本，格式：
     * 题目（可多行）
     * A. 选项A
     * B. 选项B
     * C. 选项C
     * D. 选项D
     * 答案: X
     *（多题连续，无需空行分隔）
     */
    fun parseBatch(raw: String): Pair<List<Question>, List<String>> {
        val aMarker = Regex("""^A[.、):：]\s*""", RegexOption.IGNORE_CASE)
        val optRegex = Regex("""^([ABCD])[.、):：]\s*(.+)$""", RegexOption.IGNORE_CASE)
        val ansRegex = Regex("""^(?:答案|answer)[：:]\s*([ABCD])\s*$""", RegexOption.IGNORE_CASE)

        val lines = raw.trim().lines()
        val aIndices = lines.indices.filter { aMarker.containsMatchIn(lines[it].trim()) }

        if (aIndices.isEmpty()) {
            return Pair(emptyList(), listOf("未找到选项标记（A.），请检查格式"))
        }

        val questions = mutableListOf<Question>()
        val errors = mutableListOf<String>()

        var pendingContent = lines.take(aIndices[0])
            .filter { it.isNotBlank() }.joinToString("\n").trim()

        aIndices.forEachIndexed { n, aIdx ->
            val blockEnd = if (n + 1 < aIndices.size) aIndices[n + 1] else lines.size
            val blockLines = lines.subList(aIdx, blockEnd).map { it.trim() }

            val options = mutableMapOf<String, String>()
            var answer = ""
            var ansOffset: Int? = null

            blockLines.forEachIndexed { j, ln ->
                optRegex.matchEntire(ln)?.let {
                    options[it.groupValues[1].uppercase()] = it.groupValues[2].trim()
                    return@forEachIndexed
                }
                ansRegex.matchEntire(ln)?.let {
                    answer = it.groupValues[1].uppercase()
                    ansOffset = j
                    return@forEachIndexed
                }
            }

            val nextContentLines = ansOffset?.let { off ->
                blockLines.drop(off + 1).filter { it.isNotBlank() }
            } ?: emptyList()

            val content = pendingContent
            when {
                content.isBlank() -> errors.add("第 ${n + 1} 题题目内容为空，已跳过")
                options.size != 4 -> errors.add("选项不完整：${content.take(20)}")
                answer !in listOf("A", "B", "C", "D") -> errors.add("答案缺失或无效：${content.take(20)}")
                else -> questions.add(
                    Question(
                        content = content,
                        optionA = options["A"]!!,
                        optionB = options["B"]!!,
                        optionC = options["C"]!!,
                        optionD = options["D"]!!,
                        answer = answer
                    )
                )
            }
            pendingContent = nextContentLines.joinToString("\n").trim()
        }

        return Pair(questions, errors)
    }

    suspend fun addAll(questions: List<Question>) = dao.insertAll(questions)

    // ── 单例 ──────────────────────────────────────────────────────────────────

    companion object {
        @Volatile
        private var INSTANCE: QuestionRepository? = null

        fun getInstance(context: Context): QuestionRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: QuestionRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}

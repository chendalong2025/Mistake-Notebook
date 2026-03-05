package com.mistakenotebook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val content: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: String,          // "A" / "B" / "C" / "D"
    val createdAt: Long = System.currentTimeMillis()
) {
    /** 将选项转为 Map，方便 UI 遍历 */
    fun optionsMap(): Map<String, String> = mapOf(
        "A" to optionA, "B" to optionB, "C" to optionC, "D" to optionD
    )
}

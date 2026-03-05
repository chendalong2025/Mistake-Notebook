package com.mistakenotebook.data.db

import androidx.room.*
import com.mistakenotebook.data.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY createdAt ASC")
    fun getAllFlow(): Flow<List<Question>>

    @Query("SELECT * FROM questions ORDER BY createdAt ASC")
    suspend fun getAll(): List<Question>

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getById(id: String): Question?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<Question>)

    @Update
    suspend fun update(question: Question)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun count(): Int
}

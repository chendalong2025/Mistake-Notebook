package com.mistakenotebook.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mistakenotebook.data.model.Question

@Database(entities = [Question::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        private const val DB_NAME = "mistake_notebook.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DB_NAME
                ).build().also { INSTANCE = it }
            }
    }
}

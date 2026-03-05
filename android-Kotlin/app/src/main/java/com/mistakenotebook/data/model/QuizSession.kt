package com.mistakenotebook.data.model

import android.os.Parcel
import android.os.Parcelable

/** 单题答题记录 */
data class QuizRecord(
    val questionId: String,
    val questionContent: String,
    val options: Map<String, String>,
    val correctAnswer: String,
    val userAnswer: String?,
    val isCorrect: Boolean,
    val isTimeout: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readHashMap(String::class.java.classLoader) as Map<String, String>,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(questionId); parcel.writeString(questionContent)
        parcel.writeMap(options); parcel.writeString(correctAnswer)
        parcel.writeString(userAnswer)
        parcel.writeByte(if (isCorrect) 1 else 0)
        parcel.writeByte(if (isTimeout) 1 else 0)
    }
    override fun describeContents() = 0
    companion object CREATOR : Parcelable.Creator<QuizRecord> {
        override fun createFromParcel(p: Parcel) = QuizRecord(p)
        override fun newArray(size: Int): Array<QuizRecord?> = arrayOfNulls(size)
    }
}

/** 一次做题会话统计 */
data class QuizSession(
    val records: MutableList<QuizRecord> = mutableListOf()
) : Parcelable {
    val total: Int get() = records.size
    val correct: Int get() = records.count { it.isCorrect }
    val timeoutCount: Int get() = records.count { it.isTimeout }
    val wrong: Int get() = total - correct - timeoutCount
    val accuracy: Float get() = if (total == 0) 0f else correct.toFloat() / total * 100

    constructor(parcel: Parcel) : this(
        parcel.createTypedArrayList(QuizRecord)!!.toMutableList()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(records)
    }
    override fun describeContents() = 0
    companion object CREATOR : Parcelable.Creator<QuizSession> {
        override fun createFromParcel(p: Parcel) = QuizSession(p)
        override fun newArray(size: Int): Array<QuizSession?> = arrayOfNulls(size)
    }
}

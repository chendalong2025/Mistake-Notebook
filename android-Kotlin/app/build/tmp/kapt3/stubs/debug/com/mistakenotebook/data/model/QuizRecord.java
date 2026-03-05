package com.mistakenotebook.data.model;

/**
 * 单题答题记录
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0013\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0086\b\u0018\u0000 *2\u00020\u0001:\u0001*B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004BK\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\t\u0012\u0006\u0010\n\u001a\u00020\u0006\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\r\u00a2\u0006\u0002\u0010\u000fJ\t\u0010\u0018\u001a\u00020\u0006H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0006H\u00c6\u0003J\u0015\u0010\u001a\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\tH\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0006H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\rH\u00c6\u0003J\t\u0010\u001e\u001a\u00020\rH\u00c6\u0003J]\u0010\u001f\u001a\u00020\u00002\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00062\u0014\b\u0002\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\t2\b\b\u0002\u0010\n\u001a\u00020\u00062\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\f\u001a\u00020\r2\b\b\u0002\u0010\u000e\u001a\u00020\rH\u00c6\u0001J\b\u0010 \u001a\u00020!H\u0016J\u0013\u0010\"\u001a\u00020\r2\b\u0010#\u001a\u0004\u0018\u00010$H\u00d6\u0003J\t\u0010%\u001a\u00020!H\u00d6\u0001J\t\u0010&\u001a\u00020\u0006H\u00d6\u0001J\u0018\u0010\'\u001a\u00020(2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010)\u001a\u00020!H\u0016R\u0011\u0010\n\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0012R\u0011\u0010\u000e\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0012R\u001d\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00060\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0011\u0010\u0007\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0011R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0011R\u0013\u0010\u000b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0011\u00a8\u0006+"}, d2 = {"Lcom/mistakenotebook/data/model/QuizRecord;", "Landroid/os/Parcelable;", "parcel", "Landroid/os/Parcel;", "(Landroid/os/Parcel;)V", "questionId", "", "questionContent", "options", "", "correctAnswer", "userAnswer", "isCorrect", "", "isTimeout", "(Ljava/lang/String;Ljava/lang/String;Ljava/util/Map;Ljava/lang/String;Ljava/lang/String;ZZ)V", "getCorrectAnswer", "()Ljava/lang/String;", "()Z", "getOptions", "()Ljava/util/Map;", "getQuestionContent", "getQuestionId", "getUserAnswer", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "describeContents", "", "equals", "other", "", "hashCode", "toString", "writeToParcel", "", "flags", "CREATOR", "app_debug"})
public final class QuizRecord implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String questionId = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String questionContent = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.lang.String> options = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String correctAnswer = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String userAnswer = null;
    private final boolean isCorrect = false;
    private final boolean isTimeout = false;
    @org.jetbrains.annotations.NotNull()
    public static final com.mistakenotebook.data.model.QuizRecord.CREATOR CREATOR = null;
    
    public QuizRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String questionId, @org.jetbrains.annotations.NotNull()
    java.lang.String questionContent, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.String> options, @org.jetbrains.annotations.NotNull()
    java.lang.String correctAnswer, @org.jetbrains.annotations.Nullable()
    java.lang.String userAnswer, boolean isCorrect, boolean isTimeout) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuestionId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getQuestionContent() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.String> getOptions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getCorrectAnswer() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getUserAnswer() {
        return null;
    }
    
    public final boolean isCorrect() {
        return false;
    }
    
    public final boolean isTimeout() {
        return false;
    }
    
    public QuizRecord(@org.jetbrains.annotations.NotNull()
    android.os.Parcel parcel) {
        super();
    }
    
    @java.lang.Override()
    public void writeToParcel(@org.jetbrains.annotations.NotNull()
    android.os.Parcel parcel, int flags) {
    }
    
    @java.lang.Override()
    public int describeContents() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, java.lang.String> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    public final boolean component6() {
        return false;
    }
    
    public final boolean component7() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mistakenotebook.data.model.QuizRecord copy(@org.jetbrains.annotations.NotNull()
    java.lang.String questionId, @org.jetbrains.annotations.NotNull()
    java.lang.String questionContent, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.String, java.lang.String> options, @org.jetbrains.annotations.NotNull()
    java.lang.String correctAnswer, @org.jetbrains.annotations.Nullable()
    java.lang.String userAnswer, boolean isCorrect, boolean isTimeout) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u001d\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/mistakenotebook/data/model/QuizRecord$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/mistakenotebook/data/model/QuizRecord;", "()V", "createFromParcel", "p", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/mistakenotebook/data/model/QuizRecord;", "app_debug"})
    public static final class CREATOR implements android.os.Parcelable.Creator<com.mistakenotebook.data.model.QuizRecord> {
        
        private CREATOR() {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.mistakenotebook.data.model.QuizRecord createFromParcel(@org.jetbrains.annotations.NotNull()
        android.os.Parcel p) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.mistakenotebook.data.model.QuizRecord[] newArray(int size) {
            return null;
        }
    }
}
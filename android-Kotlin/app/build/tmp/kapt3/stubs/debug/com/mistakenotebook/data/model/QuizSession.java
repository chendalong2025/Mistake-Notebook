package com.mistakenotebook.data.model;

/**
 * 一次做题会话统计
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0086\b\u0018\u0000 &2\u00020\u0001:\u0001&B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004B\u0015\u0012\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\u0002\u0010\bJ\u000f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0003J\u0019\u0010\u001a\u001a\u00020\u00002\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u00c6\u0001J\b\u0010\u001b\u001a\u00020\u000eH\u0016J\u0013\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u00d6\u0003J\t\u0010 \u001a\u00020\u000eH\u00d6\u0001J\t\u0010!\u001a\u00020\"H\u00d6\u0001J\u0018\u0010#\u001a\u00020$2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u000eH\u0016R\u0011\u0010\t\u001a\u00020\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0010R\u0011\u0010\u0015\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0010R\u0011\u0010\u0017\u001a\u00020\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u0018\u0010\u0010\u00a8\u0006\'"}, d2 = {"Lcom/mistakenotebook/data/model/QuizSession;", "Landroid/os/Parcelable;", "parcel", "Landroid/os/Parcel;", "(Landroid/os/Parcel;)V", "records", "", "Lcom/mistakenotebook/data/model/QuizRecord;", "(Ljava/util/List;)V", "accuracy", "", "getAccuracy", "()F", "correct", "", "getCorrect", "()I", "getRecords", "()Ljava/util/List;", "timeoutCount", "getTimeoutCount", "total", "getTotal", "wrong", "getWrong", "component1", "copy", "describeContents", "equals", "", "other", "", "hashCode", "toString", "", "writeToParcel", "", "flags", "CREATOR", "app_debug"})
public final class QuizSession implements android.os.Parcelable {
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.mistakenotebook.data.model.QuizRecord> records = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.mistakenotebook.data.model.QuizSession.CREATOR CREATOR = null;
    
    public QuizSession(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mistakenotebook.data.model.QuizRecord> records) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.mistakenotebook.data.model.QuizRecord> getRecords() {
        return null;
    }
    
    public final int getTotal() {
        return 0;
    }
    
    public final int getCorrect() {
        return 0;
    }
    
    public final int getTimeoutCount() {
        return 0;
    }
    
    public final int getWrong() {
        return 0;
    }
    
    public final float getAccuracy() {
        return 0.0F;
    }
    
    public QuizSession(@org.jetbrains.annotations.NotNull()
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
    
    public QuizSession() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.mistakenotebook.data.model.QuizRecord> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mistakenotebook.data.model.QuizSession copy(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mistakenotebook.data.model.QuizRecord> records) {
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
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00022\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u001d\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016\u00a2\u0006\u0002\u0010\u000b\u00a8\u0006\f"}, d2 = {"Lcom/mistakenotebook/data/model/QuizSession$CREATOR;", "Landroid/os/Parcelable$Creator;", "Lcom/mistakenotebook/data/model/QuizSession;", "()V", "createFromParcel", "p", "Landroid/os/Parcel;", "newArray", "", "size", "", "(I)[Lcom/mistakenotebook/data/model/QuizSession;", "app_debug"})
    public static final class CREATOR implements android.os.Parcelable.Creator<com.mistakenotebook.data.model.QuizSession> {
        
        private CREATOR() {
            super();
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.mistakenotebook.data.model.QuizSession createFromParcel(@org.jetbrains.annotations.NotNull()
        android.os.Parcel p) {
            return null;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public com.mistakenotebook.data.model.QuizSession[] newArray(int size) {
            return null;
        }
    }
}
package com.mistakenotebook.data.repository;

/**
 * 题目仓库 — 单例模式。
 * 包含批量文本解析逻辑（对应 Python 版 _parse_batch()）
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bJ\u001c\u0010\f\u001a\u00020\b2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\n0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u000fJ\u000e\u0010\u0010\u001a\u00020\u0011H\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u0014\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\n0\u000eH\u0086@\u00a2\u0006\u0002\u0010\u0012J\u0012\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000e0\u0019J\u0018\u0010\u001a\u001a\u0004\u0018\u00010\n2\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0016J&\u0010\u001b\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00150\u000e0\u001c2\u0006\u0010\u001d\u001a\u00020\u0015J\u0016\u0010\u001e\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0086@\u00a2\u0006\u0002\u0010\u000bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/mistakenotebook/data/repository/QuestionRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "dao", "Lcom/mistakenotebook/data/db/QuestionDao;", "add", "", "question", "Lcom/mistakenotebook/data/model/Question;", "(Lcom/mistakenotebook/data/model/Question;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addAll", "questions", "", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "count", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "delete", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "getById", "parseBatch", "Lkotlin/Pair;", "raw", "update", "Companion", "app_debug"})
public final class QuestionRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.mistakenotebook.data.db.QuestionDao dao = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.mistakenotebook.data.repository.QuestionRepository INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.mistakenotebook.data.repository.QuestionRepository.Companion Companion = null;
    
    private QuestionRepository(android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.mistakenotebook.data.model.Question>> getAllFlow() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.mistakenotebook.data.model.Question>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.mistakenotebook.data.model.Question> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object add(@org.jetbrains.annotations.NotNull()
    com.mistakenotebook.data.model.Question question, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.mistakenotebook.data.model.Question question, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object count(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion) {
        return null;
    }
    
    /**
     * 解析批量录入文本，格式：
     * 题目（可多行）
     * A. 选项A
     * B. 选项B
     * C. 选项C
     * D. 选项D
     * 答案: X
     * （多题连续，无需空行分隔）
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlin.Pair<java.util.List<com.mistakenotebook.data.model.Question>, java.util.List<java.lang.String>> parseBatch(@org.jetbrains.annotations.NotNull()
    java.lang.String raw) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object addAll(@org.jetbrains.annotations.NotNull()
    java.util.List<com.mistakenotebook.data.model.Question> questions, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/mistakenotebook/data/repository/QuestionRepository$Companion;", "", "()V", "INSTANCE", "Lcom/mistakenotebook/data/repository/QuestionRepository;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.mistakenotebook.data.repository.QuestionRepository getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}
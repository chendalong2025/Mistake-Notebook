package com.mistakenotebook.ui.quiz;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u001e\u001a\u00020\u0006J#\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\f2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00020$0#\u00a2\u0006\u0002\u0010%J\u0010\u0010&\u001a\u00020\u00062\b\u0010\'\u001a\u0004\u0018\u00010(R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u001e\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0010\u001a\u0004\u0018\u00010\u00118F\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00110\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\u00020\u00182\u0006\u0010\u000b\u001a\u00020\u0018@BX\u0086\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\u001c\u001a\u00020\f8F\u00a2\u0006\u0006\u001a\u0004\b\u001d\u0010\u000f\u00a8\u0006)"}, d2 = {"Lcom/mistakenotebook/ui/quiz/QuizViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "answered", "", "getAnswered", "()Z", "setAnswered", "(Z)V", "<set-?>", "", "currentIndex", "getCurrentIndex", "()I", "currentQuestion", "Lcom/mistakenotebook/data/model/Question;", "getCurrentQuestion", "()Lcom/mistakenotebook/data/model/Question;", "questions", "", "repo", "Lcom/mistakenotebook/data/repository/QuestionRepository;", "Lcom/mistakenotebook/data/model/QuizSession;", "session", "getSession", "()Lcom/mistakenotebook/data/model/QuizSession;", "totalCount", "getTotalCount", "nextQuestion", "start", "Lkotlinx/coroutines/Job;", "count", "onReady", "Lkotlin/Function0;", "", "(Ljava/lang/Integer;Lkotlin/jvm/functions/Function0;)Lkotlinx/coroutines/Job;", "submitAnswer", "selected", "", "app_debug"})
public final class QuizViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.mistakenotebook.data.repository.QuestionRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private com.mistakenotebook.data.model.QuizSession session;
    @org.jetbrains.annotations.NotNull()
    private java.util.List<com.mistakenotebook.data.model.Question> questions;
    private int currentIndex = 0;
    private boolean answered = false;
    
    public QuizViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.mistakenotebook.data.model.QuizSession getSession() {
        return null;
    }
    
    public final int getCurrentIndex() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.mistakenotebook.data.model.Question getCurrentQuestion() {
        return null;
    }
    
    public final int getTotalCount() {
        return 0;
    }
    
    public final boolean getAnswered() {
        return false;
    }
    
    public final void setAnswered(boolean p0) {
    }
    
    /**
     * 开始做题：从数据库取题，随机洗牌，按数量截取
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job start(@org.jetbrains.annotations.Nullable()
    java.lang.Integer count, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function0<kotlin.Unit> onReady) {
        return null;
    }
    
    /**
     * 提交答案，返回是否正确
     */
    public final boolean submitAnswer(@org.jetbrains.annotations.Nullable()
    java.lang.String selected) {
        return false;
    }
    
    /**
     * 下一题，返回 true 表示还有题，false 表示结束
     */
    public final boolean nextQuestion() {
        return false;
    }
}
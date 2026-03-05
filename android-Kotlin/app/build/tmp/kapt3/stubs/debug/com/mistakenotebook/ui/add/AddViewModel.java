package com.mistakenotebook.ui.add;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J.\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00102\u001e\u0010\u0013\u001a\u001a\u0012\u0004\u0012\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u0007\u0012\u0004\u0012\u00020\u00160\u0014J\"\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\b2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00160\u0019J\"\u0010\u001b\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\b2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u001a\u0012\u0004\u0012\u00020\u00160\u0019R\u001d\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/mistakenotebook/ui/add/AddViewModel;", "Landroidx/lifecycle/AndroidViewModel;", "app", "Landroid/app/Application;", "(Landroid/app/Application;)V", "questions", "Landroidx/lifecycle/LiveData;", "", "Lcom/mistakenotebook/data/model/Question;", "getQuestions", "()Landroidx/lifecycle/LiveData;", "repo", "Lcom/mistakenotebook/data/repository/QuestionRepository;", "delete", "Lkotlinx/coroutines/Job;", "id", "", "importBatch", "raw", "onDone", "Lkotlin/Function2;", "", "", "save", "q", "Lkotlin/Function1;", "", "update", "app_debug"})
public final class AddViewModel extends androidx.lifecycle.AndroidViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.mistakenotebook.data.repository.QuestionRepository repo = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.LiveData<java.util.List<com.mistakenotebook.data.model.Question>> questions = null;
    
    public AddViewModel(@org.jetbrains.annotations.NotNull()
    android.app.Application app) {
        super(null);
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.util.List<com.mistakenotebook.data.model.Question>> getQuestions() {
        return null;
    }
    
    /**
     * 保存单题
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job save(@org.jetbrains.annotations.NotNull()
    com.mistakenotebook.data.model.Question q, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onDone) {
        return null;
    }
    
    /**
     * 批量导入
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job importBatch(@org.jetbrains.annotations.NotNull()
    java.lang.String raw, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super java.util.List<java.lang.String>, kotlin.Unit> onDone) {
        return null;
    }
    
    /**
     * 删除
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job delete(@org.jetbrains.annotations.NotNull()
    java.lang.String id) {
        return null;
    }
    
    /**
     * 更新
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.Job update(@org.jetbrains.annotations.NotNull()
    com.mistakenotebook.data.model.Question q, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.Boolean, kotlin.Unit> onDone) {
        return null;
    }
}
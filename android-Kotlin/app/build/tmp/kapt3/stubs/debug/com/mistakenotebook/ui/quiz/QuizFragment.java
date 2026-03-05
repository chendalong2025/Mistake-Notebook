package com.mistakenotebook.ui.quiz;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\r\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0015H\u0002J\u0010\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J$\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u0015H\u0016J\b\u0010#\u001a\u00020\u0015H\u0002J\u001a\u0010$\u001a\u00020\u00152\u0006\u0010%\u001a\u00020\u001b2\b\u0010&\u001a\u0004\u0018\u00010!H\u0016J\b\u0010\'\u001a\u00020\u0015H\u0002J\b\u0010(\u001a\u00020\u0015H\u0002J\u0010\u0010)\u001a\u00020\u00152\u0006\u0010*\u001a\u00020\u000bH\u0002J\u0018\u0010+\u001a\u00020\u00152\u0006\u0010,\u001a\u00020\u000b2\u0006\u0010-\u001a\u00020\u000bH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u0004\u0018\u00010\u000bX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\fR\u000e\u0010\r\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006."}, d2 = {"Lcom/mistakenotebook/ui/quiz/QuizFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/mistakenotebook/databinding/FragmentQuizBinding;", "binding", "getBinding", "()Lcom/mistakenotebook/databinding/FragmentQuizBinding;", "countDownTimer", "Landroid/os/CountDownTimer;", "questionCount", "", "Ljava/lang/Integer;", "timeoutSeconds", "vm", "Lcom/mistakenotebook/ui/quiz/QuizViewModel;", "getVm", "()Lcom/mistakenotebook/ui/quiz/QuizViewModel;", "vm$delegate", "Lkotlin/Lazy;", "loadQuestion", "", "nextOrFinish", "onAnswer", "selected", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "state", "Landroid/os/Bundle;", "onDestroyView", "onTimeout", "onViewCreated", "view", "savedInstanceState", "showQuiz", "showSetup", "startCountdown", "totalSec", "updateCountdownDisplay", "remaining", "total", "app_debug"})
public final class QuizFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.mistakenotebook.databinding.FragmentQuizBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy vm$delegate = null;
    @org.jetbrains.annotations.Nullable()
    private android.os.CountDownTimer countDownTimer;
    private int timeoutSeconds = 10;
    @org.jetbrains.annotations.Nullable()
    private java.lang.Integer questionCount;
    
    public QuizFragment() {
        super();
    }
    
    private final com.mistakenotebook.databinding.FragmentQuizBinding getBinding() {
        return null;
    }
    
    private final com.mistakenotebook.ui.quiz.QuizViewModel getVm() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle state) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void showSetup() {
    }
    
    private final void showQuiz() {
    }
    
    private final void loadQuestion() {
    }
    
    private final void startCountdown(int totalSec) {
    }
    
    private final void updateCountdownDisplay(int remaining, int total) {
    }
    
    private final void onTimeout() {
    }
    
    private final void onAnswer(java.lang.String selected) {
    }
    
    private final void nextOrFinish() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}
package com.mistakenotebook.ui.add;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002JD\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000e2\u0006\u0010\u0010\u001a\u00020\u000e2\u0006\u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u000e2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150\u0014H\u0002J$\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016J\u001a\u0010\u001e\u001a\u00020\f2\u0006\u0010\u001f\u001a\u00020\u00172\b\u0010 \u001a\u0004\u0018\u00010\u001dH\u0016J$\u0010!\u001a\u00020\f2\u0012\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150\u00142\u0006\u0010\"\u001a\u00020\u0004H\u0002J\u0010\u0010#\u001a\u00020\f2\u0006\u0010$\u001a\u00020\u0017H\u0002J\u0010\u0010%\u001a\u00020\f2\u0006\u0010$\u001a\u00020\u0017H\u0002J\u0010\u0010&\u001a\u00020\f2\u0006\u0010$\u001a\u00020\u0017H\u0002J\u0018\u0010\'\u001a\u00020\f2\u0006\u0010$\u001a\u00020\u00172\u0006\u0010(\u001a\u00020)H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\b\u00a8\u0006*"}, d2 = {"Lcom/mistakenotebook/ui/add/AddFragment;", "Landroidx/fragment/app/Fragment;", "()V", "selectedAnswer", "", "vm", "Lcom/mistakenotebook/ui/add/AddViewModel;", "getVm", "()Lcom/mistakenotebook/ui/add/AddViewModel;", "vm$delegate", "Lkotlin/Lazy;", "clearSingleForm", "", "etContent", "Landroid/widget/EditText;", "etA", "etB", "etC", "etD", "btns", "", "Landroid/widget/Button;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "s", "Landroid/os/Bundle;", "onViewCreated", "view", "savedInstanceState", "selectAnswer", "key", "setupBatchForm", "v", "setupSingleForm", "setupTabs", "switchTab", "single", "", "app_debug"})
public final class AddFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy vm$delegate = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String selectedAnswer = "A";
    
    public AddFragment() {
        super();
    }
    
    private final com.mistakenotebook.ui.add.AddViewModel getVm() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle s) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupTabs(android.view.View v) {
    }
    
    private final void switchTab(android.view.View v, boolean single) {
    }
    
    private final void setupSingleForm(android.view.View v) {
    }
    
    private final void selectAnswer(java.util.Map<java.lang.String, ? extends android.widget.Button> btns, java.lang.String key) {
    }
    
    private final void clearSingleForm(android.widget.EditText etContent, android.widget.EditText etA, android.widget.EditText etB, android.widget.EditText etC, android.widget.EditText etD, java.util.Map<java.lang.String, ? extends android.widget.Button> btns) {
    }
    
    private final void setupBatchForm(android.view.View v) {
    }
}
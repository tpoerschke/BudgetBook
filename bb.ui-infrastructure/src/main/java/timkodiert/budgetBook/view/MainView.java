package timkodiert.budgetBook.view;

import org.jetbrains.annotations.Nullable;

public interface MainView {
    @Nullable
    View loadViewPartial(FxmlResource resource);
}

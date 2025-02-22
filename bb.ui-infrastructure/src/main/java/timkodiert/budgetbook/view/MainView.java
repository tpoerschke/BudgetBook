package timkodiert.budgetbook.view;

import org.jetbrains.annotations.Nullable;

public interface MainView {
    @Nullable
    View loadViewPartial(FxmlResource resource);
}

package timkodiert.budgetbook.view;

import javax.inject.Inject;

import timkodiert.budgetbook.view.mdv_base.BaseManageView;

public class ShortcutFunction {

    private final MainView mainView;

    @Inject
    public ShortcutFunction(MainView mainView) {
        this.mainView = mainView;
    }

    public void openDetailView(FxmlResource resource, int id) {
        View openedView = mainView.loadViewPartial(resource);
        if (openedView instanceof BaseManageView<?> manageView) {
            manageView.displayEntityById(id);
        }
    }
}

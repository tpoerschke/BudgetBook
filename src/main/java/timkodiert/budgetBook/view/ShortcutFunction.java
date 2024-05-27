package timkodiert.budgetBook.view;

import javax.inject.Inject;

import timkodiert.budgetBook.view.mdv_base.BaseManageView;

public class ShortcutFunction {

    private final MainView mainView;

    @Inject
    public ShortcutFunction(MainView mainView) {
        this.mainView = mainView;
    }

    public void openDetailView(FxmlResource resource, int id) {
        View openedView = mainView.loadViewPartial(resource.toString(), "test");
        if (openedView instanceof BaseManageView<?, ?> manageView) {
            manageView.displayEntityById(id);
        }
    }
}

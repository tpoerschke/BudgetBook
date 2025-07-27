package timkodiert.budgetbook.view.mdv_base;

import java.util.function.Consumer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import lombok.Setter;

public abstract class EntityBaseDetailView<D> extends BaseDetailView<D> {

    @Setter
    protected Consumer<D> onUpdate;

    public boolean isDirty() {
        return beanAdapter.isDirty();
    }

    public abstract boolean save();

    protected abstract D discardChanges();

    @FXML
    private void discardChanges(ActionEvent event) {
        setBean(discardChanges());
    }

    @FXML
    private void save(ActionEvent event) {
        save();
    }
}

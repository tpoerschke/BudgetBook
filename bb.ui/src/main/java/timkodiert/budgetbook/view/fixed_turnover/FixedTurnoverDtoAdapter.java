package timkodiert.budgetbook.view.fixed_turnover;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;

import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.view.mdv_base.DtoAdapterBase;

public class FixedTurnoverDtoAdapter implements DtoAdapterBase<FixedTurnoverDTO> {

    private final FixedTurnoverDTO fixedTurnover;

    @Getter
    private boolean isDirty;

    private final SimpleStringProperty position = new SimpleStringProperty();

    public FixedTurnoverDtoAdapter(FixedTurnoverDTO fixedTurnover) {
        this.fixedTurnover = fixedTurnover;
        position.set(fixedTurnover.getPosition());
        position.addListener(((observable, oldValue, newValue) -> {
            fixedTurnover.setPosition(newValue);
            isDirty = true;
        }));
    }

    @Override
    public FixedTurnoverDTO getDto() {
        return fixedTurnover;
    }

    public SimpleStringProperty positionProperty() {
        return position;
    }
}

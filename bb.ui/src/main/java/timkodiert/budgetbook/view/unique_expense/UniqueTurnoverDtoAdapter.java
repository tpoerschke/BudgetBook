package timkodiert.budgetbook.view.unique_expense;

import timkodiert.budgetbook.domain.UniqueTurnoverDTO;
import timkodiert.budgetbook.view.mdv_base.DtoAdapterBase;

public class UniqueTurnoverDtoAdapter implements DtoAdapterBase<UniqueTurnoverDTO> {

    private final UniqueTurnoverDTO uniqueTurnover;

    public UniqueTurnoverDtoAdapter(UniqueTurnoverDTO uniqueTurnover) {
        this.uniqueTurnover = uniqueTurnover;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public UniqueTurnoverDTO getDto() {
        return uniqueTurnover;
    }
}

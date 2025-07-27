package timkodiert.budgetbook.view.fixed_turnover;

import timkodiert.budgetbook.domain.FixedTurnoverInformationDTO;
import timkodiert.budgetbook.view.mdv_base.DtoAdapterBase;

public class FixedTurnoverInformationDtoAdapter implements DtoAdapterBase<FixedTurnoverInformationDTO> {

    private final FixedTurnoverInformationDTO fixedTurnoverInformation;

    public FixedTurnoverInformationDtoAdapter(FixedTurnoverInformationDTO fixedTurnoverInformation) {
        this.fixedTurnoverInformation = fixedTurnoverInformation;
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public FixedTurnoverInformationDTO getDto() {
        return fixedTurnoverInformation;
    }
}

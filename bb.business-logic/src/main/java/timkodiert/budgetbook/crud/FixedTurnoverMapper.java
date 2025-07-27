package timkodiert.budgetbook.crud;

import org.mapstruct.Mapper;

import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.model.FixedTurnover;

@Mapper
public interface FixedTurnoverMapper {

    FixedTurnoverDTO fixedTurnoverToFixedTurnoverDto(FixedTurnover fixedTurnover);
}

package timkodiert.budgetbook.crud;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.BillingDTO;
import timkodiert.budgetbook.domain.SimplifiedUniqueTurnoverDTO;
import timkodiert.budgetbook.domain.model.Billing;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.repository.UniqueExpensesRepository;
import timkodiert.budgetbook.representation.RowType;

@Mapper
public interface BillingMapper {

    @Mapping(target = "uniqueTurnovers", source = "billing")
    BillingDTO billingToDto(Billing billing);

    @Mapping(target = "uniqueTurnovers", source = "dto")
    void updateBilling(BillingDTO dto, @MappingTarget Billing entity, @Context UniqueExpensesRepository uniqueTurnoverRepository);

    default List<SimplifiedUniqueTurnoverDTO> mapUniqueTurnovers(Billing billing) {
        return billing.getUniqueTurnovers()
                      .stream()
                      .map(ut -> new SimplifiedUniqueTurnoverDTO(ut.getId(), ut.getBiller(), ut.getDate(), ut.getTotalValue(), RowType.DEFAULT))
                      .toList();
    }

    default List<UniqueTurnover> mapUniqueTurnovers(BillingDTO dto, @Context UniqueExpensesRepository uniqueTurnoverRepository) {
        return dto.getUniqueTurnovers().stream().map(SimplifiedUniqueTurnoverDTO::getId).map(uniqueTurnoverRepository::findById).toList();
    }
}

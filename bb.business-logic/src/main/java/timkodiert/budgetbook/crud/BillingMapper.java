package timkodiert.budgetbook.crud;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.BillingDTO;
import timkodiert.budgetbook.domain.SimplifiedUniqueTurnoverDTO;
import timkodiert.budgetbook.domain.model.Billing;

@Mapper
public interface BillingMapper {

    @Mapping(target = "uniqueTurnovers", source = "billing")
    BillingDTO billingToDto(Billing billing);

    @Mapping(target = "uniqueTurnovers", ignore = true)
    void updateBilling(BillingDTO dto, @MappingTarget Billing entity, @Context ReferenceResolver referenceResolver);

    default List<SimplifiedUniqueTurnoverDTO> mapUniqueTurnovers(Billing billing) {
        return billing.getUniqueTurnovers().stream()
                .map(ut -> new SimplifiedUniqueTurnoverDTO(ut.getId(), ut.getBiller(), ut.getDate(), ut.getTotalValue()))
                .toList();
    }
}

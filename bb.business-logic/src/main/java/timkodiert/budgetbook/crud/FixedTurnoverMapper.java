package timkodiert.budgetbook.crud;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.PaymentInformationDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.PaymentInformation;
import timkodiert.budgetbook.domain.model.PaymentType;

@Mapper
public interface FixedTurnoverMapper {

    @Mapping(target = "categories", source = "fixedTurnover")
    @Mapping(target = "paymentType", source = "fixedTurnover")
    FixedTurnoverDTO fixedTurnoverToFixedTurnoverDto(FixedTurnover fixedTurnover);

    default PaymentType mapPaymentType(FixedTurnover fixedTurnover) {
        return fixedTurnover.getType();
    }

    default List<Reference<CategoryDTO>> mapCategories(FixedTurnover fixedTurnover) {
        return fixedTurnover.getCategories()
                            .stream()
                            .map(c -> new Reference<>(CategoryDTO.class, c.getId(), c.getName()))
                            .toList();
    }

    PaymentInformationDTO paymentInformationToPaymentInformationDto(PaymentInformation paymentInformation);
    
    @Mapping(target = "categories", expression = "java(referenceResolver.resolve(dto.getCategories()))")
    @Mapping(target = "accountTurnover", ignore = true)
    void updateFixedTurnover(FixedTurnoverDTO dto, @MappingTarget FixedTurnover fixedTurnover, @Context ReferenceResolver referenceResolver);
}

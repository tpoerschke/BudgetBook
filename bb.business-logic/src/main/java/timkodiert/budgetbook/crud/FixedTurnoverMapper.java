package timkodiert.budgetbook.crud;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.AccountTurnoverDTO;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.PaymentInformationDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.AccountTurnover;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.PaymentInformation;
import timkodiert.budgetbook.domain.model.PaymentType;

@Mapper
public interface FixedTurnoverMapper {

    @Mapping(target = "category", source = "fixedTurnover")
    @Mapping(target = "paymentType", source = "fixedTurnover")
    @Mapping(target = "accountTurnover", source = "fixedTurnover")
    FixedTurnoverDTO fixedTurnoverToFixedTurnoverDto(FixedTurnover fixedTurnover);

    default PaymentType mapPaymentType(FixedTurnover fixedTurnover) {
        return fixedTurnover.getType();
    }

    default Reference<CategoryDTO> mapCategory(FixedTurnover fixedTurnover) {
        Category category = fixedTurnover.getCategory();
        if (category == null) {
            return null;
        }
        return new Reference<>(CategoryDTO.class, category.getId(), category.getName());
    }

    default List<AccountTurnoverDTO> mapAccountTurnovers(FixedTurnover fixedTurnover) {
        return fixedTurnover.getImports().stream().sorted().map(this::accountTurnoverToDto).toList();
    }

    PaymentInformationDTO paymentInformationToPaymentInformationDto(PaymentInformation paymentInformation);

    AccountTurnoverDTO accountTurnoverToDto(AccountTurnover accountTurnover);

    @Mapping(target = "category", expression = "java(referenceResolver.resolve(dto.getCategory()))")
    void updateFixedTurnover(FixedTurnoverDTO dto, @MappingTarget FixedTurnover fixedTurnover, @Context ReferenceResolver referenceResolver);
}

package timkodiert.budgetbook.crud;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.UniqueTurnoverDTO;
import timkodiert.budgetbook.domain.UniqueTurnoverInformationDTO;
import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;

@Mapper
public interface UniqueTurnoverMapper {

    @Mapping(target = "fixedTurnover", source = "uniqueTurnover")
    UniqueTurnoverDTO uniqueTurnoverToUniqueTurnoverDto(UniqueTurnover uniqueTurnover);

    default Reference<FixedTurnoverDTO> mapFixedTurnover(UniqueTurnover uniqueTurnover) {
        return Optional.ofNullable(uniqueTurnover.getFixedTurnover())
                       .map(ft -> new Reference<>(FixedTurnoverDTO.class, ft.getId(), ft.getPosition()))
                       .orElse(null);
    }

    @Mapping(target = "category", source = "uniqueTurnoverInformation")
    UniqueTurnoverInformationDTO uniqueTurnoverInformationToUniqueTurnoverInformationDto(UniqueTurnoverInformation uniqueTurnoverInformation);

    default @Nullable Reference<CategoryDTO> mapCategory(UniqueTurnoverInformation uniqueTurnoverInformation) {
        Category category = uniqueTurnoverInformation.getCategory();
        if (category == null) {
            return null;
        }
        return new Reference<>(CategoryDTO.class, category.getId(), category.getName());
    }

    @Mapping(target = "accountTurnover", ignore = true)
    @Mapping(target = "fixedTurnover", expression = "java(referenceResolver.resolve(dto.getFixedTurnover()))")
    void updateUniqueTurnover(UniqueTurnoverDTO dto, @MappingTarget UniqueTurnover uniqueTurnover, @Context ReferenceResolver referenceResolver);

    @Mapping(target = "category", expression = "java(referenceResolver.resolve(uniqueTurnoverInformationDTO.getCategory()))")
    UniqueTurnoverInformation uniqueTurnoverDTOToUniqueTurnover(UniqueTurnoverInformationDTO uniqueTurnoverInformationDTO, @Context ReferenceResolver referenceResolver);
}

package timkodiert.budgetbook.crud;

import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.UniqueTurnoverDTO;
import timkodiert.budgetbook.domain.UniqueTurnoverInformationDTO;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;

@Mapper
public interface UniqueTurnoverMapper {

    UniqueTurnoverDTO uniqueTurnoverToUniqueTurnoverDto(UniqueTurnover uniqueTurnover);

    @Mapping(target = "categories", source = "uniqueTurnoverInformation")
    UniqueTurnoverInformationDTO uniqueTurnoverInformationToUniqueTurnoverInformationDto(UniqueTurnoverInformation uniqueTurnoverInformation);

    default List<Reference<CategoryDTO>> mapCategories(UniqueTurnoverInformation uniqueTurnoverInformation) {
        return uniqueTurnoverInformation.getCategories()
                                        .stream()
                                        .map(c -> new Reference<>(CategoryDTO.class, c.getId(), c.getName()))
                                        .toList();
    }


    void updateUniqueTurnover(UniqueTurnoverDTO dto, @MappingTarget UniqueTurnover uniqueTurnover, @Context ReferenceResolver referenceResolver);

    @Mapping(target = "categories", expression = "java(referenceResolver.resolve(uniqueTurnoverInformationDTO.getCategories()))")
    UniqueTurnoverInformation uniqueTurnoverDTOToUniqueTurnover(UniqueTurnoverInformationDTO uniqueTurnoverInformationDTO, @Context ReferenceResolver referenceResolver);
}

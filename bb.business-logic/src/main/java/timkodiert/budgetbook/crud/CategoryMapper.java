package timkodiert.budgetbook.crud;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.Category;

@Mapper
public abstract class CategoryMapper {

    @Mapping(target = "group", source = "category")
    @Mapping(target = "hasLinkedTurnover", source = "category")
    abstract CategoryDTO categoryToDto(Category category);

    protected Reference<CategoryGroupDTO> mapGroup(Category category) {
        if (category.getGroup() == null) {
            return null;
        }
        return new Reference<>(CategoryGroupDTO.class, category.getGroup().getId(), category.getGroup().getName());
    }

    protected boolean mapHasLinkedTurnover(Category category) {
        return !category.getFixedExpenses().isEmpty() || !category.getUniqueExpenseInformation().isEmpty();
    }

    @Mapping(target = "group", expression = "java(referenceResolver.resolve(dto.getGroup()))")
    abstract void updateCategory(CategoryDTO dto, @MappingTarget Category entity, @Context ReferenceResolver referenceResolver);
}

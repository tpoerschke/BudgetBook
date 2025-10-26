package timkodiert.budgetbook.crud;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.CategoryGroup;

@Mapper
public interface CategoryGroupMapper {

    @Mapping(target = "categories", source = "categoryGroup")
    CategoryGroupDTO categoryGroupToDto(CategoryGroup categoryGroup);

    default List<Reference<CategoryDTO>> mapCategories(CategoryGroup categoryGroup) {
        return categoryGroup.getCategories()
                            .stream()
                            .map(c -> new Reference<>(CategoryDTO.class, c.getId(), c.getName()))
                            .toList();
    }

    @Mapping(target = "categories", ignore = true)
    void updateCategoryGroup(CategoryGroupDTO dto, @MappingTarget CategoryGroup entity);
}

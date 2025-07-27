package timkodiert.budgetbook.crud;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.model.CategoryGroup;

@Mapper
public interface CategoryGroupMapper {

    CategoryGroupDTO categoryGroupToDto(CategoryGroup categoryGroup);

    void updateCategoryGroup(CategoryGroupDTO dto, @MappingTarget CategoryGroup entity);
}

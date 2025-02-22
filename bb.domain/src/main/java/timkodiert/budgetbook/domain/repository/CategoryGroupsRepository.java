package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.CategoryGroup;

public class CategoryGroupsRepository extends Repository<CategoryGroup> {

    @Inject
    public CategoryGroupsRepository() {
        super(CategoryGroup.class);
    }
}

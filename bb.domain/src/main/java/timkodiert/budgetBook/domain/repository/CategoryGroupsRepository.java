package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.CategoryGroup;

public class CategoryGroupsRepository extends Repository<CategoryGroup> {

    @Inject
    public CategoryGroupsRepository() {
        super(CategoryGroup.class);
    }
}

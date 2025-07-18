package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.CategoryGroup;
import timkodiert.budgetbook.domain.util.EntityManager;

public class CategoryGroupsRepository extends Repository<CategoryGroup> {

    @Inject
    public CategoryGroupsRepository(EntityManager entityManager) {
        super(entityManager, CategoryGroup.class);
    }
}

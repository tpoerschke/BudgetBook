package timkodiert.budgetbook.domain.repository;

import java.util.Collection;
import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.CategoryGroup;
import timkodiert.budgetbook.domain.util.EntityManager;

public class CategoryGroupsRepository extends Repository<CategoryGroup> {

    @Inject
    public CategoryGroupsRepository(EntityManager entityManager) {
        super(entityManager, CategoryGroup.class);
    }

    @Override
    public void remove(Collection<CategoryGroup> entities) {
        // Zunächst die Kategorie aus ihren Beziehungen lösen
        entities.forEach(entity -> {
            entity.getCategories().forEach(category -> {
                category.setGroup(null);
                entityManager.persist(category);
            });
        });
        super.remove(entities);
    }
}

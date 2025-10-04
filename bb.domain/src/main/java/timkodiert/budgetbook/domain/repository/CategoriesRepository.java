package timkodiert.budgetbook.domain.repository;

import java.util.Collection;
import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.Category;
import timkodiert.budgetbook.domain.model.CategoryGroup;
import timkodiert.budgetbook.domain.util.EntityManager;

public class CategoriesRepository extends Repository<Category> {

    @Inject
    public CategoriesRepository(EntityManager entityManager) {
        super(entityManager, Category.class);
    }

    @Override
    public void remove(Collection<Category> entities) {
        // Zunächst die Kategorie aus ihren Beziehungen lösen
        entities.forEach(entity -> {
            CategoryGroup group = entity.getGroup();
            if (group != null) {
                group.getCategories().remove(entity);
            }
            entity.getFixedExpenses().forEach(expense -> expense.getCategories().remove(entity));
            entity.getUniqueTurnoverInformation().forEach(info -> info.getCategories().remove(entity));
        });
        super.remove(entities);
    }
}

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
                group.getCategories().removeIf(c -> c.getId() == entity.getId());
                entityManager.merge(group);
            }
            entity.getFixedExpenses().forEach(turnover -> {
                turnover.setCategory(null);
                entityManager.merge(turnover);
            });
            entity.getUniqueTurnoverInformation().forEach(info -> {
                info.setCategory(null);
                entityManager.merge(info);
            });
        });
        super.remove(entities);
    }
}

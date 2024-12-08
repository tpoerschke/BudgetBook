package timkodiert.budgetBook.domain.repository;

import java.util.Collection;
import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.CategoryGroup;

public class CategoriesRepository extends Repository<Category> {

    @Inject
    public CategoriesRepository() {
        super(Category.class);
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
            entity.getUniqueExpenseInformation().forEach(info -> info.getCategories().remove(entity));
        });
        super.remove(entities);
    }
}

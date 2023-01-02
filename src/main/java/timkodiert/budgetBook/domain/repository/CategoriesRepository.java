package timkodiert.budgetBook.domain.repository;

import java.util.Collection;

import timkodiert.budgetBook.domain.model.Category;

public class CategoriesRepository extends Repository<Category> {
    
    public CategoriesRepository() {
        super(Category.class);
    }

    @Override
    public void remove(Collection<Category> entities) {
        // Zunächst die Kategorie aus ihren Beziehungen lösen
        entities.forEach(entity -> {
            if(entity.getParent() != null) {
                entity.getParent().getChildren().remove(entity);
            }
           entity.getExpenses().forEach(expense -> expense.getCategories().remove(entity));
        });
        super.remove(entities);
    }
}

package timkodiert.budgetBook.domain.adapter;

import lombok.Getter;

import timkodiert.budgetBook.domain.model.Category;

public class CategoryAdapter implements Adapter<Category> {

    @Getter
    private final Category bean;

    public CategoryAdapter(Category bean) {
        this.bean = bean;
    }
}

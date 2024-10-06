package timkodiert.budgetBook.domain.adapter;

import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import lombok.Getter;

import timkodiert.budgetBook.domain.model.Category;

public class CategoryAdapter implements Adapter<Category> {

    private final StringProperty name;

    @Getter
    private final Category bean;

    public CategoryAdapter(Category bean) throws NoSuchMethodException {
        this.name = JavaBeanStringPropertyBuilder.create().bean(bean).name("name").build();

        this.bean = bean;
    }

    public StringProperty nameProperty() {
        return name;
    }
}

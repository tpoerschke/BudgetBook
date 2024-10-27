package timkodiert.budgetBook.domain.adapter;

import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import lombok.Getter;

import timkodiert.budgetBook.domain.model.CategoryGroup;

public class CategoryGroupAdapter implements Adapter<CategoryGroup> {

    private final StringProperty name;

    @Getter
    private final CategoryGroup bean;

    public CategoryGroupAdapter(CategoryGroup bean) throws NoSuchMethodException {
        this.name = JavaBeanStringPropertyBuilder.create().bean(bean).name("name").build();
        this.bean = bean;
    }

    public StringProperty nameProperty() {
        return name;
    }
}

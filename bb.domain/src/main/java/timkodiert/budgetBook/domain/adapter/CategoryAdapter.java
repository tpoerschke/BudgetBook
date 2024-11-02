package timkodiert.budgetBook.domain.adapter;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectPropertyBuilder;
import lombok.Getter;

import timkodiert.budgetBook.domain.model.Category;
import timkodiert.budgetBook.domain.model.CategoryGroup;

public class CategoryAdapter implements Adapter<Category> {

    private final StringProperty name;
    private final ReadOnlyObjectProperty<CategoryGroup> group;

    @Getter
    private final Category bean;

    public CategoryAdapter(Category bean) throws NoSuchMethodException {
        this.name = JavaBeanStringPropertyBuilder.create().bean(bean).name("name").build();
        this.group = ReadOnlyJavaBeanObjectPropertyBuilder.<CategoryGroup>create().bean(bean).name("group").build();

        this.bean = bean;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ReadOnlyObjectProperty<CategoryGroup> groupProperty() {
        return group;
    }
}

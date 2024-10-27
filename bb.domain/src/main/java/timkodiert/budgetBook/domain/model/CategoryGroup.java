package timkodiert.budgetBook.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import timkodiert.budgetBook.domain.adapter.Adaptable;
import timkodiert.budgetBook.domain.adapter.CategoryGroupAdapter;

@Getter
@Entity
public class CategoryGroup extends BaseEntity implements Adaptable<CategoryGroupAdapter> {

    @Setter
    private String name;

    @Setter
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private final Set<Category> categories = new HashSet<>();

    @Transient
    private transient CategoryGroupAdapter adapter;

    public CategoryGroup() {
        initAdapter();
    }

    @Override
    public void initAdapter() {
        try {
            adapter = new CategoryGroupAdapter(this);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryGroupAdapter getAdapter() {
        return adapter;
    }

    @Override
    public boolean contentEquals(Object other) {
        if (other instanceof CategoryGroup otherGroup) {
            return Objects.equals(name, otherGroup.getName()) && Objects.equals(description, otherGroup.getDescription());
        }
        return false;
    }

}

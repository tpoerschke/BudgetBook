package timkodiert.budgetBook.domain.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;

@Getter
@Entity
public class CategoryGroup extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private final Set<Category> categories = new HashSet<>();

    @Override
    public boolean contentEquals(Object other) {
        return false;
    }
}

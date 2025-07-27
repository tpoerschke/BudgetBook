package timkodiert.budgetbook.domain.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CategoryGroup extends BaseEntity {

    @Setter
    @NotEmpty
    private String name;

    @Setter
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private final Set<Category> categories = new HashSet<>();

    @Override
    public boolean contentEquals(Object other) {
        if (other instanceof CategoryGroup otherGroup) {
            return Objects.equals(name, otherGroup.getName()) && Objects.equals(description, otherGroup.getDescription());
        }
        return false;
    }

}

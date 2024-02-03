package timkodiert.budgetBook.domain.model;

import static timkodiert.budgetBook.util.ObjectUtils.nvl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import javafx.scene.control.CheckBoxTreeItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Category extends BaseEntity {

    @Setter
    @NonNull
    @NotBlank(message = "Die Kategorie muss benannt werden.")
    private String name;

    @Setter
    private String description;

    @Setter
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @Setter
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private List<FixedTurnover> fixedExpenses = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private List<UniqueTurnoverInformation> uniqueExpenseInformation = new ArrayList<>();

    @Transient
    private CheckBoxTreeItem<Category> treeItem = new CheckBoxTreeItem<>();

    public CheckBoxTreeItem<Category> asTreeItem() {
        this.treeItem.setValue(this);
        this.treeItem.getChildren().setAll(this.getChildren().stream().map(Category::asTreeItem).toList());
        return this.treeItem;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean contentEquals(Object other) {

        if (other instanceof Category cat) {
            boolean equals = Objects.equals(this.getName(), cat.getName())
                    && Objects.equals(this.getDescription(), cat.getDescription())
                    && nvl(this.getParent(), Category::getId) == nvl(cat.getParent(), Category::getId);

            List<Integer> thisChildrenIds = this.getChildren().stream().map(Category::getId).toList();
            List<Integer> otherChildrenIds = cat.getChildren().stream().map(Category::getId).toList();
            equals = equals && thisChildrenIds.containsAll(otherChildrenIds) && otherChildrenIds.containsAll(thisChildrenIds);

            List<Integer> thisExpIds = this.getFixedExpenses().stream().map(BaseEntity::getId).toList();
            List<Integer> otherExpIds = cat.getFixedExpenses().stream().map(BaseEntity::getId).toList();
            equals = equals && thisExpIds.containsAll(otherExpIds) && otherExpIds.containsAll(thisExpIds);

            List<Integer> thisInfoIds = this.getUniqueExpenseInformation().stream().map(UniqueTurnoverInformation::getId).toList();
            List<Integer> otherInfoIds = cat.getUniqueExpenseInformation().stream().map(UniqueTurnoverInformation::getId).toList();
            equals = equals && thisInfoIds.containsAll(otherInfoIds) && otherInfoIds.containsAll(thisInfoIds);
            return equals;
        }

        return false;
    }
}

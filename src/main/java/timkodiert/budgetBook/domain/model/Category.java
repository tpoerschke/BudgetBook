package timkodiert.budgetBook.domain.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Category {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private int id;

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
    @OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
    private List<Category> children = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private List<Expense> expenses = new ArrayList<>();

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
}

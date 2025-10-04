package timkodiert.budgetbook.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import timkodiert.budgetbook.domain.model.BudgetType;

@Getter
@Setter
public class CategoryDTO {

    private int id;
    @NonNull
    @NotBlank(message = "Die Kategorie muss benannt werden.")
    private String name;
    private String description;
    private @Nullable Reference<CategoryGroupDTO> group;

    private boolean hasLinkedTurnover;

    private boolean budgetActive;
    private @Nullable Integer budgetValue;
    private @Nullable BudgetType budgetType;

    public boolean isNew() {
        return id <= 0;
    }
}

package timkodiert.budgetbook.domain;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryGroupDTO {

    private int id = 0;
    @NotEmpty
    private String name;
    private String description;

    public boolean isNew() {
        return id <= 0;
    }
}

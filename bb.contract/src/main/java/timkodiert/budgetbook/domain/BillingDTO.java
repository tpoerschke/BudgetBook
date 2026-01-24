package timkodiert.budgetbook.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BillingDTO {

    private int id = 0;
    @NotBlank(message = "Der Titel muss angegeben werden.")
    private String title;
    @NotBlank(message = "Die Beschreibung muss angegeben werden.")
    private String description;
    private List<SimplifiedUniqueTurnoverDTO> uniqueTurnovers = new ArrayList<>();

    public boolean isNew() {
        return id <= 0;
    }
}

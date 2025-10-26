package timkodiert.budgetbook.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import timkodiert.budgetbook.domain.model.TurnoverDirection;

@Getter
@Setter
public class UniqueTurnoverInformationDTO implements Serializable {

    private int id;
    @NotBlank(message = "{attribute.notNull}")
    private String label;
    private int value;
    @NotNull(message = "{attribute.notNull}")
    private TurnoverDirection direction;
    private List<Reference<CategoryDTO>> categories = new ArrayList<>();

    public UniqueTurnoverInformationDTO() {
        id = new Random().nextInt(Integer.MIN_VALUE, 0);
    }

    public int getValueSigned() {
        return value * direction.getSign();
    }
}

package timkodiert.budgetbook.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.Getter;
import lombok.Setter;

import timkodiert.budgetbook.domain.model.TurnoverDirection;

@Getter
@Setter
public class UniqueTurnoverInformationDTO {

    private int id;
    private String label;
    private double value;
    private TurnoverDirection direction;
    private List<Reference<CategoryDTO>> categories = new ArrayList<>();

    public UniqueTurnoverInformationDTO() {
        id = new Random().nextInt(Integer.MIN_VALUE, 0);
    }

    public double getValueSigned() {
        return value * direction.getSign();
    }
}

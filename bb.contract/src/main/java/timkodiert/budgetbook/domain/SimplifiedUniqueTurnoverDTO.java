package timkodiert.budgetbook.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimplifiedUniqueTurnoverDTO {

    private int id;
    private String biller;
    private LocalDate date;
    private double totalValue;
}

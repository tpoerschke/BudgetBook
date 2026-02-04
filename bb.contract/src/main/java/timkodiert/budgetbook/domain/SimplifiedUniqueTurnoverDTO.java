package timkodiert.budgetbook.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

import timkodiert.budgetbook.representation.HasRowType;
import timkodiert.budgetbook.representation.RowType;

@Getter
@AllArgsConstructor
public class SimplifiedUniqueTurnoverDTO implements HasRowType {

    private int id;
    private String biller;
    private LocalDate date;
    private double totalValue;
    private RowType rowType;
    
}

package timkodiert.budgetbook.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class Category extends BaseEntity {

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    private String description;

    @Setter
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private CategoryGroup group;

    @Setter
    private Double budgetValue;

    @Setter
    @Column(nullable = false)
    private boolean budgetActive = true;

    @Setter
    @Enumerated(EnumType.STRING)
    private BudgetType budgetType;

    @ManyToMany(mappedBy = "categories")
    private List<FixedTurnover> fixedExpenses = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private List<UniqueTurnoverInformation> uniqueExpenseInformation = new ArrayList<>();

    public boolean hasActiveBudget() {
        return budgetActive && budgetValue != null;
    }

    public double sumTurnovers(MonthYear monthYear) {
        return switch (budgetType) {
            case MONTHLY -> sumTurnoversForMonth(monthYear);
            case ANNUAL -> sumAnnualBudget(monthYear.getYear());
        };
    }

    public double sumTurnoversForMonth(MonthYear monthYear) {
        double sum = 0;
        sum += fixedExpenses.stream().mapToDouble(ft -> ft.getValueFor(monthYear)).sum();
        sum += uniqueExpenseInformation.stream()
                                       .filter(uti -> monthYear.containsDate(uti.getExpense().getDate()))
                                       .mapToDouble(UniqueTurnoverInformation::getValueSigned)
                                       .sum();
        return sum;
    }

    private double sumAnnualBudget(int year) {
        double sum = 0;
        sum += fixedExpenses.stream().mapToDouble(ft -> ft.getValueForYear(year)).sum();
        sum += uniqueExpenseInformation.stream()
                                       .filter(uti -> uti.getExpense().getDate().getYear() == year)
                                       .mapToDouble(UniqueTurnoverInformation::getValueSigned)
                                       .sum();
        return sum;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean contentEquals(Object other) {

        if (other instanceof Category cat) {
            Integer optGroupId = Optional.ofNullable(this.getGroup()).map(CategoryGroup::getId).orElse(null);
            Integer optOtherGroupId = Optional.ofNullable(cat.getGroup()).map(CategoryGroup::getId).orElse(null);
            return Objects.equals(this.getName(), cat.getName())
                    && Objects.equals(this.getDescription(), cat.getDescription())
                    && Objects.equals(optGroupId, optOtherGroupId);
        }
        return false;
    }
}

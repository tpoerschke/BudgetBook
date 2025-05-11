package timkodiert.budgetbook.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import timkodiert.budgetbook.domain.adapter.Adaptable;
import timkodiert.budgetbook.domain.adapter.CategoryAdapter;

import static timkodiert.budgetbook.util.ObjectUtils.nvl;

@Getter
@RequiredArgsConstructor
@Entity
public class Category extends BaseEntity implements Adaptable<CategoryAdapter> {

    @Setter
    @NonNull
    @NotBlank(message = "Die Kategorie muss benannt werden.")
    private String name;

    @Setter
    private String description;

    @Setter
    @ManyToOne
    @JoinColumn(name = "group_id")
    private CategoryGroup group;

    @Setter
    private Double budgetValue;

    @Setter
    private boolean budgetActive = true;

    @Setter
    @Enumerated(EnumType.STRING)
    private BudgetType budgetType;

    @ManyToMany(mappedBy = "categories")
    private List<FixedTurnover> fixedExpenses = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private List<UniqueTurnoverInformation> uniqueExpenseInformation = new ArrayList<>();

    @Transient
    private transient CategoryAdapter adapter;

    public Category() {
        initAdapter();
    }

    @Override
    public void initAdapter() {
        try {
            this.adapter = new CategoryAdapter(this);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

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
            return Objects.equals(this.getName(), cat.getName())
                    && Objects.equals(this.getDescription(), cat.getDescription())
                    && Objects.equals(nvl(this.getGroup(), CategoryGroup::getId), nvl(cat.getGroup(), CategoryGroup::getId));
        }

        return false;
    }
}

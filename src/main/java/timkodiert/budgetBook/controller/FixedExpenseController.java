package timkodiert.budgetBook.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.repository.Repository;

@Singleton
public class FixedExpenseController {

    @Getter
    private final ObservableList<FixedExpense> allExpenses;

    private Repository<FixedExpense> repository;

    @Inject
    public FixedExpenseController(Repository<FixedExpense> repository) {
        this.repository = repository;
        this.allExpenses = FXCollections.observableArrayList();
    }

    public FixedExpense getExpense(int id) {
        return this.allExpenses.stream().filter(exp -> exp.getId() == id).findFirst().get();
    }

    public void loadAll() {
        allExpenses.setAll(repository.findAll());
    }
}

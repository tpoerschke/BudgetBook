package timkodiert.budgetBook.controller;

import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import timkodiert.budgetBook.domain.model.FixedTurnover;
import timkodiert.budgetBook.domain.repository.Repository;

@Singleton
public class FixedExpenseController {

    @Getter
    private final ObservableList<FixedTurnover> allExpenses;

    private Repository<FixedTurnover> repository;

    @Inject
    public FixedExpenseController(Repository<FixedTurnover> repository) {
        this.repository = repository;
        this.allExpenses = FXCollections.observableArrayList();
    }

    public FixedTurnover getExpense(int id) {
        return this.allExpenses.stream().filter(exp -> exp.getId() == id).findFirst().get();
    }

    public void loadAll() {
        allExpenses.setAll(repository.findAll());
    }
}

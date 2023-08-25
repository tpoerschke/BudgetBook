package timkodiert.budgetBook.controller;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.inject.Singleton;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import lombok.Getter;
import timkodiert.budgetBook.Constants;
import timkodiert.budgetBook.domain.model.FixedExpenseAdapter;
import timkodiert.budgetBook.domain.model.PaymentType;
import timkodiert.budgetBook.domain.repository.Repository;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.util.SumChangeListener;
import timkodiert.budgetBook.util.SumListChangeListener;

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

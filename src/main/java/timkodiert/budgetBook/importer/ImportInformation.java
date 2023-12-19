package timkodiert.budgetBook.importer;

import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

import timkodiert.budgetBook.domain.model.AccountTurnover;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.domain.model.UniqueExpense;
import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;


// Aus den Werten der Properties wird u.a. eine einzigartige Ausgabe
public class ImportInformation {

    private static final String ANNOTATION_EMPTY = "";
    public static final String ANNOTATION_UNIQUE_EXPENSE = "Wird zu einzigartiger Ausgabe.";
    public static final String ANNOTATION_ALREADY_IMPORTED = "Ausgabe wurde bereits importiert.";

    private final BooleanProperty selectedForImport = new SimpleBooleanProperty(true);

    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final StringProperty receiver = new SimpleStringProperty();
    private final StringProperty postingText = new SimpleStringProperty();
    private final StringProperty reference = new SimpleStringProperty();
    private final DoubleProperty amount = new SimpleDoubleProperty();
    private final ObjectProperty<FixedExpense> fixedExpense = new SimpleObjectProperty<>();
    private final StringProperty annotation = new SimpleStringProperty();
    private final BooleanProperty alreadyImported = new SimpleBooleanProperty();

    @Getter
    private final AccountTurnover accountTurnover;

    private ImportInformation(AccountTurnover accountTurnover) {
        this.accountTurnover = accountTurnover;

        date.set(accountTurnover.getDate());
        receiver.set(accountTurnover.getReceiver());
        postingText.set(accountTurnover.getPostingText());
        reference.set(accountTurnover.getReference());
        amount.set(accountTurnover.getAmount());

        fixedExpense.addListener((observableValue, oldVal, newVal) -> updateAnnotation());
        alreadyImported.addListener((observableValue, oldVal, newVal) -> updateAnnotation());
        selectedForImport.addListener((observableValue, oldVal, newVal) -> updateAnnotation());
        updateAnnotation();

        if (amount.get() > 0) {
            selectedForImport.set(false);
        }
    }

    private void updateAnnotation() {
        if (alreadyImported.get()) {
            annotation.set(ANNOTATION_ALREADY_IMPORTED);
            return;
        }

        if (!selectedForImport.get()) {
            annotation.set(ANNOTATION_EMPTY);
            return;
        }

        if (fixedExpense.get() == null) {
            annotation.set(ANNOTATION_UNIQUE_EXPENSE);
            return;
        }

        annotation.set(ANNOTATION_EMPTY);
    }

    static ImportInformation from(AccountTurnover accountTurnover) {
        return new ImportInformation(accountTurnover);
    }

    public AccountTurnover accountTurnoverWithFixedExpense() {
        accountTurnover.setFixedExpense(fixedExpense.get());
        accountTurnover.getFixedExpense().getAccountTurnover().add(accountTurnover);
        return accountTurnover;
    }

    public AccountTurnover accountTurnoverWithUniqueExpense() {
        UniqueExpense exp = new UniqueExpense();
        exp.setBiller(receiver.get());
        exp.setDate(accountTurnover.getDate());
        exp.setPaymentInformations(List.of(UniqueExpenseInformation.total(exp, Math.abs(accountTurnover.getAmount()))));
        accountTurnover.setUniqueExpense(exp);
        return accountTurnover;
    }

    public boolean isSelectedForImport() {
        return selectedForImport.get();
    }

    public boolean hasFixedExpense() {
        return fixedExpenseProperty().get() != null;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public BooleanProperty selectedForImportProperty() {
        return selectedForImport;
    }

    public StringProperty receiverProperty() {
        return receiver;
    }

    public StringProperty postingTextProperty() {
        return postingText;
    }

    public StringProperty referenceProperty() {
        return reference;
    }

    public DoubleProperty amountProperty() {
        return amount;
    }

    public ObjectProperty<FixedExpense> fixedExpenseProperty() {
        return fixedExpense;
    }

    public StringProperty annotationProperty() {
        return annotation;
    }

    public BooleanProperty alreadyImportedProperty() {
        return alreadyImported;
    }
}

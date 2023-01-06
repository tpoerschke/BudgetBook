package timkodiert.budgetBook.view.factory;

import dagger.assisted.AssistedFactory;
import timkodiert.budgetBook.domain.model.FixedExpense;
import timkodiert.budgetBook.view.EditExpenseView;

@AssistedFactory
public interface EditExpenseViewFactory {
    EditExpenseView create(FixedExpense expense);
}

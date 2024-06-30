package timkodiert.budgetBook.view.fixed_turnover;

import java.util.function.Consumer;
import java.util.function.Supplier;

import dagger.assisted.AssistedFactory;

import timkodiert.budgetBook.domain.model.PaymentInformation;

@AssistedFactory
public interface FixedTurnoverInformationDetailViewFactory {

    FixedTurnoverInformationDetailView create(Supplier<PaymentInformation> emptyEntityProducer, Consumer<PaymentInformation> onSaveCallback);
}

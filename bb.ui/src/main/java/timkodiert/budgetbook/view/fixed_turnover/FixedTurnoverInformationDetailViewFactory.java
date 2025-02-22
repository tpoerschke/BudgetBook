package timkodiert.budgetbook.view.fixed_turnover;

import java.util.function.Consumer;
import java.util.function.Supplier;

import dagger.assisted.AssistedFactory;

import timkodiert.budgetbook.domain.model.PaymentInformation;

@AssistedFactory
public interface FixedTurnoverInformationDetailViewFactory {

    FixedTurnoverInformationDetailView create(Supplier<PaymentInformation> emptyEntityProducer, Consumer<PaymentInformation> onSaveCallback);
}

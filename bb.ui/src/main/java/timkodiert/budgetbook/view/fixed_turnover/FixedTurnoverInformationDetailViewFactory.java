package timkodiert.budgetbook.view.fixed_turnover;

import java.util.function.BiConsumer;

import dagger.assisted.AssistedFactory;

import timkodiert.budgetbook.domain.PaymentInformationDTO;

@AssistedFactory
public interface FixedTurnoverInformationDetailViewFactory {

    FixedTurnoverInformationDetailView create(BiConsumer<PaymentInformationDTO, PaymentInformationDTO> updateCallback);
}

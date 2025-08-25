package timkodiert.budgetbook.view.unique_turnover;

import java.util.function.BiConsumer;

import dagger.assisted.AssistedFactory;

import timkodiert.budgetbook.domain.UniqueTurnoverInformationDTO;

@AssistedFactory
public interface UniqueTurnoverInformationDetailViewFactory {
    UniqueExpenseInformationDetailView create(BiConsumer<UniqueTurnoverInformationDTO, UniqueTurnoverInformationDTO> updateCallback);
}

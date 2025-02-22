package timkodiert.budgetbook.domain.repository;

import java.util.Collection;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;

public class UniqueExpenseInformationRepository extends Repository<UniqueTurnoverInformation> {

    @Inject
    public UniqueExpenseInformationRepository() {
        super(UniqueTurnoverInformation.class);
    }

    @Override
    public void remove(Collection<UniqueTurnoverInformation> entities) {
        // Zunächst die Entity aus ihren Beziehungen lösen
        entities.forEach(entity -> {
            entity.getExpense().getPaymentInformations().remove(entity);
        });
        super.remove(entities);
    }
}

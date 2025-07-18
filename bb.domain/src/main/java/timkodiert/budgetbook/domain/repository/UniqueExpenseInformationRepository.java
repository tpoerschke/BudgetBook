package timkodiert.budgetbook.domain.repository;

import java.util.Collection;
import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetbook.domain.util.EntityManager;

public class UniqueExpenseInformationRepository extends Repository<UniqueTurnoverInformation> {

    @Inject
    public UniqueExpenseInformationRepository(EntityManager entityManager) {
        super(entityManager, UniqueTurnoverInformation.class);
    }

    @Override
    public void remove(Collection<UniqueTurnoverInformation> entities) {
        // Zunächst die Entity aus ihren Beziehungen lösen
        entities.forEach(entity -> entity.getExpense().getPaymentInformations().remove(entity));
        super.remove(entities);
    }
}

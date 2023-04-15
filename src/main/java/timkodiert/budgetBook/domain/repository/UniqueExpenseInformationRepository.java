package timkodiert.budgetBook.domain.repository;

import java.util.Collection;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.UniqueExpenseInformation;

public class UniqueExpenseInformationRepository extends Repository<UniqueExpenseInformation> {

    @Inject
    public UniqueExpenseInformationRepository() {
        super(UniqueExpenseInformation.class);
    }

    @Override
    public void remove(Collection<UniqueExpenseInformation> entities) {
        // Zunächst die Entity aus ihren Beziehungen lösen
        entities.forEach(entity -> {
            entity.getExpense().getPaymentInformations().remove(entity);
        });
        super.remove(entities);
    }
}

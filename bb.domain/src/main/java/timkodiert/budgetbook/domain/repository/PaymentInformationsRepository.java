package timkodiert.budgetbook.domain.repository;

import java.util.Collection;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.PaymentInformation;

public class PaymentInformationsRepository extends Repository<PaymentInformation> {

    @Inject
    public PaymentInformationsRepository() {
        super(PaymentInformation.class);
    }

    @Override
    public void remove(Collection<PaymentInformation> entities) {
        // Zunächst die PaymentInformation aus ihren Beziehungen lösen
        entities.forEach(entity -> {
            entity.getExpense().getPaymentInformations().remove(entity);
        });
        super.remove(entities);
    }
}

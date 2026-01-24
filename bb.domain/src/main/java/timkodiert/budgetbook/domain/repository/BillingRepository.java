package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.Billing;
import timkodiert.budgetbook.domain.util.EntityManager;

public class BillingRepository extends Repository<Billing> {

    @Inject
    public BillingRepository(EntityManager entityManager) {
        super(entityManager, Billing.class);
    }
}

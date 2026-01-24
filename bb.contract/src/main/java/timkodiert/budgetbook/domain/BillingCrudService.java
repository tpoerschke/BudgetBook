package timkodiert.budgetbook.domain;

import java.util.List;

public interface BillingCrudService {

    List<BillingDTO> readAll();
    BillingDTO readById(int id);

    boolean create(BillingDTO billingDTO);
    boolean update(BillingDTO billingDTO);
    boolean delete(int id);
}

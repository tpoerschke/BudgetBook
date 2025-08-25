package timkodiert.budgetbook.domain;

import java.util.List;

public interface FixedTurnoverCrudService {

    List<FixedTurnoverDTO> readAll();
    FixedTurnoverDTO readById(int id);
    PaymentInformationDTO readPaymentInformationById(int id);

    boolean create(FixedTurnoverDTO fixedTurnoverDTO);
    boolean update(FixedTurnoverDTO fixedTurnoverDTO);
    boolean delete(int id);
}

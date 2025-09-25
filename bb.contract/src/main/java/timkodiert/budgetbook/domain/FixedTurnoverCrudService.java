package timkodiert.budgetbook.domain;

import java.util.List;

public interface FixedTurnoverCrudService {

    List<Reference<FixedTurnoverDTO>> findAllAsReference();
    List<FixedTurnoverDTO> readAll();
    FixedTurnoverDTO readById(int id);
    PaymentInformationDTO readPaymentInformationById(int id);

    boolean create(FixedTurnoverDTO fixedTurnoverDTO);
    boolean update(FixedTurnoverDTO fixedTurnoverDTO);
    boolean delete(int id);
}

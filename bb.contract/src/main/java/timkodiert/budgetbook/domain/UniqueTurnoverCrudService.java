package timkodiert.budgetbook.domain;

import java.util.List;

public interface UniqueTurnoverCrudService {

    List<UniqueTurnoverDTO> readAll();
    UniqueTurnoverDTO readById(int id);

    boolean create(UniqueTurnoverDTO fixedTurnoverDTO);
    boolean update(UniqueTurnoverDTO fixedTurnoverDTO);
    boolean delete(UniqueTurnoverDTO fixedTurnoverDTO);
}

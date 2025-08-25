package timkodiert.budgetbook.domain;

import java.util.List;

public interface UniqueTurnoverCrudService {

    List<UniqueTurnoverDTO> readAll();
    UniqueTurnoverDTO readById(int id);
    UniqueTurnoverInformationDTO readUniqueTurnoverInformationById(int id);

    boolean create(UniqueTurnoverDTO uniqueTurnoverDTO);
    boolean update(UniqueTurnoverDTO uniqueTurnoverDTO);
    boolean delete(int id);

    List<String> getUniqueTurnoverLabels();
    List<String> getUniqueTurnoverInformationLabels();
}

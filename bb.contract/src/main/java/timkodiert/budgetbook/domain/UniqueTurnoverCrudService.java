package timkodiert.budgetbook.domain;

import java.util.List;

import org.jetbrains.annotations.Nullable;

public interface UniqueTurnoverCrudService {

    List<UniqueTurnoverDTO> readAll(@Nullable Reference<FixedTurnoverDTO> fixedTurnoverRef);
    UniqueTurnoverDTO readById(int id);
    UniqueTurnoverInformationDTO readUniqueTurnoverInformationById(int id);

    boolean create(UniqueTurnoverDTO uniqueTurnoverDTO);
    boolean update(UniqueTurnoverDTO uniqueTurnoverDTO);
    boolean delete(int id);

    List<String> getUniqueTurnoverLabels();
    List<String> getUniqueTurnoverInformationLabels();
}

package timkodiert.budgetbook.crud;

import java.util.List;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.UniqueTurnoverCrudService;
import timkodiert.budgetbook.domain.UniqueTurnoverDTO;

public class UniqueTurnoverCrudServiceImpl implements UniqueTurnoverCrudService {

    @Inject
    public UniqueTurnoverCrudServiceImpl() {}

    @Override
    public List<UniqueTurnoverDTO> readAll() {
        return null;
    }

    @Override
    public UniqueTurnoverDTO readById(int id) {
        return null;
    }

    @Override
    public boolean create(UniqueTurnoverDTO fixedTurnoverDTO) {
        return false;
    }

    @Override
    public boolean update(UniqueTurnoverDTO fixedTurnoverDTO) {
        return false;
    }

    @Override
    public boolean delete(UniqueTurnoverDTO fixedTurnoverDTO) {
        return false;
    }
}

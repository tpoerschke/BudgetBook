package timkodiert.budgetbook.crud;

import java.util.List;

import jakarta.inject.Inject;
import org.mapstruct.factory.Mappers;

import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverDTO;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.repository.Repository;

public class FixedTurnoverCrudServiceImpl implements FixedTurnoverCrudService {

    private final Repository<FixedTurnover> repository;

    @Inject
    public FixedTurnoverCrudServiceImpl(Repository<FixedTurnover> repository) {
        this.repository = repository;
    }

    @Override
    public List<FixedTurnoverDTO> readAll() {
        FixedTurnoverMapper mapper = Mappers.getMapper(FixedTurnoverMapper.class);
        return repository.findAll().stream().map(mapper::fixedTurnoverToFixedTurnoverDto).toList();
    }

    @Override
    public FixedTurnoverDTO readById(int id) {
        FixedTurnoverMapper mapper = Mappers.getMapper(FixedTurnoverMapper.class);
        return repository.findAll().stream().filter(t -> t.getId() == id).findAny().map(mapper::fixedTurnoverToFixedTurnoverDto).orElse(null);
    }

    @Override
    public boolean create(FixedTurnoverDTO fixedTurnoverDTO) {
        return false;
    }

    @Override
    public boolean update(FixedTurnoverDTO fixedTurnoverDTO) {
        return false;
    }

    @Override
    public boolean delete(FixedTurnoverDTO fixedTurnoverDTO) {
        return false;
    }
}

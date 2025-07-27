package timkodiert.budgetbook.crud;

import java.util.List;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.CategoryCrudService;
import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.CategoryGroupDTO;

public class CategoryCrudServiceImpl implements CategoryCrudService {

    @Inject
    public CategoryCrudServiceImpl() {}

    @Override
    public List<CategoryGroupDTO> readAll() {
        return null;
    }

    @Override
    public CategoryGroupDTO readById(int id) {
        return null;
    }

    @Override
    public boolean create(CategoryDTO categoryDTO) {
        return false;
    }

    @Override
    public boolean update(CategoryDTO categoryDTO) {
        return false;
    }

    @Override
    public boolean delete(CategoryDTO categoryDTO) {
        return false;
    }
}

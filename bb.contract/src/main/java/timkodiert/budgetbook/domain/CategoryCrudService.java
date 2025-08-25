package timkodiert.budgetbook.domain;

import java.util.List;

public interface CategoryCrudService {

    List<CategoryDTO> readAll();
    CategoryDTO readById(int id);

    boolean create(CategoryDTO categoryDTO);
    boolean update(CategoryDTO categoryDTO);
    boolean delete(int id);
}

package timkodiert.budgetbook.domain;

import java.util.List;

public interface CategoryCrudService {

    List<CategoryGroupDTO> readAll();
    CategoryGroupDTO readById(int id);

    boolean create(CategoryDTO categoryDTO);
    boolean update(CategoryDTO categoryDTO);
    boolean delete(CategoryDTO categoryDTO);
}

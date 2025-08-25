package timkodiert.budgetbook.crud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.repository.CategoriesRepository;
import timkodiert.budgetbook.domain.repository.CategoryGroupsRepository;
import timkodiert.budgetbook.domain.repository.Repository;

public class ReferenceResolver {

    private final Map<Class<?>, Repository<?>> repositoryMap = new HashMap<>();

    @Inject
    public ReferenceResolver(CategoryGroupsRepository categoryGroupsRepository, CategoriesRepository categoriesRepository) {
        repositoryMap.put(CategoryGroupDTO.class, categoryGroupsRepository);
        repositoryMap.put(CategoryDTO.class, categoriesRepository);
    }

    public <R, T> T resolve(Reference<R> reference) {
        if (reference == null) {
            return null;
        }
        return loadEntityForReference(reference);
    }

    public <R, T> List<T> resolve(List<Reference<R>> references) {
        return (List<T>) references.stream().map(this::loadEntityForReference).toList();
    }

    private <R, T> T loadEntityForReference(Reference<R> reference) {
        return (T) repositoryMap.get(reference.refClass()).findById(reference.id());
    }
}

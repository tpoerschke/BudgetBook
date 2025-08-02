package timkodiert.budgetbook.crud;

import java.util.HashMap;
import java.util.Map;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.CategoryGroupDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.repository.CategoryGroupsRepository;
import timkodiert.budgetbook.domain.repository.Repository;

public class ReferenceResolver {

    private final Map<Class<?>, Repository<?>> repositoryMap = new HashMap<>();

    @Inject
    public ReferenceResolver(CategoryGroupsRepository categoryGroupsRepository) {
        repositoryMap.put(CategoryGroupDTO.class, categoryGroupsRepository);
    }

    public <R, T> T resolve(Reference<R> reference) {
        if (reference == null) {
            return null;
        }
        return (T) repositoryMap.get(reference.refClass()).findById(reference.id());
    }
}

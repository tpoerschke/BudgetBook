package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.ImportRule;
import timkodiert.budgetbook.domain.util.EntityManager;

public class ImportRulesRepository extends Repository<ImportRule> {

    @Inject
    public ImportRulesRepository(EntityManager entityManager) {
        super(entityManager, ImportRule.class);
    }
}

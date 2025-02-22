package timkodiert.budgetbook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetbook.domain.model.ImportRule;

public class ImportRulesRepository extends Repository<ImportRule> {

    @Inject
    public ImportRulesRepository() {
        super(ImportRule.class);
    }
}

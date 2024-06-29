package timkodiert.budgetBook.domain.repository;

import javax.inject.Inject;

import timkodiert.budgetBook.domain.model.ImportRule;

public class ImportRulesRepository extends Repository<ImportRule> {

    @Inject
    public ImportRulesRepository() {
        super(ImportRule.class);
    }
}

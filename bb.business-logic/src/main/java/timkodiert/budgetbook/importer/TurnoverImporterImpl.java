package timkodiert.budgetbook.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Predicate;
import javax.inject.Inject;

import com.opencsv.bean.CsvToBeanBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import timkodiert.budgetbook.domain.model.AccountTurnover;
import timkodiert.budgetbook.domain.model.ImportRule;
import timkodiert.budgetbook.domain.repository.AccountTurnoverRepository;
import timkodiert.budgetbook.domain.repository.ImportRulesRepository;

import static timkodiert.budgetbook.domain.model.AccountTurnover.SKIP_LINES;
import static timkodiert.budgetbook.util.ObjectUtils.nvl;

public class TurnoverImporterImpl implements TurnoverImporter {

    private final ImportRulesRepository importRulesRepository;
    private final AccountTurnoverRepository accountTurnoverRepository;

    @Getter
    private final ObservableList<ImportInformation> importInformationList = FXCollections.observableArrayList();

    @Inject
    public TurnoverImporterImpl(ImportRulesRepository importRulesRepository, AccountTurnoverRepository accountTurnoverRepository) {
        this.importRulesRepository = importRulesRepository;
        this.accountTurnoverRepository = accountTurnoverRepository;
    }

    @Override
    public TurnoverImporter parse(File file) throws IOException, IllegalStateException {
        var builder = new CsvToBeanBuilder<AccountTurnover>(new FileReader(file, StandardCharsets.ISO_8859_1));
        List<AccountTurnover> imports = builder.withSkipLines(SKIP_LINES).withSeparator(';').withType(AccountTurnover.class).build().parse();
        importInformationList.setAll(imports.stream().map(ImportInformation::from).toList());
        return this;
    }

    @Override
    public TurnoverImporter linkWithExpenses() {
        List<ImportRule> rules = importRulesRepository.findAll().stream().filter(ImportRule::isActive).toList();
        importInformationList.forEach(info -> rules.stream()
                                                   .filter(filterRule(info))
                                                   .findAny()
                                                   .ifPresent(rule -> info.fixedExpenseProperty().set(rule.getLinkedFixedExpense())));
        return this;
    }

    @Override
    public TurnoverImporter filterDuplicates() {
        List<AccountTurnover> allImports = accountTurnoverRepository.findAll();

        importInformationList.forEach(importInfo -> {
            if (allImports.contains(importInfo.getAccountTurnover())) {
                importInfo.selectedForImportProperty().set(false);
                importInfo.alreadyImportedProperty().set(true);
            }
        });

        return this;
    }

    @Override
    public void doImport() {
        List<AccountTurnover> importsWithFixedExpense = importInformationList.stream()
                                                                             .filter(ImportInformation::isSelectedForImport)
                                                                             .filter(ImportInformation::hasFixedExpense)
                                                                             .map(ImportInformation::accountTurnoverWithFixedExpense)
                                                                             .toList();

        List<AccountTurnover> importsWithUniqueExpense = importInformationList.stream()
                                                                              .filter(ImportInformation::isSelectedForImport)
                                                                              .filter(Predicate.not(ImportInformation::hasFixedExpense))
                                                                              .map(ImportInformation::accountTurnoverWithUniqueExpense)
                                                                              .toList();

        accountTurnoverRepository.persist(importsWithFixedExpense);
        accountTurnoverRepository.persist(importsWithUniqueExpense);
    }

    private Predicate<ImportRule> filterRule(ImportInformation importInformation) {
        return rule -> {
            if (!rule.isActive() || rule.isEmpty()) {
                return false;
            }

            boolean receiverContains = nvl(rule.getReceiverContains(),
                                           contains -> importInformation.receiverProperty().get().contains(contains),
                                           true);
            boolean referenceContains = nvl(rule.getReferenceContains(),
                                            contains -> importInformation.referenceProperty().get().contains(contains),
                                            true);
            return receiverContains && referenceContains;
        };
    }
}

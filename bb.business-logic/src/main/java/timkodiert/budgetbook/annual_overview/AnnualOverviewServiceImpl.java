package timkodiert.budgetbook.annual_overview;

import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import jakarta.inject.Inject;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;
import timkodiert.budgetbook.domain.model.FixedTurnover;
import timkodiert.budgetbook.domain.model.MonthYear;
import timkodiert.budgetbook.domain.model.TurnoverDirection;
import timkodiert.budgetbook.domain.model.UniqueTurnover;
import timkodiert.budgetbook.domain.model.UniqueTurnoverInformation;
import timkodiert.budgetbook.domain.repository.FixedExpensesRepository;
import timkodiert.budgetbook.domain.repository.UniqueExpensesRepository;

public class AnnualOverviewServiceImpl implements AnnualOverviewService {

    private final FixedExpensesRepository fixedTurnoverRepository;
    private final UniqueExpensesRepository uniqueTurnoverRepository;

    @Inject
    public AnnualOverviewServiceImpl(FixedExpensesRepository fixedTurnoverRepository, UniqueExpensesRepository uniqueTurnoverRepository) {
        this.fixedTurnoverRepository = fixedTurnoverRepository;
        this.uniqueTurnoverRepository = uniqueTurnoverRepository;
    }

    @Override
    public AnnualOverviewDTO generateOverview(int year) {
        List<FixedTurnover> fixedExpenses = loadAllRelevantFixedExpenses();
        List<UniqueTurnoverInformation> uniqueTurnoverInformationList = loadAllRelevantUniqueExpenseInformation(year);

        List<TableRowData> ftTableRowDataList = fixedExpenses.stream().map(t -> convertToRowData(t, year)).toList();
        List<TableRowData> utTableRowDataList = convertToRowData(uniqueTurnoverInformationList);

        TableRowData incomeSum = new TableRowData(-1, "Einnahmen", new HashMap<>(), null);
        sumTurnovers(incomeSum, loadAllRelevantFixedIncomes(), year);
        sumTurnovers(incomeSum, loadAllRelevantUniqueIncomeInformation(year));

        TableRowData expenseSum = new TableRowData(-1, "Einnahmen", new HashMap<>(), null);
        sumTurnovers(expenseSum, fixedExpenses, year);
        sumTurnovers(expenseSum, uniqueTurnoverInformationList);

        return new AnnualOverviewDTO(Stream.concat(ftTableRowDataList.stream(), utTableRowDataList.stream()).toList(), incomeSum, expenseSum);
    }

    private TableRowData convertToRowData(FixedTurnover turnover, int year) {
        Map<Integer, Integer> monthValueMap = new HashMap<>();
        for (Month month : Month.values()) {
            monthValueMap.put(month.getValue(), turnover.getValueFor(MonthYear.of(month.getValue(), year)));
        }
        Reference<CategoryDTO> categoryReference = Optional.ofNullable(turnover.getCategory())
                                                           .map(c -> new Reference<>(CategoryDTO.class, c.getId(), c.getName()))
                                                           .orElseGet(() -> new Reference<>(CategoryDTO.class, -1, "Sonstige"));
        return new TableRowData(turnover.getId(), turnover.getPosition(), monthValueMap, categoryReference);
    }

    private void sumTurnovers(TableRowData target, List<FixedTurnover> turnovers, int year) {
        turnovers.forEach(turnover -> {
            for (Month month : Month.values()) {
                target.monthValueMap().merge(month.getValue(), turnover.getValueFor(MonthYear.of(month.getValue(), year)), Math::addExact);
            }
        });
    }

    private void sumTurnovers(TableRowData target, List<UniqueTurnoverInformation> infoList) {
        infoList.forEach(info -> {
            int month = info.getExpense().getDate().getMonthValue();
            target.monthValueMap().merge(month, info.getValueSigned(), Math::addExact);
        });
    }

    private List<TableRowData> convertToRowData(List<UniqueTurnoverInformation> infoList) {
        Map<Reference<CategoryDTO>, Map<Integer, Integer>> categoryMonthValueMap = new HashMap<>();
        infoList.forEach(info -> {
            Reference<CategoryDTO> category = createCategoryFromTurnoverInformation(info);
            int month = info.getExpense().getDate().getMonthValue();
            var monthValueMap = categoryMonthValueMap.computeIfAbsent(category, k -> new HashMap<>());
            monthValueMap.put(month, monthValueMap.getOrDefault(month, 0) + info.getValueSigned());
        });
        return categoryMonthValueMap.entrySet().stream().map(entry -> new TableRowData(-1, "Sonstige", entry.getValue(), entry.getKey())).toList();
    }

    private Reference<CategoryDTO> createCategoryFromTurnoverInformation(UniqueTurnoverInformation info) {
        return Optional.ofNullable(info.getCategory())
                       .map(c -> new Reference<>(CategoryDTO.class, c.getId(), c.getName()))
                       .orElseGet(() -> new Reference<>(CategoryDTO.class, -1, "Sonstige"));
    }

    private List<FixedTurnover> loadAllRelevantFixedExpenses() {
        return fixedTurnoverRepository.findAll().stream().filter(turnover -> turnover.getDirection() == TurnoverDirection.OUT).toList();
    }

    private List<FixedTurnover> loadAllRelevantFixedIncomes() {
        return fixedTurnoverRepository.findAll().stream().filter(turnover -> turnover.getDirection() == TurnoverDirection.IN).toList();
    }

    private List<UniqueTurnoverInformation> loadAllRelevantUniqueExpenseInformation(int year) {
        return uniqueTurnoverRepository.findAllWithoutFixedExpense(year)
                                       .stream()
                                       .map(UniqueTurnover::getPaymentInformations)
                                       .flatMap(Collection::stream)
                                       .filter(info -> info.getDirection() == TurnoverDirection.OUT)
                                       .toList();
    }

    private List<UniqueTurnoverInformation> loadAllRelevantUniqueIncomeInformation(int year) {
        return uniqueTurnoverRepository.findAllWithoutFixedExpense(year)
                                       .stream()
                                       .map(UniqueTurnover::getPaymentInformations)
                                       .flatMap(Collection::stream)
                                       .filter(info -> info.getDirection() == TurnoverDirection.IN)
                                       .toList();
    }
}

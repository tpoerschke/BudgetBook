package timkodiert.budgetbook.analysis;

import java.time.YearMonth;
import java.util.List;

import timkodiert.budgetbook.domain.CategoryDTO;
import timkodiert.budgetbook.domain.Reference;

public interface AnalysisService {

    List<TableRowData> getTurnoverList(Reference<CategoryDTO> categoryRef, YearMonth yearMonth);
}

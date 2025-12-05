package timkodiert.budgetbook.domain.table;

import java.util.List;

// TODO: Refactor! Das soll in bb.contract (Modul) verschoben werden.
//  Da es nur zur Anzeige dient, sollte es nichts in der Domain zu suchen haben, oder?
public enum RowType {
    UNIQUE_EXPENSE, FIXED_EXPENSE, UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP, CATEGORY_GROUP, IMPORT, SUM, TOTAL_SUM, DEFAULT, EMPTY;

    public static List<RowType> getGroupTypes() {
        return List.of(UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP);
    }
}

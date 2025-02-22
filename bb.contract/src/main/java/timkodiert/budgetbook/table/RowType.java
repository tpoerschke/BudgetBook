package timkodiert.budgetbook.table;

import java.util.List;

// TODO: Refactor! Das soll in ui-infrastructure (Modul) verschoben werden.
//  Da es nur zur Anzeige dient, sollte es nichts in der Domain zu suchen haben, oder?
public enum RowType {
    UNIQUE_EXPENSE, FIXED_EXPENSE, UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP, IMPORT, SUM, TOTAL_SUM;

    public static List<RowType> getGroupTypes() {
        return List.of(UNIQUE_EXPENSE_GROUP, FIXED_EXPENSE_GROUP);
    }
}

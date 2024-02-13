package timkodiert.budgetBook.view;

import javafx.fxml.FXMLLoader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FxmlResource {

    ANNUAL_OVERVIEW("/fxml/MonthlyOverview.fxml"),
    EXPENSE_DETAIL_WIDGET("/fxml/ExpenseDetailWidget.fxml"),
    IMPORT_VIEW("/fxml/Importer/ImportView.fxml"),
    MAIN_VIEW("/fxml/Main.fxml"),
    MONTHLY_OVERVIEW("/fxml/MonthlyOverview.fxml"),
    MANAGE_FIXED_EXPENSES_VIEW("/fxml/fixed_turnover/Manage.fxml"),
    MANAGE_UNIQUE_EXPENSES_VIEW("/fxml/unique_turnover/Manage.fxml"),
    ;

    private final String path;

    public static FXMLLoader loadResourceIntoFxmlLoader(Class<?> clazz, FxmlResource fxmlResource){
        return new FXMLLoader(clazz.getResource(fxmlResource.path));
    }

    @Override
    public String toString(){
        return this.path;
    }

}

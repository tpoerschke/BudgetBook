package timkodiert.budgetBook.view;

import javafx.fxml.FXMLLoader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FxmlResource {

    ANNUAL_OVERVIEW("/fxml/MonthlyOverview.fxml"),
    EXPENSE_DETAIL_WIDGET("/fxml/ExpenseDetailWidget.fxml"),
    FIXED_TURNOVER_DETAIL_VIEW("/fxml/fixed_turnover/Detail.fxml"),
    IMPORT_VIEW("/fxml/Importer/ImportView.fxml"),
    IMAGE_VIEW("/fxml/ImageView.fxml"),
    MAIN_VIEW("/fxml/Main.fxml"),
    MONTHLY_OVERVIEW("/fxml/MonthlyOverview.fxml"),
    MONTH_YEAR_PICKER_WIDGET("/fxml/MonthYearPickerWidget.fxml"),
    MANAGE_CATEGORIES_VIEW("/fxml/ManageCategories.fxml"),
    MANAGE_FIXED_EXPENSES_VIEW("/fxml/fixed_turnover/Manage.fxml"),
    MANAGE_UNIQUE_EXPENSES_VIEW("/fxml/unique_turnover/Manage.fxml"),
    NEW_CATEGORY_VIEW("/fxml/NewCategory.fxml"),
    FIXED_EXPENSE_INFORMATION_DETAIL_VIEW("/fxml/fixed_turnover/FixedExpenseInformationDetailView.fxml"),
    UNIQUE_TURNOVER_DETAIL_VIEW("/fxml/unique_turnover/Detail.fxml"),
    UNIQUE_TURNOVER_INFORMATION_VIEW("/fxml/unique_turnover/Information.fxml"),
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

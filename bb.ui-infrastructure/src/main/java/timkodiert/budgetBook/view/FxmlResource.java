package timkodiert.budgetBook.view;

import javafx.fxml.FXMLLoader;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
@Getter
public enum FxmlResource {

    MAIN_VIEW("/fxml/Main.fxml", null),
    ANNUAL_OVERVIEW("/fxml/AnnualOverview.fxml", "stageTitle.annualOverView"),
    MONTHLY_OVERVIEW("/fxml/MonthlyOverview.fxml", "stageTitle.monthlyOverview"),
    MANAGE_REGULAR_TURNOVER_VIEW("/fxml/fixed_turnover/Manage.fxml", "stageTitle.regularExpensesOverview"),
    MANAGE_UNIQUE_TURNOVER_VIEW("/fxml/unique_turnover/Manage.fxml", "stageTitle.uniqueExpensesOverview"),
    FIXED_TURNOVER_DETAIL_VIEW("/fxml/fixed_turnover/Detail.fxml", null),
    UNIQUE_TURNOVER_DETAIL_VIEW("/fxml/unique_turnover/Detail.fxml", null),
    FIXED_TURNOVER_INFORMATION_VIEW("/fxml/fixed_turnover/FixedExpenseInformationDetailView.fxml", null),
    UNIQUE_TURNOVER_INFORMATION_VIEW("/fxml/unique_turnover/Information.fxml", null),
    EXPENSE_DETAIL_WIDGET("/fxml/ExpenseDetailWidget.fxml", null),
    IMPORT_VIEW("/fxml/Importer/ImportView.fxml", "stageTitle.importView"),
    IMAGE_VIEW("/fxml/ImageView.fxml", null),
    MONTH_YEAR_PICKER_WIDGET("/fxml/MonthYearPickerWidget.fxml", null),
    MANAGE_CATEGORY_VIEW("/fxml/category/Manage.fxml", "stageTitle.mdv.categories"),
    CATEGORY_DETAIL_VIEW("/fxml/category/Detail.fxml", null),
    NEW_CATEGORY_VIEW("/fxml/NewCategory.fxml", null),
    CATEGORY_GROUP_MANAGE_VIEW("/fxml/category_group/Manage.fxml", "stageTitle.mdv.categoryGroups"),
    CATEGORY_GROUP_DETAIL_VIEW("/fxml/category_group/Detail.fxml", null),
    BUDGET_WIDGET("/fxml/BudgetWidget.fxml", null);
    ;

    private final String path;
    private final @Nullable String stageTitle;

    public static FXMLLoader loadResourceIntoFxmlLoader(Class<?> clazz, FxmlResource fxmlResource){
        return new FXMLLoader(clazz.getResource(fxmlResource.path));
    }

    @Override
    public String toString(){
        return this.path;
    }

}

package timkodiert.budgetBook.view;

import javafx.fxml.FXMLLoader;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FxmlResource {

    EXPENSE_DETAIL_WIDGET("/fxml/ExpenseDetailWidget.fxml"),
    MONTHLY_OVERVIEW("/fxml/MonthlyOverview.fxml");

    private final String path;

    public static FXMLLoader loadResourceIntoFxmlLoader(Class<?> clazz, FxmlResource fxmlResource){
        return new FXMLLoader(clazz.getResource(fxmlResource.path));
    }

    @Override
    public String toString(){
        return this.path;
    }

}

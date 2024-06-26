package timkodiert.budgetBook.view;

import javax.inject.Inject;

import javafx.fxml.FXMLLoader;

import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.injector.ControllerFactory;

public class BbFxmlLoader extends FXMLLoader {

    @Inject
    public BbFxmlLoader(ControllerFactory controllerFactory, LanguageManager languageManager) {
        super();
        setControllerFactory(controllerFactory::create);
        setResources(languageManager.getResourceBundle());
    }
}

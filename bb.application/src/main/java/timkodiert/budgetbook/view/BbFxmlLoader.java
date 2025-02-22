package timkodiert.budgetbook.view;

import javax.inject.Inject;

import javafx.fxml.FXMLLoader;

import timkodiert.budgetbook.i18n.LanguageManager;
import timkodiert.budgetbook.injector.ControllerFactory;

public class BbFxmlLoader extends FXMLLoader {

    @Inject
    public BbFxmlLoader(ControllerFactory controllerFactory, LanguageManager languageManager) {
        super();
        setControllerFactory(controllerFactory::create);
        setResources(languageManager.getResourceBundle());
    }
}

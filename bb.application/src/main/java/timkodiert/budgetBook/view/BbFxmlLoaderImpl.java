package timkodiert.budgetBook.view;

import javax.inject.Inject;

import javafx.fxml.FXMLLoader;

import timkodiert.budgetBook.i18n.LanguageManager;
import timkodiert.budgetBook.injector.ControllerFactory;

public class BbFxmlLoaderImpl extends FXMLLoader implements BbFxmlLoader {

    @Inject
    public BbFxmlLoaderImpl(ControllerFactory controllerFactory, LanguageManager languageManager) {
        super();
        setControllerFactory(controllerFactory::create);
        setResources(languageManager.getResourceBundle());
    }
}

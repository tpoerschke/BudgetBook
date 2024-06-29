package timkodiert.budgetBook.injector;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import javafx.fxml.FXMLLoader;

import timkodiert.budgetBook.properties.PropertiesService;
import timkodiert.budgetBook.properties.PropertiesServiceImpl;
import timkodiert.budgetBook.view.BbFxmlLoader;
import timkodiert.budgetBook.view.MainView;
import timkodiert.budgetBook.view.MainViewImpl;

// @formatter:off
@Module
public interface ServiceModule {
    @Binds @Singleton MainView bindMainView(MainViewImpl impl);

    @Binds @Singleton PropertiesService bindPropertiesService(PropertiesServiceImpl impl);

    @Binds @Singleton FXMLLoader bindFXMLLoader(BbFxmlLoader impl);
}
// @formatter:on

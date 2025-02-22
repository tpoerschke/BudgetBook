package timkodiert.budgetbook.injector;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import javafx.fxml.FXMLLoader;

import timkodiert.budgetbook.properties.PropertiesService;
import timkodiert.budgetbook.view.MainView;
import timkodiert.budgetbook.properties.PropertiesServiceImpl;
import timkodiert.budgetbook.view.BbFxmlLoader;
import timkodiert.budgetbook.view.MainViewImpl;

// @formatter:off
@Module
public interface ServiceModule {
    @Binds @Singleton MainView bindMainView(MainViewImpl impl);

    @Binds @Singleton PropertiesService bindPropertiesService(PropertiesServiceImpl impl);

    @Binds FXMLLoader bindFXMLLoader(BbFxmlLoader impl);
}
// @formatter:on

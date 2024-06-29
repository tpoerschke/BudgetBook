package timkodiert.budgetBook.injector;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

import timkodiert.budgetBook.properties.PropertiesService;
import timkodiert.budgetBook.properties.PropertiesServiceImpl;
import timkodiert.budgetBook.view.BbFxmlLoader;
import timkodiert.budgetBook.view.BbFxmlLoaderImpl;
import timkodiert.budgetBook.view.MainView;
import timkodiert.budgetBook.view.MainViewImpl;

@Module
public interface ServiceModule {

    @Binds @Singleton MainView bindMainView(MainViewImpl impl);

    @Binds @Singleton PropertiesService bindPropertiesService(PropertiesServiceImpl impl);

    @Binds BbFxmlLoader bindFXMLLoader(BbFxmlLoaderImpl impl);
}

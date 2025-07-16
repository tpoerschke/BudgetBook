package timkodiert.budgetbook.injector;

import dagger.Module;
import dagger.Provides;
import jakarta.inject.Named;

import timkodiert.budgetbook.properties.PropertiesService;

@Module
public class DbPathModule {

    @Provides
    @Named("dbPath")
    String provideDbPath(PropertiesService propertiesService) {
        return propertiesService.getDbPath();
    }
}

package timkodiert.budgetbook.injector;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import javafx.fxml.FXMLLoader;

import timkodiert.budgetbook.analysis.CategorySeriesGenerator;
import timkodiert.budgetbook.analysis.CategorySeriesGeneratorImpl;
import timkodiert.budgetbook.crud.CategoryCrudServiceImpl;
import timkodiert.budgetbook.crud.CategoryGroupCrudServiceImpl;
import timkodiert.budgetbook.crud.FixedTurnoverCrudServiceImpl;
import timkodiert.budgetbook.crud.UniqueTurnoverCrudServiceImpl;
import timkodiert.budgetbook.domain.CategoryCrudService;
import timkodiert.budgetbook.domain.CategoryGroupCrudService;
import timkodiert.budgetbook.domain.FixedTurnoverCrudService;
import timkodiert.budgetbook.domain.UniqueTurnoverCrudService;
import timkodiert.budgetbook.importer.TurnoverImporter;
import timkodiert.budgetbook.importer.TurnoverImporterImpl;
import timkodiert.budgetbook.properties.PropertiesService;
import timkodiert.budgetbook.properties.PropertiesServiceImpl;
import timkodiert.budgetbook.view.BbFxmlLoader;
import timkodiert.budgetbook.view.MainView;
import timkodiert.budgetbook.view.MainViewImpl;

// @formatter:off
@Module
public interface ServiceModule {
    @Binds @Singleton MainView bindMainView(MainViewImpl impl);

    @Binds @Singleton PropertiesService bindPropertiesService(PropertiesServiceImpl impl);

    @Binds FXMLLoader bindFXMLLoader(BbFxmlLoader impl);

    // Business Logic (ggf. in eigenes Injector-Modul auslagern)
    @Binds CategorySeriesGenerator bindCategorySeriesGenerator(CategorySeriesGeneratorImpl impl);
    @Binds TurnoverImporter bindTurnoverImporter(TurnoverImporterImpl impl);

    @Binds FixedTurnoverCrudService bindFixedTurnoverCrudService(FixedTurnoverCrudServiceImpl impl);
    @Binds UniqueTurnoverCrudService bindUniqueTurnoverCrudService(UniqueTurnoverCrudServiceImpl impl);
    @Binds CategoryCrudService bindCategoryCrudService(CategoryCrudServiceImpl impl);
    @Binds CategoryGroupCrudService bindCategoryGroupCrudService(CategoryGroupCrudServiceImpl impl);
}
// @formatter:on

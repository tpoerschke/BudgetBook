package timkodiert.budgetBook.view;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface ViewComponent {
    MainView getMainView();
    AnnualOverviewView getAnnualOverviewView();
    CompactOverviewView getCompactOverviewView();
}

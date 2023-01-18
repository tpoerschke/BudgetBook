package timkodiert.budgetBook.view.widget.factory;

import dagger.assisted.AssistedFactory;
import javafx.scene.layout.Pane;
import timkodiert.budgetBook.domain.model.PaymentInformation;
import timkodiert.budgetBook.view.widget.EditPaymentInformationWidget;

@AssistedFactory
public interface EditPaymentInformationWidgetFactory {
    EditPaymentInformationWidget create(Pane parent, PaymentInformation payInfo);
}

package timkodiert.budgetBook.util;

import java.util.function.Consumer;

import javafx.scene.control.Button;
import javafx.scene.control.TreeTableCell;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ButtonTreeTableCell<S> extends TreeTableCell<S, S> {

    private String buttonText;
    private Consumer<S> onClick;

    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);

        System.out.println(item + " / "+ empty);
        setEditable(false);
        if(!empty) {             
            Button button = new Button(buttonText);   
            button.setOnAction(event -> onClick.accept(item));  
            setGraphic(button);
        } else {
            setGraphic(null);
        }
    }   
}

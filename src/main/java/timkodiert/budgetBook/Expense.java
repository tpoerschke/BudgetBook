package timkodiert.budgetBook;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Expense {
    private final SimpleStringProperty position;
    private final SimpleDoubleProperty value;
    private final SimpleStringProperty type;

    public Expense(String position, double value, String type) {
        this.position = new SimpleStringProperty(position);
        this.value = new SimpleDoubleProperty(value);
        this.type = new SimpleStringProperty(type);
    }

    public String getPosition() {
        return this.position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public double getValue() {
        return this.value.get();
    }

    public void setValue(double value) {
        this.value.set(value);
    }    

    public String getType() {
        return this.type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

}

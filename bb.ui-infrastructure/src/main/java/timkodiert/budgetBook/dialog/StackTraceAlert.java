package timkodiert.budgetBook.dialog;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class StackTraceAlert extends Alert {

    private final Exception exception;

    private StackTraceAlert(String description, Exception exception) {
        super(AlertType.ERROR, description);
        this.exception = exception;
        init();
    }

    public static StackTraceAlert of(String description, Exception exception) {
        return new StackTraceAlert(description, exception);
    }

    private void init() {
        TextArea stackTraceArea = new TextArea(getStackTrace(exception));
        stackTraceArea.setEditable(false);
        stackTraceArea.setWrapText(false);
        stackTraceArea.setMaxWidth(Double.MAX_VALUE);
        stackTraceArea.setMaxHeight(Double.MAX_VALUE);
        Label label = new Label("Stacktrace:");
        GridPane content = new GridPane();
        content.addColumn(0, label, stackTraceArea);
        GridPane.setVgrow(stackTraceArea, Priority.ALWAYS);
        GridPane.setHgrow(stackTraceArea, Priority.ALWAYS);
        getDialogPane().setExpandableContent(content);
    }

    private static String getStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}

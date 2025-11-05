package timkodiert.budgetbook.exception;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import timkodiert.budgetbook.dialog.StackTraceAlert;
import timkodiert.budgetbook.i18n.LanguageManager;

public class BbUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BbUncaughtExceptionHandler.class);

    private final LanguageManager languageManager;

    @Inject
    public BbUncaughtExceptionHandler(LanguageManager languageManager) {
        this.languageManager = languageManager;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOG.error("Unerwarteter Fehler!", e);

        if (e instanceof TechnicalException technicalException) {
            handleTechnicalException(technicalException);
            return;
        }

        StackTraceAlert.create(e.getMessage(), e).showAndWait();
    }

    private void handleTechnicalException(TechnicalException e) {
        String messageKey = getExceptionMessageKey(e);
        StackTraceAlert.create(languageManager.get(messageKey), e).showAndWait();
    }

    // TODO: Test schreiben, der prüft, dass alle Keys vorhanden sind
    private String getExceptionMessageKey(TechnicalException e) {
        return e.getClass().getSimpleName() + "." + e.getReason().name() + ".message";
    }
}

package timkodiert.budgetbook.domain.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

// TODO 01.11.23: Validierung, dass die Kombination aus receiverContains und referenceContains unique sein muss
@Getter
@Setter
@Entity
public class ImportRule extends BaseEntity {

    private boolean isActive;

    private String receiverContains;

    private String referenceContains;

    @OneToOne
    private FixedTurnover linkedFixedExpense;

    public ImportRule() {}

    public ImportRule(FixedTurnover expense) {
        this.linkedFixedExpense = expense;
    }

    public ImportRule(boolean isActive, String receiverContains, String referenceContains) {
        this.isActive = isActive;
        this.receiverContains = receiverContains;
        this.referenceContains = referenceContains;
    }

    public boolean isEmpty() {
        return receiverContains == null && referenceContains == null;
    }

    public void setReceiverContains(String receiverContains) {
        this.receiverContains = clean(receiverContains);
    }

    public void setReferenceContains(String referenceContains) {
        this.referenceContains = clean(referenceContains);
    }

    private String clean(String str) {
        return str == null || str.isBlank() ? null : str;
    }

    @Override
    public boolean contentEquals(Object other) {

        if(other instanceof ImportRule importRule) {
            return this.isActive() == importRule.isActive()
                    && Objects.equals(this.getReceiverContains(), importRule.getReceiverContains())
                    && Objects.equals(this.getReferenceContains(), importRule.getReferenceContains());
        }
        return false;
    }
}

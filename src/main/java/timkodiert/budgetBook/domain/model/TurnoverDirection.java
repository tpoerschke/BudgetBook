package timkodiert.budgetBook.domain.model;

import lombok.Getter;

public enum TurnoverDirection {

    IN(1), OUT(-1);

    @Getter
    private final int sign;

    TurnoverDirection(int sign) {
        this.sign = sign;
    }
}

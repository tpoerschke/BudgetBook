package timkodiert.budgetBook.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TurnoverDirection {

    IN(1), OUT(-1);

    private final int sign;
}

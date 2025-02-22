package timkodiert.budgetbook.domain.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Getter
public abstract class BaseEntity implements ContentEquals {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    protected int id;

    public boolean isNew() {
        return id <= 0;
    }
}

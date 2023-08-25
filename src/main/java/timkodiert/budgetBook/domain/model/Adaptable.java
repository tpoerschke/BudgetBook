package timkodiert.budgetBook.domain.model;

public interface Adaptable<A> {

    public abstract void initAdapter();

    public abstract A getAdapter();
}

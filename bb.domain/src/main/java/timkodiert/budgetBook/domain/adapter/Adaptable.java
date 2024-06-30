package timkodiert.budgetBook.domain.adapter;

public interface Adaptable<A> {

    public abstract void initAdapter();

    public abstract A getAdapter();
}

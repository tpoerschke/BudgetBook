package timkodiert.budgetbook.view.mdv_base;

public interface DtoAdapterBase<D> {

    boolean isDirty();
    D getDto();
}

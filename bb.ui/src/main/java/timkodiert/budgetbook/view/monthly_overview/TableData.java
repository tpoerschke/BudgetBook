package timkodiert.budgetbook.view.monthly_overview;

import java.time.LocalDate;
import java.util.Objects;

import timkodiert.budgetbook.table.BaseTableData;
import timkodiert.budgetbook.table.RowType;

public final class TableData extends BaseTableData {

    private final String position;
    private final double value;
    private final LocalDate date;
    private final String categories;
    private final boolean hasImport;

    public TableData(Integer id, String position, double value, LocalDate date, String categories, boolean hasImport, RowType type) {
        super(id, type);
        this.position = position;
        this.value = value;
        this.date = date;
        this.categories = categories;
        this.hasImport = hasImport;
    }

    public String position() {
        return position;
    }

    public double value() {
        return value;
    }

    public LocalDate date() {
        return date;
    }

    public String categories() {
        return categories;
    }

    public boolean hasImport() {
        return hasImport;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (TableData) obj;
        return Objects.equals(this.getId(), that.getId()) &&
                Objects.equals(this.position, that.position) &&
                Double.doubleToLongBits(this.value) == Double.doubleToLongBits(that.value) &&
                Objects.equals(this.date, that.date) &&
                Objects.equals(this.categories, that.categories) &&
                this.hasImport == that.hasImport &&
                Objects.equals(this.getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), position, value, date, categories, hasImport, getType());
    }

    @Override
    public String toString() {
        return "TableData[" +
                "id=" + getId() + ", " +
                "position=" + position + ", " +
                "value=" + value + ", " +
                "date=" + date + ", " +
                "categories=" + categories + ", " +
                "hasImport=" + hasImport + ", " +
                "type=" + getType() + ']';
    }

}

import javax.swing.table.DefaultTableModel;

public class MyTable extends DefaultTableModel implements TableInterface {

    private String[] columnNames;

    private Object[] defaultDataRow;

    public MyTable(String[] columnNames, Object[] defaultDataRow) {
        super();
        this.columnNames = columnNames;
        this.defaultDataRow = defaultDataRow;

        Object[][] df2D = {defaultDataRow};
        setDataVector(df2D, columnNames);
        removeRow(0);
    }

    @Override
    public void updateDefaultDateRow() {
        try {
            throw new Exception("Used updateDefaultDateRow from MyTable");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setZeroDataVector() {
        updateDefaultDateRow();
        Object[][] df2D = {defaultDataRow};
        setDataVector(df2D, columnNames);
    }

    public Object[] getDefaultData() {
        updateDefaultDateRow();
        return defaultDataRow;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }


}
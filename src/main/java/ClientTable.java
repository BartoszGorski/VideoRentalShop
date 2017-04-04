import java.util.Date;

public class ClientTable extends MyTable implements TableInterface {

    private static final String[] columnNames = {
            "client ID",
            "First Name",
            "Last Name",
            "Rented move",
            "How many rented",
            "Registration date"};

    private static final Object[] defaultDataRow = {new Integer(0), "", "", new Boolean(false), new Integer(0), new Date()};

    ClientTable() {
        super(columnNames, defaultDataRow);
    }

    @Override
    public void updateDefaultDateRow() {
        int IDnumber = 0;
        if (getRowCount() > 0) {
            IDnumber = (Integer) getValueAt(getRowCount() - 1, 0);
        } else {
            IDnumber = 0;
        }
        IDnumber++;
        defaultDataRow[0] = IDnumber;
        defaultDataRow[5] = new Date();
    }
}

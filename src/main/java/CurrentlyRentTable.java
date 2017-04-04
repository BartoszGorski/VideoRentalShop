import java.util.Date;

public class CurrentlyRentTable extends MyTable implements TableInterface {

    private static final String[] columnNames = {
            "client ID",
            "First Name",
            "Last Name",
            "Movie name",
            "Rent Date"};

    private static final Object[] defaultDataRow = {new Integer(0), "", "", "", new Date()};

    CurrentlyRentTable() {
        super(columnNames, defaultDataRow);
    }

    @Override
    public void updateDefaultDateRow() {
        defaultDataRow[4] = new Date();
    }
}

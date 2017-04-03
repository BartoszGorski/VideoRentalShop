import javax.swing.*;


public class AvailableList extends DefaultListModel {

    String[] strings = new String[10000];

    public AvailableList() {
        for (int i = 0; i < 10000; i++) {
            strings[i] = "bob" + i;
        }
    }

    public int getSize() {
        return strings.length;
    }

    public Object getElementAt(int index) {
        return strings[index];
    }

}

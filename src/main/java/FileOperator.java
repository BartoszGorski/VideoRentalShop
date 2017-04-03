import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.Iterator;
import java.util.Vector;

public class FileOperator {

    private JFileChooser myJFileChooser;
    private File saveFile;
    private File configFile;
    private File saveFolder;
    private File configFolder;

    public FileOperator(String configPath, String defaultFilePath) {
        configFile = new File(configPath);
        saveFile = new File(defaultFilePath);
        saveFolder = new File("./src/saves");
        configFolder = new File("./src/config");
        loadConfigFilePath();
        myJFileChooser = new JFileChooser(saveFile);
    }

    public void changeDefaultFilePath(String whatSave) {
        if (myJFileChooser.showDialog(null, "Point new default save path " + whatSave) == JFileChooser.APPROVE_OPTION) {
            saveFile = myJFileChooser.getSelectedFile();
        }
    }

    private void saveConfigFilePath() {
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(configFile.getPath())))) {
            out.write(saveFile.getPath());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }

    private void loadConfigFilePath() {

        if (!configFolder.exists()) {
            configFolder.mkdir();
        }

        if (configFile.exists()) {
            try (FileReader fr = new FileReader(configFile.getPath())) {
                BufferedReader in = new BufferedReader(fr);

                String line;
                while ((line = in.readLine()) != null) {
                    saveFile = new File(line);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public void saveTable(DefaultTableModel tableModel) {
        saveConfigFilePath();

        if (!saveFolder.exists()) {
            saveFolder.mkdir();
        }

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            out.writeObject(tableModel.getDataVector());
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveList(DefaultListModel<String> ListModel) {
        saveConfigFilePath();

        if (!saveFolder.exists()) {
            saveFolder.mkdir();
        }

        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(saveFile))) {
            for (int i = 0; i < ListModel.getSize(); i++) {
                out.write(ListModel.getElementAt(i) + "\n");
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadTable(JFrame frame, DefaultTableModel tableModel) {
        if (myJFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            loadTable(myJFileChooser.getSelectedFile(), tableModel);
        }
    }

    private void loadTable(File f, DefaultTableModel tableModel) {
        try {
            for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                tableModel.removeRow(i);
            }

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            Vector rowData = (Vector) in.readObject();
            Iterator itr = rowData.iterator();
            while (itr.hasNext()) {
                tableModel.addRow((Vector) itr.next());
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadList(JFrame farame, DefaultListModel<String> listModel) {
        if (myJFileChooser.showOpenDialog(farame) == JFileChooser.APPROVE_OPTION) {
            loadList(myJFileChooser.getSelectedFile(), listModel);
        }
    }

    private void loadList(File f, DefaultListModel<String> listModel) {
        try {
            listModel.removeAllElements();
            BufferedReader in = new BufferedReader(new FileReader(f.getPath()));
            String lineData = null;
            while ((lineData = in.readLine()) != null) {
                listModel.addElement(lineData);
            }
            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadDefaultTable(DefaultTableModel tableModel) {
        if (this.saveFile.exists())
            loadTable(this.saveFile, tableModel);
    }

    public void loadDefaultList(DefaultListModel<String> listModel) {
        if (this.saveFile.exists())
            loadList(this.saveFile, listModel);
    }

}

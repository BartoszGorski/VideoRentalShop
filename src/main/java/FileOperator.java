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
    private String fileKeyWord;

    public FileOperator(String configPath, String defaultFilePath, String fileKeyWord) {
        this.fileKeyWord = fileKeyWord;
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

        Vector myVec = new Vector();
        myVec.addElement(fileKeyWord);

        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(saveFile)))) {

            out.writeObject(myVec);
            out.writeObject(tableModel.getDataVector());

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void saveList(DefaultListModel<String> listModel) {
        saveConfigFilePath();

        if (!saveFolder.exists()) {
            saveFolder.mkdir();
        }

        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(saveFile))) {
            out.write(fileKeyWord + "\n");
            for (int i = 0; i < listModel.getSize(); i++) {
                out.write(listModel.getElementAt(i) + "\n");
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean loadTable(JFrame frame, DefaultTableModel tableModel) {
        if (myJFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            return loadTable(myJFileChooser.getSelectedFile(), tableModel);
        }
        return false;
    }

    private boolean loadTable(File f, DefaultTableModel tableModel) {


        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)))) {


            Vector rowData = (Vector) in.readObject();
            Iterator itr = rowData.iterator();

            if (itr.hasNext()) {
                if (itr.next().equals(fileKeyWord)) {

                    for (int i = tableModel.getRowCount() - 1; i > 0; i--) {
                        tableModel.removeRow(i);
                    }

                    Vector rowData1 = (Vector) in.readObject();
                    Iterator itr1 = rowData1.iterator();

                    while (itr1.hasNext()) {
                        tableModel.addRow((Vector) itr1.next());
                    }
                } else {
                    in.close();
                    return false;
                }
            } else {
                in.close();
                return false;
            }

            in.close();


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
    }

    public boolean loadList(JFrame farame, DefaultListModel<String> listModel) {
        if (myJFileChooser.showOpenDialog(farame) == JFileChooser.APPROVE_OPTION) {
            return loadList(myJFileChooser.getSelectedFile(), listModel);
        }
        return false;
    }

    private boolean loadList(File f, DefaultListModel<String> listModel) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(f.getPath()));
            String lineData = null;

            if ((lineData = in.readLine()) != null) {
                if (lineData.equals(fileKeyWord)) {
                    listModel.removeAllElements();

                    while ((lineData = in.readLine()) != null) {
                        listModel.addElement(lineData);
                    }
                } else {
                    in.close();
                    return false;
                }
            } else {
                in.close();
                return false;
            }
            in.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return true;
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

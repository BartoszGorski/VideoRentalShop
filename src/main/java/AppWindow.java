import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;


public class AppWindow {

    private final int ID = 0;
    private final int FIRSTNAME = 1;
    private final int LASTNAME = 2;
    private final int RENTEDMOVIE = 3;
    private final int HOWMANYMOVIES = 4;

    //region Window
    private JPanel AppPanel;
    private JTabbedPane tabbedPane;

    private JButton clientAddButton;
    private JButton clientRemoveButton;
    private JButton clientLoadButton;
    private JTable clientTable;
    private JPanel clientTab;

    private JList<String> availableList;
    private JList<String> takeUpList;
    private JPanel availableTab;
    private JButton availableAddButton;
    private JButton availableRentButton;
    private JButton availableRemoveButton;
    private JButton takeUpAddButton;
    private JButton takeUpRemoveButton;
    private JButton takeUpLoadButton;

    private JTabbedPane rentedPane;
    private JPanel rentedTab;
    private JPanel currentlyRentTab;
    private JPanel historyTab;
    private JTable currentlyRentTable;
    private JButton currentlyRentLoadButton;
    private JButton currentlyRentClearButton;
    private JButton currentlyRentReturnMovieButton;
    private JButton saveAllButton;
    private JButton infoButton;
    private JButton changeSavePathButton;
    private JList<String> historyList;
    private JButton historyClearButton;
    private JButton availableLoadButton;
    private JButton historyLoadButton;
    //endregion

    private JFrame app;

    private ClientTable clientTableModel;
    private CurrentlyRentTable currentlyRentTableModel;

    private DefaultListModel<String> availableListModel;
    private DefaultListModel<String> takeUpListModel;
    private DefaultListModel<String> historyListModel;

    //region File Operators
    private FileOperator clientFileOperator;
    private FileOperator availableFileOperator;
    private FileOperator takeUpFileOperator;
    private FileOperator currentlyRentFileOperator;
    private FileOperator historyFileOperator;
    //endregion

    public AppWindow() {


        //* Loads data from default configured file path*/
        $$$setupUI$$$();

        loadData();

        //region Available and TakeUp Buttons

        availableAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Give movie name:");
                if (!name.equals("")) {
                    availableListModel.addElement(name);
                    availableList.setModel(availableListModel);
                }
            }
        });
        availableRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove row", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (availableListModel.getSize() > 0 && !availableList.isSelectionEmpty()) {
                        availableListModel.removeElementAt(availableList.getSelectedIndex());
                        availableList.setModel(availableListModel);
                    }
                }
            }
        });
        availableLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                availableFileOperator.loadList(app, availableListModel);
                availableList.setModel(availableListModel);
            }
        });
        availableRentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (availableListModel.getSize() > 0 && !availableList.isSelectionEmpty()) {

                    JComboBox jcb = new JComboBox(clientTableModel.getDataVector());
                    if (JOptionPane.showConfirmDialog(null, jcb, "Select client", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

                        if (currentlyRentTable.getRowCount() > 0) {
                            currentlyRentTableModel.addRow(currentlyRentTableModel.getDefaultData());
                        } else {
                            currentlyRentTableModel.setZeroDataVector();
                        }

                        currentlyRentTable.setValueAt(((Vector) jcb.getSelectedItem()).get(ID), currentlyRentTable.getRowCount() - 1, ID);
                        currentlyRentTable.setValueAt(((Vector) jcb.getSelectedItem()).get(FIRSTNAME), currentlyRentTable.getRowCount() - 1, FIRSTNAME);
                        currentlyRentTable.setValueAt(((Vector) jcb.getSelectedItem()).get(LASTNAME), currentlyRentTable.getRowCount() - 1, LASTNAME);
                        currentlyRentTable.setValueAt(availableList.getSelectedValue(), currentlyRentTable.getRowCount() - 1, RENTEDMOVIE);

                        if (!((Boolean) clientTable.getValueAt(jcb.getSelectedIndex(), RENTEDMOVIE))) {
                            clientTable.setValueAt(true, jcb.getSelectedIndex(), RENTEDMOVIE);
                        }

                        clientTable.setValueAt(((Integer) clientTable.getValueAt(jcb.getSelectedIndex(), HOWMANYMOVIES)) + 1, jcb.getSelectedIndex(), HOWMANYMOVIES);

                        historyListModel.addElement(((Vector) jcb.getSelectedItem()).get(FIRSTNAME) + " " + ((Vector) jcb.getSelectedItem()).get(LASTNAME) + " (ID: " + ((Vector) jcb.getSelectedItem()).get(ID)
                                + ") rent \"" + availableList.getSelectedValue() + "\" - Data " + (new Date()));
                        historyList.setModel(historyListModel);

                        availableListModel.removeElementAt(availableList.getSelectedIndex());
                        availableList.setModel(availableListModel);

                        currentlyRentTableModel.fireTableDataChanged();
                        clientTableModel.fireTableDataChanged();
                    }
                }
            }
        });
        takeUpAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog("Give movie name:");
                if (!name.equals("")) {
                    takeUpListModel.addElement(name);
                    takeUpList.setModel(takeUpListModel);
                }
            }
        });
        takeUpRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove row", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (takeUpListModel.getSize() > 0 && !takeUpList.isSelectionEmpty()) {
                        takeUpListModel.removeElementAt(takeUpList.getSelectedIndex());
                        takeUpList.setModel(takeUpListModel);
                    }
                }
            }
        });
        takeUpLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                takeUpFileOperator.loadList(app, takeUpListModel);
                takeUpList.setModel(takeUpListModel);
            }
        });
        //endregion

        //region Client Buttons
        clientAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addClient();
            }
        });
        clientRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeClient();
            }
        });
        clientLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFileOperator.loadTable(app, clientTableModel);
            }
        });
        //endregion

        //region Currently rent Buttons
        currentlyRentLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentlyRentFileOperator.loadTable(app, currentlyRentTableModel);
            }
        });
        currentlyRentClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Clear all", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    for (int i = currentlyRentTable.getRowCount() - 1; i >= 0; i--) {
                        currentlyRentTableModel.removeRow(i);
                    }
                }
            }
        });
        currentlyRentReturnMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnRented();
            }
        });
        //endregion

        //region History buttons
        historyClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyListModel.removeAllElements();
            }
        });
        historyLoadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historyFileOperator.loadList(app, historyListModel);
                historyList.setModel(historyListModel);
            }
        });
        //endregion

        //region Tool Bar
        saveAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFileOperator.saveTable(clientTableModel);
                availableFileOperator.saveList(availableListModel);
                takeUpFileOperator.saveList(takeUpListModel);
                currentlyRentFileOperator.saveTable(currentlyRentTableModel);
                historyFileOperator.saveList(historyListModel);
            }
        });
        changeSavePathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFileOperator.changeDefaultFilePath("Client Table");
                availableFileOperator.changeDefaultFilePath("Available List");
                takeUpFileOperator.changeDefaultFilePath("Take-up List");
                currentlyRentFileOperator.changeDefaultFilePath("Rented Table");
                historyFileOperator.changeDefaultFilePath("History");
                JOptionPane.showMessageDialog(app, "Don't forget to \"Save All\" for change settings.");
            }
        });
        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String message = "Application made by Bartosz Górski.\n\n" +
                        "Data files are saving to default folder (./src/saves/) if you don't set other path.\n" +
                        "When you save, and close app. Next time you run it all should load automaticly.\n" +
                        "Tables can be load apart.\n" +
                        "You can edit FirstName and LastName.\n" +
                        "If you want to remove/rent/return first select the element, then click the button.\n" +
                        "Don't forget about saving before close application!";
                JOptionPane.showMessageDialog(app, message);
            }
        });
        //endregion


    }

    protected void createAndShowGUI() {
        app = new JFrame();
        app.setSize(700, 600);
        app.setLocationRelativeTo(null);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setContentPane(new AppWindow().AppPanel);
        AppPanel.setOpaque(true);

        app.setVisible(true);
    }

    private void createUIComponents() {
        clientTableModel = new ClientTable();
        clientTable = new JTable(clientTableModel);

        currentlyRentTableModel = new CurrentlyRentTable();
        currentlyRentTable = new JTable(currentlyRentTableModel);

        availableListModel = new DefaultListModel<String>();
        availableList = new JList<>(availableListModel);

        takeUpListModel = new DefaultListModel<String>();
        takeUpList = new JList<>(takeUpListModel);

        historyListModel = new DefaultListModel<String>();
        historyList = new JList<>(historyListModel);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        clientTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        clientTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        currentlyRentTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

    }

    //* Loads data from default configured file path*/
    private void loadData() {

        //region File Operators
        clientFileOperator = new FileOperator("./src/config/clientPathConfig", "./src/saves/clientTable");
        availableFileOperator = new FileOperator("./src/config/availablePathConfig", "./src/saves/availableList");
        takeUpFileOperator = new FileOperator("./src/config/takeUpPathConfig", "./src/saves/takeUpList");
        currentlyRentFileOperator = new FileOperator("./src/config/currentlyRentPathConfig", "./src/saves/rentedTable");
        historyFileOperator = new FileOperator("./src/config/historyPathConfig", "./src/saves/historyList");
        //endregion

        //region Load From default path
        currentlyRentFileOperator.loadDefaultTable(currentlyRentTableModel);
        clientFileOperator.loadDefaultTable(clientTableModel);
        availableFileOperator.loadDefaultList(availableListModel);
        takeUpFileOperator.loadDefaultList(takeUpListModel);
        historyFileOperator.loadDefaultList(historyListModel);
        availableList.setModel(availableListModel);
        takeUpList.setModel(takeUpListModel);
        historyList.setModel(historyListModel);
        //endregion
    }

    private void addClient() {
        String firstname = JOptionPane.showInputDialog(app, "First name:");
        String lastname = JOptionPane.showInputDialog(app, "Last name:");

        if (clientTable.getRowCount() > 0) {
            if (clientTable.getSelectedRow() >= 0) {
                clientTableModel.insertRow(clientTable.getSelectedRow() + 1, clientTableModel.getDefaultData());
            } else {
                clientTableModel.addRow(clientTableModel.getDefaultData());
            }
        } else {
            clientTableModel.setZeroDataVector();
        }

        if (clientTable.getRowCount() < 0) {
            clientTableModel.setValueAt(firstname, clientTable.getRowCount() - 1, FIRSTNAME);
            clientTableModel.setValueAt(lastname, clientTable.getRowCount() - 1, LASTNAME);
        } else {
            clientTableModel.setValueAt(firstname, clientTable.getSelectedRow() + 1, FIRSTNAME);
            clientTableModel.setValueAt(lastname, clientTable.getSelectedRow() + 1, LASTNAME);
        }

        clientTableModel.fireTableDataChanged();
    }

    private void removeClient() {
        if (JOptionPane.showConfirmDialog(null, "Are you sure?", "Remove row", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if (clientTable.getSelectedRows().length > 0) {
                {
                    for (int i :
                            clientTable.getSelectedRows()) {
                        if (i >= 0) {
                            clientTableModel.removeRow(clientTable.getSelectedRow());
                        }
                    }
                }
            }
            clientTableModel.fireTableDataChanged();
        }
    }

    private void returnRented() {
        if (currentlyRentTable.getSelectedRow() >= 0) {
            if (JOptionPane.showConfirmDialog(null, "Do you want to return film?", "Return film?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                int selectedRow = currentlyRentTable.getSelectedRow();
                int id = (Integer) currentlyRentTable.getValueAt(selectedRow, ID);
                int row = clientTable.getRowCount() - 1;

                for (; row >= 0; row--) {
                    if (id == (Integer) clientTable.getValueAt(row, ID)) {

                        int moviesCount = (Integer) clientTable.getValueAt(row, HOWMANYMOVIES);
                        clientTable.setValueAt(moviesCount - 1, row, HOWMANYMOVIES);

                        if (moviesCount - 1 == 0) {
                            clientTable.setValueAt(false, row, RENTEDMOVIE);
                        }
                        break;
                    }
                }

                historyListModel.addElement(clientTable.getValueAt(row, FIRSTNAME) + " " + clientTableModel.getValueAt(row, LASTNAME) + " (ID: " + id + ") return \""
                        + currentlyRentTable.getValueAt(selectedRow, RENTEDMOVIE) + "\" - Data " + (new Date()));
                historyList.setModel(historyListModel);

                availableListModel.addElement(currentlyRentTable.getValueAt(selectedRow, RENTEDMOVIE).toString());
                availableList.setModel(availableListModel);
                currentlyRentTableModel.removeRow(selectedRow);

                clientTableModel.fireTableDataChanged();
                currentlyRentTableModel.fireTableDataChanged();
            }
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        AppPanel = new JPanel();
        AppPanel.setLayout(new BorderLayout(0, 0));
        AppPanel.setEnabled(true);
        tabbedPane = new JTabbedPane();
        AppPanel.add(tabbedPane, BorderLayout.CENTER);
        availableTab = new JPanel();
        availableTab.setLayout(new BorderLayout(0, 0));
        tabbedPane.addTab("Available", availableTab);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        availableTab.add(panel1, BorderLayout.NORTH);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        availableAddButton = new JButton();
        availableAddButton.setText(" Add ");
        panel2.add(availableAddButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        availableRentButton = new JButton();
        availableRentButton.setText("   Rent   ");
        panel2.add(availableRentButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        availableRemoveButton = new JButton();
        availableRemoveButton.setText("Remove");
        panel2.add(availableRemoveButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        availableLoadButton = new JButton();
        availableLoadButton.setText("Load");
        panel2.add(availableLoadButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        takeUpAddButton = new JButton();
        takeUpAddButton.setText("Add");
        panel3.add(takeUpAddButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        takeUpRemoveButton = new JButton();
        takeUpRemoveButton.setText("Remove");
        panel3.add(takeUpRemoveButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        takeUpLoadButton = new JButton();
        takeUpLoadButton.setText("Load");
        panel3.add(takeUpLoadButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        availableTab.add(panel4, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Available");
        panel4.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Take-up");
        panel4.add(label2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        availableList.setModel(defaultListModel1);
        scrollPane1.setViewportView(availableList);
        final JScrollPane scrollPane2 = new JScrollPane();
        panel4.add(scrollPane2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final DefaultListModel defaultListModel2 = new DefaultListModel();
        takeUpList.setModel(defaultListModel2);
        scrollPane2.setViewportView(takeUpList);
        clientTab = new JPanel();
        clientTab.setLayout(new BorderLayout(0, 0));
        tabbedPane.addTab("Clients", clientTab);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        clientTab.add(panel5, BorderLayout.NORTH);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        clientLoadButton = new JButton();
        clientLoadButton.setText("Load");
        panel6.add(clientLoadButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel5.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        clientRemoveButton = new JButton();
        clientRemoveButton.setText("Remove");
        panel7.add(clientRemoveButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clientAddButton = new JButton();
        clientAddButton.setText("   Add   ");
        panel7.add(clientAddButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane3 = new JScrollPane();
        clientTab.add(scrollPane3, BorderLayout.CENTER);
        clientTable.setAutoResizeMode(2);
        clientTable.setEnabled(true);
        clientTable.putClientProperty("JTable.autoStartsEdit", Boolean.FALSE);
        scrollPane3.setViewportView(clientTable);
        rentedTab = new JPanel();
        rentedTab.setLayout(new BorderLayout(0, 0));
        tabbedPane.addTab("Rented", rentedTab);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new BorderLayout(0, 0));
        panel8.setEnabled(true);
        rentedTab.add(panel8, BorderLayout.CENTER);
        rentedPane = new JTabbedPane();
        rentedPane.setTabLayoutPolicy(0);
        panel8.add(rentedPane, BorderLayout.CENTER);
        currentlyRentTab = new JPanel();
        currentlyRentTab.setLayout(new BorderLayout(0, 0));
        rentedPane.addTab("Currently Rent", currentlyRentTab);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        currentlyRentTab.add(panel9, BorderLayout.NORTH);
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        currentlyRentReturnMovieButton = new JButton();
        currentlyRentReturnMovieButton.setText("Return Movie");
        panel10.add(currentlyRentReturnMovieButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel11 = new JPanel();
        panel11.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel9.add(panel11, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        currentlyRentClearButton = new JButton();
        currentlyRentClearButton.setText("Clear");
        panel11.add(currentlyRentClearButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentlyRentLoadButton = new JButton();
        currentlyRentLoadButton.setText("Load");
        panel11.add(currentlyRentLoadButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane4 = new JScrollPane();
        currentlyRentTab.add(scrollPane4, BorderLayout.CENTER);
        scrollPane4.setViewportView(currentlyRentTable);
        historyTab = new JPanel();
        historyTab.setLayout(new BorderLayout(0, 0));
        rentedPane.addTab("History", historyTab);
        final JPanel panel12 = new JPanel();
        panel12.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        historyTab.add(panel12, BorderLayout.CENTER);
        final JScrollPane scrollPane5 = new JScrollPane();
        panel12.add(scrollPane5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane5.setViewportView(historyList);
        final JPanel panel13 = new JPanel();
        panel13.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel12.add(panel13, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        historyClearButton = new JButton();
        historyClearButton.setHorizontalTextPosition(11);
        historyClearButton.setText("Clear History");
        panel13.add(historyClearButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        historyLoadButton = new JButton();
        historyLoadButton.setText("Load");
        panel13.add(historyLoadButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        AppPanel.add(toolBar1, BorderLayout.NORTH);
        saveAllButton = new JButton();
        saveAllButton.setText("Save All");
        toolBar1.add(saveAllButton);
        changeSavePathButton = new JButton();
        changeSavePathButton.setText("Change Save Path");
        toolBar1.add(changeSavePathButton);
        infoButton = new JButton();
        infoButton.setText("Info");
        toolBar1.add(infoButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return AppPanel;
    }
}

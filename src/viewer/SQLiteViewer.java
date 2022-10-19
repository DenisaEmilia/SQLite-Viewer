package viewer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SQLiteViewer extends JFrame implements ActionListener {
    JTextField fileNameTextField = new JTextField();
    JButton openFileButton = new JButton("Open");
    JComboBox<String> tablesComboBox = new JComboBox<>();

    JTextArea queryTextArea = new JTextArea();
    JButton executeButton = new JButton("Execute");

    List<String> tables;

    DefaultTableModel model = new DefaultTableModel();
    JTable table = new JTable(model);
    Database db;



    public SQLiteViewer() {
        try {
            Database db = new Database("");
            db.initForTesting();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
       setTitle("SQLite Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setResizable(false);
        setLocationRelativeTo(null);



        fileNameTextField.setBounds(20,20, 520,30);
        fileNameTextField.setName("FileNameTextField");
        fileNameTextField.setVisible(true);
        add(fileNameTextField);

        openFileButton.setBounds(560, 20, 100, 30);
        openFileButton.setName("OpenFileButton");
        openFileButton.setVisible(true);
        openFileButton.addActionListener(this::actionPerformed);
        add(openFileButton);

        tablesComboBox.setBounds(20, 70, 640, 30);
        tablesComboBox.setName("TablesComboBox");
        tablesComboBox.setVisible(true);
        tablesComboBox.addActionListener(this :: actionPerformed);
        add(tablesComboBox);

        queryTextArea.setBounds(20, 120, 520, 100);
        queryTextArea.setName("QueryTextArea");
        queryTextArea.setVisible(true);
        queryTextArea.setEnabled(false);
        add(queryTextArea);

        executeButton.setBounds(560, 120, 100, 30);
        executeButton.setName("ExecuteQueryButton");
        executeButton.setEnabled(false);
        executeButton.setVisible(true);
        executeButton.addActionListener(this :: actionPerformed);

        add(executeButton);



        table.setName("Table");
        JScrollPane contentTable = new JScrollPane(table);
        contentTable.setBounds(20, 250, 600, 150);
        add(contentTable);


        setLayout(null);
        setVisible(true);

        repaint();


    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == openFileButton) {
            String dbName = fileNameTextField.getText();
             db = new Database(dbName);
            tables = db.getTables();

            tablesComboBox.removeAllItems();
            for (String table : tables) {
                tablesComboBox.addItem(table);

            }
          try {
              tablesComboBox.setSelectedIndex(0);
              queryTextArea.setEnabled(true);
          } catch (IllegalArgumentException ex) {
              JOptionPane.showMessageDialog(new Frame(), "File doesn't exist!");
          }
        } else if (source == tablesComboBox && tablesComboBox.getItemCount() > 0) {
            String tableName = tablesComboBox.getSelectedItem().toString();
            String query = "SELECT * FROM " + tableName + ";";
            queryTextArea.setText(query);
            executeButton.setEnabled(true);

        }

        if (source == executeButton) {
            model.setRowCount(0);

            String request = queryTextArea.getText();
            Database.getResponse(request);

            Object[] columns = Database.getColumnNames().toArray();
            model.setColumnIdentifiers(columns);

            ArrayList<ArrayList<String>> data = Database.getContentTable();
            for (ArrayList<String> row : data) {
                model.addRow(row.toArray());
            }

        }


        repaint();
    }
}

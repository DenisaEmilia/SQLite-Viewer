package viewer;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static  Connection connection;
    private List<String> tables;
    private Statement statement;

    private static ArrayList<String> columnNames;
    private static ArrayList<ArrayList<String>> contentTable;

    public Database(String db) {
        if (db.isEmpty()) return;
        connection = null;
        tables = new ArrayList<>();
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + db);
            populateTables();
        } catch (ClassNotFoundException | SQLException  | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(new Frame(), "ERROR MESSAGE");
            System.exit(0);
        }
    }

    public void initForTesting() throws SQLException {
        deleteDatabaseFiles();

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + "dbOne.db");
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS contacts (\n" +
                "\tcontact_id INTEGER PRIMARY KEY,\n" +
                "\tfirst_name TEXT NOT NULL,\n" +
                "\tlast_name TEXT NOT NULL,\n" +
                "\temail TEXT NOT NULL UNIQUE,\n" +
                "\tphone TEXT NOT NULL UNIQUE\n" +
                ");");
        statement.execute("CREATE TABLE IF NOT EXISTS groups (\n" +
                "   group_id INTEGER PRIMARY KEY,\n" +
                "   name TEXT NOT NULL\n" +
                ");");
       // connection.close();

        connection = DriverManager.getConnection("jdbc:sqlite:" + "dbTwo.db");
        statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS projects (\n" +
                "\tid integer PRIMARY KEY,\n" +
                "\tname text NOT NULL,\n" +
                "\tbegin_date text,\n" +
                "\tend_date text\n" +
                ");");
        //connection.close();
    }

    private void deleteDatabaseFiles() {
        File firstFile = new File("dbOne.db");
        if (firstFile.exists()) {
            boolean ignored = firstFile.delete();
        }

        File secondFile = new File("dbTwo.db");
        if (secondFile.exists()) {
            boolean ignored = secondFile.delete();
        }
    }

    private void populateTables() {
        statement = null;
        try {
            statement = connection.createStatement();
            String sql = "SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%'";
            ResultSet result = statement.executeQuery(sql);

            while (result.next()) {
                tables.add(result.getString("name"));
            }

            result.close();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void getResponse(String request) {
        columnNames = new ArrayList<>();
        int columnNumbs;
        contentTable = new ArrayList<>();

        try (Connection conn = connection;
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(request)) {

            ResultSetMetaData columns = rs.getMetaData();
            columnNumbs = columns.getColumnCount();
            for (int i = 1; i <= columnNumbs; i++) {
                columnNames.add(columns.getColumnName(i));
            }

            while (rs.next()) {
                ArrayList<String> contentLine = new ArrayList<>();
                for (String columnName : columnNames) {
                    String content = rs.getString(columnName);
                    contentLine.add(content);
                }
                contentTable.add(contentLine);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new Frame(), "ERROR MESSAGE");
        }
    }

    public static ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public static ArrayList<ArrayList<String>> getContentTable() {
        return contentTable;
    }

    public List<String> getTables() {
        return tables;
    }




}
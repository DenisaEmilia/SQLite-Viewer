package viewer;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class Table_Model extends AbstractTableModel {

    private static List<String> columns;



    Table_Model (List<String> tables) {
         columns = tables;

         if(columns.size() == 0)
             System.out.println("Nue");
         for(int i = 0; i<columns.size();i++)
             System.out.println(columns.get(i));
    }

    @Override
    public int getRowCount() {
      return 8;
    }

    @Override
    public int getColumnCount() {
       return columns.size();

    }

    @Override
    public Object getValueAt(int i, int i1) {
        return null;
    }
}

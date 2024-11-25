package client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MessageList extends JPanel {
    private final JScrollPane component;
    private final DefaultTableModel tableModel;
    private final JTable table;

    public MessageList() {
        tableModel = new DefaultTableModel(0, 2);

        table = new JTable();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        component = new JScrollPane(table);

        add(component);
    }
}

package client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MessageListPanel extends JPanel {
    private final DefaultTableModel tableModel;
    private final JTable table;

    public MessageListPanel() {
        // Imposto la tabella dei messaggi con la colonna dei messaggi che occupa la maggior parte dello schermo
        tableModel = new DefaultTableModel(0, 2);
        table = new JTable(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        // Disabilito la selezione dei messaggi
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setCellSelectionEnabled(false);

        // Disabilito l'intestazione della tabella
        table.getTableHeader().setVisible(false);

        setLayout(new BorderLayout());
        add(new JScrollPane(table));
    }

    public void addMessage(String username, String message) {
        tableModel.addRow(new Object[]{username, message});
    }
}

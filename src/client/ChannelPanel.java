package client;

import protocol.AddChannelMessage;
import protocol.DeleteChannelMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChannelPanel extends JPanel {
    JPanel panel = new JPanel();

    public ChannelPanel() {
        setLayout(new BorderLayout());

        panel.setLayout(new GridBagLayout());
        add(panel, BorderLayout.NORTH);
    }

    public void channelAdded(AddChannelMessage msg) {
        Label label = new Label(msg.channelName, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));

        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Client.ui.chatPanel.setText("/join " + label.getText());
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        revalidate();
        repaint();
    }

    public void channelRemoved(DeleteChannelMessage msg) {
        for (Component comp : panel.getComponents()) {
            Label label = (Label) comp;
            if (label.getText().equals(msg.channelName)) {
                panel.remove(comp);
                break;
            }
        }

        revalidate();
        repaint();
    }
}

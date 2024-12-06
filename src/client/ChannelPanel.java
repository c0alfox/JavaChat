package client;

import protocol.AddChannelMessage;
import protocol.DeleteChannelMessage;

import javax.swing.*;
import java.awt.*;

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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        revalidate();
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
    }
}

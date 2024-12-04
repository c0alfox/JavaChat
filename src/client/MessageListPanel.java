package client;

import protocol.InboundMessage;

import javax.swing.*;
import java.awt.*;

public class MessageListPanel extends JPanel {
    JPanel panel;

    public MessageListPanel() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        setLayout(new BorderLayout());
        add(new JScrollPane(panel), BorderLayout.NORTH);
    }

    public void addMessage(InboundMessage imsg, Color color) {
        // TODO: Add Padding to gridbag
        // TODO: Make text stick to the left
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;

        panel.add(new JLabel(imsg.msg), gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String suffix = imsg.isPrivate ? " [whisper]" : "";

        if (color == null) {
            JLabel label = new JLabel(imsg.uname + suffix);
            label.setFont(font);
            panel.add(label, gbc);
        } else {
            OutlineLabel label = new OutlineLabel(imsg.uname + suffix);
            label.setFont(font);
            label.setForeground(color);
            label.setOutlineColor(ClientUI.borderColor(getBackground()));
            panel.add(label, gbc);
        }

        revalidate();
    }

    public void clear() {
        for (Component comp : panel.getComponents()) {
            panel.remove(comp);
        }

        revalidate();
    }
}

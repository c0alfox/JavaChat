package client;

import javax.swing.*;
import java.awt.*;

public class MessageListPanel extends JPanel {
    JPanel panel;

    public MessageListPanel() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
    }

    public void addMessage(String msg, String uname, Color color, boolean priv) {
        Font font = new Font("SansSerif", Font.BOLD, 14);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextArea jta = new JTextArea(msg);
        jta.setFont(font);
        jta.setWrapStyleWord(true);
        jta.setLineWrap(true);
        jta.setEditable(false);
        jta.setBackground(panel.getBackground());

        panel.add(jta, gbc);

        gbc.gridx = 0;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.NONE;

        String suffix = priv ? " [whisper]" : "";

        if (color == null) {
            JLabel label = new JLabel(uname + suffix);
            label.setFont(font);
            panel.add(label, gbc);
        } else {
            OutlineLabel label = new OutlineLabel(uname + suffix);
            label.setFont(font);
            label.setForeground(color);
            label.setOutlineColor(ClientUI.borderColor(getBackground()));
            panel.add(label, gbc);
        }

        revalidate();
        repaint();
    }

    public void clear() {
        for (Component comp : panel.getComponents()) {
            panel.remove(comp);
        }

        revalidate();
        repaint();
    }
}

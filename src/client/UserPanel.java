package client;

import protocol.JoinMessage;
import protocol.LeaveMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserPanel extends JPanel implements UserModelListener {
    JPanel panel = new JPanel();

    public UserPanel() {
        setLayout(new BorderLayout());

        panel.setLayout(new GridBagLayout());
        add(panel, BorderLayout.NORTH);
    }

    public void userAdded(JoinMessage j) {
        Color color = new Color(Integer.parseInt(j.color, 16));
        OutlineLabel label = new OutlineLabel(j.uname, JLabel.CENTER);
        label.setForeground(color);
        label.setOutlineColor(ClientUI.borderColor(getBackground()));
        label.setFont(new Font("SansSerif", Font.BOLD, 16));

        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Client.ui.chatPanel.setText("/whisper " + label.getText());
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        revalidate();
        repaint();
    }

    public void userRemoved(LeaveMessage l) {
        for (Component comp : panel.getComponents()) {
            OutlineLabel label = (OutlineLabel) comp;
            if (label.getText().equals(l.uname)) {
                panel.remove(comp);
                break;
            }
        }

        revalidate();
        repaint();
    }

    @Override
    public void channelLeft() {
    }
}

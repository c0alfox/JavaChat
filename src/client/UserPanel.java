package client;

import protocol.JoinMessage;
import protocol.LeaveMessage;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel implements UserModelListener {
    JPanel panel = new JPanel();

    public UserPanel() {
        panel.setLayout(new GridBagLayout());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.NORTH);
    }

    public void userAdded(JoinMessage j) {
        Color color = new Color(Integer.parseInt(j.color, 16));
        OutlineLabel label = new OutlineLabel(j.uname, JLabel.CENTER);
        label.setForeground(color);
        label.setOutlineColor(ClientUI.borderColor(getBackground()));
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);

        revalidate();
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
    }

    @Override
    public void channelLeft() {
    }
}

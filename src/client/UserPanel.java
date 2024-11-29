package client;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    JPanel panel = new JPanel();

    public UserPanel() {
        panel.setLayout(new GridBagLayout());

        setLayout(new BorderLayout());
        add(new JScrollPane(panel), BorderLayout.NORTH);
    }

    public void addUser(String uname, String ucolor) {
        Color color = new Color(Integer.parseInt(ucolor, 16));
        OutlineLabel label = new OutlineLabel(uname);
        label.setForeground(color);
        label.setOutlineColor(ClientUI.borderColor(getBackground()));
        label.setFont(new Font("SansSerif", Font.PLAIN, 16));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);
    }

    public void removeUser(String uname) {
        for (Component comp : panel.getComponents()) {
            OutlineLabel label = (OutlineLabel) comp;
            if (label.getText().equals(uname)) {
                panel.remove(comp);
                break;
            }
        }
    }
}

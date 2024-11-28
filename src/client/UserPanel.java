package client;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JScrollPane {
    JPanel panel = new JPanel();

    public UserPanel() {
        panel.setLayout(new GridBagLayout());
        setViewportView(panel);
    }

    public void addUser(String uname, Color ucolor) {
        OutlineLabel label = new OutlineLabel(uname);
        label.setForeground(ucolor);
        label.setOutlineColor(ClientUI.invertColor(getBackground()));
        label.setFont(new Font("SansSerif", Font.PLAIN, 21));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(label, gbc);
    }
}

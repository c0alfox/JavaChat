package client;

import javax.swing.*;
import java.awt.*;

public class ChannelPanel extends JPanel {
    JPanel panel = new JPanel();

    public ChannelPanel() {
        setLayout(new BorderLayout());

        panel.setLayout(new GridBagLayout());
        add(panel, BorderLayout.NORTH);
    }


}

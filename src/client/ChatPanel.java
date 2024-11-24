package client;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    public ChatPanel() {
        setLayout(new BorderLayout());
        add(new TextboxPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

}

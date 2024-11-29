package client;

import protocol.InboundMessage;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    private final TextboxPanel tbp;
    private final MessageListPanel mlp;

    public ChatPanel() {
        setLayout(new BorderLayout());

        tbp = new TextboxPanel(this);
        mlp = new MessageListPanel();

        add(tbp, BorderLayout.SOUTH);
        add(mlp);

        setVisible(true);
    }

    public synchronized void onMessage(InboundMessage imsg) {
        mlp.addMessage(imsg);
    }
}

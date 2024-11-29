package client;

import protocol.MsgMessage;

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

    public void onMessage(String username, String message) {
        Client.net.send(new MsgMessage(username, message).toString());
        mlp.addMessage(username, message);
    }
}

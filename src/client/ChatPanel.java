package client;

import protocol.InboundMessage;
import protocol.JoinMessage;
import protocol.LeaveMessage;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel implements UserModelListener {
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

    public synchronized void onMessage(InboundMessage imsg, Color color) {
        mlp.addMessage(imsg, color);
    }

    @Override
    public void userAdded(JoinMessage j) {
    }

    @Override
    public void userRemoved(LeaveMessage l) {
    }

    @Override
    public void channelLeft() {
        mlp.clear();
    }
}

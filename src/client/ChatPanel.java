package client;

import protocol.InboundMessage;
import protocol.JoinMessage;
import protocol.LeaveMessage;
import protocol.PrivateMessage;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel implements UserModelListener {
    private static final String defaultChannelTitle = " - Nessun Canale - ";
    private final TextboxPanel tbp;
    private final MessageListPanel mlp;
    private final JLabel channel;
    private final JScrollBar vbar;

    public ChatPanel() {
        setLayout(new BorderLayout());

        tbp = new TextboxPanel(this);
        mlp = new MessageListPanel();
        channel = new JLabel(defaultChannelTitle);
        channel.setFont(new Font("SansSerif", Font.BOLD, 20));

        add(channel, BorderLayout.NORTH);
        add(tbp, BorderLayout.SOUTH);

        JScrollPane scroll = new JScrollPane(mlp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        vbar = scroll.getVerticalScrollBar();
        add(scroll);

        setVisible(true);
    }

    public synchronized void onMessage(InboundMessage msg, Color color) {
        mlp.addMessage(msg.msg, msg.uname, color, false);
        vbar.setValue(vbar.getMaximum());
    }

    public synchronized void onMessage(PrivateMessage msg) {
        mlp.addMessage(msg.msg, msg.uname, new Color(Integer.parseInt(msg.color, 16)), true);
        vbar.setValue(vbar.getMaximum());
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
        channel.setText(defaultChannelTitle);

        Client.ui.sideScrollPanel.setViewportView(Client.ui.channelPanel);
        Client.ui.sidePanelLabel.setText(ClientUI.channelSidePanelLabel);
        Client.ui.sidePanel.revalidate();
    }

    public void channelJoined(String name) {
        channel.setText("#" + name);
        Client.ui.sideScrollPanel.setViewportView(Client.ui.userPanel);
        Client.ui.sidePanelLabel.setText(ClientUI.userSidePanelLabel);
        Client.ui.sidePanel.revalidate();
    }
}

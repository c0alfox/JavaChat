package client;

import protocol.*;

import java.io.IOException;

public class Client {
    static ConnectionManager net;
    static String uname;
    static ClientUI ui;

    public static void main(String[] args) {
        ui = new ClientUI();
    }

    public static void connect(String target) throws IOException {
        net = new ConnectionManager(target);
        net.on(InboundMessage.class, msg -> ui.chatPanel.onMessage(msg.getUname(), msg.getMsg(), msg.getPrivate()));
        net.on(JoinMessage.class, msg -> ui.userPanel.addUser(msg.getUname(), msg.getColor()));
        net.on(LeaveMessage.class, msg -> ui.userPanel.removeUser(msg.getUname()));
        net.on(UpdateMessage.class, msg -> {
            ui.userPanel.removeUser(msg.getUname());
            ui.userPanel.addUser(msg.getUname(), msg.getColor());
        });

        net.start();
    }
}

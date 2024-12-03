package client;

import protocol.*;

import javax.swing.*;
import java.io.IOException;

public class Client {
    static ConnectionManager net;
    static String uname;
    static String ucolor;
    static ClientUI ui;

    public static void main(String[] args) {
        ui = new ClientUI();
    }

    public static void connect(String target) throws IOException {
        net = new ConnectionManager(target);
        net.on(InboundMessage.class, msg -> ui.chatPanel.onMessage(msg))
                .on(JoinMessage.class, msg -> ui.userPanel.addUser(msg.uname, msg.color))
                .on(LeaveMessage.class, msg -> ui.userPanel.removeUser(msg.uname))
                .on(ResponseMessage.class, msg -> net.pollCallback().accept(msg.toOptional()))
                .addDisposeRunnable(() -> {
                    JOptionPane.showMessageDialog(null, "Connessione al server terminata");
                    System.exit(0);
                });

        net.start();
    }
}

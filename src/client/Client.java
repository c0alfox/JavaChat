package client;

import protocol.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Client {
    static ConnectionManager net;
    static String uname;
    static Color ucolor;
    static ClientUI ui;

    public static void main(String[] args) {

        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        ui = new ClientUI();
    }

    public static void connect(String target) throws IOException {
        net = new ConnectionManager(target);
        net.on(InboundMessage.class, msg -> ui.chatPanel.onMessage(msg, ui.userModel.getColor(msg.uname)))
                .on(JoinMessage.class, msg -> ui.userModel.add(msg))
                .on(LeaveMessage.class, msg -> ui.userModel.remove(msg))
                .on(ResponseMessage.class, msg -> net.pollCallback().accept(msg.toOptional()))
                .addDisposeRunnable(() -> {
                    JOptionPane.showMessageDialog(null, "Connessione al server terminata");
                    System.exit(0);
                });

        net.start();
    }
}

package client;

import protocol.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

public class Client {
    static ConnectionManager net;
    static String uname;
    static Color ucolor;
    static String ucolorstr;
    static ClientUI ui;

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        ui = new ClientUI();
    }

    public static void connect(String target) throws IOException {
        net = new ConnectionManager(target);
        net.on(InboundMessage.class, msg -> ui.chatPanel.onMessage(msg, ui.userModel.getColor(msg.uname)))
                .on(PrivateMessage.class, msg -> ui.chatPanel.onMessage(msg))
                .on(JoinMessage.class, msg -> ui.userModel.add(msg))
                .on(LeaveMessage.class, msg -> ui.userModel.remove(msg))
                .on(AddChannelMessage.class, msg -> ui.channelPanel.channelAdded(msg))
                .on(DeleteChannelMessage.class, msg -> ui.channelPanel.channelRemoved(msg))
                .on(ResponseMessage.class, msg -> {
                    Consumer<Optional<String>> callback = net.pollCallback();
                    if (callback != null) {
                        callback.accept(msg.toOptional());
                    }
                }).addDisposeRunnable(() -> {
                    JOptionPane.showMessageDialog(null, "Connessione al server terminata");
                    System.exit(0);
                });

        net.start();
    }
}

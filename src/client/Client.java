package client;

import protocol.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class Client {
    static ConnectionManager net;
    static String uname;
    static Color ucolor;
    static String ucolorstr;
    static ClientUI ui;
    static CountDownLatch latch;

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            throwable.printStackTrace();
            System.exit(1);
        });

        latch = new CountDownLatch(1);
        ui = new ClientUI();
        latch.countDown();
    }

    public static void connect(String target) throws IOException {
        net = new ConnectionManager(target);
        net.on(InboundMessage.class, msg -> afterUI(m -> ui.chatPanel.onMessage(m, ui.userModel.getColor(m.uname)), msg))
                .on(PrivateMessage.class, msg -> afterUI(m -> ui.chatPanel.onMessage(m), msg))
                .on(JoinMessage.class, msg -> afterUI(m -> ui.userModel.add(m), msg))
                .on(LeaveMessage.class, msg -> afterUI(m -> ui.userModel.remove(m), msg))
                .on(AddChannelMessage.class, msg -> afterUI(m -> ui.channelPanel.channelAdded(m), msg))
                .on(DeleteChannelMessage.class, msg -> afterUI(m -> ui.channelPanel.channelRemoved(m), msg))
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

    private static <T extends Message> void afterUI(Consumer<T> f, T arg) {
        while (true) {
            try {
                latch.await();
                f.accept(arg);
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

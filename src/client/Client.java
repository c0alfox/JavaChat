package client;

import protocol.Connection;
import protocol.ConnectionManager;
import protocol.MsgMessageRunner;

import java.io.IOException;
import java.util.List;

public class Client {
    static Connection c;
    static ConnectionManager net;
    static String uname;

    public static void main(String[] args) {
        new ClientUI();
    }

    public static void connect(String target) throws IOException {
        c = new Connection(target);
        net = new ConnectionManager(c, List.of(MsgMessageRunner.class));
        net.start();
    }
}

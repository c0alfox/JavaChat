package client;

import protocol.ConnectionManager;

import java.io.IOException;

public class Client {
    static ConnectionManager net;
    static String uname;

    public static void main(String[] args) {
        new ClientUI();
    }

    public static void connect(String target) throws IOException {
        net = new ConnectionManager(target);
        net.start();
    }
}

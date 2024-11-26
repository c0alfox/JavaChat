package client;

import java.io.IOException;

import protocol.Connection;
import protocol.MessageRunner;
import protocol.ResponseManager;

public class Client {
    static Connection c;
    static String uname;

    public static void main(String[] args) {
        new ClientUI();
        new ResponseManager(c).start();
    }

    public static void connect(String target) throws IOException {
        c = new Connection(target);
    }

    private static void close() throws IOException {
        c.close();
    }

    public static void send(String payload) {
        c.send("m this " + payload);
        System.out.println("> m this " + payload);
    }

    public static void send(MessageRunner mr) {
        send(mr.toString());
    }
}

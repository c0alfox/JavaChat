package server;

import protocol.Connection;
import protocol.ConnectionManager;
import protocol.UserMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket ss;

        try {
            ss = new ServerSocket(Connection.PORT);
        } catch (IOException e) {
            System.out.println("Errore nella creazione del server");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                Socket s = ss.accept();
                ConnectionManager net = new ConnectionManager(s);
                net.on(UserMessage.class, msg -> {
                    User user = new User(net, msg.uname, msg.color);
                    user.net.addDisposeRunnable(() -> User.removeUser(user));
                    User.addUser(user);
                });
                net.start();
            } catch (IOException e) {
                System.out.println("Errore nell'apertura della connessione");
            }
        }
    }
}

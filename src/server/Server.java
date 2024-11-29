package server;

import protocol.Connection;
import protocol.ConnectionManager;

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
                new ConnectionManager(s).start();
            } catch (IOException e) {
                System.out.println("Errore nell'apertura della connessione");
            }
        }
    }
}

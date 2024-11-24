package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import protocol.Connection;

public class Server {
    static Connection c;

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
                new ServerThread(ss.accept()).start();
            } catch (IOException e) {
                System.out.println("Errore nell'apertura della connessione");
            }
        }
    }

    private static class ServerThread extends Thread {
        final Connection c;

        ServerThread(Connection c) {
            this.c = c;
            System.out.println(c.getSocket().getInetAddress().getHostAddress() + " - Connesso");
        }

        ServerThread(Socket s) throws IOException {
            c = new Connection(s);
            System.out.println(c.getSocket().getInetAddress().getHostAddress() + " - Connesso");
        }

        @Override
        public void run() {
            super.run();

            while (true) {
                try {
                    String req = c.recv();

                    if (req == null) {
                        System.out.println(c.getSocket().getInetAddress().getHostAddress() + " - Connessione terminata");
                        c.close();
                        return;
                    }

                    System.out.println(c.getSocket().getInetAddress().getHostAddress() + " - " + req);
                } catch (IOException e) {
                    System.out.println(c.getSocket().getInetAddress().getHostAddress() + " - Errore in input");
                }
            }
        }
    }
}

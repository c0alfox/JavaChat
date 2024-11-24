package client;

import java.io.IOException;
import java.util.Scanner;

import protocol.Connection;

public class Client {
    static Connection c;
    static boolean active = true;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        new ClientUI();

        while (true) {
            String payload = s.nextLine();

            if (payload.equalsIgnoreCase("/exit")) {
                break;
            }

            c.send(payload);
        }
    }

    public static void connect(String target) throws IOException {
        c = new Connection(target);
        new ClientThread().start();
    }

    public static void send(String payload) {
        c.send(payload);
        System.out.println("> " + payload);
    }

    public static void close() throws IOException {
        active = false;
        c.close();
    }

    private static class ClientThread extends Thread {
        @Override
        public void run() {
            while(Client.active) {
                try {
                    String s = c.recv();
                    System.out.println("< " + s);
                } catch (IOException e) {
                    if (Client.active) System.out.println("Errore nella ricezione del messaggio.");
                }
            }
        }
    }
}

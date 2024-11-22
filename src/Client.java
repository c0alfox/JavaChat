import java.io.IOException;
import java.util.Scanner;

import protocol.Connection;

public class Client {
    static Connection c;
    static boolean active = true;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        while (c == null) {
            System.out.print("Inserisci l'indirizzo del server: ");
            String host = s.nextLine().strip();
            try {
                c = new Connection(host);
            } catch (IOException e) {
                System.out.println("Errore nella connessione al server.");
            }
        }

        new ClientThread().start();

        while (true) {
            String payload = s.nextLine();

            if (payload.equalsIgnoreCase("/exit")) {
                break;
            }

            c.send(payload);
        }

        try {
            active = false;
            c.close();
        } catch (IOException e) {
            System.out.println("Errore nella chiusura della connessione.");
        }
    }

    private static class ClientThread extends Thread {
        @Override
        public void run() {
            while(Client.active) {
                try {
                    c.recv();
                } catch (IOException e) {
                    if (Client.active) System.out.println("Errore nella ricezione del messaggio.");
                }
            }
        }
    }
}

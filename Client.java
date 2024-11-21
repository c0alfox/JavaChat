import java.io.IOException;
import java.util.Scanner;

public class Client {
    static Connection c;

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
            c.close();
        } catch (IOException e) {
            System.out.println("Errore nella chiusura della connessione.");
        }
    }

    private static class ClientThread extends Thread {
        @Override
        public void run() {
            while(true) {
                try {
                    c.recv();
                } catch (IOException e) {
                    System.out.println("Errore nella ricezione del messaggio.");
                }
            }
        }
    }
}

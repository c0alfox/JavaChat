package protocol;

import client.Client;

import java.io.IOException;

public class NetManager extends Thread {
    Connection netStream;
    boolean active;

    public NetManager(Connection stream) {
        netStream = stream;
        active = true;
    }

    @Override
    public void run() {
        while (active) {
            try {
                String payload = netStream.recv();
                System.out.println("< " + payload);
            } catch (IOException e) {
                if (active) System.out.println("Errore nella ricezione del messaggio.");
            }
        }
    }

    public void close() {
        active = false;
    }
}

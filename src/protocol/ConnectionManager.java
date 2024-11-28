package protocol;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.SynchronousQueue;

public class ConnectionManager {
    public class ExcludedPacketException extends Exception {};

    Connection c;
    boolean active;
    SynchronousQueue<MessageRunner> sentQueue;
    SynchronousQueue<MessageRunner> recvQueue;

    final ArrayList<Class<? extends MessageRunner>> exclude;

    public ConnectionManager(String target, Collection<Class<? extends MessageRunner>> excludes) throws IOException {
        c = new Connection(target);
        sentQueue = new SynchronousQueue<>();
        recvQueue = new SynchronousQueue<>();

        exclude = new ArrayList<>(excludes);
    }

    public ConnectionManager(Socket socket, Collection<Class<? extends MessageRunner>> excludes) throws IOException {
        c = new Connection(socket);
        sentQueue = new SynchronousQueue<>();
        recvQueue = new SynchronousQueue<>();

        exclude = new ArrayList<>(excludes);
    }

    public ConnectionManager(Connection c, Collection<Class<? extends MessageRunner>> excludes) {
        this.c = c;
        sentQueue = new SynchronousQueue<>();
        recvQueue = new SynchronousQueue<>();

        exclude = new ArrayList<>(excludes);
    }

    public void start() {
        new ReceiveThread().start();
    }

    public void send(MessageRunner m) {
        c.send(m.toString());
        System.out.println("> " + m.toString());
    }

    public synchronized <R extends MessageRunner> R awaitSend(MessageRunner m) {
        
        return (R) m;
    }

    public void close() {
        active = false;
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            while(active) {
                try {
                    String payload = c.recv();
                    System.out.println("< " + payload);
                } catch (IOException e) {
                    if (active) System.out.println("Errore nella ricezione del messaggio");
                }
            }
        }
    }
}

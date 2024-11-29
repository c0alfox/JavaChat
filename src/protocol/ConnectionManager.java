package protocol;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Consumer;

public class ConnectionManager extends Connection {
    boolean active = true;
    HashMap<Class<? extends Message>, Consumer<? extends Message>> functionMappings;
    final Queue<Consumer<Optional<String>>> responseCallbackQueue;

    public ConnectionManager(String target) throws IOException {
        super(target);
        functionMappings = new HashMap<>();
        responseCallbackQueue = new LinkedList<>();
    }

    public ConnectionManager(Socket s) throws IOException {
        super(s);
        functionMappings = new HashMap<>();
        responseCallbackQueue = new LinkedList<>();
    }

    public void start() {
        new ReceiveThread().start();
        System.out.println("Connessione stabilita con " + socket.getInetAddress().getHostAddress());
    }

    public void close() {
        active = false;
        System.out.println("Connessione terminata con " + socket.getInetAddress().getHostAddress());
    }

    public synchronized <T extends Message> ConnectionManager on(Class<T> type, Consumer<T> method) {
        functionMappings.put(type, method);
        return this;
    }

    public synchronized void send(String payload, Consumer<Optional<String>> callback) {
        responseCallbackQueue.offer(callback);
        super.send(payload);
    }

    public synchronized Consumer<Optional<String>> pollCallback() {
        return responseCallbackQueue.poll();
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            while (active) {
                try {
                    String payload = recv();
                    if (payload == null) {
                        close();
                        continue;
                    }

                    Message m = Message.create(payload);

                    Consumer<Message> consumer = (Consumer<Message>) functionMappings.get(m.getClass());

                    if (consumer != null) {
                        consumer.accept(m);
                    } else {
                        System.out.println("Messaggio " + payload + " non gestito");
                    }
                } catch (IOException e) {
                    if (active) System.out.println("Errore nella ricezione del messaggio");
                    close();
                } catch (Message.IllformedMessageException e) {
                    System.out.println("Messaggio malformato");
                }
            }
        }
    }
}

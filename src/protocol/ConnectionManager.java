package protocol;

import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.function.Consumer;

public class ConnectionManager extends Connection {
    final Queue<Consumer<Optional<String>>> responseCallbackQueue;
    final Stack<Runnable> disposeRunnables;
    boolean active = true;
    HashMap<Class<? extends Message>, Consumer<? extends Message>> functionMappings;

    public ConnectionManager(String target) throws IOException {
        super(target);
        functionMappings = new HashMap<>();
        responseCallbackQueue = new LinkedList<>();
        disposeRunnables = new Stack<>();
    }

    public ConnectionManager(Socket s) throws IOException {
        super(s);
        functionMappings = new HashMap<>();
        responseCallbackQueue = new LinkedList<>();
        disposeRunnables = new Stack<>();
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

    private void dispose() {
        synchronized (disposeRunnables) {
            close();
            disposeRunnables.forEach(Runnable::run);
        }
    }

    public void addDisposeRunnable(Runnable r) {
        synchronized (disposeRunnables) {
            disposeRunnables.push(r);
        }
    }

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            while (active) {
                try {
                    String payload = recv();
                    if (payload == null) {
                        active = false;
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
                    if (active) {
                        System.out.println("Errore nella ricezione del messaggio");
                        active = false;
                    }
                } catch (Message.IllformedMessageException e) {
                    System.out.println("Messaggio malformato");
                }
            }

            dispose();
        }
    }
}

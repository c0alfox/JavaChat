package protocol;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {
    public static final int PORT = 16384;

    final protected Socket socket;
    final protected BufferedReader recv;
    final protected PrintWriter send;

    public Connection(String target) throws IOException {
        socket = new Socket(target, PORT);
        recv = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        send = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        recv = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        send = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    public Socket getSocket() {
        return socket;
    }

    public void send(String payload) {
        synchronized (send) {
            send.println(payload);
            System.out.println(socket.getInetAddress().getHostAddress() + " > " + payload);
        }
    }

    public String recv() throws IOException {
        synchronized (recv) {
            String payload = recv.readLine();
            System.out.println(" < " + socket.getInetAddress().getHostAddress() + " - " + payload);
            return payload;
        }
    }

    public void close() throws IOException {
        socket.close();
        recv.close();
        send.close();
    }
}

package protocol;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {
    public static final int PORT = 16384;

    protected Socket socket;
    protected BufferedReader recv;
    protected PrintWriter send;

    protected Connection() {}

    public Connection(String target) throws IOException {
        socket = new Socket(target, PORT);
        setStreams();
    }

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }

    private void setStreams() throws IOException {
        recv = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        send = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized void send(String payload) {
        send.println(payload);
    }

    public synchronized String recv() throws IOException {
        return recv.readLine();
    }

    public void close() throws IOException {
        socket.close();
        recv.close();
        send.close();
    }
}

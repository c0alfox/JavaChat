import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {
    public static final int PORT = 16384;

    Socket socket;
    BufferedReader recv;
    PrintWriter send;

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

    private void send(String payload) {
        send.println(payload);
    }

    private String recv() throws IOException {
        return recv.readLine();
    }

    public void close() throws IOException {
        socket.close();
        recv.close();
        send.close();
    }
}

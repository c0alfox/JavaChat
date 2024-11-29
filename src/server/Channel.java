package server;

import java.util.concurrent.SynchronousQueue;

public class Channel {
    private final String name;
    private final SynchronousQueue<User> users;

    public Channel(String name) {
        this.name = name;
        users = new SynchronousQueue<>();
    }
}

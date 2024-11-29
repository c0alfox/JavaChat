package server;

import protocol.CommandMessage;
import protocol.ConnectionManager;
import protocol.OutboundMessage;

import java.util.HashMap;

public class User {
    final ConnectionManager net;
    String uname, color;
    static final HashMap<String, User> users = new HashMap<>();

    public static synchronized void addUser(User user) {
        if (users.containsKey(user.uname)) {
            user.net.send("r Nome utente non disponibile");
            user.net.close();
        } else {
            user.net.on(OutboundMessage.class, msg -> {
                user.net.send("r OK");
            });

            user.net.on(CommandMessage.class, msg -> {
                user.net.send("r OK");
            });
            user.net.send("r OK");
            users.put(user.uname, user);
        }
    }

    public static synchronized void removeUser(User user) {
        users.remove(user.uname, user);
    }

    public User(ConnectionManager net, String uname, String color) {
        this.net = net;
        this.uname = uname;
        this.color = color;
    }
}

package server;

import protocol.CommandMessage;
import protocol.ConnectionManager;
import protocol.OutboundMessage;
import protocol.ResponseMessage;

import java.util.HashMap;

public class User {
    final ConnectionManager net;
    String uname, color;
    static final HashMap<String, User> users = new HashMap<>();

    public static synchronized void addUser(User user) {
        if (users.containsKey(user.uname)) {
            user.net.send(new ResponseMessage("Nome utente non disponibile").toString());
            user.net.close();
        } else {
            user.net.on(OutboundMessage.class, msg -> user.net.send(new ResponseMessage().toString()));
            user.net.on(CommandMessage.class, msg -> user.net.send(new ResponseMessage().toString()));
            user.net.send(new ResponseMessage().toString());
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

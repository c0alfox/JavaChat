package server;

import protocol.CommandMessage;
import protocol.ConnectionManager;
import protocol.OutboundMessage;
import protocol.ResponseMessage;

import java.util.HashMap;

public class User {
    static final HashMap<String, User> users = new HashMap<>();
    final ConnectionManager net;
    public boolean muted = false;
    String channel = "", uname, color;

    public User(ConnectionManager net, String uname, String color) {
        this.net = net;
        this.uname = uname;
        this.color = color;
    }

    public static synchronized void addUser(User user) {
        if (user.uname.length() > 24) {
            user.net.send(new ResponseMessage("Il nome utente può essere di massimo 24 caratteri").toString());
            return;
        }

        if (users.containsKey(user.uname)) {
            user.net.send(new ResponseMessage("Nome utente non disponibile").toString());
            return;
        }

        user.net.on(OutboundMessage.class, msg -> {
            if (user.muted) {
                user.net.send(new ResponseMessage("Sei stato mutato").toString());
                return;
            }

            if (!user.channel.isEmpty()) {
                user.net.send(new ResponseMessage().toString());
                Channel.broadcastMessage(user, msg.msg);

                return;
            }

            user.net.send(new ResponseMessage("Non in un canale").toString());
        }).on(CommandMessage.class, msg -> new Command(user, msg.cmd).run());

        user.net.send(new ResponseMessage().toString());
        users.put(user.uname, user);
    }

    public static synchronized void removeUser(User user) {
        users.remove(user.uname, user);
    }

    public static synchronized User getUser(String username) {
        return users.get(username);
    }
}

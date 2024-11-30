package server;

import protocol.CommandMessage;
import protocol.ConnectionManager;
import protocol.OutboundMessage;
import protocol.ResponseMessage;

import java.util.HashMap;

public class User {
    static final HashMap<String, User> users = new HashMap<>();
    final ConnectionManager net;
    String channel = "", uname, color;

    public User(ConnectionManager net, String uname, String color) {
        this.net = net;
        this.uname = uname;
        this.color = color;
    }

    public static synchronized void addUser(User user) {
        if (users.containsKey(user.uname)) {
            user.net.send(new ResponseMessage("Nome utente non disponibile").toString());
        } else {
            user.net.on(OutboundMessage.class, msg -> {
                if (!user.channel.isEmpty()) {
                    user.net.send(new ResponseMessage().toString());
                    Channel.broadcastMessage(user, msg.msg);
                }

                user.net.send(new ResponseMessage("Non in un canale").toString());
            });

            user.net.on(CommandMessage.class, msg -> {
                user.net.send(new ResponseMessage().toString());

                String syntaxErrorMessage = new ResponseMessage("Errore di sintassi del comando").toString();
                String[] parts = msg.cmd.split("[ \t]");

                if (parts.length < 1) {
                    user.net.send(syntaxErrorMessage);
                    return;
                }

                if (parts.length == 2 && parts[0].equals("join")) {
                    Channel.joinChannel(parts[1], user);
                    return;
                }

                if (parts.length == 1 && parts[0].equals("leave")) {
                    Channel.leaveChannel(user);
                    return;
                }

                user.net.send(syntaxErrorMessage);
            });

            user.net.send(new ResponseMessage().toString());
            users.put(user.uname, user);
        }
    }

    public static synchronized void removeUser(User user) {
        users.remove(user.uname, user);
    }
}

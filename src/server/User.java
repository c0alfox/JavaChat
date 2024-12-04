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
        }).on(CommandMessage.class, msg -> {
            String[] parts = msg.cmd.split("[ \t]");

            if (parts.length == 2 && parts[0].equals("join")) {
                Channel.joinChannel(parts[1], user);
                user.net.send(new ResponseMessage("OK " + parts[1]).toString());
                return;
            }

            if (parts.length == 1 && parts[0].equals("leave")) {
                Channel.leaveChannel(user);
                user.net.send(new ResponseMessage().toString());
                return;
            }

            if (parts.length == 2 && parts[0].equals("mute")) {
                User other;
                if (!users.containsKey(parts[1]) || user.channel.equals((other = users.get(parts[1])).channel)) {
                    user.net.send(new ResponseMessage("Utente inesistente").toString());
                    return;
                }

                if (!Channel.isAdmin(user)) {
                    user.net.send(new ResponseMessage("Non sei amministratore del canale").toString());
                    return;
                }

                other.muted = true;
                user.net.send(new ResponseMessage().toString());
                return;
            }

            if (parts.length == 2 && parts[0].equals("unmute")) {
                User other;
                if (!users.containsKey(parts[1]) || user.channel.equals((other = users.get(parts[1])).channel)) {
                    user.net.send(new ResponseMessage("Utente inesistente").toString());
                    return;
                }

                if (!Channel.isAdmin(user)) {
                    user.net.send(new ResponseMessage("Non sei amministratore del canale").toString());
                    return;
                }

                other.muted = false;
                user.net.send(new ResponseMessage().toString());
                return;
            }

            user.net.send(new ResponseMessage("Errore di sintassi del comando").toString());
        });

        user.net.send(new ResponseMessage().toString());
        users.put(user.uname, user);
    }

    public static synchronized void removeUser(User user) {
        users.remove(user.uname, user);
    }
}

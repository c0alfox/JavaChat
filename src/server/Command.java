package server;

import protocol.InboundMessage;
import protocol.ResponseMessage;

public class Command {
    final String cmd;
    final String[] parts;
    final User user;

    public Command(User user, String cmd) {
        this.user = user;
        this.cmd = cmd;
        parts = cmd.split("[ \t]");
    }

    void runJoin(String channel) {
        Channel.joinChannel(channel, user);
        user.net.send(new ResponseMessage("OK " + parts[1]).toString());
    }

    void runLeave() {
        Channel.leaveChannel(user);
        user.net.send(new ResponseMessage().toString());
    }

    User getUserInChannel(String uname) {
        User other;
        if (user.uname.equals(uname)
                || !User.users.containsKey(uname)
                || !user.channel.equals((other = User.users.get(uname)).channel)) {
            return null;
        }

        return other;
    }

    void runWhisper(String uname, String msg) {
        User other;
        if ((other = getUserInChannel(uname)) == null) {
            user.net.send(new ResponseMessage("Utente inesistente").toString());
            return;
        }

        user.net.send(new ResponseMessage().toString());
        other.net.send(new InboundMessage(user.uname, msg, true).toString());
    }

    void runMute(String uname) {
        User other;
        if ((other = getUserInChannel(uname)) == null) {
            user.net.send(new ResponseMessage("Utente inesistente").toString());
            return;
        }

        if (!Channel.isAdmin(user)) {
            user.net.send(new ResponseMessage("Non sei amministratore del canale").toString());
        }

        other.muted = true;
        user.net.send(new ResponseMessage().toString());
    }

    void runUnmute(String uname) {
        User other;
        if ((other = getUserInChannel(uname)) == null) {
            user.net.send(new ResponseMessage("Utente inesistente").toString());
            return;
        }

        if (!Channel.isAdmin(user)) {
            user.net.send(new ResponseMessage("Non sei amministratore del canale").toString());
        }

        other.muted = false;
        user.net.send(new ResponseMessage().toString());
    }

    public void runUsers() {
        String[] unames = Channel.getUsernames(user.channel);
        if (unames == null) {
            user.net.send(new ResponseMessage("Non sei in un canale").toString());
            return;
        }

        user.net.send(new ResponseMessage().toString());
        user.net.send(InboundMessage.server(unames.length + "utenti: " + String.join(", ", unames))
                .toString());
    }

    public void runChannels() {
        String[] channels = Channel.getChannels();
        user.net.send(new ResponseMessage().toString());

        user.net.send(InboundMessage.server(
                channels.length == 0
                        ? "Nessun canale"
                        : (channels.length + "canali: " + String.join(", ", channels)
                )).toString());
    }

    public void run() {
        if (parts.length == 2 && (parts[0].equals("j") || parts[0].equals("join"))) {
            runJoin(parts[1]);
        } else if (parts.length == 1 && (parts[0].equals("l") || parts[0].equals("leave"))) {
            runLeave();
        } else if (parts.length > 2 && (parts[0].equals("w") || parts[0].equals("whisper"))) {
            String[] newParts = cmd.split("[ \t]", 3);
            runWhisper(parts[1], newParts[2]);
        } else if (parts.length == 2 && (parts[0].equals("m") || parts[0].equals("mute"))) {
            runMute(parts[1]);
        } else if (parts.length == 2 && parts[0].equals("unmute")) {
            runUnmute(parts[1]);
        } else if (parts.length == 1 && parts[0].equals("users")) {
            runUsers();
        } else if (parts.length == 1 && (parts[0].equals("ch") || parts[0].equals("channels"))) {
            runChannels();
        } else if (parts.length == 1 && parts[0].equals("exit")) {
            user.net.send(new ResponseMessage("QUIT").toString());
        } else {
            user.net.send(new ResponseMessage("Errore di sintassi del comando").toString());
        }

    }
}

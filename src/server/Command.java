package server;

import protocol.InboundMessage;
import protocol.ResponseMessage;

import java.util.Hashtable;
import java.util.function.Consumer;

public class Command {
    private static final Hashtable<String, Consumer<Command>> commandBindings = new Hashtable<>() {{
        put("j", Command::join);
        put("join", Command::join);

        put("l", Command::leave);
        put("leave", Command::leave);

        put("whisper", Command::whisper);
        put("w", Command::whisper);

        put("mute", Command::mute);
        put("m", Command::mute);

        put("unmute", Command::unmute);

        put("channels", Command::channels);
        put("ch", Command::channels);

        put("mychannel", Command::myChannel);

        put("users", Command::users);

        put("quit", Command::exit);
        put("exit", Command::exit);
    }};
    public final String cmd;
    public final String[] parts;
    public final User user;

    public Command(User user, String cmd) {
        this.user = user;
        this.cmd = cmd;
        parts = cmd.split("[ \t]");
    }

    void syntaxError() {
        user.net.send(new ResponseMessage("Errore di sintassi del comando").toString());
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

    void join() {
        if (parts.length != 2) {
            syntaxError();
            return;
        }

        Channel.joinChannel(parts[1], user);
        user.net.send(new ResponseMessage("OK " + parts[1]).toString());
    }

    void leave() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        Channel.leaveChannel(user);
        user.net.send(new ResponseMessage().toString());
    }

    void whisper() {
        if (parts.length <= 2) {
            syntaxError();
            return;
        }

        String[] p = cmd.split("[ \t]", 3);
        String uname = p[0];
        String msg = p[1];

        if (uname.equals(user.uname)) {
            user.net.send(new ResponseMessage("Non puoi sussurrare a te stesso").toString());
            return;
        }

        User other;
        if ((other = User.getUser(uname)) == null) {
            user.net.send(new ResponseMessage("Utente inesistente").toString());
            return;
        }

        user.net.send(new ResponseMessage().toString());
        other.net.send(new InboundMessage(user.uname, msg, true).toString());
    }

    void mute() {
        if (parts.length != 2) {
            syntaxError();
            return;
        }

        if (!Channel.isAdmin(user)) {
            user.net.send(new ResponseMessage("Non sei amministratore del canale").toString());
            return;
        }

        if (parts[1].equals(user.uname)) {
            user.net.send(new ResponseMessage("Non puoi silenziare te stesso").toString());
            return;
        }

        User other;
        if ((other = getUserInChannel(parts[1])) == null) {
            user.net.send(new ResponseMessage("Utente non connesso al canale").toString());
            return;
        }

        other.muted = true;
        user.net.send(new ResponseMessage().toString());
    }

    void unmute() {
        if (parts.length != 2) {
            syntaxError();
            return;
        }

        if (!Channel.isAdmin(user)) {
            user.net.send(new ResponseMessage("Non sei amministratore del canale").toString());
            return;
        }

        if (parts[1].equals(user.uname)) {
            user.net.send(new ResponseMessage("Non puoi togliere il silenzioso a te stesso").toString());
            return;
        }

        User other;
        if ((other = getUserInChannel(parts[1])) == null) {
            user.net.send(new ResponseMessage("Utente non connesso al canale").toString());
            return;
        }

        other.muted = false;
        user.net.send(new ResponseMessage().toString());
    }

    public void users() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        String[] unames = Channel.getUsernames(user.channel);
        if (unames == null) {
            user.net.send(new ResponseMessage("Non sei in un canale").toString());
            return;
        }

        user.net.send(new ResponseMessage().toString());
        user.net.send(InboundMessage.server(unames.length + "utenti: " + String.join(", ", unames))
                .toString());
    }

    public void channels() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        String[] channels = Channel.getChannels();
        user.net.send(new ResponseMessage().toString());

        user.net.send(InboundMessage.server(
                channels.length == 0
                        ? "Nessun canale"
                        : (channels.length + "canali: " + String.join(", ", channels)
                )).toString());
    }

    public void myChannel() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        user.net.send(new ResponseMessage().toString());
        user.net.send(InboundMessage.server(user.channel.isBlank()
                ? "Non sei connesso a nessun canale"
                : ("Sei connessso a " + user.channel)
        ).toString());
    }

    public void exit() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        user.net.send(new ResponseMessage("QUIT").toString());
    }

    public void run() {
        Consumer<Command> func = commandBindings.get(parts[0]);

        if (func == null) {
            user.net.send(new ResponseMessage("Comando Inesistente").toString());
            return;
        }

        func.accept(this);
    }
}

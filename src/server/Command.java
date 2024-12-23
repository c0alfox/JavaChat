package server;

import protocol.InboundMessage;
import protocol.PrivateMessage;
import protocol.ResponseMessage;

import java.util.Hashtable;
import java.util.Objects;
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

        put("mutelist", Command::muteList);

        put("channels", Command::channels);
        put("ch", Command::channels);

        put("mychannel", Command::myChannel);

        put("users", Command::users);
        put("whoami", Command::whoami);

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

        Channel.broadcast(parts[1], InboundMessage.server(user.uname + " è entrato nel canale"));
        Channel.joinChannel(parts[1], user);
        user.net.send(new ResponseMessage("OK " + parts[1]).toString());
    }

    void leave() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        Channel.broadcast(user.channel, InboundMessage.server(user.uname + " è uscito dal canale"));
        Channel.leaveChannel(user);
        user.net.send(new ResponseMessage().toString());
    }

    void whisper() {
        if (parts.length <= 2) {
            syntaxError();
            return;
        }

        String uname = parts[1];
        String msg = cmd.substring(cmd.indexOf(' ', cmd.indexOf(' ') + 1));

        System.out.println(uname);
        System.out.println(user.uname);
        System.out.println(uname.equals(user.uname));
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
        user.net.send(PrivateMessage.server("Messaggio inviato con successo").toString());
        other.net.send(new PrivateMessage(user.uname, user.color, msg).toString());
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

        MuteManager.mute(other, user.channel);
        user.net.send(new ResponseMessage().toString());
        user.net.send(PrivateMessage.server("Utente " + other.uname + " silenziato").toString());
        other.net.send(PrivateMessage.server("Sei stato silenziato").toString());
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

        MuteManager.unmute(other, user.channel);
        user.net.send(new ResponseMessage().toString());
        user.net.send(PrivateMessage.server("Utente " + other.uname + " non più silenziato").toString());
        other.net.send(PrivateMessage.server("Non sei più silenziato").toString());
    }

    public void muteList() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        if (!Channel.isAdmin(user)) {
            user.net.send(new ResponseMessage("Non sei amministratore del canale").toString());
            return;
        }

        String[] muted = Objects.requireNonNull(MuteManager
                        .getMutedUsers(user.channel))
                .stream().map(u -> u.uname)
                .toArray(String[]::new);

        user.net.send(PrivateMessage.server(
                muted.length == 0
                        ? "Nessun utente silenziato"
                        : (muted.length + " utenti silenziati: " + String.join(", ", muted)
                )).toString());
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
        user.net.send(PrivateMessage.server(unames.length + " utenti: " + String.join(", ", unames))
                .toString());
    }

    public void channels() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        String[] channels = Channel.getChannels();
        user.net.send(new ResponseMessage().toString());

        user.net.send(PrivateMessage.server(
                channels.length == 0
                        ? "Nessun canale"
                        : (channels.length + " canali: " + String.join(", ", channels)
                )).toString());
    }

    public void myChannel() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        user.net.send(new ResponseMessage().toString());
        user.net.send(PrivateMessage.server(user.channel.isBlank()
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

    public void whoami() {
        if (parts.length != 1) {
            syntaxError();
            return;
        }

        user.net.send(new ResponseMessage().toString());
        user.net.send(PrivateMessage.server(user.uname).toString());
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

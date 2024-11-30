package server;

import protocol.InboundMessage;
import protocol.NowAdminMessage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Channel {
    private final String name;
    private final Queue<User> users;
    private static final HashMap<String, Channel> channels = new HashMap<>();

    private synchronized static Channel getChannel(String name) {
        if (channels.containsKey(name)) {
            return channels.get(name);
        }

        Channel ret = new Channel(name);
        channels.put(name, ret);
        return ret;
    }

    public synchronized static void joinChannel(String channel, User user) {
        leaveChannel(user);
        user.channel = channel;

        Channel ch = getChannel(user.channel);

        ch.users.offer(user);
        if (ch.users.peek() == user) {
            user.net.send(new NowAdminMessage().toString());
        }
    }

    public synchronized static void leaveChannel(User user) {
        if (user.channel.isEmpty()) {
            return;
        }

        Channel ch = getChannel(user.channel);
        boolean wasFirst = ch.users.peek() == user;
        ch.users.remove(user);

        if (ch.users.isEmpty()) {
            channels.remove(user.channel, ch);
            return;
        }

        if (wasFirst) {
            ch.users.peek().net.send(new NowAdminMessage().toString());
        }
    }

    public synchronized static void broadcastMessage(User sender, String msg) {
        Channel ch = getChannel(sender.channel);

        for (User user : ch.users) {
            if (user != sender) {
                user.net.send(new InboundMessage(sender.uname, msg, false).toString());
            }
        }
    }

    private Channel(String name) {
        this.name = name;
        users = new LinkedList<>();
    }
}

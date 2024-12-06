package server;

import protocol.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Channel {
    private static final HashMap<String, Channel> channels = new HashMap<>();
    public final String name;
    private final Queue<User> users;

    private Channel(String name) {
        this.name = name;
        users = new LinkedList<>();
    }

    private synchronized static Channel getChannel(String name) {
        if (channels.containsKey(name)) {
            return channels.get(name);
        }

        Channel ret = new Channel(name);
        addChannel(ret);
        return ret;
    }

    public synchronized static void joinChannel(String channel, User user) {
        leaveChannel(user);
        user.channel = channel;

        Channel ch = getChannel(user.channel);
        broadcast(user.channel, new JoinMessage(user.uname, user.color));

        ch.users.offer(user);

        ch.users.forEach(other -> user.net.send(new JoinMessage(other.uname, other.color).toString()));
    }

    public synchronized static void leaveChannel(User user) {
        if (user.channel.isEmpty()) {
            return;
        }

        Channel ch = getChannel(user.channel);
        ch.users.forEach(other -> user.net.send(new LeaveMessage(other.uname).toString()));
        ch.users.remove(user);

        broadcast(user.channel, new LeaveMessage(user.uname));

        if (ch.users.isEmpty()) {
            removeChannel(ch);
        }

        user.channel = "";
    }

    public synchronized static void broadcast(String name, Message msg) {
        Channel ch = getChannel(name);

        ch.users.forEach(user -> user.net.send(msg.toString()));
    }

    public synchronized static void broadcast(User sender, Message msg) {
        Channel ch = getChannel(sender.channel);

        ch.users.forEach(user -> {
            if (user != sender) {
                user.net.send(msg.toString());
            }
        });
    }

    public static void broadcastMessage(User sender, String msg) {
        broadcast(sender, new InboundMessage(sender.uname, msg));
    }


    public synchronized static boolean isAdmin(User user) {
        Channel ch = getChannel(user.channel);

        return ch.users.peek() == user;
    }

    public synchronized static String[] getUsernames(String name) {
        if (name.isEmpty()) {
            return null;
        }

        return getChannel(name)
                .users
                .stream().map(user -> user.uname)
                .toArray(String[]::new);
    }

    public synchronized static String[] getChannels() {
        return channels
                .keySet()
                .toArray(String[]::new);
    }

    private synchronized static void addChannel(Channel c) {
        channels.put(c.name, c);
        User.broadcast(new AddChannelMessage(c.name).toString());
    }

    private synchronized static void removeChannel(Channel c) {
        channels.remove(c.name, c);
        User.broadcast(new DeleteChannelMessage(c.name).toString());
    }

    public synchronized static void newUser(User user) {
        channels.values().forEach(channel -> user.net.send(new AddChannelMessage(channel.name).toString()));
    }
}

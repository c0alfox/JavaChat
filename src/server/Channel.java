package server;

import protocol.InboundMessage;
import protocol.JoinMessage;
import protocol.LeaveMessage;
import protocol.Message;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Channel {
    private static final HashMap<String, Channel> channels = new HashMap<>();
    private final String name;
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
        channels.put(name, ret);
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
            channels.remove(user.channel, ch);
        }

        user.channel = " ";
        user.muted = false;
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
        broadcast(sender, new InboundMessage(sender.uname, msg, false));
    }


    public synchronized static boolean isAdmin(User user) {
        Channel ch = getChannel(user.channel);

        return ch.users.peek() == user;
    }
}

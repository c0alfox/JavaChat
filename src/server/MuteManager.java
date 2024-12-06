package server;

import java.util.HashSet;
import java.util.Hashtable;

public class MuteManager {
    static final Hashtable<User, HashSet<String>> user2Channel = new Hashtable<>();
    static final Hashtable<String, HashSet<User>> channel2User = new Hashtable<>();

    public synchronized static void mute(User user, String channel) {
        if (channel.isBlank()) {
            return;
        }

        user2Channel.computeIfAbsent(user, k -> new HashSet<>()).add(channel);
        channel2User.computeIfAbsent(channel, k -> new HashSet<>()).add(user);
    }

    public synchronized static void unmute(User user, String channel) {
        if (channel.isBlank()) {
            return;
        }

        if (!user2Channel.contains(user)) {
            return;
        }

        if (user2Channel.containsKey(user)) {
            user2Channel.get(user).remove(channel);
        }

        if (channel2User.containsKey(channel)) {
            channel2User.get(channel).remove(user);
        }
    }

    public synchronized static boolean isMuted(User user, String channel) {
        if (channel.isBlank()) {
            return false;
        }

        return user2Channel.containsKey(user) && user2Channel.get(user).contains(channel);
    }

    public synchronized static HashSet<User> getMutedUsers(String channel) {
        if (channel.isBlank()) {
            return null;
        }

        return channel2User.get(channel);
    }

    public synchronized static void onUserDestroy(User user) {
        if (!user2Channel.contains(user)) {
            return;
        }

        user2Channel.get(user).forEach(ch -> channel2User.get(ch).remove(user));
        user2Channel.remove(user);
    }

    public synchronized static void onChannelDestroy(String channel) {
        if (!channel2User.contains(channel)) {
            return;
        }

        channel2User.get(channel).forEach(user -> user2Channel.get(user).remove(channel));
        channel2User.remove(channel);
    }
}

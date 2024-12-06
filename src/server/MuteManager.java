package server;

import java.util.HashSet;
import java.util.Hashtable;

public class MuteManager {
    static final Hashtable<User, HashSet<Channel>> user2Channel = new Hashtable<>();
    static final Hashtable<Channel, HashSet<User>> channel2User = new Hashtable<>();

    public synchronized static void mute(User user, Channel channel) {
        user2Channel.get(user).add(channel);
        channel2User.get(channel).add(user);
    }

    public synchronized static void unmute(User user, Channel channel) {
        if (!user2Channel.contains(user)) {
            return;
        }

        user2Channel.get(user).remove(channel);
        channel2User.get(channel).remove(user);
    }

    public synchronized static boolean isMuted(User user, Channel channel) {
        if (!user2Channel.contains(channel)) {
            return false;
        }

        return user2Channel.get(user).contains(channel);
    }

    public synchronized static void onUserDestroy(User user) {
        if (!user2Channel.contains(user)) {
            return;
        }

        user2Channel.get(user).forEach(ch -> channel2User.get(ch).remove(user));
        user2Channel.remove(user);
    }

    public synchronized static void onChannelDestroy(Channel channel) {
        if (!channel2User.contains(channel)) {
            return;
        }

        channel2User.get(channel).forEach(user -> user2Channel.get(user).remove(channel));
        channel2User.remove(channel);
    }
}

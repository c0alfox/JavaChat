package client;

import protocol.JoinMessage;
import protocol.LeaveMessage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserModel {
    private final HashMap<String, Color> userList;
    private final ArrayList<UserModelListener> userModelListeners;

    public UserModel() {
        userList = new HashMap<>();
        userModelListeners = new ArrayList<>();
    }

    public synchronized Color add(JoinMessage join) {
        Color ret = userList.put(join.uname, new Color(Integer.parseInt(join.color, 16)));
        dispatchUserAdded(join);
        return ret;
    }

    public synchronized boolean remove(JoinMessage join) {
        boolean ret = userList.remove(join.uname, new Color(Integer.parseInt(join.color, 16)));
        dispatchUserRemoved(new LeaveMessage(join.uname));
        if (userList.isEmpty()) {
            dispatchChannelLeft();
        }
        return ret;
    }

    public synchronized Color remove(LeaveMessage leave) {
        Color ret = userList.remove(leave.uname);
        dispatchUserRemoved(leave);
        if (userList.isEmpty()) {
            dispatchChannelLeft();
        }
        return ret;
    }

    public synchronized Color getColor(String uname) {
        return userList.get(uname);
    }

    public synchronized void addUserModelListener(UserModelListener l) {
        userModelListeners.add(l);
    }

    public synchronized void removeUserModelListener(UserModelListener l) {
        userModelListeners.remove(l);
    }

    public synchronized void dispatchUserAdded(JoinMessage join) {
        for (UserModelListener ml : userModelListeners) {
            ml.userAdded(join);
        }
    }

    public synchronized void dispatchUserRemoved(LeaveMessage leave) {
        for (UserModelListener ml : userModelListeners) {
            ml.userRemoved(leave);
        }
    }

    public synchronized void dispatchChannelLeft() {
        for (UserModelListener ml : userModelListeners) {
            ml.channelLeft();
        }
    }
}

package client;

import protocol.JoinMessage;
import protocol.LeaveMessage;

public interface UserModelListener {
    void userAdded(JoinMessage j);

    void userRemoved(LeaveMessage l);

    void channelLeft();
}

import protocol.Connection;
import protocol.MessageRunner;

import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Consumer;

public class ReqRespManager {
    Connection c;
    int greatestUsedId = 0;
    TreeSet<Integer> availableKnownIds;

    public ReqRespManager(Connection c) {
        this.c = c;
    }

    int popId() {
        return Objects.requireNonNullElse(availableKnownIds.pollLast(), ++greatestUsedId);
    }

    void pushId(int id) {
        if (id == greatestUsedId) {
            greatestUsedId--;
        } else {
            availableKnownIds.add(id);
        }
    }

    public void request(String payload, Consumer<String> responseCallback) {
        int id = popId();
        c.send(id + " " + payload);
    }

    public MessageRunner handleRecived(String payload) throws MessageRunner.IllformedMessageException {
        if (payload.isEmpty() || !Character.isDigit(payload.charAt(0))) {
            return MessageRunner.create(payload);
        }

        String[] parts = payload.split(" ", 2);
        return MessageRunner.create(parts[1], Integer.parseInt(parts[0]));
    }
}

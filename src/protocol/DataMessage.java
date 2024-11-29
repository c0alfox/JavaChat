package protocol;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class DataMessage extends Message {
    private final HashMap<String, String> users;

    public DataMessage(HashMap<String, String> users) {
        this.users = users;
    }

    @Override
    private String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        AtomicReference<String> res = new AtomicReference<>("d");
        users.forEach((String uname, String color) -> res.set(res.get() + (" " + uname + " " + color)));

        return res.get();
    }
}

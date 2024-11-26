package protocol;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public final class DataMessageRunner extends MessageRunner {
    private HashMap<String, String> users;

    public DataMessageRunner(HashMap<String, String> users) {
        this.users = users;
    }

    @Override
    protected String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        AtomicReference<String> res = new AtomicReference<>("d");
        users.forEach((String uname, String color) -> res.set(res.get() + (" " + uname + " " + color)));

        return res.get();
    }
}

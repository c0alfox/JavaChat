package protocol;

import java.util.HashMap;

public final class DataMessageRunner extends MessageRunner {
    public DataMessageRunner(HashMap<String, String> users) {
    }

    @Override
    protected String serverNoId() {
        return "";
    }
}

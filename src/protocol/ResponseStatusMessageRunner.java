package protocol;

public final class ResponseStatusMessageRunner extends MessageRunner {
    String msg;

    public ResponseStatusMessageRunner(String substring) {
        msg = substring;
    }

    @Override
    protected String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "r " + msg;
    }
}

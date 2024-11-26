package protocol;

public final class StatusMessageRunner extends MessageRunner {
    String msg;

    public StatusMessageRunner(String substring) {
        msg = substring;
    }

    @Override
    protected String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "s " + msg;
    }
}

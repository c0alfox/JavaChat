package protocol;

public final class CommandMessage extends Message {
    private final String cmd;

    public CommandMessage(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    @Override
    public String toString() {
        return "c " + cmd;
    }
}

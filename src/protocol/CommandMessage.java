package protocol;

public class CommandMessage extends Message {
    public final String cmd;

    public CommandMessage(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "c " + cmd;
    }
}

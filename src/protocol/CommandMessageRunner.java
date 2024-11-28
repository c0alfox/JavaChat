package protocol;

public final class CommandMessageRunner extends MessageRunner {
    private final String cmd;

    public CommandMessageRunner(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public void client() {

    }

    @Override
    public String toString() {
        return "c " + cmd;
    }
}

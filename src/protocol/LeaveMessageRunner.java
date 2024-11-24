package protocol;

public final class LeaveMessageRunner extends MessageRunner {
    public LeaveMessageRunner(String word) {
    }

    @Override
    protected String serverNoId() {
        return "";
    }
}

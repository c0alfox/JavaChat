package protocol;

public final class LeaveMessageRunner extends MessageRunner {
    String uname;

    public LeaveMessageRunner(String uname) {
        this.uname = uname;
    }

    @Override
    protected String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "l " + uname;
    }
}

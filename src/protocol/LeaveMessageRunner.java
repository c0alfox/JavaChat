package protocol;

public final class LeaveMessageRunner extends MessageRunner {
    String uname;

    public LeaveMessageRunner(String uname) {
        this.uname = uname;
    }

    @Override
    public void server() {

    }

    @Override
    public String toString() {
        return "l " + uname;
    }
}

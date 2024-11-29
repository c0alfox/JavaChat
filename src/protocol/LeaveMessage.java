package protocol;

public final class LeaveMessage extends Message {
    String uname;

    public LeaveMessage(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "l " + uname;
    }
}

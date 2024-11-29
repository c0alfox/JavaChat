package protocol;

public final class LeaveMessage extends Message {
    final String uname;

    public LeaveMessage(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return uname;
    }

    @Override
    public String toString() {
        return "l " + uname;
    }
}

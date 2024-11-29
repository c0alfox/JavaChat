package protocol;

public class LeaveMessage extends Message {
    public final String uname;

    public LeaveMessage(String uname) {
        this.uname = uname;
    }

    @Override
    public String toString() {
        return "l " + uname;
    }
}

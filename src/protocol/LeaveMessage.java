package protocol;

public final class LeaveMessage extends Message {
    String uname;

    public LeaveMessage(String uname) {
        this.uname = uname;
    }

    @Override
    private String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "l " + uname;
    }
}

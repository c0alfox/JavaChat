package protocol;

public final class MsgMessage extends Message {
    String uname;
    String msg;

    public MsgMessage(String uname, String msg) {
        this.uname = uname;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "m " + uname + " " + msg;
    }
}

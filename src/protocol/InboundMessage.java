package protocol;

public class InboundMessage extends Message {
    final String uname;
    final String msg;
    final boolean isPrivate;

    public InboundMessage(String uname, String msg, boolean isPrivate) {
        this.uname = uname;
        this.msg = msg;
        this.isPrivate = isPrivate;
    }

    public String getUname() {
        return uname;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return (isPrivate ? "p" : "i") + " " + uname + " " + msg;
    }
}

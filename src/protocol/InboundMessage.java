package protocol;

public class InboundMessage extends Message {
    public final String uname;
    public final String msg;
    public final boolean isPrivate;

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

    public boolean getPrivate() {
        return isPrivate;
    }

    @Override
    public String toString() {
        return (isPrivate ? "p" : "i") + " " + uname + " " + msg;
    }
}

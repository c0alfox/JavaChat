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

    public static InboundMessage server(String msg) {
        return new InboundMessage("<SERVER>", msg, true);
    }

    @Override
    public String toString() {
        return (isPrivate ? "p" : "i") + " " + uname + " " + msg;
    }
}

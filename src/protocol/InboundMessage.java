package protocol;

public class InboundMessage extends Message {
    public final String uname;
    public final String msg;

    public InboundMessage(String uname, String msg) {
        this.uname = uname;
        this.msg = msg;
    }

    public static InboundMessage server(String msg) {
        return new InboundMessage("<SERVER>", msg);
    }

    @Override
    public String toString() {
        return "i " + uname + " " + msg;
    }
}

package protocol;

public class OutboundMessage extends Message {
    public final String msg;

    public OutboundMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "o " + msg;
    }
}

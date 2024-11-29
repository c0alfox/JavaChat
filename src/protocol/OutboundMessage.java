package protocol;

public class OutboundMessage extends Message {
    final String msg;

    public OutboundMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "o " + msg;
    }
}

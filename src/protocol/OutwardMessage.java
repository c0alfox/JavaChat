package protocol;

public class OutwardMessage extends Message {
    final String msg;

    public OutwardMessage(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}

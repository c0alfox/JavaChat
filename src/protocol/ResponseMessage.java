package protocol;

public class ResponseMessage extends Message {
    public final String msg;

    public ResponseMessage(String substring) {
        msg = substring;
    }

    @Override
    public String toString() {
        return "r " + msg;
    }
}

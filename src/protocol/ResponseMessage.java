package protocol;

import java.util.Optional;

public class ResponseMessage extends Message {
    public final String msg;

    public ResponseMessage(String substring) {
        msg = substring;
    }

    public ResponseMessage() {
        this.msg = "OK";
    }

    @Override
    public String toString() {
        return "r " + msg;
    }

    public Optional<String> toOptional() {
        return msg.equals("OK") ? Optional.empty() : Optional.of(msg);
    }
}

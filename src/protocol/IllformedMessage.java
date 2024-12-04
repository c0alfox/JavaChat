package protocol;

public class IllformedMessage extends Message {
    public final String message;

    public IllformedMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

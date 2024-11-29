package protocol;

public final class ResponseStatusMessage extends Message {
    String msg;

    public ResponseStatusMessage(String substring) {
        msg = substring;
    }

    @Override
    private String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "r " + msg;
    }
}

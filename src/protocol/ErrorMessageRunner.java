package protocol;

public final class ErrorMessageRunner extends MessageRunner {
    String msg;

    public ErrorMessageRunner(String substring) {
        msg = substring;
    }

    @Override
    public void server() {

    }

    @Override
    public String toString() {
        return "e " + msg;
    }
}

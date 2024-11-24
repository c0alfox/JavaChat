package protocol;

public final class ErrorMessageRunner extends MessageRunner {
    public ErrorMessageRunner(String substring) {
        super();
    }

    @Override
    protected String serverNoId() {
        return "";
    }
}

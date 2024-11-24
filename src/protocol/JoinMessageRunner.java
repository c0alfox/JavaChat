package protocol;

public final class JoinMessageRunner extends MessageRunner {
    public JoinMessageRunner(String word, String word1) {
    }

    @Override
    protected String serverNoId() {
        return "";
    }
}

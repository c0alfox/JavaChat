package protocol;

public final class MsgMessageRunner extends MessageRunner {
    String uname;
    String msg;

    public MsgMessageRunner(String uname, String msg) {
        this.uname = uname;
        this.msg = msg;
    }

    @Override
    public void client() {

    }

    @Override
    protected String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "m " + uname + " " + msg;
    }
}

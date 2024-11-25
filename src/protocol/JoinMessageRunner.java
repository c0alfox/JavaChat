package protocol;

public final class JoinMessageRunner extends MessageRunner {
    String uname;
    String color;

    public JoinMessageRunner(String uname, String color) {
        this.uname = uname;
        this.color = color;
    }

    @Override
    public void server() {

    }

    @Override
    public String toString() {
        return "j " + uname + " " + color;
    }
}

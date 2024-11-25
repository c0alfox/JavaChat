package protocol;

public final class UpdateMessageRunner extends MessageRunner {
    String uname;
    String color;

    public UpdateMessageRunner(String color, String uname) {
        this.uname = uname;
        this.color = color;
    }

    @Override
    public void server() {

    }

    @Override
    public String toString() {
        return "u " + uname + " " + color;
    }
}

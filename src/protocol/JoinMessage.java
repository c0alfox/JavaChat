package protocol;

public class JoinMessage extends Message {
    public final String uname;
    public final String color;

    public JoinMessage(String uname, String color) {
        this.uname = uname;
        this.color = color;
    }

    @Override
    public String toString() {
        return "j " + uname + " " + color;
    }
}

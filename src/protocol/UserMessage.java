package protocol;

public class UserMessage extends Message {
    public final String uname;
    public final String color;

    public UserMessage(String uname, String color) {
        this.uname = uname;
        this.color = color;
    }

    @Override
    public String toString() {
        return "u " + uname + " " + color;
    }
}

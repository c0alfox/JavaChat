package protocol;

public class PrivateMessage extends Message {
    public final String uname;
    public final String color;
    public final String msg;

    public PrivateMessage(String uname, String color, String msg) {
        this.uname = uname;
        this.color = color;
        this.msg = msg;
    }

    public static PrivateMessage server(String msg) {
        return new PrivateMessage("<SERVER>", "NULL", msg);
    }

    @Override
    public String toString() {
        return "p " + uname + " " + color + " " + msg;
    }
}

package protocol;

public final class JoinMessage extends Message {
    String uname;
    String color;

    public JoinMessage(String uname, String color) {
        this.uname = uname;
        this.color = color;
    }

    @Override
    private String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "j " + uname + " " + color;
    }
}

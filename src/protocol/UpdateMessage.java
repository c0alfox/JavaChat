package protocol;

public final class UpdateMessage extends Message {
    final String uname;
    final String color;

    public UpdateMessage(String uname, String color) {
        this.uname = uname;
        this.color = color;
    }

    public String getUname() {
        return uname;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "u " + uname + " " + color;
    }
}

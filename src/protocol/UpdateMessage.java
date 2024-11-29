package protocol;

public final class UpdateMessage extends Message {
    String uname;
    String color;

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
    private String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "u " + uname + " " + color;
    }
}

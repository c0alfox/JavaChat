package protocol;

public final class UpdateMessageRunner extends MessageRunner {
    String uname;
    String color;

    public UpdateMessageRunner(String uname, String color) {
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
    protected String serverNoId() {
        return "";
    }

    @Override
    public String toString() {
        return "u " + uname + " " + color;
    }
}

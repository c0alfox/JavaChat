package protocol;

public class AddChannelMessage extends Message {
    public final String channelName;

    public AddChannelMessage(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "a " + channelName;
    }
}

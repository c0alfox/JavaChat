package protocol;

public class DeleteChannelMessage extends Message {
    public final String channelName;

    public DeleteChannelMessage(String channelName) {
        this.channelName = channelName;
    }

    @Override
    public String toString() {
        return "d " + channelName;
    }
}

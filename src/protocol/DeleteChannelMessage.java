package protocol;

public class DeleteChannelMessage extends Message {
    public final String channelName;

    public DeleteChannelMessage(String channelName) {
        this.channelName = channelName;
    }
}

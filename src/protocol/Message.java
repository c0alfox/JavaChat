package protocol;

public abstract class Message {
    public static Message create(String payload) {
        payload = payload.strip();
        if (payload.isEmpty()) {
            return new IllformedMessage(payload);
        }

        String msgString = payload.substring(2);
        String[] words = msgString.split(" ");

        switch (payload.charAt(0)) {
            case 'u': {
                if (words.length != 2) {
                    return new IllformedMessage(payload);
                }

                return new UserMessage(words[0], words[1]);
            }

            case 'r':
                return new ResponseMessage(msgString);

            case 'c':
                return new CommandMessage(msgString);

            case 'j': {
                if (words.length != 2) {
                    return new IllformedMessage(payload);
                }

                return new JoinMessage(words[0], words[1]);
            }

            case 'l': {
                if (words.length != 1) {
                    return new IllformedMessage(payload);
                }

                return new LeaveMessage(words[0]);
            }

            case 'i': {
                if (words.length < 2) {
                    return new IllformedMessage(payload);
                }

                return new InboundMessage(words[0],
                        msgString.substring(msgString.indexOf(' '))
                );
            }

            case 'p': {
                if (words.length < 3) {
                    return new IllformedMessage(payload);
                }

                return new PrivateMessage(words[0], words[1], (msgString.split(" ", 4))[3]);
            }

            case 'o':
                return new OutboundMessage(msgString);

            case 'a':
                return new AddChannelMessage(msgString);

            case 'd':
                return new DeleteChannelMessage(msgString);

            default:
                return new IllformedMessage(payload);
        }
    }
}
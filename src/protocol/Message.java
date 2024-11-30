package protocol;

public abstract class Message {
    public static Message create(String payload) throws IllformedMessageException {
        if (payload.isEmpty()) {
            throw new IllformedMessageException();
        }

        String msgString = payload.substring(2);
        String[] words = msgString.split(" ");

        switch (payload.charAt(0)) {
            case 'u': {
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new UserMessage(words[0], words[1]);
            }

            case 'r':
                return new ResponseMessage(msgString);

            case 'c':
                return new CommandMessage(msgString);

            case 'j': {
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new JoinMessage(words[0], words[1]);
            }

            case 'l': {
                if (words.length != 1) {
                    throw new IllformedMessageException();
                }

                return new LeaveMessage(words[0]);
            }

            case 'i':
            case 'p': {
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new InboundMessage(words[0], words[1], payload.charAt(0) == 'p');
            }

            case 'o':
                return new OutboundMessage(msgString);

            case 'a':
                return new AddChannelMessage(msgString);

            case 'd':
                return new DeleteChannelMessage(msgString);

            default:
                throw new IllformedMessageException();
        }
    }

    public static final class IllformedMessageException extends Exception {
    }
}
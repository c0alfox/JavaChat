package protocol;

import java.util.HashMap;

public abstract class Message {
    public static Message create(String payload) throws IllformedMessageException {
        if (payload.isEmpty()) {
            throw new IllformedMessageException();
        }

        String msgString = payload.substring(2);
        String[] words = msgString.split(" ");

        switch (payload.charAt(0)) {
            case 'o':
                return new OutboundMessage(msgString);

            case 'u': {
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new UpdateMessage(words[0], words[1]);
            }

            case 'i':
            case 'p': {
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new InboundMessage(words[0], words[1], payload.charAt(0) == 'p');
            }

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

            case 'd': {
                if (words.length < 1) {
                    throw new IllformedMessageException();
                }

                // TODO: Check case for when N is NaN
                int n = Integer.parseInt(words[0]);
                if (words.length != n * 2 + 1) {
                    throw new IllformedMessageException();
                }

                HashMap<String, String> users = new HashMap<>();
                for (int i = 0; i < n; i++) {
                    users.put(words[i * 2 + 1], words[i * 2 + 2]);
                }

                return new DataMessage(users);
            }

            case 'c':
                return new CommandMessage(payload.substring(1));

            case 's':
                return new SuggestionMessage();

            case 'r':
                return new ResponseStatusMessage(payload.substring(1));

            default:
                throw new IllformedMessageException();
        }
    }

    public static final class IllformedMessageException extends Exception {
    }

    public static final class WrongEnvironmentException extends Exception {
    }
}

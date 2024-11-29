package protocol;

import java.util.HashMap;

public abstract class Message {
    public static Message create(String payload) throws IllformedMessageException {
        if (payload.isEmpty()) {
            throw new IllformedMessageException();
        }

        String msgString = payload.substring(2);

        switch (payload.charAt(0)) {
            case 'o':
                return new OutwardMessage(msgString);

            case 'c':
                return new CommandMessage(payload.substring(1));

            case 'm': {
                int idx = payload.indexOf(' ');
                if (idx < 0) {
                    throw new IllformedMessageException();
                }
                return new MsgMessage(payload.substring(1, idx), payload.substring(idx + 1));
            }

            case 's':
                return new SuggestionMessage();

            case 'j': {
                String[] words = payload.substring(1).split(" ");
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new JoinMessage(words[0], words[1]);
            }

            case 'l': {
                String[] words = payload.substring(1).split(" ");
                if (words.length != 1) {
                    throw new IllformedMessageException();
                }

                return new LeaveMessage(words[0]);
            }

            case 'u': {
                String[] words = payload.substring(1).split(" ");
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new UpdateMessage(words[0], words[1]);
            }

            case 'd': {
                String[] words = payload.substring(1).split(" ");
                if (words.length < 1) {
                    throw new IllformedMessageException();
                }

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

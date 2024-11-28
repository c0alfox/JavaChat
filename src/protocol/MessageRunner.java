package protocol;

import java.util.HashMap;

public sealed abstract class MessageRunner permits CommandMessageRunner, DataMessageRunner, ResponseStatusMessageRunner, JoinMessageRunner, LeaveMessageRunner, MsgMessageRunner, SuggestionMessageRunner, UpdateMessageRunner {
    private int id = 0;

    public static final class IllformedMessageException extends Exception {}
    public static final class WrongEnvironmentException extends Exception {}

    public static MessageRunner create(String payload) throws IllformedMessageException {
        if (payload.isEmpty()) {
            throw new IllformedMessageException();
        }

        switch (payload.charAt(0)) {
            case 'c':
                return new CommandMessageRunner(payload.substring(1));

            case 'm': {
                int idx = payload.indexOf(' ');
                if (idx < 0) {
                    throw new IllformedMessageException();
                }
                return new MsgMessageRunner(payload.substring(1, idx), payload.substring(idx + 1));
            }

            case 's':
                return new SuggestionMessageRunner();

            case 'j': {
                String[] words = payload.substring(1).split(" ");
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new JoinMessageRunner(words[0], words[1]);
            }

            case 'l': {
                String[] words = payload.substring(1).split(" ");
                if (words.length != 1) {
                    throw new IllformedMessageException();
                }

                return new LeaveMessageRunner(words[0]);
            }

            case 'u': {
                String[] words = payload.substring(1).split(" ");
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new UpdateMessageRunner(words[0], words[1]);
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

                return new DataMessageRunner(users);
            }

            case 'r':
                return new ResponseStatusMessageRunner(payload.substring(1));

            default:
                throw new IllformedMessageException();
        }
    }

    public static MessageRunner create(String payload, int id) throws IllformedMessageException {
        MessageRunner ret = MessageRunner.create(payload);
        ret.id = id;
        return ret;
    }

    public void client() throws WrongEnvironmentException {
        throw new WrongEnvironmentException();
    }

    protected String serverNoId() throws WrongEnvironmentException {
        throw new WrongEnvironmentException();
    }

    public String server() throws WrongEnvironmentException {
        if (id == 0) {
            serverNoId();
        }

        return id + serverNoId();
    }
}

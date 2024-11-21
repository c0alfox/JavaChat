import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public sealed abstract class MessageRunner permits CommandMessageRunner, DataMessageRunner, ErrorMessageRunner, JoinMessageRunner, LeaveMessageRunner, MsgMessageRunner, SuggestionMessageRunner, UpdateMessageRunner {
    public static final class IllformedMessageException extends Exception {}

    public static MessageRunner create(String msg) throws IllformedMessageException {
        if (msg.isEmpty()) {
            throw new IllformedMessageException();
        }

        switch (msg.charAt(0)) {
            case 'c':
                return new CommandMessageRunner(msg.substring(1));

            case 'm': {
                int idx = msg.indexOf(' ');
                if (idx < 0) {
                    throw new IllformedMessageException();
                }
                return new MsgMessageRunner(msg.substring(1, idx), msg.substring(idx + 1));
            }

            case 's':
                return new SuggestionMessageRunner();

            case 'j': {
                String[] words = msg.substring(1).split(" ");
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new JoinMessageRunner(words[0], words[1]);
            }

            case 'l': {
                String[] words = msg.substring(1).split(" ");
                if (words.length != 1) {
                    throw new IllformedMessageException();
                }

                return new LeaveMessageRunner(words[0]);
            }

            case 'u': {
                String[] words = msg.substring(1).split(" ");
                if (words.length != 2) {
                    throw new IllformedMessageException();
                }

                return new UpdateMessageRunner(words[0], words[1]);
            }

            case 'd': {
                String[] words = msg.substring(1).split(" ");
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

            case 'e':
                return new ErrorMessageRunner(msg.substring(1));

            default:
                throw new IllformedMessageException();
        }
    }

    public abstract void run();
}

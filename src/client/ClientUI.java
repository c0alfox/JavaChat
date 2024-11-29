package client;

import protocol.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientUI extends JFrame {
    public final ChatPanel chatPanel;
    public final UserPanel userPanel;

    public ClientUI() {
        super("Java Chat");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        while (Client.net == null) {
            try {
                String input = JOptionPane.showInputDialog(null, "Inserisci l'indirizzo del server");
                if (input == null || input.isEmpty()) {
                    int out = JOptionPane.showConfirmDialog(null, "Sicuro di voler uscire?", "Conferma", JOptionPane.YES_NO_OPTION);
                    if (out == JOptionPane.OK_OPTION) {
                        System.exit(1);
                    }
                    continue;
                }

                Client.connect(input.strip());

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null,
                        "Errore nella connessione al server",
                        "ERRORE",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        String uname = "";
        String ucolor = "";
        while (Client.uname == null) {
            UserMessage u = UserCreationDialog.showUserCreationDialog();
            if (u == null) {
                continue;
            }

            Client.uname = u.uname;
            Client.uname = uname = u.uname;
            ucolor = u.color;
            Client.net.send(u.toString(), msg -> { if (msg.isPresent()) System.out.println(msg); });
        }

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                userPanel = new UserPanel(),
                chatPanel = new ChatPanel()
        );
        userPanel.addUser(uname, ucolor);

        add(split);
        setVisible(true);
    }

    public static Color borderColor(Color bgColor) {
        return new Color(0x11FFFFFF & (0xFFFFFFFF - bgColor.getRGB()));
    }
}

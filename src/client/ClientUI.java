package client;

import protocol.ResponseStatusMessageRunner;
import protocol.UpdateMessageRunner;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientUI extends JFrame {
    public static Color invertColor(Color c) {
        return new Color(0xFF000000 | (0xFFFFFFFF - c.getRGB()));
    }

    public ClientUI() {
        super("Java Chat");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        while (Client.c == null) {
            try {
                String input = JOptionPane.showInputDialog(null, "Inserisci l'indirizzo del server");
                if (input == null || input.isEmpty()) {
                    int out = JOptionPane.showConfirmDialog(null, "Sicuro di voler uscire?", "Conferma", JOptionPane.YES_NO_OPTION);
                    if (out == JOptionPane.OK_OPTION) {
                        System.exit(1);
                        return;
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
        Color ucolor = new Color(0);
        while (Client.uname == null) {
            UpdateMessageRunner u = UserCreationDialog.showUserCreationDialog();
            if (u == null) {
                continue;
            }

            Client.uname = uname = u.getUname();
            ucolor = new Color(Integer.parseInt(u.getColor(), 16));
            Client.net.<ResponseStatusMessageRunner>awaitSend(u);
        }

        UserPanel usrPanel = new UserPanel();
        usrPanel.addUser(uname, ucolor);
        usrPanel.addUser(uname, ucolor);
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, usrPanel, new ChatPanel());

        add(split);
        setVisible(true);
    }
}

package client;

import javax.swing.*;
import java.io.IOException;
import protocol.UpdateMessageRunner;

public class ClientUI extends JFrame {
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

        while(Client.uname == null) {
            UpdateMessageRunner u = UserCreationDialog.showUserCreationDialog();
            if (u == null) {
                continue;
            }

            Client.uname = u.getUname();
            Client.send(u);
        }

        add(new ChatPanel());

        setVisible(true);
    }
}

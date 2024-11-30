package client;

import protocol.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientUI extends JFrame {
    public final ChatPanel chatPanel;
    public final UserPanel userPanel;
    boolean suspended;

    public ClientUI() {
        super("Java Chat");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        suspended = false;

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

        while (Client.uname == null) {
            UserMessage u = UserCreationDialog.showUserCreationDialog();
            if (u == null || u.uname.isBlank() || u.uname.isEmpty()) {
                continue;
            }

            Client.net.send(u.toString(), error -> {
                if (error.isEmpty()) {
                    Client.uname = u.uname;
                    Client.ucolor = u.color;
                }
                resume();
            });

            suspend();

            synchronized (this) {
                while (suspended) {
                    try {
                        System.out.println("printinz");
                        wait();
                        System.out.println("printfinz");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                userPanel = new UserPanel(),
                chatPanel = new ChatPanel()
        );
        userPanel.addUser(Client.uname, Client.ucolor);

        add(split);
        setVisible(true);
    }

    public static Color borderColor(Color bgColor) {
        return new Color(0x11FFFFFF & (0xFFFFFFFF - bgColor.getRGB()));
    }

    public void suspend() {
        suspended = true;
    }

    public synchronized void resume() {
        suspended = false;
        notify();
    }
}

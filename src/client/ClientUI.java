package client;

import protocol.UserMessage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientUI extends JFrame {
    public final ChatPanel chatPanel;
    public final UserPanel userPanel;
    public final UserModel userModel;
    boolean suspended;

    public ClientUI() {
        super("Java Chat");

        userModel = new UserModel();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(400, 300));
        setLocationRelativeTo(null);

        suspended = false;

        establishConnection();
        login();

        ResizablePane split = new ResizablePane(
                userPanel = new UserPanel(),
                chatPanel = new ChatPanel(),
                (int) (getWidth() * 0.25)
        );

        userModel.addUserModelListener(userPanel);
        userModel.addUserModelListener(chatPanel);

        split.setMinimumSize(new Dimension(100, 0));

        add(split);
        setVisible(true);
    }

    public static Color borderColor(Color bgColor) {
        return new Color(0x11FFFFFF & (0xFFFFFFFF - bgColor.getRGB()));
    }

    private void establishConnection() {
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
    }

    private void login() {
        while (Client.uname == null) {
            UserMessage u = UserCreationDialog.showUserCreationDialog();
            if (u == null || u.uname.isBlank() || u.uname.isEmpty()) {
                int out = JOptionPane.showConfirmDialog(null, "Sicuro di voler uscire?", "Conferma", JOptionPane.YES_NO_OPTION);
                if (out == JOptionPane.OK_OPTION) {
                    System.exit(1);
                }
                continue;
            }

            Client.net.send(u.toString(), error -> {
                if (error.isEmpty()) {
                    Client.uname = u.uname;
                    Client.ucolor = new Color(Integer.parseInt(u.color, 16));
                } else {
                    JOptionPane.showMessageDialog(null, error.get(), "Errore", JOptionPane.ERROR_MESSAGE);
                }

                resume();
            });

            suspend();

            synchronized (this) {
                while (suspended) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void suspend() {
        suspended = true;
    }

    public synchronized void resume() {
        suspended = false;
        notify();
    }
}

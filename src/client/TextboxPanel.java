package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TextboxPanel extends JPanel implements KeyListener, ActionListener {
    JTextField textField;
    JButton sendButton;
    ChatPanel parent;

    public TextboxPanel(ChatPanel parent) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        this.parent = parent;

        textField = new JTextField();
        textField.addKeyListener(this);
        add(textField);

        sendButton = new JButton("Invia");
        sendButton.addActionListener(this);
        add(sendButton);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        send();
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            send();
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyPressed(KeyEvent keyEvent) {}

    private void send() {
        String txt = textField.getText();

        if (txt == null) {
            System.out.println("Errore: messaggio inviato null");
            return;
        }

        if (txt.isEmpty()) {
            return;
        }

        parent.onMessage("this", txt);
        textField.setText("");
    }
}

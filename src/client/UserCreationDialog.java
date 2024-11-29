package client;

import protocol.UserMessage;

import javax.swing.*;
import java.awt.*;

public class UserCreationDialog {
    static UserMessage showUserCreationDialog() {
        JTextField username = new JTextField();
        JColorChooser color = new JColorChooser(Color.RED);

        final JComponent[] inputs = new JComponent[]{
                new JLabel("Nome Utente: "),
                username,
                new JLabel("Colore: "),
                color
        };

        int result = JOptionPane.showConfirmDialog(null, inputs);
        if (result == JOptionPane.OK_OPTION) {
            String buf = Integer.toHexString(color.getColor().getRGB());
            String colorstring = buf.substring(buf.length() - 6);
            return new UserMessage(username.getText(), colorstring);
        }
        return null;
    }
}

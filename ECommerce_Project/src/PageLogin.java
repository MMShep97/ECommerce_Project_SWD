import javax.swing.*;
import java.awt.*;

public class PageLogin extends JPanel {

    private final JPanel panel;
    private final JButton button;
    private final JTextField usernameField;
    private final JTextField passwordField;

    public PageLogin() {
        panel = new JPanel();
        button = new JButton("LOGIN");
        usernameField = new JTextField("Username");
        passwordField = new JTextField("Password");
        usernameField.setEditable(true);
        passwordField.setEditable(true);
        panel.setLayout(new BorderLayout());
        panel.add(button, BorderLayout.SOUTH);
        panel.add(usernameField, BorderLayout.NORTH);
        panel.add(passwordField, BorderLayout.CENTER);

        add(panel);
    }
}

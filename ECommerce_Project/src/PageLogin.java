import javax.swing.*;
import java.awt.*;

public class PageLogin extends JPanel {

    private final JButton button;
    private final JTextField usernameField;
    private final JTextField passwordField;

    public PageLogin() {
        button = new JButton("LOGIN");
        usernameField = new JTextField("Username");
        passwordField = new JTextField("Password");
        usernameField.setEditable(true);
        passwordField.setEditable(true);
        setLayout(new BorderLayout());
        add(button, BorderLayout.SOUTH);
        add(usernameField, BorderLayout.NORTH);
        add(passwordField, BorderLayout.CENTER);
    }
}

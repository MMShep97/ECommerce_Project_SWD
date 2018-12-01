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
        setLayout(new BorderLayout());
        panel.setSize(new Dimension(100, 100));
        panel.setLayout(new GridLayout(3, 1));
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(button);
        add(panel, BorderLayout.NORTH);
    }
}

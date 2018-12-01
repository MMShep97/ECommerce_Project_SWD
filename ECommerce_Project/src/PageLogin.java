import javax.swing.*;
import java.awt.*;

public class PageLogin extends JPanel {

    private final JPanel panel;
    private final JButton button;
    private final JTextField usernameField;
    private final JTextField passwordField;
    private final JPanel buttonPanel;
    private final JPanel usernamePanel;
    private final JPanel passwordPanel;

    public PageLogin() {
        buttonPanel = new JPanel();
        usernamePanel = new JPanel();
        passwordPanel = new JPanel();
        panel = new JPanel();
        button = new JButton("LOGIN");
        usernameField = new JTextField("Username");
        passwordField = new JTextField("Password");
        usernameField.setEditable(true);
        passwordField.setEditable(true);
        setLayout(new GridLayout(3, 3));
    }
}

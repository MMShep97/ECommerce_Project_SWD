import javax.swing.*;
import java.awt.*;

public class PageLogin extends JPanel {

    private final JPanel panel;
    private final JButton button;

    public PageLogin() {
        panel = new JPanel();
        button = new JButton();

        setLayout(new FlowLayout());
        panel.add(button.add(new JLabel("SHIT")));
        add(panel);
    }
}

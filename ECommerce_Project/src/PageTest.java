import javax.swing.*;
import java.awt.*;

public class PageTest extends JPanel {

    private final JPanel panel;
    private final JButton button;

    public PageTest() {
        panel = new JPanel();
        button = new JButton();

        setLayout(new FlowLayout());
        panel.add(button.add(new JLabel("BITCH!")));
        add(panel);
    }
}

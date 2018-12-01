import javax.swing.*;
import java.awt.*;

public class PageBrowse extends JPanel {

    private final JPanel panel;
    private final JButton button;

    public PageBrowse() {
        panel = new JPanel();
        button = new JButton();

        setLayout(new FlowLayout());
        panel.add(button.add(new JLabel("FUCK")));
        add(panel);

    }
}

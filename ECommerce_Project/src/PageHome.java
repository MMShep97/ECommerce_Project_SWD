import javax.swing.*;
import java.awt.*;

public class PageHome extends JPanel {


    PageHome() {
        JPanel logoPanel = new JPanel();


        setLayout(new BorderLayout(5, 5));








        add(Page.createNavbar(), BorderLayout.NORTH);
        add(logoPanel, BorderLayout.CENTER);
    }
}

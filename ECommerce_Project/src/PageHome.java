import javax.swing.*;
import java.awt.*;

public class PageHome extends JPanel {

    private ECommerceClient client;
    private NavigationBar navBar;

    PageHome(ECommerceClient client, NavigationBar navBar) {
        JPanel logoPanel = new JPanel();
        this.client = client;
        this.navBar = navBar;


        setLayout(new BorderLayout(5, 5));








        add(navBar, BorderLayout.NORTH);
        add(logoPanel, BorderLayout.CENTER);
    }
}

import javax.swing.*;
import java.awt.*;

public class PageHome extends JPanel {


    PageHome() {
        String welcomeMessage = "Hi and welcome to our humble E-Commerce web application! Feel free to shop at your own peril.\n" +
                                "We offer no warranties on our products and no discounts are allowed. Have a nice day and don't come again!";
        JPanel contentPanel = new JPanel();
        JPanel welcomePanel = new JPanel();

        welcomePanel.add(new JLabel(welcomeMessage));
        welcomePanel.setBackground(Color.WHITE);

        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(Page.createLogoPanel());
        contentPanel.add(welcomePanel);

        add(Page.createNavbar(), BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}

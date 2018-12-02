import javax.swing.*;
import java.awt.*;
import static util.PageUtilityMethods.*;

public class PageHome extends JPanel {

    private ECommerceClient client;
    private NavigationBar navBar;



    PageHome(ECommerceClient client, NavigationBar navBar) {
        this.client = client;
        this.navBar = navBar;

        String welcomeMessage = "Hi and welcome to our humble E-Commerce web application! Feel free to shop at your own peril.\n" +
                                "We offer no warranties on our products and no discounts are allowed. Have a nice day and don't come again!";
        JPanel contentPanel = new JPanel();
        JPanel welcomePanel = new JPanel();

        welcomePanel.add(new JLabel(welcomeMessage));
        welcomePanel.setBackground(Color.WHITE);

        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(createLogoPanel());
        contentPanel.add(welcomePanel);

        add(navBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}

import javax.swing.*;
import java.awt.*;
import static util.PageUtilityMethods.*;

/**
 * Acts as the homepage for the E-Commerce web application. This GUI is the first screen the user encounters when connecting
 * to the server. User is greeted with a description of the site and the ability to navigate to other pages through the navbar.
 */
public class PageHome extends JPanel {

    private ECommerceClient client;
    private NavigationBar navBar;

    /**
     * Instantiating a <code>PageHome</code> object will paint the GUI with components that are shown below and be added to
     * the frame
     * @param client -- Each user's information is taken in to be dealt with accordingly
     * @param navBar -- navbar is taken into each page's constructor to be used at the top of each page
     */
    PageHome(ECommerceClient client, NavigationBar navBar) {
        this.client = client;
        this.navBar = navBar;

        String welcomeMessage = "Your one stop shop for all your population surveillance needs";
        JPanel contentPanel = new JPanel();
        JPanel welcomePanel = new JPanel();

        welcomePanel.add(new JLabel(welcomeMessage));
        welcomePanel.setBackground(Color.WHITE);

        setLayout(new BorderLayout(5, 5));
        setBackground(Color.WHITE);

        //Image and description are added to content panel i.e. below navbar
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(createLogoPanel());
        contentPanel.add(welcomePanel);

        //content & navbar added to encapsulating panel to be added to frame
        add(this.navBar, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
}

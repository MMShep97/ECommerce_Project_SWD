import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Creates frame for application -- can pass in 'JPanels' (different pages) to constructor based on what interface developer
 * would like to utilize for startup screen. From there, developer can invoke <code>changePage(JPanel panel)</code> to alter
 * content of pane, essentially changing the current interface, or view.
 */
public class PageDriver extends JFrame {

    //Might have 1 panel, might have 2, more variety
    PageDriver(JPanel panel) {
        add(panel);
        configureDefaultSettings();
    }

    /**
     * Configures basic settings for JFrame such as setting frame size, close operation, and visibility
     */
    public void configureDefaultSettings() {
        final String title = "O B E Y";

//        this.setLayout(new BorderLayout());
        this.setTitle(title);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * Delete old content (panel) from frame, add new content (panel) to frame
     * @param toPage
     */
    public void changePage(JPanel toPage) {
        this.getContentPane().removeAll(); //Deleting content from frame
        this.add(toPage); //Add new page/panel to frame
        this.revalidate();
    }

    public static void main(String [] args) {
        ECommerceClient client = new ECommerceClient("localhost");
        NavigationBar navbar = new NavigationBar(client);
        PageListItem listItemPage = new PageListItem(client, navbar);
        PageBrowse browseSection = new PageBrowse(client, navbar);
//        PageLogin loginSection = new PageLogin(client, navbar);
        PageHome homeSection = new PageHome(client, navbar);
        PageDriver driver = new PageDriver(homeSection);
        driver.pack();
    }
}

import javax.swing.*;
import java.util.concurrent.TimeUnit;

/**
 * Creates frame for application -- can pass in 'JPanels' (different pages) to constructor based on what interface developer
 * would like to utilize for startup screen. From there, developer can invoke <code>changePage(JPanel panel)</code> to alter
 * content of pane, essentially changing the current interface, or view.
 */
public class PageDriver extends JFrame {

    //Throw in desired panels
    PageDriver(JPanel panel1, JPanel panel2) {
        add(panel1);
        add(panel2);
        configureDefaultSettings();
    }

    //Might have 1 panel, might have 2, more variety
    PageDriver(JPanel panel) {
        add(panel);
        configureDefaultSettings();
    }

    public static void main(String [] args) {
        PageBrowse browseSection = new PageBrowse();
        PageLogin loginSection = new PageLogin();
        PageTest testSection = new PageTest();
        PageDriver driver = new PageDriver(browseSection);
        try {Thread.sleep(1300); } catch (InterruptedException ex) {}
        driver.changePage(loginSection);
        try {Thread.sleep(1400); } catch (InterruptedException ex) {}
        driver.changePage(testSection);


    }


    /**
     * Configures basic settings for JFrame such as setting frame size, close operation, and visibility
     */
    public void configureDefaultSettings() {
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
}

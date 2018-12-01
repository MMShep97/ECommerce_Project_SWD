import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PageBrowse extends JPanel {

    private final JPanel listings;
    private final JButton homeButton;
    private final JButton browseButton;
    private final JButton loginButton;

    private final int LISTING_ROWS = 2;
    private final int LISTING_COLUMNS = 6;

    private BufferedImage[] images; //Get images passed in here somehow

    public PageBrowse() {

        setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createTitledBorder("Border Layout"));
        setBackground(Color.WHITE);

        homeButton = new JButton("HOME");
        browseButton = new JButton("BROWSE");
        loginButton = new JButton("LOGIN");

//        navbar.setLayout(new FlowLayout(0, 20, 20));


        listings = new JPanel();
        listings.setLayout(new GridLayout(LISTING_ROWS, LISTING_COLUMNS));
        BufferedImage testImage = loadImage("C:\\Users\\dkelly1\\Desktop\\git\\swd team project\\team23_swd\\ECommerce_Project\\logo\\logo_small.jpg");
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));
        listings.add(createListing(testImage));


        add(createNavbar(), BorderLayout.NORTH);
        add(listings);
        add(createFooter(), BorderLayout.SOUTH);

    }

    public BufferedImage loadImage(String filename) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException ex) {}
        return image;
    }


    public JPanel createFooter() {
//        final JToolBar toolBar = new JToolBar();
//        final JPanel wrapper = new JPanel();
//
//        toolBar.setSize(100, 100);
//
//        wrapper.setBorder(BorderFactory.createLineBorder(Color.BLACK));
//        wrapper.add(toolBar);
//        return wrapper;
        final JPanel wrapper = new JPanel();
        final JButton button = new JButton("Next 12");
        wrapper.add(button);
        return wrapper;
    }

    public JPanel createNavbar() {
        JPanel navbar = new JPanel();
        navbar.setLayout(new GridLayout(1, 1));
        navbar.add(homeButton);
        navbar.add(browseButton);
        navbar.add(loginButton);
        navbar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return navbar;
    }

    public JPanel createListing(BufferedImage image/*, String item, String price, String description*/) {
        final int ROWS_IN_LISTING = 4;
        final int COLS_IN_LISTING = 1;

        JPanel listing = new JPanel();
        JPanel itemPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        listing.setLayout(new GridLayout(ROWS_IN_LISTING, COLS_IN_LISTING));
        listing.setSize(100, 300);
        listing.setBackground(Color.WHITE);
        listing.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        listing.add(new JLabel(new ImageIcon(image)));
//        listing.add
        return listing;
    }

//protected void addButtons(JToolBar toolBar) {
//        JButton button = null;
//
//        //first button
//        button = makeNavigationButton("Back24", PREVIOUS,
//        "Back to previous something-or-other",
//        "Previous");
//        toolBar.add(button);
//
//        //second button
//        button = makeNavigationButton("Up24", UP,
//        "Up to something-or-other",
//        "Up");
//        toolBar.add(button);
//
//        ...//similar code for creating and adding the third button...
//        }
//
//protected JButton makeNavigationButton(String imageName,
//        String actionCommand,
//        String toolTipText,
//        String altText) {
//        //Look for the image.
//        String imgLocation = "images/"
//        + imageName
//        + ".gif";
//        URL imageURL = ToolBarDemo.class.getResource(imgLocation);
//
//        //Create and initialize the button.
//        JButton button = new JButton();
//        button.setActionCommand(actionCommand);
//        button.setToolTipText(toolTipText);
//        button.addActionListener(this);
//
//        if (imageURL != null) {                      //image found
//        button.setIcon(new ImageIcon(imageURL, altText));
//        } else {                                     //no image found
//        button.setText(altText);
//        System.err.println("Resource not found: " + imgLocation);
//        }
//
//        return button;
//        }
}


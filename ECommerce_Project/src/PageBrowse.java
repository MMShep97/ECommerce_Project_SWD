import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static util.ECommerceUtilityMethods.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.MalformedURLException;
import java.net.URL;


public class PageBrowse extends JPanel {

    private JPanel listings;
    private JButton homeButton;
    private JButton browseButton;
    private JButton loginButton;

    private ECommmerceClient client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final int LISTING_ROWS = 2;
    private final int LISTING_COLUMNS = 6;
    private final ButtonListener buttonListener = new ButtonListener();

    private BufferedImage[] images; //Get images passed in here somehow

    public PageBrowse(ECommmerceClient client) {

        this.client = client;

        setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setBackground(Color.WHITE);

        listings = new JPanel();
        listings.setLayout(new GridLayout(LISTING_ROWS, LISTING_COLUMNS));
        BufferedImage testImage = loadImage("http://www.mkyong.com/image/mypic.jpg");
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "JANE DOE"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "SWINGWORKER"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));


        add(createNavbar(), BorderLayout.NORTH);
        add(listings);
        add(createFooter(), BorderLayout.SOUTH);

    }

    public BufferedImage loadImage(String url) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new URL(url));
        } catch (IOException ex) {}
        return image;
    }

    public void populate(Item[] items){
        for(int i = 0; i < items.length; i++){
            BufferedImage testImage = loadImage(items[i].getImageURL());
            listings.add(createListing(testImage, items[i].getName(), items[i].getPrice(), items[i].getSeller()));
        }
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
        final JButton previous = new JButton("Last");
        final JButton next = new JButton("Next");

        //Alter button colors
        previous.setBackground(Color.BLACK);
        previous.setForeground(Color.WHITE);
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        wrapper.add(previous);
        wrapper.add(next);
        return wrapper;
    }

    public JPanel createNavbar() {
        homeButton = new JButton("HOME");
        browseButton = new JButton("BROWSE");
        loginButton = new JButton("LOGIN");
        JPanel navbar = new JPanel();

        //change color of buttons
        homeButton.setBackground(Color.BLACK);
        browseButton.setBackground(Color.BLACK);
        loginButton.setBackground(Color.BLACK);
        homeButton.setForeground(Color.WHITE);
        browseButton.setForeground(Color.WHITE);
        loginButton.setForeground(Color.WHITE);

        navbar.setLayout(new GridLayout(1, 1));
        navbar.add(homeButton);
        navbar.add(browseButton);
        navbar.add(loginButton);
        navbar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return navbar;
    }

    public JPanel createListing(BufferedImage image, String item, double price, String seller) {
        final int ROWS_IN_LISTING = 3;
        final int COLS_IN_LISTING = 1;

        JPanel listing = new JPanel();
        JPanel imageInfo = new JPanel();
        JPanel listingInfo = new JPanel();
        JPanel itemPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel sellerPanel = new JPanel();
        JButton viewItemButton = new JButton("VIEW");
        viewItemButton.addActionListener(buttonListener);
        Font plainStyle = new Font("Courier", Font.PLAIN, 12);

        JLabel itemContent = new JLabel(item);
        JLabel priceContent = new JLabel(Double.toString(price));
        JLabel sellerContent = new JLabel(seller);
        JLabel itemHeader = new JLabel("ITEM: ");
        JLabel priceHeader = new JLabel("PRICE: ");
        JLabel sellerHeader = new JLabel("SELLER: ");
        itemContent.setFont(plainStyle);
        priceContent.setFont(plainStyle);
        sellerContent.setFont(plainStyle);
        itemContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        priceContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sellerContent.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        itemHeader.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        priceHeader.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sellerHeader.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        listing.setLayout(new GridLayout(ROWS_IN_LISTING, COLS_IN_LISTING));
        listing.setMaximumSize(new Dimension(100, 100));
        listing.setBackground(Color.WHITE);
        listing.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        imageInfo.add(new JLabel(new ImageIcon(image)));

        final int INFO_ROWS = 3;
        final int INFO_COLS = 1;

        listingInfo.setLayout(new GridLayout(INFO_ROWS, INFO_COLS));
        listingInfo.add(itemPanel.add(itemHeader));
        listingInfo.add(itemPanel.add(itemContent));
        listingInfo.add(pricePanel.add(priceHeader));
        listingInfo.add(priceContent);
        listingInfo.add(sellerPanel.add(sellerHeader));
        listingInfo.add(sellerContent);

        listing.add(imageInfo);
        listing.add(listingInfo);
        listing.add(viewItemButton);

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

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println("in eh");
            JButton button = (JButton) e.getSource();
            if(button.getText().equals("VIEW")){
                //TODO CALL METHOD TO SET UP ITEM VIEW PAGE IN ECOMMMERCECLIENT
            }
            if(button.getText().equals("HOME")){
                //""
            }
            if(button.getText().equals("BROWSE")){
                disp("in browse if");
                transmit("BROWSE", client.getOutput());
                transmit(client.getPageNum(), client.getOutput());
                transmit(client.getBrowsePageCapacity(), client.getOutput());
                client.getContentPane().removeAll();
                client.add(PageBrowse.this);
            }
            if(button.getText().equals("LOGIN/SIGNUP")){
                //""
            }
        }
    }

}


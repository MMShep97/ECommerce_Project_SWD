import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import static util.PageUtilityMethods.*;

/**
 * User is 'redirected' or displayed the browse page when the browse button featured on the navbar is clicked. This page consists
 * of a set of up to a maximum of 12 items on the screen at any time. Each listing consists of an image, listing content including
 * the item, price, and seller of the item, as specified by the lister (seller/owner of that current item), and finally, a button
 * to view the desired item. The UI design also includes a -last- and -next- button selection near the bottom of the screen, allowing
 * the user to see the next item listings (up to 12)
 */
public class PageBrowse extends JPanel{

    private NavigationBar navBar;
    private JPanel listings;
    private JPanel footer;
    private JButton previous;
    private JButton next;

    private ECommerceClient client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final int LISTING_ROWS = 2;
    private final int LISTING_COLUMNS = 6;
    private final ButtonListener buttonListener = new ButtonListener();

    public PageBrowse(ECommerceClient client, NavigationBar navBar) {

        this.client = client;
        this.navBar = navBar;

        setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setBackground(Color.WHITE);

        listings = new JPanel();
        listings.setLayout(new GridLayout(LISTING_ROWS, LISTING_COLUMNS));

        //initialize swing components
        this.footer = new JPanel();
        previous = new JButton("Last");
        next = new JButton("Next");

        //Alter button colors
        previous.setForeground(Color.BLACK);
        next.setForeground(Color.BLACK);

        //Add Action Listener to footer buttons
        previous.addActionListener(buttonListener);
        next.addActionListener(buttonListener);

        //Configure footer design and add buttons
        footer.setBackground(Color.WHITE);
        footer.add(previous);
        footer.add(next);

        //Add everything to wrapper panel
        add(this.navBar, BorderLayout.NORTH);
        add(listings, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }

    /**
     * Brings in an array list of item instances (first removes everything from the current listings panel) and creates a
     * new listing FOR EACH item in the list. This includes getting the url and loading the image, as well as getting all other
     * necessary item information from the instance and throwing it into the <code>createListing()</code> method to create an encasing
     * panel that includes everything you need to know about each listing. Each individual listing created is added to the encapsulating
     * <code>listings</code> panel
     * @param items -- contains information about each and every item in the "database" held on the server side
     */
    public void populate(ArrayList<Item> items){
        listings.removeAll();

        for (Item item : items) {
            BufferedImage testImage = loadImage(item.getImageURL());
            listings.add(createListing(testImage, item.getName(), item.getPrice(), item.getSeller(), item.getListingID()));
        }
        //Formatting
        add(this.navBar, BorderLayout.NORTH);
        add(listings, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        revalidate();
    }

    /**
     * Creates an individual listing panel that encapsulates every smaller component within it, including an image panel,
     * a listing information content panel, and finally a view button.
     * @param image -- image loaded from item information url
     * @param item -- actual item
     * @param price -- item price
     * @param seller -- seller of item
     * @param listingID -- id of listing for reference to display and for use by server
     * @return listing -- individual listing panel that encapsulates the given item instance's information in a readable visual display
     */
    private JPanel createListing(BufferedImage image, String item, double price, String seller, int listingID) {
        final int ROWS_IN_LISTING = 3;
        final int COLS_IN_LISTING = 1;

        JPanel listing = new JPanel();
        JPanel imageInfo = new JPanel();
        JPanel listingInfo = new JPanel();
        JPanel itemPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel sellerPanel = new JPanel();
        JButton viewItemButton = new JButton("VIEW");
        viewItemButton.setToolTipText(listingID + "");
        viewItemButton.addActionListener(buttonListener);
        Font plainStyle = new Font("Courier", Font.PLAIN, 12);

        //UI/UX changes to list info
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

        //Encapsulating panel configuration
        listing.setLayout(new BorderLayout());
//        listing.setMaximumSize(new Dimension(100, 100));
        listing.setBackground(Color.WHITE);
        listing.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        imageInfo.add(new JLabel(new ImageIcon(image)));

        //Rows & col for panel that encapsulates item information
        final int INFO_ROWS = 3;
        final int INFO_COLS = 1;

        //item information configuration
        JPanel listingInfoWrapper = new JPanel(new BorderLayout());
        listingInfo.setLayout(new GridLayout(INFO_ROWS, INFO_COLS));
        listingInfo.add(itemPanel.add(itemHeader));
        listingInfo.add(itemPanel.add(itemContent));
        listingInfo.add(pricePanel.add(priceHeader));
        listingInfo.add(priceContent);
        listingInfo.add(sellerPanel.add(sellerHeader));
        listingInfo.add(sellerContent);
        listingInfoWrapper.add(listingInfo);

        listing.add(imageInfo, BorderLayout.NORTH);
        listing.add(listingInfoWrapper, BorderLayout.CENTER);
        listing.add(viewItemButton, BorderLayout.SOUTH);

        return listing;
    }

    /**
     * Checks for button clicks by the user and acts accordingly to each individual button.
     */
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton) e.getSource(); //Gets current event source (i.e. component being clicked)

            switch (button.getText()) {
                case "VIEW":
                    try {
                        int listingID = Integer.parseInt(button.getToolTipText());
                        client.sendToServer("VIEW"); //User wants to interact with specific listing, notify server
                        client.sendToServer(listingID); //Notify server of what listing user wants
                    } catch (NumberFormatException n) {}
                    break;
                case "Next":
                    client.incrementPageNum();
                    client.sendToServer("BROWSE"); //Still on browse page
                    client.sendToServer(client.getPageNum());
                    client.sendToServer(client.getBrowsePageCapacity());
                    break;
                case "Last":
                    client.decrementPageNum();
                    client.sendToServer("BROWSE");
                    client.sendToServer(client.getPageNum());
                    client.sendToServer(client.getBrowsePageCapacity());
                    break;
            }
        }
    }

}

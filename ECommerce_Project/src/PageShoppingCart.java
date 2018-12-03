import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static util.PageUtilityMethods.loadImage;

/*
 * An object that represents a shopping cart page, where users can see the items in their cart
 */
public class PageShoppingCart extends JPanel {

    /*
     * client is the ECommerce client that communicates with the server
     * navbar is the NavigationBar object
     * cartItems is a hashmap with keys that are the items in the user's cart and values that are the quantity of that
     *      item in the user's cart
     * removeButtons is a list of JItemButtons. There is one button per item in the cart. pressing a remove button
     *       removes the item from the user's cart
     * cs is the continue shopping button that brings a user back to the browse page
     * ba is the buy all button, the transaction for all items in the user's cart will be attempted
     * buttonListener is our actionlistener object
     */

    private JPanel listings;
    private final int LISTING_ROWS = 2;
    private final int LISTING_COLUMNS = 6;


    private ECommerceClient client;
    private NavigationBar navbar;
    private ConcurrentHashMap<Item, Integer> cartItems;
    private ArrayList<JItemButton> removeButtons = new ArrayList<>();
    private JButton cs = new JButton("Continue Shopping");
    private JButton ba = new JButton("Buy All");
    private ButtonListener buttonListener = new ButtonListener();

    /*
     * constructor takes in and assigns ECommerceClient and NavigationBar objects, and sets up GUI components for
     * shopping cart page. It assingns removeButtons and fills the removeButton list and fills in all the item and price
     * info for items in the cart.
     */
    public PageShoppingCart(ECommerceClient client, NavigationBar navbar) {
        this.client = client;
        this.navbar = navbar;
        this.cartItems = client.getCart();

        listings = new JPanel(new GridLayout(LISTING_ROWS, LISTING_COLUMNS));
        JPanel buttonPanel = new JPanel();

        Iterator<Item> iter = cartItems.keySet().iterator();
        for(int i = 0; i < cartItems.size(); i++) {
            removeButtons.add(new JItemButton("Remove", iter.next()));
            removeButtons.get(i).addActionListener(buttonListener);
        }
        setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setBackground(Color.WHITE);
        Iterator<Item> it = cartItems.keySet().iterator();
        for(int i = 0; i < cartItems.size(); i++){
            Item curItem = it.next();
            BufferedImage image = loadImage(curItem.getImageURL());
            listings.add(createListing(removeButtons.get(i), curItem, image, curItem.getName(), curItem.getPrice(), curItem.getSeller(), curItem.getListingID()));
        }
        cs.addActionListener(buttonListener);
        ba.addActionListener(buttonListener);
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(cs);
        buttonPanel.add(ba);


        listings.setBorder(BorderFactory.createTitledBorder("CART"));

        add(navbar, BorderLayout.NORTH);
        add(listings, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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
    private JPanel createListing(JItemButton removeButton, Item itemObject, BufferedImage image, String item, double price, String seller, int listingID) {
        final int ROWS_IN_LISTING = 3;
        final int COLS_IN_LISTING = 1;

        JPanel listing = new JPanel();
        JPanel imageInfo = new JPanel();
        JPanel listingInfo = new JPanel();
        JPanel itemPanel = new JPanel();
        JPanel pricePanel = new JPanel();
        JPanel sellerPanel = new JPanel();
        JItemButton viewItemButton = new JItemButton("VIEW", itemObject);
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

        JPanel buttonsWrapper = new JPanel(new BorderLayout(5, 5));
        buttonsWrapper.add(viewItemButton, BorderLayout.NORTH);
        buttonsWrapper.add(removeButton, BorderLayout.SOUTH);

        listing.add(imageInfo, BorderLayout.NORTH);
        listing.add(listingInfoWrapper, BorderLayout.CENTER);
        listing.add(buttonsWrapper, BorderLayout.SOUTH);

        return listing;
    }

    /*
     * Implements ActionListener to handle button presses. It handles Remove button presses by removing the object
     * associated with the button, and handles the Continue Shopping and Buy All button presses by returning to the
     * browse page and attempting to process the user's transaction, respectively.
     */
    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton)e.getSource();
            if(button.getText().equals("Remove")){
                JItemButton itemButton = (JItemButton) button;
                //find and remove specific item, update cartItems
                for(int i = 0; i < removeButtons.size(); i++){
                    if(itemButton.equals(removeButtons.get(i))){
                        client.updateCart(itemButton.getItem());
                        continue;
                    }
                }
            }
            else if(button.getText().equals("Continue Shopping")){
                //load browse page
                client.sendToServer("BROWSE");
                client.sendToServer(1);
                client.sendToServer(client.getBrowsePageCapacity());
            }
            else if(button.getText().equals("Buy All")){
                Set<Map.Entry<Item, Integer>> cart = client.getCart().entrySet();

                for(Map.Entry<Item, Integer> entry : cart)
                {
                    client.sendToServer("PURCHASE");
                    client.sendToServer(entry.getKey());
                    client.sendToServer(client.getAccount().getUsername());
                    client.sendToServer(entry.getValue());
                    client.getCart().remove(entry);
                }

            }

            else if (button.getText().equals("VIEW")) {
                //make a view page of an item
                client.getContentPane().removeAll();
                JItemButton itemButton = (JItemButton)button;
                client.add(new PageViewItem(client, navbar, itemButton.getItem()));
                client.revalidate();
            }
        }
    }
}

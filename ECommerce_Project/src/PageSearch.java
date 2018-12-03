import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static util.PageUtilityMethods.loadImage;

/*
 * This class represents a search page. It extends JPanel. We generate a search page object and add it to the
 * ECommerceClient object client, which itself extends JFrame. So it's adding a new page basically to an existing frame
 */
public class PageSearch extends JPanel {

    private JPanel listings;
    private final int LISTING_ROWS = 2;
    private final int LISTING_COLUMNS = 6;

    /*
     * client is the client object that communicates with the server
     * navbar is the NavigationBar object that appears at the top of each screen
     * searchButton is the button users press to search
     * buttonListener is the instance of our button action listener
     * searchField is the text field users enter their query into
     * searchPanel is the panel that holds the search bar, search button, and results
     */
    private ECommerceClient client;
    private NavigationBar navbar;
    private JItemButton searchButton;
    private ButtonListener buttonListener = new ButtonListener();
    private JTextField searchField = new JTextField(40);
    private JPanel searchPanel = new JPanel();

    /*
     * This constructor sets up the search bar page GUI components, short of the results section
     */
    public PageSearch(ECommerceClient client, NavigationBar navbar){
        this.client = client;
        this.navbar = navbar;

        setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setBackground(Color.WHITE);

        listings = new JPanel(new GridLayout(LISTING_ROWS, LISTING_COLUMNS));

        searchButton = new JItemButton("Search", new Item());
        searchButton.addActionListener(buttonListener);

        JPanel searchBarPanel = new JPanel();
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        searchPanel.add(searchBarPanel, BorderLayout.NORTH);

        //Add everything to wrapper panel
        add(navbar, BorderLayout.NORTH);
        add(listings, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);
    }

    /*
     * fetchResults takes in the results array, which is a list of Items that the search turned up, and configures
     * the name and price in a results JPanel that is added to searchPanel. It also adds a View button for each result,
     * that if pressed makes a PageViewItem for the item and displays it on the screen
     */
    public void fetchResults(ArrayList<Item> results){

        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout(5, 10));
        JPanel hitsPanel = new JPanel();
        hitsPanel.setLayout(new GridLayout(results.size(), 2));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(results.size(), 1));
        if(results.isEmpty()){
            hitsPanel.add(new JLabel("No results found"));
        }
        for(Item item : results){
            BufferedImage testImage = loadImage(item.getImageURL());
            listings.add(createListing(item, testImage, item.getName(), item.getPrice(), item.getSeller(), item.getListingID()));
        }
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
    private JPanel createListing(Item itemObject, BufferedImage image, String item, double price, String seller, int listingID) {
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

        listing.add(imageInfo, BorderLayout.NORTH);
        listing.add(listingInfoWrapper, BorderLayout.CENTER);
        listing.add(viewItemButton, BorderLayout.SOUTH);

        return listing;
    }

    /*
     * This class handles presses of the search button and all view buttons of different search results. If a search
     * button is pressed it sends the search query found in the search text field to the server. If any result's view
     * button is pressed a PageViewItem obejct is generated for the result and displayed for the user on the GUI
     */
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JItemButton button = (JItemButton) e.getSource();
            if(button.getText().equals("Search")){
                client.sendToServer("SEARCH");
                client.sendToServer(searchField.getText());
            }
            else{
                //make a view page of an item
                client.getContentPane().removeAll();
                client.add(new PageViewItem(client, navbar, button.getItem()));
                client.revalidate();
            }
        }
    }
}

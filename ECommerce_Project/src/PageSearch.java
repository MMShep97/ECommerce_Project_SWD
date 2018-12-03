import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static util.PageUtilityMethods.loadImage;

public class PageSearch extends JPanel {

    private JPanel listings;
    private final int LISTING_ROWS = 2;
    private final int LISTING_COLUMNS = 6;

    private ECommerceClient client;
    private NavigationBar navbar;
    private JItemButton searchButton;
    private ButtonListener buttonListener = new ButtonListener();
    private JTextField searchField = new JTextField(40);
    private JPanel searchPanel = new JPanel();


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

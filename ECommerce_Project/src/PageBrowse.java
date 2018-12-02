import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import static util.PageUtilityMethods.*;

public class PageBrowse extends JPanel{

    private JPanel listings;
    private JButton homeButton;
    private JButton browseButton;
    private JButton loginButton;
    private NavigationBar navBar;

    private ECommerceClient client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private final int LISTING_ROWS = 2;
    private final int LISTING_COLUMNS = 6;
    private final ButtonListener buttonListener = new ButtonListener();

    private BufferedImage[] images; //Get images passed in here somehow

    public PageBrowse(ECommerceClient client, NavigationBar navBar) {

        this.client = client;
        this.navBar = navBar;

        setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setBackground(Color.WHITE);

        listings = new JPanel();
        listings.setLayout(new GridLayout(LISTING_ROWS, LISTING_COLUMNS));
        BufferedImage testImage = loadImage("https://farm2.staticflickr.com/1410/1385703004_0c7b798b98.jpg");
        String [] urls = {"https://farm2.staticflickr.com/1410/1385703004_0c7b798b98.jpg",
                "http://www.mkyong.com/image/mypic.jpg",
                "http://www.digitalphotoartistry.com/rose1.jpg"
        };

        add(this.navBar, BorderLayout.NORTH);
        add(listings, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }



    public void populate(ArrayList<Item> items){
        listings.removeAll();

        for(int i = 0; i < items.size(); i++){
            BufferedImage testImage = loadImage(items.get(i).getImageURL());
            listings.add(createListing(testImage, items.get(i).getName(), items.get(i).getPrice(), items.get(i).getSeller()));
        }

        removeAll();
        add(this.navBar, BorderLayout.NORTH);
        add(listings, BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }


    public JPanel createFooter() {
        //initialize swing components
        final JPanel wrapper = new JPanel();
        final JButton previous = new JButton("Last");
        final JButton next = new JButton("Next");

        //Alter button colors
        previous.setForeground(Color.BLACK);
        next.setForeground(Color.BLACK);

        wrapper.setBackground(Color.WHITE);
        wrapper.add(previous);
        wrapper.add(next);
        return wrapper;
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
        listing.setLayout(new GridLayout(ROWS_IN_LISTING, COLS_IN_LISTING));
        listing.setMaximumSize(new Dimension(100, 100));
        listing.setBackground(Color.WHITE);
        listing.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        imageInfo.add(new JLabel(new ImageIcon(image)));

        //Rows & col for panel that encapsulates item information
        final int INFO_ROWS = 3;
        final int INFO_COLS = 1;

        //item information configuration
        listingInfo.setLayout(new GridLayout(INFO_ROWS, INFO_COLS));
        listingInfo.add(itemPanel.add(itemHeader));
        listingInfo.add(itemPanel.add(itemContent));
        listingInfo.add(pricePanel.add(priceHeader));
        listingInfo.add(priceContent);
        listingInfo.add(sellerPanel.add(sellerHeader));
        listingInfo.add(sellerContent);

        //add all panels to encapsulating panel
        listing.add(imageInfo);
        listing.add(listingInfo);
        listing.add(viewItemButton);

        return listing;
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton) e.getSource();
            if(button.getText().equals("VIEW"))
            {
                //((JButton) e.getSource()).getParent().getComponent(1).get
            }
        }
    }

}

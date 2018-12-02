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

public class PageBrowse extends JPanel implements Page{

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
        BufferedImage testImage = Page.loadImage("https://farm2.staticflickr.com/1410/1385703004_0c7b798b98.jpg");
        String [] urls = {"https://farm2.staticflickr.com/1410/1385703004_0c7b798b98.jpg",
                "http://www.mkyong.com/image/mypic.jpg",
                "http://www.digitalphotoartistry.com/rose1.jpg"
        };
        listings.add(createListing(testImage, "Enchiladas", 23.42, "JANE DOE"));
        listings.add(createListing(testImage, "Enchiladas", 23.42, "SWINGWORKER"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "JANE DOE"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "SWINGWORKER"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));
//        listings.add(createListing(testImage, "Enchiladas", "23.42", "THESE ENCHILADAS ARE TASTY!"));


        add(Page.createNavbar(), BorderLayout.NORTH);
        add(listings);
        add(createFooter(), BorderLayout.SOUTH);

    }



    public void populate(Item[] items){
        for(int i = 0; i < items.length; i++){
            BufferedImage testImage = Page.loadImage(items[i].getImageURL());
            listings.add(createListing(testImage, items[i].getName(), items[i].getPrice(), items[i].getSeller()));
        }
    }


    public JPanel createFooter() {
        //initialize swing components
        final JPanel wrapper = new JPanel();
        final JButton previous = new JButton("Last");
        final JButton next = new JButton("Next");

        //Alter button colors
        previous.setBackground(Color.BLACK);
        previous.setForeground(Color.WHITE);
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);

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

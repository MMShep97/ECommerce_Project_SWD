import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static util.PageUtilityMethods.loadImage;

/**
 * Displays page of individual item after user clicks on button for specified item
 */
public class PageViewItem extends JPanel
{
    private ECommerceClient client;
    private NavigationBar navBar;
    private Item item;

    private JButton backButton;
    private JButton buyNowButton;
    private JButton addToCartButton;

    /**
     * Initialize GUI components to view current Item
     * @param client
     * @param navBar
     * @param item
     */
    public PageViewItem(ECommerceClient client, NavigationBar navBar, Item item)
    {
        super(new BorderLayout());
        this.client = client;
        this.navBar = navBar;
        this.item = item;

        backButton = new JButton("BACK"); //Allows user to return to browser
        buyNowButton = new JButton("BUY NOW"); //Allows user to buy now
        addToCartButton = new JButton("ADD TO CART"); //Allows user to add item to cart

        backButton.addActionListener(new ViewerButtonListener());
        buyNowButton.addActionListener(new ViewerButtonListener());
        addToCartButton.addActionListener(new ViewerButtonListener());

        JPanel listing = createListing(this.item);
        JPanel buttons = createButtonsBar();

        add(this.navBar, BorderLayout.NORTH);
        add(listing, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    /**
     * Creates a listing panel for the input Item
     * @param item
     * @return
     */
    private JPanel createListing(Item item)
    {
        //Initialize listing fonts
        Font plainStyle = new Font("Courier", Font.PLAIN, 16);
        Font italicStyle = new Font("Courier", Font.ITALIC, 16);

        //Initialize all panels
        JPanel listing = new JPanel(new GridLayout(2,1)); //Parent panel
        JPanel image = new JPanel(); //Image panel
        JPanel listingInfo = new JPanel(new GridLayout(6,2)); //Item info panel

        //Adding image
        image.add(new JLabel(new ImageIcon(loadImage(item.getImageURL()))));
        image.setSize(225,225);

        //Set up listing info
        JLabel listingIDLabel = new JLabel("Listing ID: ");
        listingIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel nameLabel = new JLabel("Item: ");
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel priceLabel = new JLabel("Price: ");
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel sellerLabel = new JLabel("Seller: ");
        sellerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel descriptionLabel = new JLabel("Description: ");
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel quantityLabel = new JLabel("Stock: ");
        quantityLabel.setHorizontalAlignment(SwingConstants.CENTER);

        //Sets font of all labels
        listingIDLabel.setFont(plainStyle);
        nameLabel.setFont(plainStyle);
        priceLabel.setFont(plainStyle);
        sellerLabel.setFont(plainStyle);
        descriptionLabel.setFont(plainStyle);
        quantityLabel.setFont(plainStyle);

        JLabel listingID = new JLabel("["+item.getListingID()+"]");
        listingID.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel name = new JLabel(item.getName());
        name.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel price = new JLabel("$"+item.getPrice());
        price.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel seller = new JLabel(item.getSeller());
        seller.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel description = new JLabel(item.getDescription());
        description.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel quantity = new JLabel(item.getQuantity() + " left");
        quantity.setHorizontalAlignment(SwingConstants.CENTER);

        //Set fonts of all info
        listingID.setFont(plainStyle);
        name.setFont(plainStyle);
        price.setFont(plainStyle);
        seller.setFont(plainStyle);
        description.setFont(italicStyle);
        quantity.setFont(plainStyle);

        //Adds all components to listingInfo panel
        listingInfo.add(listingIDLabel);
        listingInfo.add(listingID);
        listingInfo.add(nameLabel);
        listingInfo.add(name);
        listingInfo.add(priceLabel);
        listingInfo.add(price);
        listingInfo.add(sellerLabel);
        listingInfo.add(seller);
        listingInfo.add(descriptionLabel);
        listingInfo.add(description);
        listingInfo.add(quantityLabel);
        listingInfo.add(quantity);

        //Add image and info to listing
        listing.add(image);
        listing.add(listingInfo);

        //UI/UX changes to list info
        listingID.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        name.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        price.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        seller.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        description.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        quantity.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        listingIDLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nameLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        priceLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        sellerLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        descriptionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        quantityLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        //Encapsulating panel configuration
        listing.setBackground(Color.WHITE);
        listing.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return listing; //Return listing panel for this Item
    }

    /**
     * Creates panel for all 3 buttons
     * @return
     */
    private JPanel createButtonsBar()
    {
        //Panel to hold buttons
        JPanel buttons = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        buttons.add(backButton, gbc);
        gbc.gridx++;
        buttons.add(buyNowButton, gbc);
        gbc.gridx++;
        buttons.add(addToCartButton, gbc);

        return buttons;
    }

    /**
     * Button listener for all buttons on view page
     */
    private class ViewerButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JButton button = (JButton) e.getSource();

            //Determine which button was pressed
            if(button.equals(backButton))
            {
                //Return user to browse page
                client.sendToServer("BROWSE");
                client.sendToServer(client.getPageNum());
                client.sendToServer(client.getBrowsePageCapacity());
            }
            else
            {
                //If not logged in cannot buy or add to cart
                if(client.getAccount() == null)
                {
                    //Redirect to login page
                    client.getContentPane().removeAll();
                    PageLogin loginPg = new PageLogin(client, navBar);
                    loginPg.requireLoginSignUp();
                    client.add(loginPg);
                    client.revalidate();
                }
                else
                {
                    if(button.equals(buyNowButton))
                    {
                        //Purchase item in viewer
                        client.sendToServer("PURCHASE");
                        client.sendToServer(PageViewItem.this.item);
                        client.sendToServer(client.getAccount().getUsername());
                        client.sendToServer(1);
                    }
                    else if(button.equals(addToCartButton))
                    {
                        //Add item in viewer to the cart
                        client.addToCart(PageViewItem.this.item);
                    }
                }

            }
        }
    }
}

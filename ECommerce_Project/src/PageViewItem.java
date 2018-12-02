import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static util.PageUtilityMethods.loadImage;

public class PageViewItem extends JPanel
{
    private ECommerceClient client;
    private NavigationBar navBar;
    private Item item;

    private JButton backButton;
    private JButton buyNowButton;
    private JButton addToCartButton;

    public PageViewItem(ECommerceClient client, NavigationBar navBar, Item item)
    {
        super(new BorderLayout());
        this.client = client;
        this.navBar = navBar;
        this.item = item;

        backButton = new JButton("BACK");
        buyNowButton = new JButton("BUY NOW");
        addToCartButton = new JButton("ADD TO CART");

        backButton.addActionListener(new ViewerButtonListener());
        buyNowButton.addActionListener(new ViewerButtonListener());
        addToCartButton.addActionListener(new ViewerButtonListener());

        JPanel listing = createListing(this.item);
        JPanel buttons = createButtonsBar();

        add(this.navBar, BorderLayout.NORTH);
        add(listing, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private JPanel createListing(Item item)
    {
        Font plainStyle = new Font("Courier", Font.PLAIN, 16);
        Font italicStyle = new Font("Courier", Font.ITALIC, 16);

        JPanel listing = new JPanel(new GridLayout(2,1));
        JPanel image = new JPanel();
        JPanel listingInfo = new JPanel(new GridLayout(6,2));

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

        listingID.setFont(plainStyle);
        name.setFont(plainStyle);
        price.setFont(plainStyle);
        seller.setFont(plainStyle);
        description.setFont(italicStyle);
        quantity.setFont(plainStyle);

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

        return listing;
    }

    private JPanel createButtonsBar()
    {
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

    private class ViewerButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JButton button = (JButton) e.getSource();

            if(button.equals(backButton))
            {
                client.sendToServer("BROWSE");
                client.sendToServer(client.getPageNum());
                client.sendToServer(client.getBrowsePageCapacity());
            }
            else
            {
                if(client.getAccount() == null)
                {
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
                        client.sendToServer("PURCHASE");
                        client.sendToServer(PageViewItem.this.item);
                    }
                    else if(button.equals(addToCartButton))
                    {
                        client.addToCart(PageViewItem.this.item);
                    }
                }

            }
        }
    }
}

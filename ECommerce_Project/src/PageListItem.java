import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static util.PageUtilityMethods.loadImage;

/**
 * Page for adding a listing from a seller
 */
public class PageListItem extends JPanel {

    private final JTextField urlField;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField descriptionField;
    private final JTextField quantityField;
    private final JLabel urlLabel;
    private final JLabel nameLabel;
    private final JLabel priceLabel;
    private final JLabel descriptionLabel;
    private final JLabel quantityLabel;
    private final JButton submitButton;

    private ECommerceClient client;
    private NavigationBar navBar;

    /**
     * Initializes page and stores client and navBar instances
     * @param clientObject
     * @param navBar
     */
    public PageListItem(ECommerceClient clientObject, NavigationBar navBar) {

        this.client = clientObject;
        this.navBar = navBar;

        final int FIELD_COLUMNS = 20;
        urlField = new JTextField(FIELD_COLUMNS);
        nameField = new JTextField(FIELD_COLUMNS);
        priceField = new JTextField(FIELD_COLUMNS);
        descriptionField = new JTextField(FIELD_COLUMNS);
        quantityField = new JTextField(FIELD_COLUMNS);
        urlLabel = new JLabel("Image (URL format):");
        nameLabel = new JLabel("Name of Item (orange):");
        priceLabel = new JLabel("Listing Price (7.29):");
        descriptionLabel = new JLabel("Item Description (oranges):");
        quantityLabel = new JLabel("Quantity for sale (14):");
        submitButton = new JButton("SUBMIT");
        submitButton.addActionListener(new SellListener());


        JPanel wrapper = new JPanel();
        setLayout(new BorderLayout(5, 5));

        wrapper.add(createlistItemPanel());

        add(this.navBar, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }

    /**
     * Structures the panel fields using GridBagLayout
     * @return
     */
    public JPanel createlistItemPanel() {
        JPanel listItemPanel = new JPanel();

        listItemPanel.setLayout(new GridBagLayout());
        listItemPanel.setBackground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        listItemPanel.add(urlLabel, gbc);
        gbc.gridy++;
        listItemPanel.add(nameLabel, gbc);
        gbc.gridy++;
        listItemPanel.add(priceLabel, gbc);
        gbc.gridy++;
        listItemPanel.add(descriptionLabel, gbc);
        gbc.gridy++;
        listItemPanel.add(quantityLabel, gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        listItemPanel.add(urlField, gbc);
        gbc.gridy++;
        listItemPanel.add(nameField, gbc);
        gbc.gridy++;
        listItemPanel.add(priceField, gbc);
        gbc.gridy++;
        listItemPanel.add(descriptionField, gbc);
        gbc.gridy++;
        listItemPanel.add(quantityField, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        listItemPanel.add(submitButton, gbc);

        return listItemPanel;
    }

    /**
     * Button listener for the add listing button, validates new entry, and sends to server to be added to inventory
     */
    private class SellListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                if(loadImage(urlField.getText()) != null) //Valid url
                {
                    double price = Double.parseDouble(priceField.getText());
                    if(price < 0) throw new NumberFormatException("PRICE MUST BE NON-NEGATIVE");
                    //Try to create new item from the user input information
                    Item newListing = new Item(0, nameField.getText(), price,
                            client.getAccount().getUsername(), descriptionField.getText(), urlField.getText(),
                            Integer.parseInt(quantityField.getText()) > 0 ? Integer.parseInt(quantityField.getText()): 1);
                    client.sendToServer("ADD LISTING");
                    client.sendToServer(newListing);
                    urlField.setText("SUCCESS");
                    nameField.setText("SUCCESS");
                    priceField.setText("SUCCESS");
                    descriptionField.setText("SUCCESS");
                    client.revalidate();
                }
                else
                {
                    //Invalid url message
                    client.getContentPane().removeAll();
                    client.add(PageListItem.this);
                    urlField.setText("Invalid image url");
                    client.revalidate();
                }
            }
            catch(NumberFormatException n) //Ensure valid price and quantity
            {
                //Error message for invalid price or quantity
                client.getContentPane().removeAll();
                client.add(PageListItem.this);
                System.out.println(n.getCause().toString());
                priceField.setText("Invalid number format");
                client.revalidate();
            }
        }
    }
}

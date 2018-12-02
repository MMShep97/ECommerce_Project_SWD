import javax.swing.*;
import java.awt.*;

public class PageListItem extends JPanel {

    private final JTextField urlField;
    private final JTextField nameField;
    private final JTextField priceField;
    private final JTextField descriptionField;
    private final JLabel urlLabel;
    private final JLabel nameLabel;
    private final JLabel priceLabel;
    private final JLabel descriptionLabel;
    private final JButton submitButton;

    private ECommerceClient client;
    private NavigationBar navbar;

    public PageListItem(ECommerceClient clientObject, NavigationBar navBar) {

        this.client = clientObject;
        this.navbar = navBar;

        final int FIELD_COLUMNS = 6;
        urlField = new JTextField(FIELD_COLUMNS);
        nameField = new JTextField(FIELD_COLUMNS);
        priceField = new JTextField(FIELD_COLUMNS);
        descriptionField = new JTextField(FIELD_COLUMNS);
        urlLabel = new JLabel("Image (URL format):");
        nameLabel = new JLabel("Name of Item (orange):");
        priceLabel = new JLabel("Listing Price (7.29):");
        descriptionLabel = new JLabel("Item Description (oranges):");
        submitButton = new JButton("SUBMIT");


        JPanel wrapper = new JPanel();
        JPanel listingWrapper = new JPanel();
        setLayout(new BorderLayout(5, 5));

        wrapper.add(createlistItemPanel());

        add(navbar, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }

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

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        listItemPanel.add(submitButton, gbc);

        return listItemPanel;
    }
}
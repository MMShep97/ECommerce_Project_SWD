import javax.swing.*;
import java.awt.*;

public class PageListItem extends JPanel {

    private ECommmerceClient client;
    private NavigationBar navbar;

    public PageListItem(ECommmerceClient clientObject, NavigationBar navBar) {

        this.client = clientObject;
        this.navbar = navBar;


        JPanel wrapper = new JPanel();
        JPanel listingWrapper = new JPanel();
        setLayout(new BorderLayout(5, 5));

        final int INFO_ROWS = 4;
        final int INFO_COLS = 1;

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
        listItemPanel.add(new JLabel("Image (URL format):"), gbc);
        gbc.gridy++;
        listItemPanel.add(new JLabel("Name of Item (orange):"), gbc);
        gbc.gridy++;
        listItemPanel.add(new JLabel("Listing Price (7.29):"), gbc);
        gbc.gridy++;
        listItemPanel.add(new JLabel("Item Description (oranges):"), gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        listItemPanel.add(new JTextField(10), gbc);
        gbc.gridy++;
        listItemPanel.add(new JTextField(10), gbc);
        gbc.gridy++;
        listItemPanel.add(new JTextField(10), gbc);
        gbc.gridy++;
        listItemPanel.add(new JTextField(10), gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        listItemPanel.add(new JButton("Login"), gbc);
        gbc.gridx++;
        listItemPanel.add(new JButton("Cancel"), gbc);

        return listItemPanel;
    }
}

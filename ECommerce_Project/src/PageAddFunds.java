import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static util.PageUtilityMethods.createLogoPanel;

/*
 * Represents a page where users add funds to their accounts. extends JPanel. Is added to ECommerceClient object in
 * GUI to display the page
 */
public class PageAddFunds extends JPanel
{
    /*
     * client is the ECommerceClient object used to communicate with the server, and also functions as the frame that
     *      holds this PageAddFunds object
     * navBar is the NavigationBar object in this GUI
     * inputFundsField is the field where users input the amount of funding they want to add to their account
     * addFundsButton is a JButton users press to actually add the funds. There is an actionlistener attached to it
     */
    private ECommerceClient client;
    private NavigationBar navBar;
    private JTextField inputFundsField;
    private JButton addFundsButton;

    /*
     * Constructor configures the PageAddFunds panel with inputFundsField and addFundsButton
     * @param client the ECommerceClient object passed in and assigned to client
     * @param navBar the NavigationBar object passed in and assigned to NavBar
     */
    public PageAddFunds(ECommerceClient client, NavigationBar navBar)
    {
        super(new BorderLayout());
        setBackground(Color.WHITE);
        this.client = client;
        this.navBar = navBar;
        inputFundsField = new JTextField(15);
        addFundsButton = new JButton("ADD FUNDS");
        addFundsButton.addActionListener(new AddFundsButtonListener());

        JPanel wrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        wrapper.add(createLogoPanel());
        gbc.gridy++;
        wrapper.add(inputFundsField, gbc);
        gbc.gridy++;
        wrapper.add(addFundsButton, gbc);

        add(navBar, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }

    /*
     * if funds were unable to be added, reports an error message
     */
    public void unsuccessful()
    {
        inputFundsField.setText("An Error Has Occurred");
    }

    /*
     * The action listener class for the addFundsButton. Overrides actionPerformed, communicates with server telling it
     * to add specified amount of funds to user's account
     */
    private class AddFundsButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                double credits = Double.parseDouble(inputFundsField.getText());

                client.sendToServer("ADD CREDITS");
                client.sendToServer(credits);
                client.sendToServer(client.getAccount().getUsername());
            }
            catch (NumberFormatException n)
            {
                inputFundsField.setText("Invalid number entered");
            }

        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static util.PageUtilityMethods.createLogoPanel;

public class PageAddFunds extends JPanel
{
    private ECommerceClient client;
    private NavigationBar navBar;
    private JTextField inputFundsField;
    private JButton addFundsButton;

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

    public void unsuccessful()
    {
        inputFundsField.setText("An Error Has Occurred");
    }

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

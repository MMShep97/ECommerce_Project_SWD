import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PageAddFunds extends JPanel
{
    private ECommerceClient client;
    private NavigationBar navBar;
    private JTextField inputFundsField;
    private JButton addFundsButton;

    public PageAddFunds(ECommerceClient client, NavigationBar navBar)
    {
        super(new BorderLayout());
        this.client = client;
        this.navBar = navBar;
        inputFundsField = new JTextField(15);
        addFundsButton = new JButton("ADD FUNDS");
        addFundsButton.addActionListener(new AddFundsButtonListener());

        add(inputFundsField, BorderLayout.CENTER);
        add(addFundsButton, BorderLayout.SOUTH);
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

                client.sendToServer("ADD FUNDS");
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

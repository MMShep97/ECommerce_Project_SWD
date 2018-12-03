import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;

/**
 * Instantiated one time within the main method (navBar fixed to the top is created)
 * and thrown into each new page created/redirected to. Allows for the same <code>actionListener</code> to handle the navigation
 * through pages within the site based on the user's decision on which button to click to display a new page.
 */
public class NavigationBar extends JPanel
{
    //Navigation components
    private JButton homeButton = new JButton("HOME");
    private JButton browseButton = new JButton("BROWSE");
    private JButton sellButton = new JButton("SELL");
    private JButton cartButton = new JButton("CART");
    private JButton searchButton = new JButton("SEARCH");
    private JButton loginButton = new JButton("LOGIN/SIGNUP");
    private JButton addFundsButton = new JButton("ADD FUNDS");
    private ECommerceClient client; //client that is interacting
    private ButtonListener buttonListener = new ButtonListener(); //handles button events to navigate pages

    /**
     * Creates navigation bar, adds handlers to each button to navigate to specific pages, and formatting.
     * @param client -- user who is currently interacting, should be unique to each individual user
     */
    public NavigationBar(ECommerceClient client)
    {
        this.client = client;

        Color foreground = Color.WHITE;
        Color background = Color.BLACK;

        //change foreground for buttons
        homeButton.setForeground(foreground);
        browseButton.setForeground(foreground);
        sellButton.setForeground(foreground);
        cartButton.setForeground(foreground);
        searchButton.setForeground(foreground);
        loginButton.setForeground(foreground);
        addFundsButton.setForeground(foreground);

        //change background for buttons
        homeButton.setBackground(background);
        browseButton.setBackground(background);
        sellButton.setBackground(background);
        cartButton.setBackground(background);
        searchButton.setBackground(background);
        loginButton.setBackground(background);
        addFundsButton.setBackground(background);

        //registering event handlers
        homeButton.addActionListener(buttonListener);
        browseButton.addActionListener(buttonListener);
        sellButton.addActionListener(buttonListener);
        cartButton.addActionListener(buttonListener);
        searchButton.addActionListener(buttonListener);
        loginButton.addActionListener(buttonListener);
        addFundsButton.addActionListener(buttonListener);

        setLayout(new GridLayout(1, 1));
        add(homeButton);
        add(browseButton);
        add(sellButton);
        add(cartButton);
        add(searchButton);
        add(loginButton);
        add(addFundsButton);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    /**
     * Displays current user's username and credit line
     */
    public void loggedIn()
    {
        NumberFormat formatter = new DecimalFormat("#0.00");
        loginButton.setText(client.getAccount().getUsername() + " [" + formatter.format(client.getAccount().getCredit()) + "]");
    }

    /**
     * Displays shopping cart labels (i.e. the amount of items in the cart)
     */
    public void updateCartLabel()
    {
        Collection<Integer> quantities = client.getCart().values();

        int totalItems = 0;

        for(int q : quantities)
        {
            totalItems += q;
        }

        cartButton.setText("CART (" + totalItems + ")");
    }

    /**
     * Gets and returns home button
     * @return -- homeButton
     */
    public JButton getHomeButton()
    {
        return homeButton;
    }

    /**
     * Gets and returns browse button
     * @return -- browseButton
     */
    public JButton getBrowseButton()
    {
        return browseButton;
    }

    /**
     * Gets and returns login button
     * @return -- loginButton
     */
    public JButton getLoginButton()
    {
        return loginButton;
    }

    /**
     * Checks for button clicks by the user and acts accordingly to each individual button. Should send/redirect/display
     * the new page according to which button the user clicked (ex. home button was clicked, takes you to home page)
     */
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton) e.getSource(); //Gets current event source (i.e. component being clicked)

            //HOME BUTTON
            if(button.equals(homeButton)){
                client.getContentPane().removeAll();
                client.add(new PageHome(client, NavigationBar.this));
                client.revalidate();
            }
            //BROWSE BUTTON
            else if(button.equals(browseButton)){
                client.sendToServer("BROWSE");
                client.sendToServer(1);
                client.sendToServer(client.getBrowsePageCapacity());
            }
            //CART BUTTON
            else if(button.equals(cartButton))
            {
                client.getContentPane().removeAll();
                client.add(new PageShoppingCart(client, NavigationBar.this));
                client.revalidate();
            }
            //SEARCH BUTTON
            else if(button.equals(searchButton))
            {
                client.getContentPane().removeAll();
                client.add(new PageSearch(client, NavigationBar.this));
                client.revalidate();
            }
            //LOGIN BUTTON
            else if(button.equals(loginButton)){
                client.getContentPane().removeAll();
                client.add(new PageLogin(client, NavigationBar.this));
                client.revalidate();
            }
            //SELL BUTTON and ADD FUNDS BUTTON
            else
            {
                if(client.getAccount() != null)
                {
                    if(button.equals(sellButton))
                    {
                        client.getContentPane().removeAll();
                        client.add(new PageListItem(client, NavigationBar.this));
                        client.revalidate();
                    }
                    else if(button.equals(addFundsButton))
                    {
                        client.getContentPane().removeAll();
                        client.add(new PageAddFunds(client, NavigationBar.this));
                        client.revalidate();
                    }
                }
                else
                {
                    client.getContentPane().removeAll();
                    PageLogin loginPg = new PageLogin(client, NavigationBar.this);
                    loginPg.requireLoginSignUp();
                    client.add(loginPg);
                    client.revalidate();
                }
            }
        }
    }
}

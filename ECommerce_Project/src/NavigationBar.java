import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class NavigationBar extends JPanel
{
    private JButton homeButton = new JButton("HOME");
    private JButton browseButton = new JButton("BROWSE");
    private JButton sellButton = new JButton("SELL");
    private JButton cartButton = new JButton("CART");
    private JButton searchButton = new JButton("SEARCH");
    private JButton loginButton = new JButton("LOGIN/SIGNUP");
    private ECommerceClient client;
    private ButtonListener buttonListener = new ButtonListener();

    public NavigationBar(ECommerceClient client)
    {
        this.client = client;

        //change color of buttons
        homeButton.setForeground(Color.BLACK);
        browseButton.setForeground(Color.BLACK);
        sellButton.setForeground(Color.BLACK);
        cartButton.setForeground(Color.BLACK);
        searchButton.setForeground(Color.BLACK);
        loginButton.setForeground(Color.BLACK);

        //registering event handlers
        homeButton.addActionListener(buttonListener);
        browseButton.addActionListener(buttonListener);
        sellButton.addActionListener(buttonListener);
        cartButton.addActionListener(buttonListener);
        searchButton.addActionListener(buttonListener);
        loginButton.addActionListener(buttonListener);

        setLayout(new GridLayout(1, 1));
        add(homeButton);
        add(browseButton);
        add(sellButton);
        add(cartButton);
        add(searchButton);
        add(loginButton);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void loggedIn()
    {
        loginButton.setText("Signed in as: " + client.getAccount().getUsername() + " [$" + client.getAccount().getCredit() + "]");
    }

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

    public JButton getHomeButton()
    {
        return homeButton;
    }

    public JButton getBrowseButton()
    {
        return browseButton;
    }

    public JButton getLoginButton()
    {
        return loginButton;
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton) e.getSource();
            if(button.equals(homeButton)){
                client.getContentPane().removeAll();
                client.add(new PageHome(client, NavigationBar.this));
                client.revalidate();
            }
            else if(button.equals(browseButton)){
                client.sendToServer("BROWSE");
                client.sendToServer(1);
                client.sendToServer(client.getBrowsePageCapacity());
            }
            else if(button.equals(sellButton))
            {
                client.getContentPane().removeAll();
                client.add(new PageListItem(client, NavigationBar.this));
                client.revalidate();
            }
            else if(button.equals(cartButton))
            {
                client.getContentPane().removeAll();
                client.add(new PageShoppingCart(client, NavigationBar.this));
                client.revalidate();
            }
            else if(button.equals(searchButton))
            {

            }
            else if(button.equals(loginButton)){
                client.getContentPane().removeAll();
                client.add(new PageLogin(client, NavigationBar.this));
                client.revalidate();
            }
        }
    }
}

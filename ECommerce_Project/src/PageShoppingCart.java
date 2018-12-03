import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/*
 * An object that represents a shopping cart page, where users can see the items in their cart
 */
public class PageShoppingCart extends JPanel {

    /*
     * client is the ECommerce client that communicates with the server
     * navbar is the NavigationBar object
     * cartItems is a hashmap with keys that are the items in the user's cart and values that are the quantity of that
     *      item in the user's cart
     * removeButtons is a list of JItemButtons. There is one button per item in the cart. pressing a remove button
     *       removes the item from the user's cart
     * cs is the continue shopping button that brings a user back to the browse page
     * ba is the buy all button, the transaction for all items in the user's cart will be attempted
     * buttonListener is our actionlistener object
     */
    private ECommerceClient client;
    private NavigationBar navbar;
    private ConcurrentHashMap<Item, Integer> cartItems;
    private ArrayList<JItemButton> removeButtons = new ArrayList<>();
    private JButton cs = new JButton("Continue Shopping");
    private JButton ba = new JButton("Buy All");
    private ButtonListener buttonListener = new ButtonListener();

    /*
     * constructor takes in and assigns ECommerceClient and NavigationBar objects, and sets up GUI components for
     * shopping cart page. It assingns removeButtons and fills the removeButton list and fills in all the item and price
     * info for items in the cart.
     */
    public PageShoppingCart(ECommerceClient client, NavigationBar navbar) {
        JPanel itemPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel cartPanel = new JPanel();
        this.client = client;
        this.navbar = navbar;
        cartItems = client.getCart();
        Iterator<Item> iter = cartItems.keySet().iterator();
        for(int i = 0; i < cartItems.size(); i++){
            removeButtons.add(new JItemButton("Remove", iter.next()));
            removeButtons.get(i).addActionListener(buttonListener);
        }
        setLayout(new BorderLayout(5, 10));
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setBackground(Color.WHITE);
        itemPanel.setLayout(new GridLayout(cartItems.size(), 3));
        Iterator<Item> it = cartItems.keySet().iterator();
        for(int i = 0; i < cartItems.size(); i++){
            Item curItem = it.next();
            itemPanel.add(new JLabel(curItem.getName()));
            itemPanel.add(new JLabel("Price: " + curItem.getPrice()));
            itemPanel.add(removeButtons.get(i));
        }
        cartPanel.setLayout(new BorderLayout());
        cartPanel.add(itemPanel, BorderLayout.CENTER);
        cartPanel.add(new JLabel("Cart:"), BorderLayout.NORTH);
        cs.addActionListener(buttonListener);
        ba.addActionListener(buttonListener);
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(cs);
        buttonPanel.add(ba);
        cartPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(cartPanel, BorderLayout.CENTER);
        add(navbar, BorderLayout.NORTH);
    }

    /*
     * Implements ActionListener to handle button presses. It handles Remove button presses by removing the object
     * associated with the button, and handles the Continue Shopping and Buy All button presses by returning to the
     * browse page and attempting to process the user's transaction, respectively.
     */
    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton)e.getSource();
            if(button.getText().equals("Remove")){
                JItemButton itemButton = (JItemButton) button;
                //find and remove specific item, update cartItems
                for(int i = 0; i < removeButtons.size(); i++){
                    if(itemButton.equals(removeButtons.get(i))){
                        client.updateCart(itemButton.getItem());
                        continue;
                    }
                }
            }
            else if(button.getText().equals("Continue Shopping")){
                //load browse page
                client.sendToServer("BROWSE");
                client.sendToServer(1);
                client.sendToServer(client.getBrowsePageCapacity());
            }
            else if(button.getText().equals("Buy All")){
                Set<Map.Entry<Item, Integer>> cart = client.getCart().entrySet();

                for(Map.Entry<Item, Integer> entry : cart)
                {
                    client.sendToServer("PURCHASE");
                    client.sendToServer(entry.getKey());
                    client.sendToServer(client.getAccount().getUsername());
                    client.sendToServer(entry.getValue());
                    client.getCart().remove(entry);
                }

            }
        }
    }
}

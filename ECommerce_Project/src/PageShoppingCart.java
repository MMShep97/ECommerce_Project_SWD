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

public class PageShoppingCart extends JPanel {

    private ECommerceClient client;
    private NavigationBar navbar;
    private ConcurrentHashMap<Item, Integer> cartItems;
    private ArrayList<JButton> removeButtons = new ArrayList<>();
    private JButton cs = new JButton("Continue Shopping");
    private JButton ba = new JButton("Buy All");
    private ButtonListener buttonListener = new ButtonListener();

    public PageShoppingCart(ECommerceClient client, NavigationBar navbar) {
        JPanel itemPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JPanel cartPanel = new JPanel();
        this.client = client;
        this.navbar = navbar;
        cartItems = client.getCart();
        for(int i = 0; i < cartItems.size(); i++){
            removeButtons.add(new JButton("Remove"));
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

    private class ButtonListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton)e.getSource();
            if(button.getText().equals("Remove")){
                //find and remove specific item, update cartItems
                for(int i = 0; i < removeButtons.size(); i++){
                    if(button.equals(removeButtons.get(i))){
                        Iterator<Map.Entry<Item, Integer>> it = cartItems.entrySet().iterator();
                        for(int j = 0; j < i - 1; j++){
                            it.next();
                        }
                        Map.Entry<Item, Integer> removeEntry = it.next();
                        removeEntry.setValue(removeEntry.getValue() - 1);
                        client.updateCart(removeEntry.getKey(), removeEntry.getValue());
                        cartItems = client.getCart();
                        if(removeEntry.getValue() == 0){
                            removeButtons.remove(i);
                            //todo remove item and price display as well
                        }
                    }
                }

                //todo update this page
            }
            else if(button.getText().equals("Continue Shopping")){
                //load browse page
                client.sendToServer("BROWSE");
                client.sendToServer(1);
                client.sendToServer(client.getBrowsePageCapacity());
            }
            else if(button.getText().equals("Buy All")){
                // attempt to buy and adjust funds

            }
        }
    }
}

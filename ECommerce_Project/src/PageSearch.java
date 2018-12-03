import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
 * This class represents a search page. It extends JPanel. We generate a search page object and add it to the
 * ECommerceClient object client, which itself extends JFrame. So it's adding a new page basically to an existing frame
 */
public class PageSearch extends JPanel {

    /*
     * client is the client object that communicates with the server
     * navbar is the NavigationBar object that appears at the top of each screen
     * searchButton is the button users press to search
     * buttonListener is the instance of our button action listener
     * searchField is the text field users enter their query into
     * searchPanel is the panel that holds the search bar, search button, and results
     */
    private ECommerceClient client;
    private NavigationBar navbar;
    private JItemButton searchButton;
    private ButtonListener buttonListener = new ButtonListener();
    private JTextField searchField = new JTextField(40);
    private JPanel searchPanel = new JPanel();


    /*
     * This constructor sets up the search bar page GUI components, short of the results section
     */
    public PageSearch(ECommerceClient client, NavigationBar navbar){
        this.client = client;
        this.navbar = navbar;
        searchButton = new JItemButton("Search", new Item());
        searchButton.addActionListener(buttonListener);
        setLayout(new BorderLayout(5, 10));
        add(navbar, BorderLayout.NORTH);
        searchPanel.setLayout(new BorderLayout(5, 10));
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout());
        searchBarPanel.add(searchField);
        searchBarPanel.add(searchButton);
        searchPanel.add(searchBarPanel, BorderLayout.NORTH);
        add(searchPanel, BorderLayout.CENTER);
    }

    /*
     * fetchResults takes in the results array, which is a list of Items that the search turned up, and configures
     * the name and price in a results JPanel that is added to searchPanel. It also adds a View button for each result,
     * that if pressed makes a PageViewItem for the item and displays it on the screen
     */
    public void fetchResults(ArrayList<Item> results){
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BorderLayout(5, 10));
        JPanel hitsPanel = new JPanel();
        hitsPanel.setLayout(new GridLayout(results.size(), 2));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(results.size(), 1));
        if(results.isEmpty()){
            hitsPanel.add(new JLabel("No results found"));
        }
        for(Item i:results){
            hitsPanel.add(new JLabel(i.getName()));
            hitsPanel.add(new JLabel(Double.toString(i.getPrice())));
            JItemButton newButton = new JItemButton("View", i);
            newButton.addActionListener(buttonListener);
            buttonsPanel.add(newButton);
        }
        resultsPanel.add(hitsPanel, BorderLayout.CENTER);
        resultsPanel.add(buttonsPanel, BorderLayout.EAST);
        searchPanel.add(resultsPanel, BorderLayout.CENTER);
        revalidate();
    }

    /*
     * This class handles presses of the search button and all view buttons of different search results. If a search
     * button is pressed it sends the search query found in the search text field to the server. If any result's view
     * button is pressed a PageViewItem obejct is generated for the result and displayed for the user on the GUI
     */
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JItemButton button = (JItemButton) e.getSource();
            if(button.getText().equals("Search")){
                client.sendToServer("SEARCH");
                client.sendToServer(searchField.getText());
            }
            else{
                //make a view page of an item
                client.getContentPane().removeAll();
                client.add(new PageViewItem(client, navbar, button.getItem()));
                client.revalidate();
            }
        }

    }

}

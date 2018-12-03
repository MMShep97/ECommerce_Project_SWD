import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PageSearch extends JPanel {

    private ECommerceClient client;
    private NavigationBar navbar;
    private JItemButton searchButton;
    private ButtonListener buttonListener = new ButtonListener();
    private JTextField searchField = new JTextField(40);
    private JPanel searchPanel = new JPanel();


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
            buttonsPanel.add(new JItemButton("View", i));
        }
        resultsPanel.add(hitsPanel, BorderLayout.CENTER);
        resultsPanel.add(buttonsPanel, BorderLayout.EAST);
        searchPanel.add(resultsPanel, BorderLayout.CENTER);
        revalidate();
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JItemButton button = (JItemButton) e.getSource();
            if(button.getText().equals("Search")){
                System.out.println("in search reactor");
                client.sendToServer("SEARCH");
                System.out.println("yarp");
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

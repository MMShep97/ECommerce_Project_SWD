import javax.swing.*;

public class Daniel_test2 {
    public static void main(String[] args){
        ECommerceClient client = new ECommerceClient("a");
        client.initializeGUI();
        NavigationBar navbar = new NavigationBar(client);
        PageLogin pl = new PageLogin(client, navbar);
        client.getContentPane().removeAll();
        client.add(pl);
        client.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static util.ECommerceUtilityMethods.*;

public class NavigationBar extends JPanel
{
    private JButton homeButton = new JButton("HOME");
    private JButton browseButton = new JButton("BROWSE");
    private JButton loginButton = new JButton("LOGIN/SIGNUP");
    private ECommerceClient client;
    private ButtonListener buttonListener = new ButtonListener();

    public NavigationBar(ECommerceClient client)
    {
        this.client = client;

        //change color of buttons
        homeButton.setBackground(Color.BLACK);
        browseButton.setBackground(Color.BLACK);
        loginButton.setBackground(Color.BLACK);
        homeButton.setForeground(Color.WHITE);
        browseButton.setForeground(Color.WHITE);
        loginButton.setForeground(Color.WHITE);

        //registering event handlers
        homeButton.addActionListener(buttonListener);
        browseButton.addActionListener(buttonListener);
        loginButton.addActionListener(buttonListener);

        setLayout(new GridLayout(1, 1));
        add(homeButton);
        add(browseButton);
        add(loginButton);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
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
            if(button.getText().equals("HOME")){
                //""
            }
            else if(button.getText().equals("BROWSE")){
                transmit("BROWSE", client.getOutput());
                transmit(client.getPageNum(), client.getOutput());
                transmit(client.getBrowsePageCapacity(), client.getOutput());
                client.getContentPane().removeAll();
                client.add(NavigationBar.this);
            }
            else if(button.getText().equals("LOGIN/SIGNUP")){
                //""
            }
        }
    }
}

import javax.swing.*;
import java.awt.*;

public class NavigationBar extends JPanel
{
    private JButton homeButton = new JButton("HOME");
    private JButton browseButton = new JButton("BROWSE");
    private JButton loginButton = new JButton("LOGIN/SIGNUP");

    public NavigationBar()
    {
        //change color of buttons
        homeButton.setBackground(Color.BLACK);
        browseButton.setBackground(Color.BLACK);
        loginButton.setBackground(Color.BLACK);
        homeButton.setForeground(Color.WHITE);
        browseButton.setForeground(Color.WHITE);
        loginButton.setForeground(Color.WHITE);

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
}

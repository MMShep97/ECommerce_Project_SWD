import javax.swing.*;
import java.awt.*;

import static util.PageUtilityMethods.*;

public class PageLogin extends JPanel{

    private ECommerceClient client;
    private NavigationBar navBar;


    public PageLogin(ECommerceClient client, NavigationBar navBar) {
        this.navBar = navBar;
        setBackground(Color.WHITE);
        final JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(0,1));
        topPanel.add(this.navBar);
        add(topPanel);
        add(createLoginPanel());
        add(createLogoPanel());
    }

    public JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel();

        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBackground(Color.GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridy++;
        loginPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        loginPanel.add(new JTextField(10), gbc);
        gbc.gridy++;
        loginPanel.add(new JTextField(10), gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(new JButton("Login"), gbc);
        gbc.gridx++;
        loginPanel.add(new JButton("Cancel"), gbc);

        return loginPanel;
    }
}

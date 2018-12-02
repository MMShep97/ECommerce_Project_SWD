import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static util.ECommerceUtilityMethods.transmit;
import static util.PageUtilityMethods.*;

public class PageLogin extends JPanel{

    private ECommerceClient client;
    private NavigationBar navBar;


    public PageLogin(ECommerceClient client, NavigationBar navBar) {
        this.navBar = navBar;
        setBackground(Color.WHITE);
        final JPanel topPanel = new JPanel();

        JPanel wrapper = new JPanel();
        JPanel listingWrapper = new JPanel();
        setLayout(new BorderLayout(5, 5));

        final int INFO_ROWS = 4;
        final int INFO_COLS = 1;

        wrapper.add(createLoginPanel());

        add(navBar, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
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

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton) e.getSource();
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static util.ECommerceUtilityMethods.transmit;
import static util.PageUtilityMethods.*;

public class PageLogin extends JPanel{

    private ECommerceClient client;
    private NavigationBar navBar;
    private JButton loginButton;
    private JButton cancelButton;
    private JTextField utf;
    private JTextField ptf;
    private ButtonListener buttonListener = new ButtonListener();


    public PageLogin(ECommerceClient client, NavigationBar navBar) {
        this.navBar = navBar;
        setBackground(Color.WHITE);
        final JPanel topPanel = new JPanel();

        loginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");
        utf = new JTextField(10);
        ptf = new JTextField(10);
        loginButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);

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
        loginPanel.add(utf, gbc);
        gbc.gridy++;
        loginPanel.add(ptf, gbc);

        gbc.gridx = 1;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(loginButton, gbc);
        gbc.gridx++;
        loginPanel.add(cancelButton, gbc);

        return loginPanel;
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println("in action listener");
            JButton button = (JButton)e.getSource();
            if(button.getText().equals("Login")){
                System.out.println("inside the thing");
                System.out.println(utf.getText());
                System.out.println(ptf.getText());
                transmit(utf.getText(), client.getOutput());
                transmit(ptf.getText(), client.getOutput());
                client.getContentPane().removeAll();
                client.add(PageLogin.this);
            }
            else if(button.getText().equals("Cancel")){
                //TODO
            }

        }
    }
}

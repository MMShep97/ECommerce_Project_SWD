import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PageLogin extends JPanel{

    private ECommerceClient client;
    private NavigationBar navBar;
    private JButton loginButton;
    private JButton signupButton;
    private JButton cancelButton;
    private JTextField utf;
    private JPasswordField ptf;
    private ButtonListener buttonListener = new ButtonListener();


    public PageLogin(ECommerceClient client, NavigationBar navBar) {
        this.client = client;
        this.navBar = navBar;
        setBackground(Color.WHITE);

        loginButton = new JButton("Login");
        signupButton = new JButton("Sign-up");
        cancelButton = new JButton("Cancel");
        utf = new JTextField(10);
        ptf = new JPasswordField(10);
        loginButton.addActionListener(buttonListener);
        signupButton.addActionListener(buttonListener);
        cancelButton.addActionListener(buttonListener);

        JPanel wrapper = new JPanel();
        setLayout(new BorderLayout(5, 5));

        wrapper.add(createLoginPanel());

        add(navBar, BorderLayout.NORTH);
        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
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

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(loginButton, gbc);
        gbc.gridx++;
        loginPanel.add(signupButton, gbc);
        gbc.gridx++;
        loginPanel.add(cancelButton, gbc);

        return loginPanel;
    }

    public void invalidUsernameOrPassword()
    {
        client.getContentPane().removeAll();
        client.add(PageLogin.this);
        client.revalidate();
        utf.setText("Invalid");
        ptf.setText("Username or Password");
    }

    public void usernameAlreadyExists()
    {
        client.getContentPane().removeAll();
        client.add(PageLogin.this);
        client.revalidate();
        utf.setText("Username already exists");
    }

    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton)e.getSource();
            if(button.getText().equals("Login")){
                client.sendToServer("LOGIN");
                client.sendToServer(utf.getText());
                client.sendToServer(ptf.getPassword());
                client.getContentPane().removeAll();
                client.add(PageLogin.this);
                client.revalidate();
            }
            else if(button.getText().equals("Sign-up"))
            {
                client.sendToServer("SIGN-UP");
                client.sendToServer(utf.getText());
                client.sendToServer(ptf.getPassword());
                client.getContentPane().removeAll();
                client.add(PageLogin.this);
                client.revalidate();
            }
            else if(button.getText().equals("Cancel")){
                client.getContentPane().removeAll();
                client.add(new PageHome(client, navBar));
                client.revalidate();
            }

        }
    }
}

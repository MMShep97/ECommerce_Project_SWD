import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User is 'redirected' or displayed a new page when the login/signup button featured on the navbar is clicked. This page
 * creates the login page when instantiated and put into the frame. Login or signup information is input by the user, sent to the server
 * to check validity / if the user already exists on signup, and sent back to the client. The GUI is updated accordingly to show
 * the user the next correct steps to create.
 */
public class PageLogin extends JPanel{

    private ECommerceClient client;
    private NavigationBar navBar;
    private JButton loginButton;
    private JButton signupButton;
    private JButton cancelButton;
    private JTextField utf;
    private JPasswordField ptf;
    private ButtonListener buttonListener = new ButtonListener();

    /**
     * The GUI is comprised of the navbar and content. The content includes two JLabels (username and password) and two
     * JTextFields which includes the ability for user input based on those JLabel's. An actionListener is used to check
     * for user input on each button (login, signup, and cancel) in which the buttons each perform separate tasks.
     * @param client
     * @param navBar
     */
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

    /**
     * Creates login panel to be used within THIS constructor
     * @return -- login panel
     */
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

    /**
     * Checks for button clicks by the user and acts accordingly to each individual button.
     */
    private class ButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e){
            JButton button = (JButton)e.getSource();

            //LOGIN SECTION
            if(button.getText().equals("Login")){
                client.sendToServer("LOGIN"); //Tell server which command is occurring
                client.sendToServer(utf.getText()); //Grab user inputted username
                client.sendToServer(ptf.getPassword()); //Grab user inputted password
                client.getContentPane().removeAll(); //Removes current content from interface
                client.add(PageLogin.this); //Adds updated page to interface
                client.revalidate(); //Necessary when updating interface display
            }
            //SIGN-UP SECTION
            else if(button.getText().equals("Sign-up"))
            {
                client.sendToServer("SIGN-UP");
                client.sendToServer(utf.getText());
                client.sendToServer(ptf.getPassword());
                client.getContentPane().removeAll();
                client.add(PageLogin.this);
                client.revalidate();
            }
            //CANCEL SECTION
            else if(button.getText().equals("Cancel")){
                client.getContentPane().removeAll();
                client.add(new PageHome(client, navBar));
                client.revalidate();
            }

        }
    }
}

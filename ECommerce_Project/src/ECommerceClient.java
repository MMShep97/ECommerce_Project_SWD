import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static util.ECommerceUtilityMethods.*;

public class ECommerceClient extends JFrame
{
    //Network members
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    private String host;
    private boolean hasAccount = false;
    private Account myAccount;
    private ConcurrentHashMap<Item,Integer> cart = new ConcurrentHashMap<>();

    //GUI components/parameters
    private int pageNum = 1;
    private int browsePageCapacity = 12;
    private NavigationBar navBar;
    private PageHome homePage;
    private PageBrowse pb;
    private PageViewItem viewItemPage;
    private PageShoppingCart cartPage;
    private PageLogin loginPage;

    public ECommerceClient(String host)
    {
        super("O B E Y");
        this.host = host;
    }

    /**
     * this method must be called, or pages will not be initialized
     */
    public void initializeGUI(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        navBar = new NavigationBar(this);
        homePage = new PageHome(this, navBar);
        this.add(homePage);
        this.setVisible(true);
    }

    public void runClient()
    {
        try
        {
            //Connect to server
            disp("Attempting to connect");
            client = new Socket(InetAddress.getLocalHost(), ECommerceServer.PORT);
            disp("Connected to: " + client.getInetAddress().getHostName());

            //Get I/O streams
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());

            //Interact with server
            interact();
        }
        catch (EOFException eof)
        {
            transmit("TERMINATE", output);
            disp("Terminated connection");
        }
        catch (IOException ioe)
        {
            transmit("TERMINATE", output);
            ioe.printStackTrace();
        }
        finally
        {
            transmit("TERMINATE", output);
            disp("Closing connection");
            closeConnections(output, input, client);
        }
    }

    private void interact()
    {
        boolean interact = true;

        do
        {
            try
            {
                String dataType = (String) input.readObject();

                if(dataType != null)
                {
                    switch (dataType)
                    {
                        case "CONNECTION":
                            String connectionResult = (String) input.readObject();
                            disp(connectionResult);
                            break;
                        case "SIGN-UP":
                            String signUpResult = (String) input.readObject();
                            if (signUpResult.equals("Sign-up successful"))
                            {
                                myAccount = (Account) input.readObject();
                                successfulLoginSignUp();
                            }
                            else
                            {
                                hasAccount = false;
                                loginPage = new PageLogin(this, navBar);
                                loginPage.usernameAlreadyExists();
                                add(loginPage);
                                revalidate();
                            }
                            break;
                        case "LOGIN":
                            String loginResult = (String) input.readObject();
                            if (loginResult.equals("Login successful"))
                            {
                                myAccount = (Account) input.readObject();
                                successfulLoginSignUp();
                            }
                            else
                            {
                                hasAccount = false;
                                loginPage = new PageLogin(this, navBar);
                                loginPage.invalidUsernameOrPassword();
                                add(loginPage);
                                revalidate();
                            }
                            break;
                        case "BROWSE":
                            String browseResult = (String) input.readObject();

                            if(browseResult.equals("VALID"))
                            {
                                ArrayList<Item> listings = new ArrayList<>();

                                for (int i = 0; i < browsePageCapacity; i++)
                                {
                                    Object item = input.readObject();
                                    if (item != null)
                                    {
                                        listings.add((Item) item);
                                    }
                                }

                                pb = new PageBrowse(this, navBar);
                                pb.populate(listings);
                                getContentPane().removeAll();
                                add(pb);
                                revalidate();
                            }
                            else
                            {
                                getContentPane().removeAll();
                                add(pb);
                                revalidate();
                            }
                            break;
                        case "VIEW":
                            String viewResult = (String) input.readObject();
                            Item viewing;

                            if (viewResult.equals("Valid item"))
                            {
                                viewing = (Item) input.readObject();
                            }
                            else
                            {
                                viewing = null;
                            }

                            if(viewing != null)
                            {
                                getContentPane().removeAll();
                                viewItemPage = new PageViewItem(this, navBar, viewing);
                                add(viewItemPage);
                                revalidate();
                            }
                            break;
                        case "PURCHASE":
                            String purchaseResult = (String) input.readObject();

                            if (purchaseResult.equals("Purchase made successfully"))
                            {

                            }
                            else if (purchaseResult.contains("There are no longer enough"))
                            {
                                //TODO: Update Client GUI (insufficient inventory)
                            }
                            else
                            {
                                //TODO: Update Client GUI (account doesn't have enough credits)
                            }
                            break;
                        case "ADD CREDITS":
                            String addCreditsResult = (String) input.readObject();

                            if (addCreditsResult.equals("Credits added successfully"))
                            {
                                //TODO: Update Client GUI (credited to account)
                            }
                            else
                            {
                                //TODO: Update Client GUI (invalid account)
                            }
                            break;
                        case "ADD LISTING":
                            String addListingResult = (String) input.readObject();

                            if (addListingResult.contains("added to listings"))
                            {
                                transmit("VIEW", output);
                                //transmit();
                            }
                            break;
                        case "TERMINATE":
                            interact = false;
                            break;
                        default:
                            disp("Unknown data transmission from server");
                    }
                }
            }
            catch (ClassNotFoundException cnf)
            {
                disp("Unknown data type received from server");
            }
            catch (EOFException eof)
            {
                disp("Connection terminated");
                interact = false;
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }

        }while(interact);
    }

    public void addToCart(Item item)
    {
        if(cart.putIfAbsent(item, 1) != null)
        {
            int quantity = cart.get(item);
            cart.put(item, quantity+1);
        }

        navBar.updateCartLabel();
        getContentPane().removeAll();
        cartPage = new PageShoppingCart(this, navBar);
        add(cartPage);
        revalidate();
    }

    public void sendToServer(Serializable data) { transmit(data, output);}

    public Account getAccount()
    {
        return hasAccount ? this.myAccount : null;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getBrowsePageCapacity() {
        return browsePageCapacity;
    }

    public void incrementPageNum() { pageNum++; }

    public void decrementPageNum() { pageNum--; }

    /**
     * @return the cart HashMap with keys of type Item and values of type Integer
     */
    public ConcurrentHashMap<Item, Integer> getCart() {
        return cart;
    }

    public void updateCart(Item item){
        cart.remove(item);
        getContentPane().removeAll();
        navBar.updateCartLabel();
        add(new PageShoppingCart(this, navBar));
        revalidate();
    }

    private void successfulLoginSignUp()
    {
        hasAccount = true;
        homePage = new PageHome(this, navBar);
        getContentPane().removeAll();
        add(homePage);
        navBar.loggedIn();
        revalidate();
    }

}

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

/**
 * E-Commerce Client allows a user to navigate the E-Commerce Server through a GUI
 */
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

    public ECommerceClient(String host)
    {
        super("O B E Y");
        this.host = host;
    }

    /**
     * Initializes GUI components to starting values
     */
    private void initializeGUI(){
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);
        navBar = new NavigationBar(this);
        homePage = new PageHome(this, navBar);
        this.add(homePage);
        this.setVisible(true);
    }

    /**
     * Establishes connection with Server, interacts with the Server, and closes connection when
     * finished or an error occurs
     */
    public void runClient()
    {
        initializeGUI();

        try
        {
            //Connect to server
            disp("Attempting to connect");
            client = new Socket(InetAddress.getByName(host), ECommerceServer.PORT);
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

    /**
     * Receives and handles input from the Server through the input stream
     */
    private void interact()
    {
        boolean interact = true; //Interact until done, or server terminates connection

        do
        {
            try
            {
                String dataType = (String) input.readObject(); //First input will always identify what data is to come

                //As long as data is not null, interpret
                if(dataType != null)
                {
                    PageLogin loginPage;
                    switch (dataType)
                    {
                        case "CONNECTION": //Initial connection successful message
                            String connectionResult = (String) input.readObject();
                            disp(connectionResult);
                            break;
                        case "SIGN-UP":
                            String signUpResult = (String) input.readObject(); //Explains if server was able to create account
                            if (signUpResult.equals("Sign-up successful"))
                            {
                                myAccount = (Account) input.readObject(); //Assign myAccount to newly created account sent by server
                                successfulLoginSignUp(); //Update login/sign up button and return to homepage
                            }
                            else
                            {
                                //Display error message in login page
                                hasAccount = false;
                                loginPage = new PageLogin(this, navBar);
                                loginPage.usernameAlreadyExists();
                                add(loginPage);
                                revalidate();
                            }
                            break;
                        case "LOGIN":
                            String loginResult = (String) input.readObject(); //Explains if server was able to login
                            if (loginResult.equals("Login successful"))
                            {
                                myAccount = (Account) input.readObject(); //Assign myAccount to Account server sent from its registry
                                successfulLoginSignUp(); //Update login/sign up button and return to homepage
                            }
                            else
                            {
                                //Display error message in login page
                                hasAccount = false;
                                loginPage = new PageLogin(this, navBar);
                                loginPage.invalidUsernameOrPassword();
                                add(loginPage);
                                revalidate();
                            }
                            break;
                        case "BROWSE":
                            String browseResult = (String) input.readObject(); //Explains if successful

                            if(browseResult.equals("VALID"))
                            {
                                //Create ArrayList to hold Items from server's inventory
                                ArrayList<Item> listings = new ArrayList<>();

                                //Read all of input from server
                                for (int i = 0; i < browsePageCapacity; i++)
                                {
                                    Object item = input.readObject();
                                    if (item != null)
                                    {
                                        listings.add((Item) item);
                                    }
                                }

                                //Add PageBrowser instance to content pane
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
                            String viewResult = (String) input.readObject(); //Explains if successful
                            Item viewing;

                            if (viewResult.equals("Valid item"))
                            {
                                viewing = (Item) input.readObject(); //Read Item in question from input stream
                            }
                            else
                            {
                                viewing = null;
                            }

                            //If server was able to send Item, open it in a PageViewItem instance
                            if(viewing != null)
                            {
                                getContentPane().removeAll();
                                PageViewItem viewItemPage = new PageViewItem(this, navBar, viewing);
                                add(viewItemPage);
                                revalidate();
                            }
                            break;
                        case "PURCHASE":
                            String purchaseResult = (String) input.readObject(); //Explains result of attempted purchase

                            if (purchaseResult.equals("Purchase made successfully"))
                            {
                                myAccount.makePurchase((double) input.readObject());
                                successfulLoginSignUp();
                            }
                            else if (purchaseResult.contains("There are no longer enough"))
                            {
                                //Display error window
                                JOptionPane.showMessageDialog(this, "Not enough " +
                                        input.readObject() + " left in stock to fulfill order", "OUT OF STOCK",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                            else
                            {
                                //Display error window
                                JOptionPane.showMessageDialog(this, "Insufficient funds to purchase: "
                                        + input.readObject(), "INSUFFICIENT FUNDS", JOptionPane.ERROR_MESSAGE);

                            }
                            break;
                        case "ADD CREDITS":
                            String addCreditsResult = (String) input.readObject(); //Explains if credits were added properly
                            PageAddFunds addFundsPage = new PageAddFunds(this, navBar);

                            if (addCreditsResult.equals("Credits added successfully"))
                            {
                                myAccount.addFunds((double) input.readObject());
                                successfulLoginSignUp(); //Updates navBar and reroutes back to homepage
                            }
                            else
                            {
                                addFundsPage.unsuccessful(); //Display error message on addFundsPage
                            }
                            break;
                        case "ADD LISTING":
                            String addListingResult = (String) input.readObject(); //Explains result of trying to add listing

                            if (addListingResult.contains("added to listings"))
                            {
                                transmit("VIEW", output); //Prompts server to open view page
                                transmit(((Item) input.readObject()).getListingID(), output); //Opens new listing in view page
                            }
                            break;
                        case "SEARCH":
                            int numResults = (int) input.readObject(); //Number of search results
                            ArrayList<Item> results = new ArrayList<>(numResults); //ArrayList to hold result items (if any)
                            for(int i = 0; i  < numResults; i++)
                            {
                                results.add((Item) input.readObject()); //Add each item from input stream to list
                            }

                            //Update search page

                            getContentPane().removeAll();
                            PageSearch searchPage = new PageSearch(this, navBar);
                            searchPage.fetchResults(results);
                            add(searchPage);
                            revalidate();
                            break;
                        case "TERMINATE": //If server terminates connection, done interacting
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

    /**
     * Adds the Item to the Client cart, or adds quantity if already present
     * @param item
     */
    public void addToCart(Item item)
    {
        if(cart.putIfAbsent(item, 1) != null)
        {
            int quantity = cart.get(item);
            cart.put(item, quantity+1);
        }

        navBar.updateCartLabel();
        getContentPane().removeAll();
        PageShoppingCart cartPage = new PageShoppingCart(this, navBar);
        add(cartPage);
        revalidate();
    }

    /**
     * Allows other classes to send data through client instance without needing to call getter for output stream
     * @param data
     */
    public void sendToServer(Serializable data) { transmit(data, output);}

    /**
     * Returns the client's account
     * @return
     */
    public Account getAccount()
    {
        return hasAccount ? this.myAccount : null;
    }

    /**
     * Reutrns client's current page in browser
     * @return
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * Returns client's browsing page capacity
     * @return
     */
    public int getBrowsePageCapacity() {
        return browsePageCapacity;
    }

    /**
     * Advance a page
     */
    public void incrementPageNum() { pageNum++; }

    /**
     * Backtrack a page
     */
    public void decrementPageNum() { if(pageNum > 1) pageNum--; }

    /**
     * @return the cart HashMap with keys of type Item and values of type Integer
     */
    public ConcurrentHashMap<Item, Integer> getCart() {
        return cart;
    }

    /**
     * Updates state of cart
     * @param item
     */
    public void updateCart(Item item){
        cart.remove(item);
        getContentPane().removeAll();
        navBar.updateCartLabel();
        add(new PageShoppingCart(this, navBar));
        revalidate();
    }

    /**
     * After successfully logging in or signing up, updates login/sign-up button to show username and credits
     */
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

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import static util.ECommerceUtilityMethods.*;

public class ECommmerceClient extends JFrame
{
    //Network members
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    private String host;
    private boolean hasAccount = false;
    private JFrame frame;
    private int pageNum = 1; //TODO update page number. it is currently always 1

    private PageBrowse pb;

    private ConcurrentHashMap<Item,Integer> cart = new ConcurrentHashMap<>();

    //GUI components/parameters
    private int browsePageCapacity = 8;

    public ECommmerceClient(String host)
    {
        this.host = host;
        frame = new JFrame("O B E Y");
    }

    /**
     * this method must be called, or pages will not be initialized
     */
    public void initializeGUI(){
        pb = new PageBrowse(this);
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
            disp("Terminated connection");
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally
        {
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

                switch (dataType)
                {
                    case "SIGN-UP":
                        String signUpResult = (String) input.readObject();
                        if(signUpResult.equals("Sign-up successful"))
                        {
                            hasAccount = true;
                            //TODO: Update Client GUI (account created)
                        }
                        else
                        {
                            //TODO: Update Client GUI (try to sign up again / login)
                        }
                        break;
                    case "LOGIN":
                        String loginResult = (String) input.readObject();
                        if(loginResult.equals("Login successful"))
                        {
                            hasAccount = true;
                            //TODO: Update Client GUI (login successful)
                        }
                        else
                        {
                            //TODO: Update Client GUI (invalid username/password)
                        }
                        break;
                    case "BROWSE":
                        Item [] listings = new Item[browsePageCapacity];

                        for(int i = 0; i < browsePageCapacity; i++)
                        {
                            Object item = input.readObject();
                            if(item != null)
                            {
                                listings[i] = (Item) item;
                            }
                        }

                        //TODO: Update Browser Page with listings

                        break;
                    case "PURCHASE":
                        String purchaseResult = (String) input.readObject();

                        if(purchaseResult.equals("Purchase made successfully"))
                        {
                            //TODO: Update Client GUI (purchase successful)
                        }
                        else if(purchaseResult.contains("There are no longer enough"))
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

                        if(addCreditsResult.equals("Credits added successfully"))
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

                        if(addListingResult.contains("added to listings"))
                        {
                            //TODO: Update Client GUI (successfully added to listing)
                        }
                        else
                        {
                            //Should never occur
                            disp("Error adding listing");
                        }
                        break;
                    case "TERMINATE":
                        interact = false;
                        break;
                    default:
                        disp("Unknown data transmission from server");
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


    public ObjectOutputStream getOutput() {
        return output;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getBrowsePageCapacity() {
        return browsePageCapacity;
    }

    //TODO: Display to sepcific GUI area (maybe using invokelater)
    private void toGUI(final String message)
    {

    }

}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static util.ECommerceUtilityMethods.*;

/**
 * E-Commerce Server allows Clients to connect and buy/sell products
 */
public class ECommerceServer extends JFrame
{
    //Headers for csv files
    private final String INVENTORY_HEADER = "LISTING_ID,NAME,PRICE,SELLER,DESCRIPTION,IMAGE_URL,QUANTITY\n";
    private final String ACCOUNT_HEADER = "USERNAME,PASSWORD,CREDITS\n";

    //Network components
    private ServerSocket server;
    private ServerThread[] sockets;
    private ExecutorService executor;
    private ConcurrentHashMap<Integer,Item> inventory = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String,Account> accounts = new ConcurrentHashMap<>();
    private AtomicInteger listingIDS;
    private int connectionID = 1;
    private int num_active_clients = 0;
    public static final int PORT = 23501;

    //GUI Component
    private JButton terminate;

    /**
     * Sets up terminate button and initializes maps from logs
     */
    public ECommerceServer()
    {
        //Initializing terminate button GUI
        super("E-Commerce Server");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //Allows window listener to carry out closing procedure
        addWindowListener(new WindowAdapter() //Add window listener for when window is closing
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                updateLogs(); //Write maps to csv files
                executor.shutdown(); //Shutdown executor service
                dispose(); //Dispose of GUI
                super.windowClosing(e); //Use super windowClosing to finish closing the GUI
                System.exit(0); //End program
            }
        });
        JPanel serverGUI = new JPanel(new BorderLayout());
        terminate = new JButton("TERMINATE");
        terminate.setSize(250,250);
        terminate.addActionListener(new StopServerButtonListener());
        serverGUI.add(terminate, BorderLayout.CENTER);
        add(serverGUI, BorderLayout.CENTER);
        pack();
        setVisible(true);

        //Initializing server back end
        sockets = new ServerThread[100];
        executor = Executors.newFixedThreadPool(100);
        initializeMaps();
    }

    /**
     * Looks for Clients to connect, starts new Clients in their own thread
     */
    public void serve()
    {
        try
        {
            server = new ServerSocket(PORT);
            disp("E-Commerce Server INITIALIZED");

            while(true)
            {
                try
                {
                    sockets[connectionID-1] = new ServerThread(connectionID); //Add new user to sockets
                    sockets[connectionID-1].waitForConnection(); //New user waits for connection with server
                    num_active_clients++; //Increment clients
                    executor.execute(sockets[connectionID-1]); //Run
                }
                catch (EOFException eof)
                {
                    disp("Server terminated connection");
                }
                finally
                {
                    connectionID++;
                }
            }
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
        finally
        {
            updateLogs();
            executor.shutdown();
        }
    }

    /**
     * Initializes concurrency maps from the log csv files
     */
    private void initializeMaps()
    {
        //Try to open inventory log file
        try
        {
            //Inventory
            Scanner invReader = new Scanner(new File("ECommerce_Project/logs/inventoryLog.csv"));
            invReader.nextLine(); //Skip header line

            while (invReader.hasNextLine())
            {
                //Add each item to the inventory map
                String[] parsed = invReader.nextLine().split(",");
                inventory.put(Integer.parseInt(parsed[0]), new Item(Integer.parseInt(parsed[0]), parsed[1], Double.parseDouble(parsed[2]), parsed[3], parsed[4], parsed[5], Integer.parseInt(parsed[6])));
            }

            listingIDS = new AtomicInteger(inventory.size() + 1); //Initialize listingID counter to current value
            invReader.close(); //End reading inventory log csv
        }
        catch (IOException ioe)
        {
            disp("Inventory log not found");
        }

        //Try to open account log file
        try
        {
            //Accounts
            Scanner acctReader = new Scanner(new File("ECommerce_Project/logs/accountLog.csv"));
            acctReader.nextLine(); //Skip header line

            while (acctReader.hasNextLine())
            {
                //Add each account to the accounts map
                String[] parsed = acctReader.nextLine().split(",");
                Account newAcct = new Account(parsed[0], parsed[1]);
                newAcct.addFunds(Double.parseDouble(parsed[2]));
                accounts.put(parsed[0], newAcct);
            }

            acctReader.close(); //End reading account log csv
        }
        catch (IOException ioe)
        {
            disp("Account log not found");
        }
    }

    /**
     * Called before properly shutting down the server, writes maps to appropriate csv logs for storage between server runs
     */
    private void updateLogs()
    {
        //Attempt to open both log files
        try
        {
            FileWriter invWriter = new FileWriter("ECommerce_Project/logs/inventoryLog.csv");
            //Inventory
            invWriter.write(INVENTORY_HEADER); //Header line
            for (Item item : inventory.values())
            {
                invWriter.write(item.toCSVFormat()); //Write each item in inventory to a line in csv
            }

            invWriter.close(); //End writing inventory log
        }
        catch(IOException ioe)
        {
            disp("Inventory log error");
        }

        try
        {
            FileWriter acctWriter = new FileWriter("ECommerce_Project/logs/accountLog.csv");
            //Accounts
            acctWriter.write(ACCOUNT_HEADER); //Header line
            for (Account acct : accounts.values())
            {
                acctWriter.write(acct.toCSVFormat()); //Write each account to a line in csv
            }

            acctWriter.close(); //End writing account log csv
        }
        catch (IOException ioe)
        {
            disp("Account log error");
        }

    }

    /**
     * Inner class handles interactions with a client of the server in its own thread
     */
    private class ServerThread implements Runnable
    {
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket connection;
        private int connectID;

        public ServerThread(int connectID) { this.connectID = connectID; }

        /**
         * Run method implemented from Runnable allows thread to be executed by ExecutorService, running client interactions
         * in own thread
         */
        @Override
        public void run()
        {
            try
            {
                try
                {
                    //Get I/O Streams
                    output = new ObjectOutputStream(connection.getOutputStream());
                    output.flush();
                    input = new ObjectInputStream(connection.getInputStream());

                    //Interact with client
                    interact();

                    //No longer active
                    num_active_clients--;

                }
                catch (EOFException eof)
                {
                    disp(connectID + " terminated connection");
                }
                finally
                {
                    //Close connection and I/O streams
                    disp("Terminating connection " + connectID);
                    disp("Number of connections = " + num_active_clients);
                    closeConnections(output, input, connection);
                }
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        /**
         * Waits for server to connect to the client
         * @throws IOException
         */
        private void waitForConnection() throws IOException
        {
            disp("Waiting for connection " + connectID);
            connection = server.accept();
            disp("Connection " + connectID + " received from: " + connection.getInetAddress().getHostName());
        }

        /**
         * Handles interactions with the client, receives and responds to input from object stream
         */
        private void interact()
        {
            transmit("CONNECTION", output);
            transmit("Connection " + connectID + " successful", output); //Send connection successful message to client
            boolean interact = true; //Interact while client is connected

            //Process client transmissions
            do
            {
                //Read data and respond
                try
                {
                    String dataType = (String) input.readObject(); //Identifies what data will come next
                    String username;
                    Account curAcct;

                    switch (dataType) //Determine what to do from sentinel input
                    {
                        case "SIGN-UP":
                            username = (String) input.readObject();
                            String password = new String((char []) input.readObject());
                            transmit("SIGN-UP", output);
                            if(accounts.putIfAbsent(username, new Account(username, password)) == null) //If null, new account
                            {
                                transmit("Sign-up successful", output);
                                transmit(accounts.get(username), output);
                            }
                            else //Prior account in map returned if username already in use
                            {
                                transmit("Username already exists", output);
                            }
                            break;
                        case "LOGIN":
                            username = (String) input.readObject();
                            String pass = new String((char []) input.readObject());
                            curAcct = accounts.get(username);
                            transmit("LOGIN", output);
                            if(curAcct != null && curAcct.checkPassword(pass)) //Check if account existed and password was correct
                            {
                                transmit("Login successful", output);
                                transmit(accounts.get(username), output);
                            }
                            else
                            {
                                transmit("Invalid username or password", output);
                            }
                            break;
                        case "BROWSE":
                            int pageNumber = (int) input.readObject();
                            int pageCapacity = (int) input.readObject();
                            ArrayList<Item> items = new ArrayList<>(inventory.values());
                            transmit("BROWSE", output);
                            if(pageNumber*pageCapacity <= items.size() + pageCapacity && pageNumber > 0) //Send the inventory items for the current page
                            {
                                transmit("VALID", output);
                                for (int i = ((pageNumber - 1) * pageCapacity); i < pageNumber * pageCapacity; i++)
                                {
                                    transmit(items.get(i), output);
                                    if (i == items.size() - 1)
                                    {
                                        //If not enough items for a full page, transmit the rest of space as null, and exit loop
                                        for (int j = 0; j < pageNumber*pageCapacity - items.size(); j++)
                                        {
                                            transmit(null, output);
                                        }
                                        i = pageNumber * pageCapacity; //exit loop
                                    }
                                }
                            }
                            else
                            {
                                transmit("INVALID PAGE NUMBER", output);
                            }
                            break;
                        case "VIEW":
                            int listID = (int) input.readObject();
                            Item viewing = inventory.get(listID);
                            transmit("VIEW", output);
                            if(viewing != null) //valid item in inventory
                            {
                                transmit("Valid item", output);
                                transmit(viewing, output); //Send item to be viewed to client
                                disp("Connection " + connectID + " viewing " + viewing.getName());
                            }
                            else
                            {
                                transmit("Invalid item requested", output);
                            }
                            break;
                        case "PURCHASE":
                            Item item = (Item) input.readObject();
                            Item inventoryItem = inventory.get(item.getListingID());
                            username = (String) input.readObject();
                            int quantityPurchased = (int) input.readObject();
                            //If insufficient supplies remain in inventory, purchase remaining stores
                            if(inventoryItem.getQuantity() < quantityPurchased) quantityPurchased = inventoryItem.getQuantity();
                            transmit("PURCHASE",output);
                            if(item.equals(inventoryItem)) //Check item is valid
                            {
                                curAcct = accounts.get(username);
                                //If valid account and sufficient funds tied to that account
                                if(curAcct != null && curAcct.makePurchase(item.getPrice()*quantityPurchased))
                                {
                                    if(inventoryItem.purchased(quantityPurchased))
                                    {
                                        inventory.put(inventoryItem.getListingID(), inventoryItem);
                                        transmit("Purchase made successfully", output);
                                        transmit(item.getPrice()*quantityPurchased, output);
                                        disp(quantityPurchased + " " + inventoryItem.getName() + "s purchased by " + username);

                                        //If now out of stock remove from inventory
                                        if(inventoryItem.getQuantity() == 0)
                                        {
                                            inventory.remove(inventoryItem.getListingID());
                                            disp(inventoryItem.getName() + " is now sold out and was removed from the inventory");
                                        }
                                    }
                                    else
                                    {
                                        transmit("There are no longer enough " + inventoryItem.getName() + "s in stock to fulfill the request", output);
                                        transmit(inventoryItem.getName(), output);
                                    }
                                }
                                else
                                {
                                    transmit("Insufficient credits", output);
                                    transmit(inventoryItem.getName(), output);
                                }
                            }
                            break;
                        case "ADD CREDITS":
                            double credits = (double) input.readObject();
                            username = (String) input.readObject();
                            curAcct = accounts.get(username);
                            transmit("ADD CREDITS", output);
                            if(curAcct != null)
                            {
                                curAcct.addFunds(credits);
                                transmit("Credits added successfully", output);
                                transmit(credits, output);
                                disp(credits + " credits added to " + username + "'s account");
                            }
                            else
                            {
                                transmit("Invalid account", output);
                            }
                            break;
                        case "ADD LISTING":
                            Item newItem = (Item) input.readObject();
                            newItem.setListingID(listingIDS.getAndIncrement()); //Update listingID to current value in map
                            inventory.put(newItem.getListingID(), newItem); //Create new listing and add it to the inventory
                            transmit("ADD LISTING", output);
                            transmit(newItem.getName() + "added to listings", output);
                            disp("Added: " + newItem.toString() + " to listings\n\tSeller: " + newItem.getSeller() +
                                    "\n\tQuantity: " + newItem.getQuantity());
                            transmit(inventory.get((listingIDS.get()-1)), output);
                            break;
                        case "SEARCH":
                            String query = (String) input.readObject();
                            Stack<Item> hits = new Stack<>();
                            System.out.println("in SEARCH in server, query is : " + query);

                            //Compares each item in the inventory against the query
                            for(Item i : inventory.values())
                            {
                                if(i.getName().equalsIgnoreCase(query)) //If equal add item to hit list
                                {
                                    hits.push(i);
                                }
                                else //If not compare name to each word in the query
                                {
                                    double similarWords = 0.0;
                                    String [] queryWords = query.toUpperCase().split(" ");
                                    for(String w : queryWords)
                                    {
                                        if(i.getName().toUpperCase().contains(w)) similarWords++;
                                    }

                                    if((similarWords/((double) queryWords.length)) > 0.75) hits.push(i); //If query is 75% similar to name add to hit list
                                }
                            }

                            transmit("SEARCH", output);
                            transmit(hits.size(), output); //Send how many hits are coming
                            while(!hits.empty()) //Send each hit individually
                            {
                                transmit(hits.pop(), output);
                            }
                            break;
                        case "TERMINATE": //Client terminated connection
                            interact = false;
                            break;
                        default:
                            disp("Unknown data transmission from client");
                    }
                }
                catch (ClassNotFoundException cnf)
                {
                    disp("Unknown data type received from client");
                }
                catch (EOFException eof)
                {
                    interact = false;
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }while(interact);
        }
    }

    /**
     * Terminate button listener provides same functionality as windowListener: ensuring logs are updated before closing
     */
    private class StopServerButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            updateLogs();
            executor.shutdown();
            dispose();
            System.exit(0);
        }
    }
}

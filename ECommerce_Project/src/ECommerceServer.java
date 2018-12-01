import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static util.ECommerceUtilityMethods.*;

public class ECommerceServer
{
    private ServerSocket server;
    private ServerThread[] sockets;
    private ExecutorService executor;
    private ConcurrentHashMap<Integer,Item> inventory;
    private ConcurrentHashMap<String,Account> accounts;
    private AtomicInteger listingIDS = new AtomicInteger(1);
    private int connectionID = 1;
    private int num_active_clients = 0;
    public static final int PORT = 23501;

    public ECommerceServer()
    {
        sockets = new ServerThread[100];
        executor = Executors.newFixedThreadPool(100);
        initializeMaps();
    }

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
        }
    }

    private void initializeMaps()
    {
        //Inventory
        Scanner invReader = new Scanner("ECommerce_Project/logs/inventoryLog.csv");
        invReader.nextLine(); //Skip file path line

        while (invReader.hasNextLine())
        {
            String [] parsed = invReader.nextLine().split(",");
            inventory.put(Integer.parseInt(parsed[0]), new Item(Integer.parseInt(parsed[0]), parsed[1], Double.parseDouble(parsed[2]), parsed[3], parsed[4], parsed[5], Integer.parseInt(parsed[6])));
        }

        //Accounts
        Scanner acctReader = new Scanner("ECommerce_Project/logs/accountLog.csv");
        acctReader.nextLine(); //Skip file path line

        while (acctReader.hasNextLine())
        {
            String[] parsed = acctReader.nextLine().split(",");
            Account newAcct = new Account(parsed[0], parsed[1]);
            newAcct.addFunds(Double.parseDouble(parsed[3]));
            accounts.put(parsed[0], newAcct);
        }
    }

    private void updateLogs()
    {
        try
        {
            FileWriter invWriter = new FileWriter("ECommerce_Project/logs/inventoryLog.csv");
            //Inventory
            for (Item item : inventory.values())
            {
                invWriter.append(item.toCSVFormat());
            }

            FileWriter acctWriter = new FileWriter("ECommerce_Project/logs/accountLog.csv");
            //Accounts
            for (Account acct : accounts.values())
            {
                acctWriter.append(acct.toCSVFormat());
            }
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private class ServerThread implements Runnable
    {
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket connection;
        private int connectID;
        private boolean active = false;

        public ServerThread(int connectID) { this.connectID = connectID; }

        @Override
        public void run()
        {
            try
            {
                active = true;
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
                    active = false;

                    closeConnections(output, input, connection);
                }
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        private void waitForConnection() throws IOException
        {
            disp("Waiting for connection " + connectID);
            connection = server.accept();
            disp("Connection " + connectID + " received from: " + connection.getInetAddress().getHostName());
        }

        private void interact()
        {
            transmit("Connection " + connectID + " successful", output); //Send connection successful message to client
            boolean interact = true;

            //Process client transmissions
            do
            {
                //Read data and respond
                try
                {
                    String dataType = (String) input.readObject();
                    String username;
                    Account curAcct;

                    switch (dataType)
                    {
                        case "SIGN-UP":
                            username = (String) input.readObject();
                            String password = (String) input.readObject();
                            transmit("SIGN-UP", output);
                            if(accounts.putIfAbsent(username, new Account(username, password)) == null)
                            {
                                transmit("Sign-up successful", output);
                            }
                            else
                            {
                                transmit("Username already exists", output);
                            }
                            break;
                        case "LOGIN":
                            username = (String) input.readObject();
                            String pass = (String) input.readObject();
                            curAcct = accounts.get(username);
                            transmit("LOGIN", output);
                            if(curAcct != null && curAcct.checkPassword(pass))
                            {
                                transmit("Login successful", output);
                            }
                            else
                            {
                                transmit("Invalid username or password", output);
                            }
                            break;
                        case "BROWSE":
                            int pageNumber = (int) input.readObject();
                            int pageCapacity = (int) input.readObject();
                            Item [] items = new Item[inventory.size()];
                            inventory.values().toArray(items);
                            transmit("BROWSE", output);
                            for(int i = ((pageNumber - 1) * pageCapacity); i < pageNumber * pageCapacity; i++)
                            {
                                transmit(items[i], output);
                                if(i == items.length-1)
                                {
                                    //If not enough items for a full page, transmit the rest of space as null, and exit loop
                                    for(int j = 0; j < i - ((pageNumber-1)*pageCapacity); j++)
                                    {
                                        transmit(null, output);
                                    }
                                    i = pageNumber*pageCapacity;
                                }
                            }
                            break;
                        case "VIEW":
                            int listID = (int) input.readObject();
                            Item viewing = inventory.get(listID);
                            transmit("VIEW", output);
                            if(viewing != null)
                            {
                                transmit(viewing, output);
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
                            transmit("PURCHASE",output);
                            if(item.equals(inventoryItem))
                            {
                                curAcct = accounts.get(username);
                                if(curAcct != null && curAcct.makePurchase(item.getPrice()))
                                {
                                    if(inventoryItem.purchased(quantityPurchased))
                                    {
                                        transmit("Purchase made successfully", output);
                                        disp(quantityPurchased + " " + inventoryItem.getName() + "s purchased by " + username);

                                        if(inventoryItem.getQuantity() == 0)
                                        {
                                            inventory.remove(inventoryItem.getListingID());
                                            disp(inventoryItem.getName() + " is now sold out and was removed from the inventory");
                                        }
                                    }
                                    else
                                    {
                                        transmit("There are no longer enough " + inventoryItem.getName() + "s in stock to fulfill the request", output);
                                    }
                                }
                                else
                                {
                                    transmit("Insufficient credits", output);
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
                                disp(credits + " credits added to " + username + "'s account");
                            }
                            else
                            {
                                transmit("Invalid account", output);
                            }
                            break;
                        case "ADD LISTING":
                            Item newItem = (Item) input.readObject();
                            newItem.setListingID(listingIDS.incrementAndGet());
                            inventory.put(newItem.getListingID(), newItem);
                            transmit("ADD LISTING", output);
                            transmit(newItem.getName() + "added to listings", output);
                            disp("Added: " + newItem.toString() + " to listings\n\tSeller: " + newItem.getSeller() +
                                    "\n\tQuantity: " + newItem.getQuantity());
                            break;
                        case "SEARCH":
                            String query = (String) input.readObject();
                            Stack<Item> hits = new Stack<>();

                            for(Item i : inventory.values())
                            {
                                if(i.getName().equalsIgnoreCase(query))
                                {
                                    hits.push(i);
                                }
                                else
                                {
                                    double similarWords = 0.0;
                                    String [] queryWords = query.toUpperCase().split(" ");
                                    for(String w : queryWords)
                                    {
                                        if(i.getName().toUpperCase().contains(w)) similarWords++;
                                    }

                                    if((similarWords/((double) queryWords.length)) > 0.75) hits.push(i);
                                }
                            }

                            transmit("SEARCH", output);
                            transmit(hits.size(), output);
                            while(!hits.empty())
                            {
                                transmit(hits.pop(), output);
                            }
                            break;
                        case "TERMINATE":
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
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }while(interact);
        }
    }
}

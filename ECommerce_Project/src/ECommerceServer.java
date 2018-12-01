import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ECommerceServer
{
    private ServerSocket server;
    private ServerThread[] sockets;
    private ExecutorService executor;
    private ConcurrentHashMap<Integer,Item> itemInventory;
    private ConcurrentHashMap<String,Account> accounts;
    private AtomicInteger listingIDS = new AtomicInteger(1);
    private int connectionID = 1;
    private int num_active_clients = 0;
    public final int PORT = 23501;

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

    private void disp(final String message) { System.out.println(message); }

    private void initializeMaps()
    {
        //Inventory
        Scanner invReader = new Scanner("ECommerce_Project/logs/inventoryLog.csv");

        while (invReader.hasNextLine())
        {
            String [] nextLine = invReader.nextLine().split(",");
            itemInventory.put(Integer.parseInt(nextLine[0]), new Item(Integer.parseInt(nextLine[0]), nextLine[1], Double.parseDouble(nextLine[2]), nextLine[3], nextLine[4], nextLine[5], Integer.parseInt(nextLine[6])));
        }

        //Accounts
        Scanner acctReader = new Scanner("ECommerce_Project/logs/accountLog.csv");

        while (acctReader.hasNextLine())
        {
            String [] nextLine = acctReader.nextLine().split(",");
            Account newAcct = new Account(nextLine[0], nextLine[1]);
            newAcct.addFunds(Double.parseDouble(nextLine[3]));
            accounts.put(nextLine[0], newAcct);
        }
    }

    private void updateLogs()
    {
        try
        {
            FileWriter invWriter = new FileWriter("ECommerce_Project/logs/inventoryLog.csv");
            //Inventory
            for (Item item : itemInventory.values())
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

                    //Interact with connection
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

                    try
                    {
                        input.close();
                        output.close();
                        connection.close();
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
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
            transmit("Connection " + connectID + " successful"); //Send connection successful message to client
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
                            accounts.put(username, new Account(username, password));
                            transmit("Sign-up successful");
                            break;
                        case "LOGIN":
                            username = (String) input.readObject();
                            String pass = (String) input.readObject();
                            curAcct = accounts.get(username);
                            if(curAcct != null && curAcct.checkPassword(pass))
                            {
                                transmit("Login successful");
                            }
                            else
                            {
                                transmit("Invalid username or password");
                            }
                            break;
                        case "BROWSE":
                            int pageNumber = (int) input.readObject();
                            int pageCapacity = (int) input.readObject();
                            Item [] items = new Item[itemInventory.size()];
                            itemInventory.values().toArray(items);
                            for(int i = ((pageNumber - 1) * pageCapacity); i < pageNumber * pageCapacity; i++)
                            {
                                transmit(items[i]);
                                if(i == items.length-1) i = pageNumber*pageCapacity;
                            }
                            break;
                        case "PURCHASE":
                            Item item = (Item) input.readObject();
                            Item inventoryItem = itemInventory.get(item.getListingID());
                            username = (String) input.readObject();
                            int quantityPurchased = (int) input.readObject();
                            if(item.equals(inventoryItem))
                            {
                                curAcct = accounts.get(username);
                                if(curAcct != null && curAcct.makePurchase(item.getPrice()))
                                {
                                    if(inventoryItem.purchased(quantityPurchased))
                                    {
                                        transmit("Purchase made successfully");
                                        disp(quantityPurchased + " " + inventoryItem.getName() + "s purchased by " + username);

                                        if(inventoryItem.getQuantity() == 0)
                                        {
                                            itemInventory.remove(inventoryItem.getListingID());
                                            disp(inventoryItem.getName() + " is now sold out and was removed from the inventory");
                                        }
                                    }
                                    else
                                    {
                                        transmit("There are no longer enough " + inventoryItem.getName() + "s in stock to fulfill the request");
                                    }
                                }
                                else
                                {
                                    transmit("Insufficient credits");
                                }
                            }
                            break;
                        case "ADD CREDITS":
                            double credits = (double) input.readObject();
                            username = (String) input.readObject();
                            curAcct = accounts.get(username);
                            if(curAcct != null)
                            {
                                curAcct.addFunds(credits);
                                transmit("Credits added successfully");
                                disp(credits + " credits added to " + username + "'s account");
                            }
                            else
                            {
                                transmit("Invalid account");
                            }
                            break;
                        case "ADD LISTING":
                            Item newItem = (Item) input.readObject();
                            newItem.setListingID(listingIDS.incrementAndGet());
                            itemInventory.put(newItem.getListingID(), newItem);
                            transmit(newItem.getName() + "added to listings");
                            disp("Added: " + newItem.toString() + " to listings\n\tSeller: " + newItem.getSeller() +
                                    "\n\tQuantity: " + newItem.getQuantity());
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

        private void transmit(Serializable data)
        {
            try
            {
                output.writeObject(data);
                output.flush();
            }
            catch (IOException ioe)
            {
                disp("Error: data could not be relayed to client");
            }
        }
    }
}

import util.ECommerceUtilityMethods;

import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import static util.ECommerceUtilityMethods.*;

public class ECommmerceClient extends JFrame
{
    //Network members
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket client;
    private String host;

    //GUI components/parameters
    private int browsePageCapacity = 8;

    public ECommmerceClient(String host)
    {
        this.host = host;
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
            disp();
        }
        catch (IOException ioe)
        {

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

            }
            catch ()
            {

            }

        }while(interact)
    }


    //TODO: Display to sepcific GUI area (maybe using invokelater)
    private void toGUI(final String message)
    {

    }
}

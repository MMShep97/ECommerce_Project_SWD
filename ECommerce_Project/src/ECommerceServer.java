import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECommerceServer
{
    private ServerSocket server;
    private User[] sockets;
    private ExecutorService executor;
    private int connectionID = 1;
    private int num_active_clients = 0;
    public final int PORT = 23501;

    public ECommerceServer()
    {
        sockets = new User[100];
        executor = Executors.newFixedThreadPool(100);

        //TODO: Set
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
                    sockets[connectionID-1] = new User(connectionID);

                    sockets[connectionID-1].waitForConnection();
                    num_active_clients++;

                    executor.execute(sockets[connectionID-1]);
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
    }

    private void disp(final String message) { System.out.println(message); }

    private class User implements Runnable
    {
        private ObjectOutputStream output;
        private ObjectInputStream input;
        private Socket connection;
        private int connectID;
        private boolean active = false;

        public User(int connectID) { this.connectID = connectID; }

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

            //Process client transmissions
            /*do
            {
                //Read data and respond
                try
                {

                }
                catch ()
                {

                }
            }*/
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

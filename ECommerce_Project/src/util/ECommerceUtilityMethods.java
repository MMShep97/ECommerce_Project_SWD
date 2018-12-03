package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 * Utility methods used throughout both server and client classes
 */
public class ECommerceUtilityMethods
{
    /**
     *  Allows client and server to write to the command line.
     *  Synchronization prevents any potential command line interleaving.
     */
    synchronized public static void disp(final String message)
    {
        System.out.println(message);
    }

    /**
     * Closes Socket and all ObjectStreams
     * @param out
     * @param in
     * @param sock
     */
    public static void closeConnections(ObjectOutputStream out, ObjectInputStream in, Socket sock)
    {
        try
        {
            in.close();
            out.close();
            sock.close();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    /**
     * Sends Serializable data to the output stream
     * @param data
     * @param output
     */
    public static void transmit(Serializable data, ObjectOutputStream output)
    {
        try
        {
            output.writeObject(data);
            output.flush();
        }
        catch (IOException ioe)
        {
            disp("Error: unable to transmit data");
        }
    }
}

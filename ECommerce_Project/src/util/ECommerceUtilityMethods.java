package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class ECommerceUtilityMethods
{
    public static void disp(final String message)
    {
        synchronized (System.out) //Ensuring Sys.out calls are not interleaved
        {
            System.out.println(message);
        }
    }

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

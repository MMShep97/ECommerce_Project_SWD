//import javax.swing.*;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.io.Serializable;
//import java.net.Socket;
//
//public class ECommmerceClient extends JFrame
//{
//    private ObjectOutputStream output;
//    private ObjectInputStream input;
//    private Socket client;
//    private String host;
//
//    public ECommmerceClient(String host)
//    {
//        this.host = host;
//    }
//
//    public void runClient()
//    {
//        try
//        {
//
//        }
//    }
//
//
//    private void transmit(Serializable data)
//    {
//        try
//        {
//            output.writeObject(data);
//            output.flush();
//        }
//        catch (IOException ioe)
//        {
//            disp(data.toString() + " could not be transmitted");
//        }
//    }
//
//    private void disp(final String message) { System.out.println(message); }
//
//    //TODO: Display to sepcific GUI area (maybe using invokelater)
//    private void toGUI(final String message)
//    {
//
//    }
//}

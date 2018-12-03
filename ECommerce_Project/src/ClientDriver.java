/**
 * Starts up client, instantiates new client(s) every time main method is ran by calling <code>ECommerceClient</code> instance's
 * <code>runClient()</code> method which attempts to establish a client-server connection
 */
public class ClientDriver {
    public static void main(String[] args){
        ECommerceClient client = new ECommerceClient("localhost");
        client.runClient();
    }
}

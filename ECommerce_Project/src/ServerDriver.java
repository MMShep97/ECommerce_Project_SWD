/**
 * Starts up server -- server continuously checks for new connections on a specified port within <code>ECommerceServer</code>
 * instance's method <code>serve()</code>.
 */
public class ServerDriver
{
    public static void main(String [] args)
    {
        ECommerceServer commerceServer = new ECommerceServer();
        commerceServer.serve();
    }
}

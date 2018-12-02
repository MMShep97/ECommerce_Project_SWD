import util.ECommerceUtilityMethods;

public class ClientDriver {
    public static void main(String[] args){
        ECommmerceClient client = new ECommmerceClient("yarp");
        client.initializeGUI();
        client.runClient();
    }
}

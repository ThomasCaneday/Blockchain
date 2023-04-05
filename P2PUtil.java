import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * This class holds any utility methods needed for P2P networking communication.
 */
public class P2PUtil {


    /**
     * Allows a one time socket call to a server, gets reply, and then closes connection.
     * @param sIP
     * @param iPort
     * @param sMessage
     * @return
     */
    public static String connectForOneMessage(String sIP, int iPort, String sMessage){
        try(Socket oSocket = new Socket()){

//            Attempt to connect to server at given IP address and port.
            oSocket.connect(new InetSocketAddress(sIP, iPort), 5000);

//            Build a writer.
            OutputStream output = oSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

//            Send message to server.
            writer.println(sMessage);
            writer.flush(); // Guarantee that message is sent immediately.

//            Build a reader.
            InputStream input = oSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String sReply = reader.readLine();

            return sReply;
        }
        catch(Exception e) {
            System.out.println("[client]: Client exception: " + e.getMessage());
//            ex.printStackTrace();
            return null;
        }
    }
}

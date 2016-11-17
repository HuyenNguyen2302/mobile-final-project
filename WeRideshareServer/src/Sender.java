
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.iqrequest.IQRequestHandler;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.PlainStreamElement;
import org.jivesoftware.smack.packet.Stanza;

/**
 * Created by mrampiah on 11/16/16.
 */
public class Sender {

    public void connect() throws XMPPException, InterruptedException{

        XMPPConnection connection = new CustomConnection();
        System.out.println(connection);
        connection.connect();
        connection.login("testuser1", "test123");



        Chat chat = connection.getChatManager().createChat("testuser2@sameek", new MessageListener() {

            public void processMessage(Chat chat, Message message) {
                // Print out any messages we get back to standard out.
                System.out.println("Received message: " + message);
            }
        });
        chat.sendMessage("Howdy test1!");

        while (true) {
            Thread.sleep(50);
    }
}

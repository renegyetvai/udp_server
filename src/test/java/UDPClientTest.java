import COM.Format.Message;
import COM.UDP.Client.UDPClient;
import COM.UDP.Server.UDPServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UDPClientTest {

    private Thread setupTest() throws InterruptedException {
        Thread serverThread = new Thread(new UDPServer(8081));
        serverThread.start();
        serverThread.join();

        return serverThread;
    }

    @Test
    void clientTest() throws IOException, InterruptedException {
        Thread serverThread = setupTest();

        DatagramPacket answer;
        Message message;

        UDPClient UDPClient = new UDPClient("localhost", 8081);

        answer = UDPClient.sendPacket(new Message(Message.messageTypes.MSG_REQUEST, "request"));

        UDPClient.close();

        message = Message.readFromBytes(answer.getData());
        assertEquals("reply", message.getPayload());

        // send "exit" to cli of server thread
        serverThread.interrupt();
    }

}

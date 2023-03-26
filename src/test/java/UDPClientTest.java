import COM.Format.Message;
import COM.UDP.Client.UDPClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.DatagramPacket;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UDPClientTest {

    @Test
    void clientTest() throws IOException, InterruptedException {
        DatagramPacket answer;
        Message message;

        UDPClient UDPClient = new UDPClient("localhost");

        answer = UDPClient.sendPacket(new Message(Message.messageTypes.MSG_REQUEST, "request"));

        UDPClient.close();

        message = Message.readFromBytes(answer.getData());
        assertEquals("request", message.getPayload());
    }

}

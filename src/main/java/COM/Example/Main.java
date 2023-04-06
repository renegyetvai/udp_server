package COM.Example;

import COM.Format.Message;
import COM.UDP.Client.UDPClient;
import COM.UDP.Server.UDPServer;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * @author René Gyetvai
 */
public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {

        if (args.length == 0) {
            System.err.println("Please provide an argument for the execution mode");
            System.exit(0);
        }

        switch (args[0]) {
            case "-s" -> {
                Thread serverThread = new Thread(new UDPServer(8081));
                System.out.println("Starting server mode");
                serverThread.start();
                serverThread.join();
            }
            case "-c" -> {
                DatagramPacket answer;
                Message message;

                System.out.println("Starting in client mode");
                UDPClient UDPClient = new UDPClient("localhost", 8081);

                System.out.println("Sending request message");
                answer = UDPClient.sendPacket(new Message(Message.messageTypes.MSG_REQUEST, "request"));

                UDPClient.close();

                // Convert the received message from a byte array to a message object.
                message = Message.readFromBytes(answer.getData());
                System.out.println("Received answer: " + "type = " + message.getMessageType() + " & content = '" + message.getPayload() + "'");
                System.out.println("Client 1 done");
            }
            default -> System.out.println("Provided parameter was not found!\n " +
                    "Please enter parameter '-s' to start as server or '-c' to start as client!");
        }
    }
}
package COM.UDP.Client;

import COM.Format.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPClient {

    private final InetAddress serverAddress;
    private DatagramSocket clientSocket;

    public UDPClient(String address) throws IOException {
        serverAddress = InetAddress.getByName(address);
        this.connectTo();
    }

    public UDPClient(String address, int port) throws IOException {
        serverAddress = InetAddress.getByName(address);
        this.connectTo(port);
    }

    /**
     * Creates a socket to connect to the server.
     * @throws SocketException If the socket could not be created.
     */
    private void connectTo(int... port) throws SocketException {

        switch (port.length) {
            case 0 -> clientSocket = new DatagramSocket();
            case 1 -> clientSocket = new DatagramSocket(port[0]);
            default -> throw new IllegalArgumentException("Too many arguments!");
        }
        System.err.println("Created client socket.");
    }

    /**
     * Sends a message to the server.
     * @param msg The message to send.
     * @throws IOException If the message could not be sent.
     */
    public synchronized DatagramPacket sendPacket(Message msg) throws IOException {
        // TODO: Maybe no port is needed since we use it in connectTo()?
        byte[] buffer = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, clientSocket.getLocalPort());
        clientSocket.send(packet);
        packet = new DatagramPacket(buffer, buffer.length);
        clientSocket.receive(packet);
        return packet;
    }

    /**
     * Closes the socket.
     */
    public void close() {
        clientSocket.close();
    }
}

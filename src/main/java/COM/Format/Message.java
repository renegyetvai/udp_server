package COM.Format;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Message {
    public enum messageTypes {
        MSG_REQUEST, MSG_REPLY, MSG_ERROR;

        int getValue() {
            switch (this) {
                case MSG_REQUEST:
                    return 1;
                case MSG_REPLY:
                    return 2;
                case MSG_ERROR:
                    return 3;
                default:
                    System.err.println("Unknown message type.");
                    return 0;
            }
        }

        static int getMessageTypeSize() {
            return Integer.toString(messageTypes.MSG_ERROR.getValue()).getBytes(StandardCharsets.UTF_8).length;
        }
    }

    private final String payload;
    private final int messageType;

    public Message(messageTypes messageType, String payload) {
        this.messageType = messageType.getValue();
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public int getMessageType() {
        return messageType;
    }

    public messageTypes getMessageTypeEnum(int type) {
        switch (type) {
            case 1:
                return messageTypes.MSG_REQUEST;
            case 2:
                return messageTypes.MSG_REPLY;
            default:
                System.err.println("Unknown message type.");
                return messageTypes.MSG_ERROR;
        }
    }

    /**
     * Converts the given message to a byte array.
     * @return The byte array representation of the message.
     */
    public synchronized byte[] getBytes(){
        System.err.println("Converting message to bytes");

        // Convert message type to bytes
        byte[] type = Integer.toString(this.getMessageType()).getBytes(StandardCharsets.UTF_8);
        byte[] payload = this.getPayload().getBytes(StandardCharsets.UTF_8);

        // Calculate length of payload
        ByteBuffer byteBuffer = ByteBuffer.allocate(type.length + payload.length);

        // Add message type as payload into byte buffer
        byteBuffer.put(type, 0, type.length);
        byteBuffer.put(payload, 0, payload.length);

        return byteBuffer.array();
    }

    /**
     * Converts the given byte array to a message.
     * @param bytes The byte array to convert.
     * @return The message representation of the byte array.
     */
    public static synchronized Message readFromBytes(byte[] bytes) {
        System.err.println("Converting bytes to message");

        // Split bytes into message type and payload
        byte[] type = new byte[messageTypes.getMessageTypeSize()];
        byte[] payload = new byte[bytes.length - messageTypes.getMessageTypeSize()];

        // Copy message type from bytes into type and payload
        System.arraycopy(bytes, 0, type, 0, messageTypes.getMessageTypeSize());
        System.arraycopy(bytes, messageTypes.getMessageTypeSize(), payload, 0, payload.length);

        // Convert message type to string
        String typeString = new String(type, StandardCharsets.UTF_8);

        // Convert typeString to int and then to messageType
        int typeInt = Integer.parseInt(typeString);
        messageTypes messageType = messageTypes.values()[typeInt - 1];

        // Convert payload to string
        String payloadString = new String(payload, StandardCharsets.UTF_8);

        // Cut NUL characters from payloadString if there are any
        if (payloadString.contains("\0")) {
            payloadString = payloadString.substring(0, payloadString.indexOf("\0"));
        }

        Message msg = new Message(messageType, payloadString);
        System.err.println("Message type: " + messageType + " Payload: " + payloadString);

        // Return message
        return msg;
    }
}

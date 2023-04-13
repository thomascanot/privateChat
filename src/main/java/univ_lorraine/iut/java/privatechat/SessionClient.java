package univ_lorraine.iut.java.privatechat;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

class SessionClient {
    private String serverId;
    private BigInteger sessionKey;
    private static final BigInteger P;
    private static final BigInteger G;
    private BigInteger privateSessionKey;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream in;
    private final String ip;
    private final int port;

    /**
     * @param ip the ip address or host to connect
     * @param port the port number to identify the process
     */
    public SessionClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    static {
        P = new BigInteger("B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD7098488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371", 16);
        G = new BigInteger("A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507FD6406CFF14266D31266FEA1E5C41564B777E690F5504F213160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28AD662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5", 16);
    }
    /**
     * A utility Function to calculate Session Key
     * @param p The prime number is needed to calculate the session key
     *          Session key is a random number between 1 and p-1
     * @return THe session key
     */
    private static BigInteger calcPrivateSessionKey(BigInteger p) {
        Random rand = new Random();
        BigInteger randomLong = BigInteger.valueOf(rand.nextLong());
        BigInteger midState = p.multiply(randomLong);
        return midState.divideAndRemainder(BigInteger.valueOf(Long.MAX_VALUE))[0];
    }

    /**
     * Calculates the session key
     * @param serverPublicKey public key returned by server
     * @param p the prime no
     * @return the calculated session key
     */
    private BigInteger calcSessionKey(BigInteger serverPublicKey, BigInteger p) {
        return serverPublicKey.modPow(privateSessionKey, p);
    }

    /**
     * Function called to connect to Server
     * @return if connection was established or not
     */
    public boolean connect() {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {
                System.out.println("Client busy couldn't establish new connection");
            }
        }
        /* Documentation */
        System.out.println("Sending Connection Request To Server");
        try {
            socket = new Socket(ip, port);
        } catch (Exception e) {
            System.out.println("Invalid Host or Port provided");
            return false;
        }
        return establishIO();
    }

    /**
     * To check if IO from server is established or not
     * @return boolean for same
     */
    private boolean establishIO() {
        if (socket == null) {
            System.out.println("Establish a Connection Before");
            return false;
        }
        if (in != null) {
            try {
                in.close();
            } catch (Exception e) {
                System.out.println("Input Channel Error");
                return false;
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (Exception e) {
                System.out.println("Output Channel Error");
                return false;
            }
        }
        try {
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (Exception e) {
            System.out.println("Unable to open a I/O Channel");
            return false;
        }
        return true;
    }

    /**
     * Receive Keys from the Server
     * @param p prime number
     */
    public void receiveKeys(BigInteger p) {
        System.out.println("Waiting for Server's Public Key");
        String[] receivedData;
        try {
            receivedData = in.readUTF().split("\\s+");
        } catch (Exception e) {
            System.out.println("Unable to get back Server's Public Key");
            return;
        }
        serverId = receivedData[0];
        BigInteger serverPublicKey;
        try {
            serverPublicKey = new BigInteger(receivedData[1]);
        } catch (Exception e) {
            System.out.println("Invalid key received");
            return;
        }
        sessionKey = calcSessionKey(serverPublicKey, p);
        /* Documentation */
        System.out.println("Session key Established");
        System.out.println("Session Key: " + sessionKey.toString());
    }

    /**
     * Function to start the Key Request
     * @param id the client id, to send the server a client's info
     * @param p the prime no
     * @param g the generator no
     */
    public void keyRequest(String id, BigInteger p, BigInteger g) {
        if (socket == null) {
            System.out.println("First Establish a Connection");
            return;
        }
        if (in == null || out == null) {
            establishIO();
        }
        /* Documentation */
        System.out.println("Starting Key Exchange");
        privateSessionKey = calcPrivateSessionKey(p);
        BigInteger publicSessionKey = g.modPow(privateSessionKey,p);
        // sending keys to client
        // First build the message with a particular format
        // "id g^x\n"
        StringBuilder buf = new StringBuilder();
        buf.append(id);
        buf.append(' ');
        buf.append(publicSessionKey.toString());
        buf.append('\n');
        try {
            // send the keys
            out.writeUTF(buf.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Unable to initiate a session");
        }
        /* Documentation */
        System.out.println("Request for Key Exchange Sent to Server");
    }

    /**
     * Close the connection
     */
    public void close() {
        try {
            if (socket != null)
                socket.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (Exception e) {
            System.out.println("Unable to close Resources");
        }
        /* Documentation */
        System.out.println("Connection with Server Closed");
    }

    /**
     * A function to communicate from the server
     */
    public void communicate() {
        try {
            Scanner scanner = new Scanner(System.in);
            String message = "";
            while (!message.equalsIgnoreCase("quit")) {
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = null;

                System.out.print("Saisie d'un message : ");
                message = scanner.nextLine();
                try {
                    oos.writeObject(message);
                    oos.flush();
                } catch (IOException e) {
                    System.out.println("Error sending message to server");
                    return;
                }

                String messageRecu;
                try {
                    ois = new ObjectInputStream(socket.getInputStream());
                    messageRecu = (String) ois.readObject();
                } catch (IOException e) {
                    System.out.println("Error receiving message from client");
                    return;
                }

                System.out.println("Message reçu : " + messageRecu);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
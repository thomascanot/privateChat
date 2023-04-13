package univ_lorraine.iut.java.privatechat;

import univ_lorraine.iut.java.privatechat.model.Contact;

import java.io.Console;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Client {
    private static String id;
    private static final BigInteger p;
    private static final BigInteger g;
    private static BigInteger privateT;
    private static BigInteger publicT;
    private static SessionClient session;

    private Client() {
        // empty constructor
    }

    /**
     * Static Variables for the Client Class
     * p - The prime no
     * g - The generator for prime
     */
    static {
        p = new BigInteger("B10B8F96A080E01DDE92DE5EAE5D54EC52C99FBCFB06A3C69A6A9DCA52D23B616073E28675A23D189838EF1E2EE652C013ECB4AEA906112324975C3CD49B83BFACCBDD7D90C4BD7098488E9C219A73724EFFD6FAE5644738FAA31A4FF55BCCC0A151AF5F0DC8B4BD45BF37DF365C1A65E68CFDA76D4DA708DF1FB2BC2E4A4371", 16);
        g = new BigInteger("A4D1CBD5C3FD34126765A442EFB99905F8104DD258AC507FD6406CFF14266D31266FEA1E5C41564B777E690F5504F213160217B4B01B886A5E91547F9E2749F4D7FBD7D3B9A92EE1909D0D2263F80A76A6A24C087A091F531DBF0A0169B6A28AD662A4D18E73AFA32D779D5918D08BC8858F4DCEF97C2A24855E6EEB22B3B2E5", 16);
    }

    /**
     * To wait for an confirmation from the command line
     * @param message  The format of message to print
     * @param args   Args represents the data to print
     */
    public static void waitForEnter(String message, Object... args) {
        Console c = System.console();
        if (c != null) {
            // printf-like arguments
            if (message != null)
                c.format(message, args);
            c.format(" --Press ENTER to proceed.");
            c.readLine();
        }
    }

    /**
     * The starting point of calculations
     * @param args an array of command line inputs
     */
    public static void main(String[] args) {
        id = "1";
        String message = "1. For new Connection type: connect <hostname> <port>\n2. newKeys to calculate new permanent Keys\n3. Anything else to exit application\n";
        newSession(args);
    }

    /**
     * A function to maintain a new client session
     * It creates a new session and does all key exchange part
     */
    private static void newSession(String[] args) {
        InetAddress host = null;
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        waitForEnter("Send Connection Request To Server?");
        session = new SessionClient(args[1], Integer.parseInt(args[2]));
        session.connect();
        waitForEnter("Send Key Exchange Request To Server? (Key Request Must be Sent within Two Minutes of Connection)");
        Runtime runtime = Runtime.getRuntime();
        long startKeyExchange = System.currentTimeMillis();
        int noOfKeyExchanges = 1;
        for(int i = 0; i < noOfKeyExchanges; i++) {
            session.keyRequest(id, p, g);
            // waitForEnter("Receive Server's Public Key?");
            session.receiveKeys(p);
        }
        long endKeyExchange = System.currentTimeMillis();
        System.out.println("Elapsed Time: " + (endKeyExchange-startKeyExchange));
        System.out.println("Memory Usage: " + (runtime.totalMemory() - runtime.freeMemory()));
        session.communicate();
    }
}
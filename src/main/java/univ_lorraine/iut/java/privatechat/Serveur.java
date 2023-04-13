package univ_lorraine.iut.java.privatechat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public final class Serveur implements Runnable{
    private static String id;
    private static ServerSocket server;
    private static int port = 12345;
    private static Boolean running = true;


    /**
     * A function to check if the required command line arguments were passed or not
     * It also creates the socket for listening connections
     * @param args command line arguments
     */
    private static void handleArgs(String[] args) {
        id = "1";
        server = null;
        int port  = 12345;
        // try creating a ServerSocket
        try {
            server = new ServerSocket(port);
        } catch (Exception e) {
            try {
                server = new ServerSocket(port + 1);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(e.getMessage());
        }
    }

    /**
     * A function that handles a particular client
     * This function opens a client and returns a thread
     * @param socket the socket representing a client
     * @return thread created to handle client
     */
    private static Thread handleConnection(Socket socket) {
        System.out.println("Connection request from: " + socket.getRemoteSocketAddress().toString());
        Thread th = new ClientThread(socket, id);
        try {
            th.start();
        } catch (Exception e) {
            return null;
        }
        return th;
    }

    public void run() {
        String[] args = {""};
        handleArgs(args);
        if (server == null) {
            System.out.println("Unable to initiate a server");
            System.exit(1);
        }
        ArrayList<Thread> clientThreads = new ArrayList<>();
        // server is ready
        /* Documentation */
        System.out.println("Server Started At: " + server.getLocalPort());
        int connections = 1;
        // The main loop to listen for connections
        while (connections > 0) {
            Socket socket = null;
            try {
                socket = server.accept();
            } catch (Exception e) {
                System.out.println("Unable to accept request");
            }
            if (socket != null) {
                Thread newClient = handleConnection(socket);
                if(newClient != null)
                    clientThreads.add(newClient);
                else
                    System.out.println("The client connection refused unable to allocate a new thread");
                connections--;
            } else {
                System.out.println("Connection Error");
            }
        }
        for (Thread clientThread : clientThreads) {
            try {
                clientThread.join();
            } catch (InterruptedException e) {
                System.out.println("A client thread was interrupted");
            }
        }
        // Close the server/ listening socket
        if (server != null) {
            try {
                server.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(0);
            }
        }
    }

    /**
     * Main function to start a client running
     * @param args command line arguments
     */
    public static void main(String[] args) {
        //rien
    }
}

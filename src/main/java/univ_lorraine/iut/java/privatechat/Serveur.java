package univ_lorraine.iut.java.privatechat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;
    private static Boolean running=true;
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        List<Thread> threadList = new ArrayList<>();
        while(running){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            Thread thread = new Thread(new ClientCommunication(socket));
            threadList.add(thread);
            thread.start();


        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        for(Thread thread:threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        server.close();
    }

}
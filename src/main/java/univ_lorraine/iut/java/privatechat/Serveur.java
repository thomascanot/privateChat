package univ_lorraine.iut.java.privatechat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {
    private static ServerSocket server;
    private static int port = 9876;
    private static Boolean running=true;

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server = new ServerSocket(port);
        List<Thread> threadList = new ArrayList<>();

        while(running){
            Socket socket = server.accept();
            Thread thread = new Thread(new ClientCommunication(socket));
            threadList.add(thread);
            thread.start();
        }
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
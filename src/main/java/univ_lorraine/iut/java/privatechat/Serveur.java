package univ_lorraine.iut.java.privatechat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur {

    public static void main(String[] args) throws IOException, ClassNotFoundException{
        int port = 12345;
        ServerSocket server = new ServerSocket(port);
        List<Thread> threadList = new ArrayList<>();
        boolean running = true;

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
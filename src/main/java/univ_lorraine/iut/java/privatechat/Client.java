package univ_lorraine.iut.java.privatechat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            InetAddress host = InetAddress.getLocalHost();
            Socket socket = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            Scanner scanner = new Scanner(System.in);
            // establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            // write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());

            System.out.println("Saisie d'un message : ");
            String message = scanner.nextLine();
            oos.writeObject(message);
            oos.flush();
            // read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message2 = (String) ois.readObject();
            System.out.println("Message reçu : " + message2);

            // close resources
            ois.close();
            oos.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package univ_lorraine.iut.java.privatechat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            while(true) {
                InetAddress host = InetAddress.getLocalHost();
                Socket socket  = new Socket(host.getHostName(), 9876);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream ois = null;
                Scanner scanner = new Scanner(System.in);

                System.out.println("Saisie d'un message : ");
                oos.writeObject(scanner.nextLine());
                oos.flush();

                // On récupère le message du serveur
                ois = new ObjectInputStream(socket.getInputStream());
                String message2 = (String) ois.readObject();
                System.out.println("Message reçu : " + message2);

                // On ferme la connexion
                ois.close();
                oos.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

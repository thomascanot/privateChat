package univ_lorraine.iut.java.privatechat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientCommunication implements Runnable {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public ClientCommunication(Socket socket) {
        super();
        this.socket = socket;

    }

    @Override
    public void run() {
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in);
            String message = (String) ois.readObject();
            System.out.println("Message reçu : " + message);

            System.out.println("Saisie d'un message : ");
            String message2 = scanner.nextLine();
            oos.writeObject(message2);


        } catch (Exception e) {

        }

    }

    public void close() {
        try {
            // close resources
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
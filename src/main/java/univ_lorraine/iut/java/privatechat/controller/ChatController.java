package univ_lorraine.iut.java.privatechat.controller;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;
import univ_lorraine.iut.java.privatechat.Serveur;
import univ_lorraine.iut.java.privatechat.model.Contact;
import univ_lorraine.iut.java.privatechat.model.Conversation;
import univ_lorraine.iut.java.privatechat.model.ConversationListCell;
import univ_lorraine.iut.java.privatechat.model.Message;

public class ChatController implements DataController{
    @FXML private ListView<Conversation> conversationListView;
    private ObservableList<Conversation> conversationList = FXCollections.observableArrayList();

    @FXML private Button btnAddContact;

    public void addConversation(Conversation conversation) {
        conversationList.add(conversation);
    }

    public void initialize() {
        String userLogin = App.getUser();
        if (conversationListView != null) {
            conversationListView.setItems(conversationList);
            conversationListView.setCellFactory(listView -> new ConversationListCell());
        }

        File contactFile = new File(App.getUser() + "_contact.obj");
        FileInputStream f = null;
        ObjectInputStream s = null;
        if (contactFile.exists()) {
            try {
                f = new FileInputStream(contactFile);
                s = new ObjectInputStream(f);
                List<Conversation> List = (List)s.readObject();
                for (Conversation conversation : List) {
                    conversationList.add(conversation);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            finally {
                try {
                    s.close();
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void uninitialize() {
        FileOutputStream f = null;
        ObjectOutputStream s = null;
        try {
            // Créer une liste de conversations
            List<Conversation> conversations = new ArrayList<>(conversationList);

            // Essayer de la sérialiser
            File contactFile = new File(App.getUser() + "_contact.obj");
            f = new FileOutputStream(contactFile);
            s = new ObjectOutputStream(f);
            s.writeObject(conversations);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (s != null) {
                try {

                    s.close();
                    f.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void addContact() throws IOException {
        uninitialize();
        App.setWindowSize(520, 400);
        App.setRoot("AddContact");
    }
    @FXML
    private void logout() throws IOException {
        uninitialize();
        App.setWindowSize(520, 400);
        App.setRoot("login");
    }

    @FXML
    public TextField inputField;

    @FXML
    private TextArea messageArea;
    
    @FXML
    private void sendMessage(ActionEvent event) {
        String messageText = inputField.getText();
        inputField.clear();
        Message message = new Message();
        message.setContent(messageText);
        message.setSender(App.getUser());
        message.setSendedDate(LocalDateTime.now());
        if (message != null) {
            System.out.println(message);
            messageArea.appendText(message.getSender() + " : " + message.getContent() + "\n");
        }
    }
    @Override
    public void setData(Object data) {
        if (data instanceof Contact) {
           var contact = (Contact) data;
           conversationList.add(new Conversation(null,contact));
        }
    }

    private static Contact contact;
    public static void main(String[] args) {
        contact = new Contact(args[0], args[1], Integer.parseInt(args[2]));
    }
}
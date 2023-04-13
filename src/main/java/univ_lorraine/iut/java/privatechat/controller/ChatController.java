package univ_lorraine.iut.java.privatechat.controller;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import univ_lorraine.iut.java.privatechat.App;
import univ_lorraine.iut.java.privatechat.model.Contact;
import univ_lorraine.iut.java.privatechat.model.Conversation;
import univ_lorraine.iut.java.privatechat.model.ConversationListCell;

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
        App.setRoot("AddContact");
    }
    @FXML
    private void logout() throws IOException {
        uninitialize();
        App.setRoot("login");
    }

    @Override
    public void setData(Object data) {
        if (data instanceof Contact) {
           var contact = (Contact) data;
           conversationList.add(new Conversation(null,contact));
        }
    }
}
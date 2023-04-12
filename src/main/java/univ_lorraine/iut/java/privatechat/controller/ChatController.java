package univ_lorraine.iut.java.privatechat.controller;

import java.io.*;
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

    private String userLogin;
    @FXML private Button btnAddContact;

    public void addConversation(Conversation conversation) {
        conversationList.add(conversation);
    }

    public void initialize() {
        userLogin = App.getUser();
        if (conversationListView != null) {
            conversationListView.setItems(conversationList);
            conversationListView.setCellFactory(listView -> new ConversationListCell());
        }

        //Lire le fichier de contact
        File contactFile = new File(App.getUser() + "_contact.txt");
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

    public void deinitialize()  {
        System.out.println("Finalize");
        File contactFile = new File(App.getUser() + "_contact.txt");
        FileOutputStream f = null;
        ObjectOutputStream s = null;
        try {
            f = new FileOutputStream(contactFile);
            s = new ObjectOutputStream(f);
            s.writeObject(conversationList);
            s.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
    @FXML
    public void addContact() throws IOException {

        App.setRoot("AddContact");
    }
    @FXML
    private void logout() throws IOException {
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
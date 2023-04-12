package univ_lorraine.iut.java.privatechat.model;

import javafx.collections.ObservableList;

public class Conversation {

    private ObservableList<Message> messages;
    private Contact contact;

    public Conversation(ObservableList<Message> messages, Contact contact) {
        this.messages = messages;
        this.contact = contact;
    }


    public ObservableList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

}
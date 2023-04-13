package univ_lorraine.iut.java.privatechat.model;
import javafx.scene.control.ListCell;
import univ_lorraine.iut.java.privatechat.Client;
import univ_lorraine.iut.java.privatechat.Serveur;

public class ConversationListCell extends ListCell<Conversation> {

    public ConversationListCell() {
        super();
        this.setOnMouseClicked(event -> {
            if (! this.isEmpty()) {
                String[] args = null;
                Serveur.main(args);
            }
        });
    }

    @Override
    public void updateItem(Conversation conversation, boolean empty) {
        super.updateItem(conversation, empty);
        if (empty) {
            setText(null);
        } else {
            setText(conversation.getContact().getPseudo());
        }
    }
}
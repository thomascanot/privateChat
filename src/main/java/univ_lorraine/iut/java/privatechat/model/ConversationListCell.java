package univ_lorraine.iut.java.privatechat.model;
import javafx.scene.control.ListCell;
public class ConversationListCell extends ListCell<Conversation> {

    public ConversationListCell() {
        super();
        this.setOnMouseClicked(event -> {
            if (! this.isEmpty()) {
                Conversation conversation = this.getItem();
                System.out.println(conversation.getContact());
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
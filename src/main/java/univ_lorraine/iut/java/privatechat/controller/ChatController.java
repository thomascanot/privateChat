package univ_lorraine.iut.java.privatechat.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import univ_lorraine.iut.java.privatechat.App;

public class ChatController {

    @FXML
    private void logout() throws IOException {
        App.setRoot("login");
    }
}
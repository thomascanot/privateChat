package univ_lorraine.iut.java.privatechat.controller;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;
import univ_lorraine.iut.java.privatechat.model.Contact;


public class AddContactController {

    @FXML
    private TextField txt_pseudo;

    @FXML
    private TextField txt_ip;

    @FXML
    private TextField txt_port;

    NumberFormat format = NumberFormat.getInstance(Locale.getDefault());

    @FXML void goBack() throws IOException {
        App.setWindowSize(1310, 760);
        App.setRoot("chat");
    }

    @FXML
    private void submit() throws IOException {
        if (!Objects.equals(txt_pseudo.getText(), "") && !Objects.equals(txt_ip.getText(), "") && !Objects.equals(txt_port.getText(), "")) {
            App.setWindowSize(1310, 760);
            App.setRoot("chat", new Contact(txt_pseudo.getText(), txt_ip.getText(), Integer.parseInt(txt_port.getText())));
        }
    }
}
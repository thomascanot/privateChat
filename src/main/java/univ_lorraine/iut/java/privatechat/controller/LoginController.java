package univ_lorraine.iut.java.privatechat.controller;

import java.io.*;
import java.util.Arrays;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import univ_lorraine.iut.java.privatechat.App;
import univ_lorraine.iut.java.privatechat.Serveur;
import univ_lorraine.iut.java.privatechat.model.PasswordHasher;


public class LoginController {

    private final File directory = new File("data");
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    private Thread threadServeur;

    @FXML
    private Button loginButton;

    private static final String FILE_EXTENSION = ".pwd";

    private boolean isValidUsername(String username) {
        return username != null && !username.isEmpty();
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }


    private boolean checkPassword(String login, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(login + ".pwd"))) {

            String readLine = reader.readLine();
            String salt = "sSQD4sq45dSQDq2a";
            String requiredLine = "password=" + PasswordHasher.hashPassword(password, salt);
            if (requiredLine.equals(readLine)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    @FXML
    private void login() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String salt = "sSQD4sq45dSQDq2a";
        String hashedPassword = PasswordHasher.hashPassword(password, salt);

        // Vérifier si le nom d'utilisateur est valide
        if (!isValidUsername(username)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Le nom d'utilisateur est invalide");
            alert.showAndWait();
            return;
        }

        // Vérifier si le mot de passe est valide
        if (!isValidPassword(password)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Le mot de passe doit contenir au moins 8 caractères");
            alert.showAndWait();
            return;
        }

        // Vérifier si le compte existe déjà dans la base de données
        File passwordFile = new File(username + FILE_EXTENSION);
        if (passwordFile.exists()) {
            //verifier si le mot de passe est correct
            if (!checkPassword(username, password)) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Le mot de passe est incorrect");
                alert.showAndWait();
                return;
            }
        } else {
            // Créer le fichier contenant le mot de passe avec buffer Writer
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(passwordFile))) {
                writer.write("password=" + hashedPassword + "\rsalt=" + salt + "\riv=" + PasswordHasher.saveIv());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Lancer le chat
        App.setUser(username);
        App.setWindowSize(1310, 760);

        // Lancement du SERVEUR
        threadServeur = new Thread(new Serveur());
        threadServeur.start();

        App.setRoot("chat");
    }
}


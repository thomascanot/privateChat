package univ_lorraine.iut.java.privatechat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import univ_lorraine.iut.java.privatechat.controller.DataController;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static String user;


    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"), 520, 400);
        stage.setScene(scene);
        stage.setTitle("PrivateChat");
        stage.show();
    }

    public static void setUser(String newUser) {
        user = newUser;
    }

    public static String getUser() {
        return user;
    }
    public static void setWindowSize(int width, int height) {
        scene.getWindow().setWidth(width);
        scene.getWindow().setHeight(height);
    }


    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void setRoot(String fxml, Object data) throws IOException {
        scene.setRoot(loadFXML(fxml,data));
    }
    private static Parent loadFXML(String fxml,Object data) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml +
                ".fxml"));
        Parent root = fxmlLoader.load();
        if(data!=null &&
                fxmlLoader.getController() instanceof DataController) {
            ((DataController) fxmlLoader.getController()).setData(data);
        }
        return root;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
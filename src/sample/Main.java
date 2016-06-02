package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.Client;
import model.User;

import java.io.File;

public class Main extends Application {

    public static Main main;
    public static User user;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Authentication.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        main = this;


    }

    public static Parent getParent(String filename) throws Exception{
        return FXMLLoader.load(main.getClass().getResource(filename));

    }


    public static Client client;
    public static Stage stage;
    public static void main(String[] args) {
        client = new Client();
        launch(args);

    }
}

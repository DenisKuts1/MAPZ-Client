package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Mark;
import model.User;

import java.io.IOException;

public class AuthenticationController {

    @FXML
    private Button authorBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label infoLbl;

    @FXML
    private TextField loginField;

    @FXML
    private Button regBtn;

    @FXML
    public void about(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"В цьому вікні користувач може зареєструватись або ввійти в свій аккаунт",
                ButtonType.OK);
        alert.show();
    }

    @FXML
    void authorOnAction(ActionEvent event) {
        User user = Main.client.authorize(loginField.getText(), passwordField.getText());
        Main.user = user;
        if(user == null){
            infoLbl.setText("Wrong login or password");
        } else {

            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(Main.getParent("CourseListForm.fxml")));
                stage.show();
                ((Stage) regBtn.getScene().getWindow()).close();

            }catch (Exception e){

            }
        }
    }

    @FXML
    void regOnAction(ActionEvent event) {
        if(Main.client.register(loginField.getText(), passwordField.getText())){
            infoLbl.setText("Successful registration");
        } else{
            infoLbl.setText("Cannot register this user");
        }
    }

}

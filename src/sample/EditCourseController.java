package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditCourseController {
    public static boolean newCourse;

    public TextField link;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField description;

    @FXML
    private TextField title;

    @FXML
    void chooseVideo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Video Files", "*.mp4"));


        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            link.setText(file.getAbsolutePath());
        }
    }

    @FXML
    void save(ActionEvent event) {
        if(title.getText().equals("") || description.getText().equals("")){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING,"Після виконання цієї дії потрібно буде перезапустити програму.", ButtonType.OK, ButtonType.CANCEL);
        if(alert.showAndWait().get().equals(ButtonType.CANCEL))
            return;
        String oldLink = CourseController.course.getLink();
        String oldTitle = CourseController.course.getTitle();
        if (newCourse) {
            Main.client.createCourse(title.getText(), description.getText(), link.getText());
        } else {
            Main.client.updateCourse(title.getText(), description.getText(), link.getText());
        }
        if (!link.getText().equals("") || !oldTitle.equals(title.getText())) {
            File file = new File(oldLink);
            file.deleteOnExit();
        }
        Platform.exit();


    }

    @FXML
    void about(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"В цьому вікні модератор може змінити назву, опис або відео курсу, і відправити ці зміни на сервер. Після натиснення на " +
                "кнопку 'Save' програма виключиться для внесення всіх змін.", ButtonType.OK);
        alert.show();

    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = new Stage();
        try {
            if(newCourse) {
                stage.setScene(new Scene(Main.getParent("CourseListForm.fxml")));
            } else {
                stage.setScene(new Scene(Main.getParent("CourseForm.fxml")));
            }
            ((Stage) link.getScene().getWindow()).close();
            stage.show();
        }catch (Exception e){}
    }

    @FXML
    void initialize() {
        assert description != null : "fx:id=\"description\" was not injected: check your FXML file 'EditCourseForm.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'EditCourseForm.fxml'.";
        description.setText(CourseController.course.getDescription());
        title.setText(CourseController.course.getTitle());
    }
}
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
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EditCourseController {
    public static boolean newCourse;

    public TextField link;
    public Button delete;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField description;

    @FXML
    private TextField title;

    @FXML
    void delete(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.WARNING,"Ви дійсно хочете видалити цей курс? Після видалення програму потрібно буде перезапустити.",ButtonType.YES,ButtonType.NO);
        if(alert.showAndWait().get().equals(ButtonType.YES)){
            Main.client.deleteCourse();
            new File(CourseController.course.getLink()).deleteOnExit();
            Platform.exit();
        }
    }

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
        if (title.getText().equals("") || description.getText().equals("")) {
            return;
        }
        if (newCourse && link.getText().equals(""))
            return;
        for(String course : CourseController.courses){
            if(course.equals(title.getText())){
                if(!newCourse && course.equals(CourseController.course.getTitle())){
                    continue;
                }
                Alert alert = new Alert(Alert.AlertType.WARNING, "Курс з такою назвою вже існує", ButtonType.OK);
                alert.show();
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.WARNING, "Після виконання цієї дії потрібно буде перезапустити програму.", ButtonType.OK, ButtonType.CANCEL);
        if (alert.showAndWait().get().equals(ButtonType.CANCEL))
            return;
        if(!newCourse) {
            String oldLink = CourseController.course.getLink();
            String oldTitle = CourseController.course.getTitle();
            if (!link.getText().equals("") || !oldTitle.equals(title.getText())) {
                File file = new File(oldLink);
                file.deleteOnExit();
            }
        }
        if (newCourse) {
            Main.client.createCourse(title.getText(), description.getText(), link.getText());
        } else {
            Main.client.updateCourse(title.getText(), description.getText(), link.getText());
        }


        Platform.exit();


    }

    @FXML
    void about(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "В цьому вікні модератор може змінити назву, опис або відео курсу, і відправити ці зміни на сервер. Після натиснення на " +
                "кнопку 'Save' програма виключиться для внесення всіх змін.", ButtonType.OK);
        alert.show();

    }

    @FXML
    void cancel(ActionEvent event) {
        Stage stage = new Stage();
        try {
            if (newCourse) {
                stage.setScene(new Scene(Main.getParent("CourseListForm.fxml")));
            } else {
                stage.setScene(new Scene(Main.getParent("CourseForm.fxml")));
            }
            ((Stage) link.getScene().getWindow()).close();
            stage.show();
        } catch (Exception e) {
        }
    }

    @FXML
    void initialize() {
        assert description != null : "fx:id=\"description\" was not injected: check your FXML file 'EditCourseForm.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'EditCourseForm.fxml'.";
        delete.setVisible(false);
        if(!newCourse) {
            description.setText(CourseController.course.getDescription());
            title.setText(CourseController.course.getTitle());
            delete.setVisible(true);
        }
    }
}
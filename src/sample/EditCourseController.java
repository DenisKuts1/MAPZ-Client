package sample;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        String oldLink = CourseController.course.getLink();
        if (newCourse) {
            // Main.client.createCourse(title.getText(), description.getText(), link.getText());
        } else {
            Main.client.updateCourse(title.getText(), description.getText(), link.getText());
        }
        if (!link.getText().equals("")) {
            File file = new File(oldLink);
            file.deleteOnExit();

        }


    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert description != null : "fx:id=\"description\" was not injected: check your FXML file 'EditCourseForm.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'EditCourseForm.fxml'.";
        description.setText(CourseController.course.getDescription());
        title.setText(CourseController.course.getTitle());
    }
}
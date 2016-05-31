package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import model.Course;

import java.io.File;
import java.util.ArrayList;

public class CourseController {

    @FXML
    private Button openBtn;

    private static Course course;

    @FXML
    private ListView<String> list;

    @FXML
    private MediaView mediaPlayer;

    private MediaPlayer player;

    @FXML
    void openOnAction(ActionEvent event) {

    }

    @FXML
    void chooseCourse(MouseEvent event) {
        course = Main.client.course(list.getSelectionModel().getSelectedItem());
        if (course != null) {
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(Main.getParent("CourseForm.fxml")));
                stage.show();
                ((Stage) list.getScene().getWindow()).close();

            } catch (Exception e) {

            }
        }
    }

    void checkVideoExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            Media media = new Media(file.toURI().toString());
            player = new MediaPlayer(media);
            player.setAutoPlay(true);
            mediaPlayer.setMediaPlayer(player);
            player.pause();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Do you want to download video?", ButtonType.YES, ButtonType.NO);
            alert.show();
        }
    }

    @FXML
    void initialize() {
        assert openBtn != null : "fx:id=\"openBtn\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        if (course == null) {
            try {
                ArrayList<String> arrayList = Main.client.listOfCourses();
                ObservableList<String> items = FXCollections.observableArrayList(arrayList);
                list.getItems().addAll(items);
            } catch (Exception w) {

            }
        } else {
            checkVideoExist(course.getLink());
        }


    }

    @FXML
    void editOnAction(ActionEvent event) {

    }

    @FXML
    void evaluateOnAction(ActionEvent event) {

    }

    @FXML
    void play(ActionEvent event) {
        player.play();
    }

    @FXML
    void pause(ActionEvent event) {
        player.pause();
    }

    @FXML
    void stop(ActionEvent event) {
        player.stop();
    }

}

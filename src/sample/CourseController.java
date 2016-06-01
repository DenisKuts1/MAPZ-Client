package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import model.Comment;
import model.Course;
import model.Mark;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class CourseController {

    @FXML
    private Button openBtn;

    private static Course course;

    @FXML
    private ListView<String> list;

    @FXML
    private MediaView mediaPlayer;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ListView<String> comments;

    public static ProgressBar bar;

    private static MediaPlayer player;

    @FXML
    void openOnAction(ActionEvent event) {

    }

    public static CourseController courseController;

    @FXML
    void chooseCourse(MouseEvent event) {

        course = Main.client.course(list.getSelectionModel().getSelectedItem());
        if (course != null) {

            File file = new File(course.getLink());
            if (file.exists()) {
                openNewStage();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Do you want to download video?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get().equals(ButtonType.YES)){


                    Main.client.downloadFile(course.getLink());


                }
            }



        }
    }

    public void openNewStage(){

        try {


            File file = new File(course.getLink());
            Media media = new Media(file.toURI().toString());
            player = new MediaPlayer(media);
            //mediaPlayer.setMediaPlayer(player);
            //mediaPlayer.setVisible(true);
            player.setAutoPlay(true);
            player.pause();

            Stage stage = new Stage();

            stage.setScene(new Scene(Main.getParent("CourseForm.fxml")));
            ((Stage) list.getScene().getWindow()).close();
            stage.show();



        } catch (Exception e) {

        }
    }

    void checkVideoExist(String path) {

    }

    @FXML
    void initialize() {
        //assert openBtn != null : "fx:id=\"openBtn\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        //assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        bar = progressBar;
        courseController = this;
        if (course == null) {
            try {
                ArrayList<String> arrayList = Main.client.listOfCourses();
                ObservableList<String> items = FXCollections.observableArrayList(arrayList);
                list.getItems().addAll(items);
            } catch (Exception w) {

            }
        } else {
            mediaPlayer.setMediaPlayer(player);
            ArrayList<Mark> markArrayList = Main.client.getAllMarks(course);
            ArrayList<Comment> commentArrayList = Main.client.getAllComments(course);
            if(markArrayList != null) {
                ArrayList<String> strings = new ArrayList<>();
                for (Mark mark : markArrayList) {
                    Comment comment = null;
                    for(Comment c : commentArrayList){
                        if(c.getUser().getId() == mark.getUser().getId()){
                            comment = c;
                            break;
                        }
                    }
                    String comm = mark.getUser().getUserName() + ": " + mark.getMark();
                    if(comment != null){
                        comm += " | " + comment.getComment();
                    }
                    strings.add(comm);
                }
                ObservableList<String> items = FXCollections.observableArrayList(strings);
                comments.getItems().addAll(items);
            } else {
                System.out.println(2);
            }


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

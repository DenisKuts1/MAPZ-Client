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

    public TextField commentField;
    public Slider markSlider;
    public Button deleteComment;
    public Button editCourse;
    public Button createBtn;

    public static Course course;

    @FXML
    private ListView<String> list;

    @FXML
    private MediaView mediaPlayer;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ListView<String> comments;

    public static ProgressBar bar;


    public static MediaPlayer player;

    @FXML
    void create(ActionEvent event) {
        Stage stage = new Stage();
        try {
            EditCourseController.newCourse = true;
            stage.setScene(new Scene(Main.getParent("EditCourseForm.fxml")));
            ((Stage) list.getScene().getWindow()).close();
            stage.show();
        }catch (Exception e){}
    }

    @FXML
    void onDeleteComment(ActionEvent event) {
        String line = comments.getFocusModel().getFocusedItem();
        if(line != null){
            Main.client.deleteComment(line.substring(0,line.indexOf(':')));
            fillComments();
        }
    }

    public static CourseController courseController;

    @FXML
    void chooseCourse(MouseEvent event) {

        course = Main.client.course(list.getSelectionModel().getSelectedItem());
        if (course != null) {

            File file = new File(course.getLink());
            if (file.exists()) {
                openNewStage("CourseForm.fxml");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Do you want to download video?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get().equals(ButtonType.YES)){


                    Main.client.downloadFile(course.getLink());


                }
            }



        } else
        {
            // Надо перезагрузится
        }
    }

    public void openNewStage(String filename){

        try {


            File file = new File(course.getLink());
            Media media = new Media(file.toURI().toString());
            player = new MediaPlayer(media);
            //mediaPlayer.setMediaPlayer(player);
            //mediaPlayer.setVisible(true);
            player.setAutoPlay(true);
            player.pause();

            Stage stage = new Stage();

            stage.setScene(new Scene(Main.getParent(filename)));
            ((Stage) list.getScene().getWindow()).close();
            stage.show();



        } catch (Exception e) {

        }
    }

    public static ArrayList<String> courses;

    @FXML
    void initialize() {
        //assert openBtn != null : "fx:id=\"openBtn\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        //assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        bar = progressBar;
        courseController = this;
        if (course == null) {
            try {
                if(courses==null)
                    courses = Main.client.listOfCourses();
                ObservableList<String> items = FXCollections.observableArrayList(courses);
                list.getItems().addAll(items);
            } catch (Exception w) {

            }
        } else {

            if(!Main.user.isModerator()){
                editCourse.setVisible(false);
                deleteComment.setVisible(false);
            }
            mediaPlayer.setMediaPlayer(player);
            fillComments();
        }


    }

    void fillComments(){
        try {
            Thread.sleep(10);
        }catch (Exception e){}
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
            comments.getItems().clear();
            comments.getItems().addAll(items);
        } else {
            System.out.println(2);
        }
    }

    @FXML
    void editOnAction(ActionEvent event) {
        EditCourseController.newCourse = false;
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(Main.getParent("EditCourseForm.fxml")));
            ((Stage) comments.getScene().getWindow()).close();
            stage.show();
        }catch (Exception e){
            System.out.println(22);
        }
    }

    @FXML
    void evaluateOnAction(ActionEvent event) {
        int mark = (int) markSlider.getValue();
        String comment = commentField.getText();
        Main.client.addComment(mark, comment);
        try{
            Thread.sleep(10);
        } catch (Exception e){}
        fillComments();
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

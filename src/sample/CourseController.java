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
    public Button evaluate;
    public TextField descriprion;

    @FXML
    private ListView<String> list;

    @FXML
    private MediaView mediaPlayer;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private ListView<String> comments;

    public static ProgressBar bar;

    @FXML
    public void onAbout(){
        String line;
        if(Main.user.isModerator()){
            line = "В цьому вікні модератор може відкрити потрібний йому курс або створити новий.";
        } else {
            line = "В цьому вікні користувач може вибрати курс, який він хоче переглянути.";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION,line,ButtonType.OK);
        alert.show();
    }

    @FXML
    public void onBack(){
        try {
            courses = null;
            Stage stage = new Stage();
            stage.setScene(new Scene(Main.getParent("Authentication.fxml")));
            ((Stage) list.getScene().getWindow()).close();
            stage.show();
        }catch (Exception e){}
    }

    @FXML
    public void back(){
        try {
            courses = null;
            course = null;
            Stage stage = new Stage();
            stage.setScene(new Scene(Main.getParent("CourseListForm.fxml")));
            ((Stage) comments.getScene().getWindow()).close();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static MediaPlayer player;

    @FXML
    void about(ActionEvent event){
        String line;
        if(Main.user.isModerator()){
            line = "В цьому вікні модератор може переглянути відео курсу, оцінити курс, видалити коментарі інших користувачів а також перейти у вікно редагування курсу.";
        } else {
            line = "В цьому вікні користувач може переглянути відео курсу та оцінити курс й залишити коментар";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, line, ButtonType.OK);
        alert.show();
    }

    @FXML
    void create(ActionEvent event) {
        Stage stage = new Stage();
        System.out.println(1);
        try {
            EditCourseController.newCourse = true;
            System.out.println(2);
            stage.setScene(new Scene(Main.getParent("EditCourseForm.fxml")));
            System.out.println(3);
            ((Stage) list.getScene().getWindow()).close();
            System.out.println(4);
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if(progressBar!= null)
            bar = progressBar;
        courseController = this;
        if (course == null) {
            try {
                if(courses==null)
                    courses = Main.client.listOfCourses();
                ObservableList<String> items = FXCollections.observableArrayList(courses);
                list.getItems().addAll(items);
                progressBar.setProgress(0);
                if(!Main.user.isModerator()){
                    createBtn.setVisible(false);
                }
            } catch (Exception w) {

            }
        } else {
            try {

                if (!Main.user.isModerator()) {
                    editCourse.setVisible(false);
                    deleteComment.setVisible(false);
                }
                mediaPlayer.setMediaPlayer(player);
                descriprion.setText(course.getDescription());
                fillComments();
            }catch (Exception e){}
        }


    }

    void fillComments(){
        try {
            Thread.sleep(30);
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
            for(Mark marks : markArrayList){
                if(marks.getUser().getUserName().equals(Main.user.getUserName())){
                    evaluate.setVisible(false);
                    break;
                }
            }
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
            Thread.sleep(100);
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

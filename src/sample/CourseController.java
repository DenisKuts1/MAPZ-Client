package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class CourseController {

    @FXML
    private Button openBtn;

    @FXML
    private ListView<String> list;

    @FXML
    void openOnAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert openBtn != null : "fx:id=\"openBtn\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        assert list != null : "fx:id=\"list\" was not injected: check your FXML file 'CourseListForm.fxml'.";
        try {
            ArrayList<String> arrayList = Main.client.listOfCourses();
            ObservableList<String> items = FXCollections.observableArrayList (arrayList);
            list.getItems().addAll(items);
        } catch (Exception w){

        }

    }

}

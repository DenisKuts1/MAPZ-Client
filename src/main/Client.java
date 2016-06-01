package main;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.Course;
import model.Mark;
import model.User;
import sample.CourseController;
import sample.Main;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

/**
 * Created by denak on 16.05.2016.
 */
public class Client {

    Socket socket;
    public boolean downloading = false;
    public long sizeOfFile = 0;
    public int alreadyReaded = 0;

    public Client() {
    }

    public ArrayList<Mark> getAllMarks(Course course){
        try {
            socket = new Socket("localhost", 7755);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("marks\n" + course.getTitle());
            return getListOfMarks();
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<Mark> getListOfMarks() throws Exception{
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        ArrayList<Mark> list = (ArrayList<Mark>)inputStream.readObject();
        System.out.println(list.size());
        return list;
    }

    public ArrayList<String> listOfCourses() {
        try {
            socket = new Socket("localhost", 7755);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("courses");
            return getListOfCourses();
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<String> getListOfCourses() throws Exception{
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return (ArrayList<String>)inputStream.readObject();
    }

    public void downloadFile(String filename){
        try{
            socket = new Socket("localhost", 7755);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("download\n" + filename);
            outputStream.flush();
            download(filename);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void download(String filename) throws Exception{
        /*BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filename));
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        byte[] byteArray = new byte[8192];
        while ((in = bis.read(byteArray)) != -1){
            bos.write(byteArray,0,in);
        }
        bis.close();
        bos.close();*/
        downloading = true;


        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filename));

                byte[] byteArray = new byte[8192];
                sizeOfFile = (long)inputStream.readObject();
                alreadyReaded = 0;
                System.out.println(sizeOfFile);
                int in;
                while ((in = bis.read(byteArray)) != -1){
                    bos.write(byteArray,0,in);
                    alreadyReaded += 8192;
                    double a =  1.0 * alreadyReaded / sizeOfFile;
                    System.out.println(a);
                    try{
                        Thread.sleep(1);
                    }catch (Exception e){}
                    updateValue(a);
                }
                bis.close();
                bos.close();
                inputStream.close();
                System.out.println(1);
                //CourseController.courseController.openNewStage();
                System.out.println(1);
                return 1.0;
            }
        };

        CourseController.bar.progressProperty().unbind();
        CourseController.bar.progressProperty().bind(task.valueProperty());
        new Thread(task).start();





    }




    public Course course(String name){
        try{
            socket = new Socket("localhost", 7755);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("course\n" + name);
            outputStream.flush();
            return getCourse();
        }catch (Exception e){
            return null;
        }
    }

    private Course getCourse()throws Exception{
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return (Course)inputStream.readObject();
    }


    public boolean register(String login, String password) {
        try {
            socket = new Socket("localhost", 7755);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("register\n" + login + "\n" + password);
            outputStream.flush();
            return isRegister();
        } catch (IOException e) {
            return true;
        }
    }

    private boolean isRegister() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("success");
            Boolean bool = (Boolean) inputStream.readObject();
            System.out.println("success");
            return bool;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public User authorize(String login, String password) {
        try {
            socket = new Socket("localhost", 7755);
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF("authorize\n" + login + "\n" + password);
            outputStream.flush();
            return getUser();
        } catch (IOException e) {
            return null;
        }

    }

    private User getUser() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("success");
            User user = (User) inputStream.readObject();
            System.out.println("success");
            return user;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}

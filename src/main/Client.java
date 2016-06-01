package main;

import javafx.collections.ObservableList;
import model.Course;
import model.User;

import java.io.DataOutputStream;
import java.io.*;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by denak on 16.05.2016.
 */
public class Client {

    Socket socket;

    public Client() {
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

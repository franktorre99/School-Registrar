package com.example.schoolregistrar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;

import static javafx.fxml.FXMLLoader.load;

public class SchoolRegistrarApplication extends Application {
    
    public static Stage stage;
    public static Scene scene;
    public static Firestore fstore;
    public static FirebaseAuth fauth;
    private final FirestoreContext contxtFirebase = new FirestoreContext();

    @Override
    public void start(Stage theStage) throws IOException {
        fstore = contxtFirebase.firebase();
        fauth = FirebaseAuth.getInstance();
        stage=theStage;
        FXMLLoader fxmlLoader = new FXMLLoader(SchoolRegistrarApplication.class.getResource("login.fxml"));

        scene= new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
     static Stage getStage(){return stage;}
     static Scene getScene(){return scene;}
     static void openNewStage(String fileName, String title){
         FXMLLoader fxmlLoader = new FXMLLoader(SchoolRegistrarApplication.class.getResource(fileName));
         Stage stage = SchoolRegistrarApplication.getStage();
         Scene scene = SchoolRegistrarApplication.getScene();
         try {
             scene.setRoot(fxmlLoader.load());
             stage.setTitle(title);
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
     }

    static void openNewWindow(String fileName, String title) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(SchoolRegistrarApplication.class.getResource(fileName));
            Scene scene = new Scene(fxmlLoader.load(), 630, 400);
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new IOException();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}
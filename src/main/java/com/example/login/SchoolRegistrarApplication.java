package com.example.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;

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
//        FileInputStream serviceAccount =
//                new FileInputStream("src/main/resources/com/example/login/key.json");
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .build();

//        FirebaseApp.initializeApp(options);
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

    public static void main(String[] args) {
        launch();
    }
}
package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class StudentRegistrationController {
    @FXML
    private Label registration;
    @FXML
    private MenuBar homeMenu;
    @FXML
    private Menu home;
    @FXML
    private Label semester;
    @FXML
    private Label subject;
    @FXML
    private Label course;
    @FXML
    private Label time;
    @FXML
    private Label professor;
    @FXML
    private ChoiceBox<String> semesterChoice;
    @FXML
    private ChoiceBox<String> subjectChoice;
    @FXML
    private ChoiceBox<String> courseChoice;
    @FXML
    private ChoiceBox<String> timeChoice;
    @FXML
    private ChoiceBox<String> professorChoice;
    @FXML
    private Label userName;
    @FXML
    private Circle profilePic;
    public static Student user;
    static boolean key;
    private String selectedSubject;

    private void readFirebaseName(){
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(LoginController.type.toLowerCase()+"s").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Getting (reading) data from firebase database....");
                for (QueryDocumentSnapshot document : documents) {
                    if(LoginController.user.equals(document.getData().get("email"))) {
                        userName.setText((String) document.getData().get("name"));
                    }
                }
            }
            else
            {
                System.out.println("No data");
            }
        }
        catch (InterruptedException | ExecutionException ex){
            ex.printStackTrace();
        }
        LoginController.key=true;
    }

    public void initialize() throws IOException {
        readFirebaseName();

        readSubjects();
        subjectChoice.setOnAction(event -> {
            selectedSubject = subjectChoice.getSelectionModel().getSelectedItem();
            courseChoice.getItems().clear();
            readCourses(selectedSubject);
        });



    }

    public void handleHomeMenuButtonClicked() throws IOException {
        LoginController.dashboardChooser(LoginController.type);
    }

    public boolean readSubjects() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("departments").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    subjectChoice.getItems().add(doc.getId());
                }
            }
            else
            {
                System.out.println("No data");
            }
            key=true;

        }
        catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }
        return key;
    }

    public boolean readCourses(String subject) {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("courses").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    if (doc.getId().substring(0, 3).equals(subject)) {
                        courseChoice.getItems().add(doc.getData().get("Course Name").toString());
                    }
                }
            }
            else
            {
                System.out.println("No data");
            }
            key=true;

        }
        catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }
        return key;
    }
}

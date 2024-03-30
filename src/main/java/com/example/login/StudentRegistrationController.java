package com.example.login;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.shape.Circle;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class StudentRegistrationController {
    @FXML
    private Label registration;
    @FXML
    private Menu homeMenu;
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
    @FXML
    private List<ObservableList<String>> listsList;
    @FXML
    private List<ChoiceBox<String>> choiceBoxes;
    private void readFirebaseName(){
        ApiFuture<QuerySnapshot> future =  LoginApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(LoginController.type.toLowerCase()+"s").get();
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

    private void readAllData(Object r, int i, ObservableList<String> ol, ChoiceBox<String> cb) {
            if(i%2==0){
             DocumentReference dr= (DocumentReference) r;
             ol.add(dr.getId());
             Iterable<CollectionReference> crs=dr.listCollections();
             for(CollectionReference cr: crs){
                 try {
                         if(cr.getId().equals(cb.getSelectionModel().getSelectedItem())||i==0) {
                             readAllData(cr, i + 1, listsList.get(i + 1), choiceBoxes.get(i));
                         }
                 }
                 catch(ArrayIndexOutOfBoundsException e){
                     return;
                 }
             }
            }
            else{
                CollectionReference cr=(CollectionReference) r;
                ol.add(cr.getId());
                Iterable<DocumentReference> drs=cr.listDocuments();
                for(DocumentReference dr: drs){
                    try {
                        if(dr.getId().equals(cb.getSelectionModel().getSelectedItem())||i==0) {
                            readAllData(dr, i + 1, listsList.get(i + 1), choiceBoxes.get(i));
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                        return;
                    }
                }
            }
     }


    public void initialize(){
        readFirebaseName();
        ObservableList<String> semesters=semesterChoice.getItems();
        ObservableList<String> subjects=subjectChoice.getItems();
        ObservableList<String> courses=courseChoice.getItems();
        ObservableList<String> times=timeChoice.getItems();
        ObservableList<String> professors=professorChoice.getItems();
        listsList =new ArrayList<>();
        listsList.add(subjects);
        listsList.add(courses);
        listsList.add(semesters);
        listsList.add(times);
        listsList.add(professors);
        choiceBoxes=new ArrayList<>();
        choiceBoxes.add(subjectChoice);
        choiceBoxes.add(courseChoice);
        choiceBoxes.add(semesterChoice);
        choiceBoxes.add(timeChoice);
        choiceBoxes.add(professorChoice);
        CollectionReference root=LoginApplication.fstore.collection("classes");
        ApiFuture<QuerySnapshot> future = root.get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Getting (reading) data from firebase database....");
                for (QueryDocumentSnapshot document : documents) {
                    readAllData(document.getReference(), 0, listsList.get(0),choiceBoxes.get(0));
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
    }
    public void handleHomeMenuButtonClicked(){
        LoginApplication.openNewStage("studenthomepage.fxml","Student Homepage");
    }
}

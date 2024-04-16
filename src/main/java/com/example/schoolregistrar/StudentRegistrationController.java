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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
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
    @FXML
    private List<ObservableList<String>> listsList;
    @FXML
    private List<ChoiceBox<String>> choiceBoxes;

    ObservableList<String> semesters;
    ObservableList<String> subjects;
    ObservableList<String> courses;
    ObservableList<String> times;
    ObservableList<String> professors;

//    private void deleteFuture(int i){
//        if(i==1) {
//            courses.clear();
//        }
//        else if(i<=2) {
//            semesters.clear();
//        }
//        else if(i<=3) {
//            times.clear();
//        }
//        professors.clear();
//        for(int j=i; j<5; j++) {
//            choiceBoxes.get(j).setValue("");
//        }
//    }
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

    public void initialize(){
        readFirebaseName();
        semesters=semesterChoice.getItems();
        subjects=subjectChoice.getItems();
        courses=courseChoice.getItems();
        times=timeChoice.getItems();
        professors=professorChoice.getItems();
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
        CollectionReference root= SchoolRegistrarApplication.fstore.collection("classes");
        ApiFuture<QuerySnapshot> future = root.get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            System.out.println("Getting (reading) data from firebase database....");
            for (QueryDocumentSnapshot document : documents) {
                listsList.get(0).add(document.getId());
            }

            List<CollectionReference> collections = new ArrayList<>();
            choiceBoxes.get(0).getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
//                    deleteFuture(1);
                    DocumentReference document = documents.get(newValue.intValue()).getReference();
                    Iterable<CollectionReference> crs = document.listCollections();
                    crs.forEach(collections::add);
                    for (CollectionReference cr : collections) {
                        listsList.get(1).add(cr.getId());
                    }
                }
            });

            List<DocumentReference> docs = new ArrayList<>();
            choiceBoxes.get(1).getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
//                    deleteFuture(2);
                    CollectionReference collection = collections.get(newValue.intValue());
                    Iterable<DocumentReference> drs = collection.listDocuments();
                    drs.forEach(docs::add);

                    for (DocumentReference dr : docs) {
                        listsList.get(2).add(dr.getId());
                    }
                }
            });
            choiceBoxes.get(2).getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
//                    deleteFuture(3);
                    DocumentReference document = docs.get(newValue.intValue());
                    Iterable<CollectionReference> crs = document.listCollections();
                    collections.clear();
                    crs.forEach(collections::add);
                    for (CollectionReference cr : collections) {
                        listsList.get(3).add(cr.getId());
                    }
                }
            });
            choiceBoxes.get(3).getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number value, Number newValue) {
//                    deleteFuture(4);
                    CollectionReference collection = collections.get(newValue.intValue());
                    Iterable<DocumentReference> drs = collection.listDocuments();
                    docs.clear();
                    drs.forEach(docs::add);

                    for (DocumentReference dr : docs) {
                        listsList.get(4).add(dr.getId());
                    }
                }
            });

        }
        catch (InterruptedException | ExecutionException ex){
            ex.printStackTrace();
        }
    }
    public void handleHomeMenuButtonClicked() throws IOException {
        LoginController.dashboardChooser(LoginController.type);
    }
}

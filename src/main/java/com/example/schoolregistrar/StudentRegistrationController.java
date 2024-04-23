package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private MenuButton courseChoice;
    @FXML
    private ChoiceBox<String> timeChoice;
    @FXML
    private ChoiceBox<String> professorChoice;
    @FXML
    private Label userName;
    @FXML
    private Circle profilePic;
    @FXML
    private TableView<RegisterSection> searchTable;
    @FXML
    private TableColumn<RegisterSection, String> crnColumn;
    @FXML
    private TableColumn<RegisterSection, String> courseNameColumn;
    @FXML
    private TableColumn<RegisterSection, String> semesterColumn;
    @FXML
    private TableColumn<RegisterSection, String> timeColumn;
    @FXML
    private TableColumn<RegisterSection, String> professorColumn;
    public static Student user;
    static boolean key;
    private String selectedSubject;
    private String selectedCourse;
    private String selectedTime;
    private String selectedProfessor;
    private String selectedSemester;

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
        crnColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("crn"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("courseName"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("semester"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("time"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("professor"));
        readFirebaseName();

        timeChoice.getItems().add("8:00 AM - 9:15 AM");
        timeChoice.getItems().add("9:25 AM - 10:40 AM");
        timeChoice.getItems().add("10:50 AM - 12:05 PM");
        timeChoice.getItems().add("12:15 PM - 1:30 PM");
        timeChoice.getItems().add("1:40 PM - 2:55 PM");
        timeChoice.getItems().add("3:05 PM - 4:20 PM");
        timeChoice.setOnAction(e -> selectedTime = timeChoice.getSelectionModel().getSelectedItem());

        semesterChoice.getItems().add("Summer 2024");
        semesterChoice.getItems().add("Fall 2024");
        semesterChoice.getItems().add("Winter 2024");
        semesterChoice.getItems().add("Spring 2025");
        semesterChoice.setOnAction(e -> selectedSemester = semesterChoice.getSelectionModel().getSelectedItem());

        readProfessors();
        professorChoice.setOnAction(e -> selectedProfessor = professorChoice.getSelectionModel().getSelectedItem());

        readSubjects();
        subjectChoice.setOnAction(event -> {
            selectedSubject = subjectChoice.getSelectionModel().getSelectedItem();
            courseChoice.getItems().clear();
            courseChoice.setText("");
            readCourses(selectedSubject);
        });
    }

    public void handleHomeMenuButtonClicked() throws IOException {
        LoginController.dashboardChooser(LoginController.type);
    }

    public void handleSearch() {
        search(selectedSubject, selectedCourse, selectedSemester, selectedTime, selectedProfessor);
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
                        courseChoice.getItems().add(new MenuItem(doc.getData().get("Department").toString() + " "
                                + doc.getData().get("Course Number").toString() + " "
                                + doc.getData().get("Course Name").toString()));
                    }
                }
                for (MenuItem item : courseChoice.getItems()) {
                    item.setOnAction(e -> {
                        selectedCourse = item.getText().substring(0, 7);
                        courseChoice.setText(item.getText().substring(8));
                    });
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

    public boolean readProfessors() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh")
                .collection("professors").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    professorChoice.getItems().add(doc.getId());
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

    public boolean search(String subject, String course, String semester, String time, String professor) {
        key = false;
        ApiFuture<QuerySnapshot> future = null;
        List<QueryDocumentSnapshot> documents;
        if (time == null && professor == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester)) {
                            searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                    , doc.getData().get("Course Name").toString()
                                    , doc.getData().get("Semester").toString()
                                    , doc.getData().get("Time").toString()
                                    , doc.getData().get("Professor First Name").toString() + " "
                                    + doc.getData().get("Professor Last Name")));
                        }
                    }
                }
                else {
                    System.out.println("No data");
                }
                key=true;
            }
            catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }

        return key;
    }
}

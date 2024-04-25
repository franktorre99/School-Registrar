package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class StudentRegistrationController {
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
    @FXML
    private Button registerButton;
    static boolean key;
    private String selectedSubject;
    private String selectedCourse;
    private String selectedCourseName;
    private String selectedTime;
    private String selectedProfessor;
    private String selectedSemester;
    private String registerSection;
    private String registerCourse;
    private String registerTime;
    private String registerProfessor;
    private String registerSemester;

    private void readFirebaseName(){
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(LoginController.type.toLowerCase()+"s").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                System.out.println("Getting (reading) data from firebase database....");
                for (QueryDocumentSnapshot document : documents) {
                    if(LoginController.user.equals(document.getData().get("email"))) {
                        userName.setText((String) document.getData().get("name"));
                    }
                }
            }
            else {
                System.out.println("No data");
            }
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        LoginController.key=true;
    }

    public void initialize() throws IOException {
        registerButton.disableProperty().bind(searchTable.getSelectionModel().selectedItemProperty().isNull());

        crnColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("crn"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("courseName"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("semester"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("time"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("professor"));
        searchTable.setOnMouseClicked(e -> {
            try {
                registerSection = searchTable.getSelectionModel().getSelectedItem().getCrn();
                registerCourse = searchTable.getSelectionModel().getSelectedItem().getCourseName();
                registerTime = searchTable.getSelectionModel().getSelectedItem().getTime();
                registerSemester = searchTable.getSelectionModel().getSelectedItem().getSemester();
                registerProfessor = searchTable.getSelectionModel().getSelectedItem().getProfessor();
            }
            catch (NullPointerException ex) {}
        });
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
        searchTable.getItems().clear();
        search(selectedCourse, selectedSemester, selectedTime, selectedProfessor);
    }

    public void handleRegister() {
        addToRoster(selectedCourse, registerSection);
        addToSchedule();
    }

    public void handleClearTime() {
        clear(timeChoice);
    }

    public void handleClearProfessor() {
        clear(professorChoice);
    }

    public void clear(ChoiceBox<String> choiceBox) {
        choiceBox.getSelectionModel().clearSelection();
    }

    public boolean readSubjects() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("departments").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty()) {
                for (QueryDocumentSnapshot doc : documents) {
                    subjectChoice.getItems().add(doc.getId());
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
        return key;
    }

    public boolean readCourses(String subject) {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("courses").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty()) {
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
                        selectedCourseName = item.getText().substring(8);
                        courseChoice.setText(item.getText().substring(8));
                    });
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
        return key;
    }

    public boolean readProfessors() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh")
                .collection("professors").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty()) {
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
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    public boolean search(String course, String semester, String time, String professor) {
        key = false;
        ApiFuture<QuerySnapshot> future = null;
        List<QueryDocumentSnapshot> documents;
        if (time == null && professor == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(course)
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
        else if (time == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester))
                            if ((doc.getData().get("Professor First Name").toString() + " " + doc.getData().get("Professor Last Name").toString()).equals(professor)) {
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
        else if (professor == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester))
                            if (doc.getData().get("Time").toString().equals(time)) {
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
        else {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester))
                            if (doc.getData().get("Time").toString().equals(time) &&
                                    (doc.getData().get("Professor First Name").toString() + " " + doc.getData().get("Professor Last Name").toString()).equals(professor)) {
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

    public void addToRoster(String course, String section) {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("courses")
                .document(course)
                .collection("sections")
                .document(section)
                .collection("roster")
                .document(StudentDashboardController.user.getFirstName() + " " + StudentDashboardController.user.getLastName());

        Map<String, Object> data = new HashMap<>();
        data.put("First Name", StudentDashboardController.user.getFirstName());
        data.put("Last Name", StudentDashboardController.user.getLastName());
        data.put("ID", StudentDashboardController.user.getId());

        ApiFuture<WriteResult> result = docRef.set(data);
    }

    public void addToSchedule() {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh")
                .collection("students")
                .document(StudentDashboardController.user.getFirstName() + " " + StudentDashboardController.user.getLastName())
                .collection("schedule")
                .document(registerSection);

        Map<String, Object> data = new HashMap<>();
        data.put("CRN", registerSection);
        data.put("Department", selectedCourse.substring(0, 3));
        data.put("Course Number", selectedCourse.substring(4, 7));
        data.put("Course Name", registerCourse);
        data.put("Time", registerTime);
        data.put("Semester", registerSemester);
        data.put("Professor", registerProfessor);

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

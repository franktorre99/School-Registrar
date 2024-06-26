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
    private ChoiceBox<String> daysChoice;
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
    private TableColumn<RegisterSection, String> daysColumn;
    @FXML
    private Button registerButton;
    @FXML
    private Button searchButton;
    static boolean key;
    private String selectedSubject;
    private String selectedCourse;
    private String selectedTime;
    private String selectedProfessor;
    private String selectedSemester;
    private String selectedDays;
    private String registerSection;
    private String registerCourse;
    private String registerTime;
    private String registerProfessor;
    private String registerSemester;
    private String registerDays;
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<String> semesters = new ArrayList<>();

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
        readSchedule();
        registerButton.disableProperty().bind(searchTable.getSelectionModel().selectedItemProperty().isNull());
        searchButton.disableProperty().bind(courseChoice.textProperty().isEqualToIgnoreCase("").or(semesterChoice.getSelectionModel().selectedItemProperty().isNull().or(subjectChoice.getSelectionModel().selectedItemProperty().isNull())));
        crnColumn.setCellValueFactory(new PropertyValueFactory<>("crn"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<>("professor"));
        daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));
        searchTable.setOnMouseClicked(e -> {
            try {
                registerSection = searchTable.getSelectionModel().getSelectedItem().getCrn();
                registerCourse = searchTable.getSelectionModel().getSelectedItem().getCourseName();
                registerTime = searchTable.getSelectionModel().getSelectedItem().getTime();
                registerSemester = searchTable.getSelectionModel().getSelectedItem().getSemester();
                registerProfessor = searchTable.getSelectionModel().getSelectedItem().getProfessor();
                registerDays = searchTable.getSelectionModel().getSelectedItem().getDays();
            }
            catch (NullPointerException ex) {
                throw new RuntimeException(ex);
            }
        });
        readFirebaseName();

        timeChoice.getItems().add("8:00 AM - 9:15 AM");
        timeChoice.getItems().add("9:25 AM - 10:40 AM");
        timeChoice.getItems().add("10:50 AM - 12:05 PM");
        timeChoice.getItems().add("12:15 PM - 1:30 PM");
        timeChoice.getItems().add("1:40 PM - 2:55 PM");
        timeChoice.getItems().add("3:05 PM - 4:20 PM");
        timeChoice.setOnAction(e -> selectedTime = timeChoice.getSelectionModel().getSelectedItem());

        daysChoice.getItems().add("M/W");
        daysChoice.getItems().add("T/R");
        daysChoice.getItems().add("M");
        daysChoice.getItems().add("T");
        daysChoice.getItems().add("W");
        daysChoice.getItems().add("R");
        daysChoice.setOnAction(e -> selectedDays = daysChoice.getSelectionModel().getSelectedItem());

        semesterChoice.getItems().add("Spring 2024");
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

    public void handleHome() throws IOException {
        SchoolRegistrarApplication.openNewStage("studentdashboard.fxml", "Student Dashboard");
    }

    public void handleSearch() {
        searchTable.getItems().clear();
        search(selectedCourse, selectedSemester, selectedTime, selectedProfessor, selectedDays);
    }

    public void handleRegister() {
        boolean conflict = false;
        for (String semester : semesters) {
            if (semester.equals(registerSemester)) {
                for (String time : times) {
                    if (time.equals(registerTime)) {
                        for (String days : days) {
                            if (days.equals(registerDays)) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Cannot register for 2 classes on the same day at the same time");
                                alert.setTitle("Schedule Conflict");
                                alert.show();
                                conflict = true;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }

        if (!conflict) {
            addToRoster(selectedCourse, registerSection);
            addToSchedule();
        }
    }

    public void handleClearTime() {
        clear(timeChoice);
    }

    public void handleClearProfessor() {
        clear(professorChoice);
    }

    public void handleClearDays() {
        clear(daysChoice);
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
            key=true;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    public boolean readSchedule() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh")
                .collection("students")
                .document(StudentDashboardController.user.getFirstName() + " " + StudentDashboardController.user.getLastName())
                .collection("schedule").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                for (QueryDocumentSnapshot doc : documents) {
                    times.add(doc.getData().get("Time").toString());
                    days.add(doc.getData().get("Days").toString());
                    semesters.add(doc.getData().get("Semester").toString());
                }
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
                        courseChoice.setText(item.getText().substring(8));
                    });
                }
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
            key=true;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    public boolean search(String course, String semester, String time, String professor, String days) {
        key = false;
        ApiFuture<QuerySnapshot> future = null;
        List<QueryDocumentSnapshot> documents;
        if (time == null && professor == null && days == null) {
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
                                    + doc.getData().get("Professor Last Name")
                                    , doc.getData().get("Days").toString()));
                        }
                    }
                }
                key=true;
            }
            catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
        else if (time == null && days == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester))
                            if ((doc.getData().get("Professor First Name").toString()
                                    + " " + doc.getData().get("Professor Last Name").toString()).equals(professor)) {
                                searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                        , doc.getData().get("Course Name").toString()
                                        , doc.getData().get("Semester").toString()
                                        , doc.getData().get("Time").toString()
                                        , doc.getData().get("Professor First Name").toString() + " "
                                        + doc.getData().get("Professor Last Name")
                                        , doc.getData().get("Days").toString()));
                            }
                    }
                }
                key=true;
            }
            catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
        else if (time == null && professor == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester))
                            if ((doc.getData().get("Days").toString().equals(days))) {
                                searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                        , doc.getData().get("Course Name").toString()
                                        , doc.getData().get("Semester").toString()
                                        , doc.getData().get("Time").toString()
                                        , doc.getData().get("Professor First Name").toString() + " "
                                        + doc.getData().get("Professor Last Name")
                                        , doc.getData().get("Days").toString()));
                            }
                    }
                }
                key=true;
            }
            catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
        else if (professor == null && days == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester)
                                && doc.getData().get("Time").toString().equals(time)) {
                            searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                    , doc.getData().get("Course Name").toString()
                                    , doc.getData().get("Semester").toString()
                                    , doc.getData().get("Time").toString()
                                    , doc.getData().get("Professor First Name").toString() + " "
                                    + doc.getData().get("Professor Last Name")
                                    , doc.getData().get("Days").toString()));
                        }
                    }
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
                            if ((doc.getData().get("Professor First Name").toString()
                                    + " " + doc.getData().get("Professor Last Name").toString()).equals(professor)
                                    && doc.getData().get("Days").toString().equals(days)) {
                                searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                        , doc.getData().get("Course Name").toString()
                                        , doc.getData().get("Semester").toString()
                                        , doc.getData().get("Time").toString()
                                        , doc.getData().get("Professor First Name").toString() + " "
                                        + doc.getData().get("Professor Last Name")
                                        , doc.getData().get("Days").toString()));
                            }
                    }
                }
                key=true;
            }
            catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
            }
        }
        else if (days == null) {
            future = SchoolRegistrarApplication.fstore.collection("courses")
                    .document(selectedCourse)
                    .collection("sections").get();
            try {
                documents = future.get().getDocuments();
                if(!documents.isEmpty()) {
                    for (QueryDocumentSnapshot doc : documents) {
                        if (doc.getData().get("Semester").toString().equals(semester))
                            if (doc.getData().get("Time").toString().equals(time) &&
                                    (doc.getData().get("Professor First Name").toString()
                                            + " " + doc.getData().get("Professor Last Name").toString()).equals(professor)) {
                                searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                        , doc.getData().get("Course Name").toString()
                                        , doc.getData().get("Semester").toString()
                                        , doc.getData().get("Time").toString()
                                        , doc.getData().get("Professor First Name").toString() + " "
                                        + doc.getData().get("Professor Last Name")
                                        , doc.getData().get("Days").toString()));
                            }
                    }
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
                            if (doc.getData().get("Days").toString().equals(days)
                                    && doc.getData().get("Time").toString().equals(time)) {
                                searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                        , doc.getData().get("Course Name").toString()
                                        , doc.getData().get("Semester").toString()
                                        , doc.getData().get("Time").toString()
                                        , doc.getData().get("Professor First Name").toString() + " "
                                        + doc.getData().get("Professor Last Name")
                                        , doc.getData().get("Days").toString()));
                            }
                    }
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
                            if (doc.getData().get("Days").toString().equals(days)
                                    && doc.getData().get("Time").toString().equals(time)
                                    && (doc.getData().get("Professor First Name").toString()
                                    + " " + doc.getData().get("Professor Last Name").toString()).equals(professor)) {
                                searchTable.getItems().add(new RegisterSection(doc.getData().get("CRN").toString()
                                        , doc.getData().get("Course Name").toString()
                                        , doc.getData().get("Semester").toString()
                                        , doc.getData().get("Time").toString()
                                        , doc.getData().get("Professor First Name").toString() + " "
                                        + doc.getData().get("Professor Last Name")
                                        , doc.getData().get("Days").toString()));
                            }
                    }
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
        data.put("Days", registerDays);

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AddSectionController {
    @FXML private MenuButton courseMenu;
    @FXML private TextField crnTextField;
    @FXML private MenuButton timeMenu;
    @FXML private MenuButton semesterMenu;
    @FXML private MenuButton professorMenu;
    @FXML private Button addButton;
    @FXML private MenuButton dayMenu;
    static boolean key;
    private Course selectedCourse;
    private String selectedTime;
    private Professor selectedProfessor;
    private String selectedSemester;
    private String selectedDays;

    public void initialize() {
        readCourses();
        readProfessors();
        for (MenuItem item : courseMenu.getItems()) {
            item.setOnAction(event -> {
                selectedCourse = new Course(item.getText().substring(0, 3), Integer.parseInt(item.getText().substring(4, 7)), item.getText().substring(8));
                courseMenu.setText(selectedCourse.getDepartment() + " " + selectedCourse.getCourseNumber());
            });
        }

        for (MenuItem item : timeMenu.getItems()) {
            item.setOnAction(event -> {
                selectedTime = item.getText();
                timeMenu.setText(selectedTime);
            });
        }

        for (MenuItem item : semesterMenu.getItems()) {
            item.setOnAction(event -> {
                selectedSemester = item.getText();
                semesterMenu.setText(selectedSemester);
            });
        }

        for (MenuItem item : professorMenu.getItems()) {
            item.setOnAction(event -> {
                selectedProfessor = new Professor(item.getText().substring(0, item.getText().indexOf(" "))
                        , item.getText().substring(item.getText().indexOf(" ") + 1, item.getText().lastIndexOf(" "))
                        , Integer.parseInt(item.getText().substring(item.getText().lastIndexOf(" ") + 1)));
                professorMenu.setText(selectedProfessor.toString());
            });
        }

        for (MenuItem item : dayMenu.getItems()) {
            item.setOnAction(event -> {
                selectedDays = item.getText();
                dayMenu.setText(selectedDays);
            });
        }

        addButton.disableProperty().bind(courseMenu.textProperty().isEqualToIgnoreCase("Select Course").or(crnTextField.textProperty().isEmpty().or(semesterMenu.textProperty().isEqualToIgnoreCase("Select Semester").or(timeMenu.textProperty().isEqualToIgnoreCase("Select Time").or(professorMenu.textProperty().isEqualToIgnoreCase("Select Professor"))))));
    }

    public void handleHome() throws IOException {
        SchoolRegistrarApplication.openNewStage("administratordashboard.fxml", "Home");
    }

    public void handleAdd() throws IOException {
        addSection();
        SchoolRegistrarApplication.openNewStage("administratordashboard.fxml", "Home");
    }

    public boolean readCourses() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("courses").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty()) {
                for (QueryDocumentSnapshot doc : documents) {
                    courseMenu.getItems().add(new MenuItem(doc.getId() + " " + doc.getData().get("Course Name")));
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
                    professorMenu.getItems().add(new MenuItem(doc.getId() + " " + doc.getData().get("ID")));
                }
            }
            key=true;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    public void addSection() {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("courses")
                .document(selectedCourse.getDepartment() + " " + selectedCourse.getCourseNumber())
                .collection("sections")
                .document(crnTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("CRN", Integer.parseInt(crnTextField.getText()));
        data.put("Course Name", selectedCourse.getCourseName());
        data.put("Course Number", selectedCourse.getCourseNumber());
        data.put("Department", selectedCourse.getDepartment());
        data.put("Time", selectedTime);
        data.put("Days", selectedDays);
        data.put("Semester", selectedSemester);
        data.put("Professor First Name", selectedProfessor.getFirstName());
        data.put("Professor Last Name", selectedProfessor.getLastName());
        data.put("Professor ID", selectedProfessor.getId());

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

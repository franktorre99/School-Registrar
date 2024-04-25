package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class SubmitFinalGradesController {
    @FXML private MenuButton selectSection;
    @FXML private TableView<Student> rosterTable;
    @FXML private TableColumn<Student, Integer> idColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private MenuButton selectGrade;
    @FXML private Button submitGradeButton;
    private String selectedSection;
    private String selectedCourse;
    private String selectedCourseName;
    private String selectedGrade;
    static boolean key;

    public void initialize() {
        submitGradeButton.disableProperty().bind(rosterTable.getSelectionModel().selectedItemProperty().isNull());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        for (Section section : ProfessorDashboardController.user.getSectionsTaught()) {
            selectSection.getItems().add(new MenuItem(section.getCrn() + " " + section.getCourse().toString()));
        }

        for (MenuItem item : selectSection.getItems()) {
            item.setOnAction(e -> {
                rosterTable.getItems().clear();
                selectedSection = item.getText().substring(0, 5);
                selectedCourse = item.getText().substring(6, 13);
                selectedCourseName = item.getText().substring(14);
                readRoster(selectedCourse, selectedSection);
                selectSection.setText(item.getText());
            });
        }

        for (MenuItem item : selectGrade.getItems()) {
            item.setOnAction(e -> {
                selectedGrade = item.getText();
                selectGrade.setText(item.getText());
            });
        }
    }

    public void handleSubmit() {
        addGrade();
    }

    public boolean readRoster(String course, String section) {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("courses")
                .document(course)
                .collection("sections")
                .document(section)
                .collection("roster").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                for (QueryDocumentSnapshot doc : documents) {
                    rosterTable.getItems().add(new Student(doc.getData().get("First Name").toString()
                            , doc.getData().get("Last Name").toString()
                            , Integer.parseInt(doc.getData().get("ID").toString())));
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

    public void addGrade() {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh")
                .collection("students")
                .document(rosterTable.getSelectionModel().getSelectedItem().getFirstName() + " "
                        + rosterTable.getSelectionModel().getSelectedItem().getLastName())
                .collection("grades")
                .document(selectedSection);

        Map<String, Object> data = new HashMap<>();
        data.put("CRN", selectedSection);
        data.put("Course Name", selectedCourseName);
        data.put("Grade", selectedGrade);

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}


package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AddCourseController {
    @FXML private MenuButton departmentMenu;
    @FXML private TextField courseNumberTextField;
    @FXML private TextField courseNameTextField;
    static boolean key;
    private String selectedDepartment;

    public void initialize() {
        readDepartments();
        for (MenuItem item : departmentMenu.getItems()) {
            item.setOnAction(event -> {
                selectedDepartment = item.getText();
                departmentMenu.setText(selectedDepartment);
            });
        }
    }

    public void handleAdd() {
        addCourse();
        ProfessorDashboardController.coursesAvailable.add(new Course(selectedDepartment
                , Integer.parseInt(courseNumberTextField.getText())
                , courseNameTextField.getText()));
    }

    public void handleHome() throws IOException {
        SchoolRegistrarApplication.openNewStage("administratordashboard.fxml", "Home");
    }

    public boolean readDepartments() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("departments").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    departmentMenu.getItems().add(new MenuItem(doc.getId()));
                }
            }
            key=true;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    public void addCourse() {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("courses")
                .document(selectedDepartment + " " + courseNumberTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("Course Name", courseNameTextField.getText());
        data.put("Course Number", Integer.parseInt(courseNumberTextField.getText()));
        data.put("Department", selectedDepartment);

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

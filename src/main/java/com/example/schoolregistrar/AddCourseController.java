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
    }

    public boolean readDepartments() {
        key = false;

        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("departments").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                for (QueryDocumentSnapshot doc : documents) {
                    departmentMenu.getItems().add(new MenuItem(doc.getId()));
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

    public void addCourse() {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("courses")
                .document(selectedDepartment + " " + courseNumberTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("CourseName", courseNameTextField.getText());
        data.put("CourseNumber", Integer.parseInt(courseNumberTextField.getText()));
        data.put("Department", selectedDepartment);

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

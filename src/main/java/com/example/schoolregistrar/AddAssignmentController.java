package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddAssignmentController {
    @FXML private TextField nameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker dueDatePicker;
    @FXML private MenuButton categoryMenu;
    @FXML private MenuItem homework;
    @FXML private MenuItem quiz;
    @FXML private MenuItem exam;
    private String selectedCategory = "";
    private LocalDate date;

    public void initialize() {
        homework.setOnAction(event -> {
            selectedCategory = homework.getText();
            categoryMenu.setText(homework.getText());
        });
        quiz.setOnAction(event -> {
            selectedCategory = quiz.getText();
            categoryMenu.setText(quiz.getText());
        });
        exam.setOnAction(event -> {
            selectedCategory = exam.getText();
            categoryMenu.setText(exam.getText());
        });
        dueDatePicker.setOnAction(event -> {
            date = dueDatePicker.getValue();
        });
    }
    public void addAssignment() {
        DocumentReference docRef = Application.fstore.collection("Assignments").document(UUID.randomUUID().toString());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameTextField.getText());
        data.put("Category", selectedCategory);
        data.put("Description", descriptionTextArea.getText());
        data.put("Due Date", date.toString());

        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

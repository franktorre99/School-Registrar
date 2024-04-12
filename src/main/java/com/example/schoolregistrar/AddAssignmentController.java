package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AddAssignmentController {
    @FXML private TextField nameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private DatePicker dueDatePicker;
    @FXML private MenuButton categoryMenu;
    @FXML private MenuButton sectionMenu;
    @FXML private MenuItem homework;
    @FXML private MenuItem quiz;
    @FXML private MenuItem exam;
    @FXML private TextField timeTextField;
    @FXML private RadioButton amRadioButton;
    @FXML private RadioButton pmRadioButton;
    private String selectedCategory = "";
    private String selectedTimeOfDay = "";
    private static String selectedSection;
    private static String selectedCourse;
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
        dueDatePicker.setOnAction(event -> date = dueDatePicker.getValue());
        amRadioButton.setOnAction(event -> selectedTimeOfDay = "AM");
        pmRadioButton.setOnAction(event -> selectedTimeOfDay = "PM");

        getSections(sectionMenu);
    }

    public void handleAddAssignment() {
        addAssignment(selectedCourse, selectedSection);
    }

    public void addAssignment(String course, String section) {
        String dueTime;
        int dueHour = Integer.parseInt(timeTextField.getText().substring(0, 2));
        int dueMinute = Integer.parseInt(timeTextField.getText().substring(3, 5));
        dueTime = (dueHour + " " + dueMinute + " " + selectedTimeOfDay);

        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("courses")
                .document(course)
                .collection("sections")
                .document(section)
                .collection("assignments")
                .document(nameTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameTextField.getText());
        data.put("Category", selectedCategory);
        data.put("Description", descriptionTextArea.getText());
        data.put("Due Date", date.toString());
        data.put("Due Time", dueTime.toString());

        ApiFuture<WriteResult> result = docRef.set(data);
    }

    private static void getSections(MenuButton menuButton) {
        for (Section section : ProfessorDashboardController.user.getSectionsTaught()) {
            menuButton.getItems().add(new MenuItem(section.getCrn() + " " + section.getCourse().toString()));
        }
        for (MenuItem item : menuButton.getItems()) {
            item.setOnAction(event -> {
                selectedSection = item.getText().substring(0, 5);
                selectedCourse = item.getText().substring(6, 13);
            });
        }
    }
}

package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
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

    @FXML private Button addButton;
    private String selectedCategory = "";
    private String selectedTimeOfDay = "";
    private static String selectedSection;
    private static String selectedCourse;
    private LocalDate date;
    public String selectedCategory = "";
    public String selectedTimeOfDay = "";
    public static String selectedSection;
    public static String selectedCourse;
    public LocalDate date;
    public static String category;
    public static String name = "";
    public static String description = "";


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
        addButton.disableProperty().bind(nameTextField.textProperty().isEmpty().or(descriptionTextArea.textProperty().isEmpty().or(dueDatePicker.valueProperty().isNull().or(sectionMenu.textProperty().isEqualToIgnoreCase("Select Section").or(categoryMenu.textProperty().isEqualToIgnoreCase("Category").or(timeTextField.textProperty().isEmpty().or(amRadioButton.selectedProperty().and(pmRadioButton.selectedProperty()))))))));
    }

    public void handleHome() throws IOException {
        SchoolRegistrarApplication.openNewStage("professordashboard.fxml", "Home");
    }

    public void handleAddAssignment() {
        addAssignment(selectedCourse, selectedSection);
    }

    public void addAssignment(String course, String section) {

        name = nameTextField.getText();
        description = descriptionTextArea.getText();
        category = selectedCategory;

        String dueTime;
        String dueHour = timeTextField.getText().substring(0, 2);
        String dueMinute = timeTextField.getText().substring(3, 5);
        dueTime = (dueHour + ":" + dueMinute + " " + selectedTimeOfDay);

        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("courses")
                .document(course)
                .collection("sections")
                .document(section)
                .collection("assignments")
                .document(nameTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", selectedSection + " " + selectedCourse + " " + nameTextField.getText());
        data.put("Category", selectedCategory);
        data.put("Description", descriptionTextArea.getText());
        data.put("Due Date", date.toString());
        data.put("Due Time", dueTime);

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
                menuButton.setText(item.getText());
            });
        }
    }
}

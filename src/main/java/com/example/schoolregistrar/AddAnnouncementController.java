package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.util.HashMap;
import java.util.Map;

public class AddAnnouncementController {
    @FXML private TextField nameTextField;
    @FXML private TextArea descriptionTextArea;
    @FXML private MenuButton sectionMenu;
    private static String selectedSection;
    private static String selectedCourse;

    public void initialize() {
        getSections(sectionMenu);
    }

    public void handleAdd() {
        addAnnouncement(selectedCourse, selectedSection);
    }

    public void addAnnouncement(String course, String section) {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("courses")
                .document(course)
                .collection("sections")
                .document(section)
                .collection("announcements")
                .document(nameTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameTextField.getText());
        data.put("Description", descriptionTextArea.getText());

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

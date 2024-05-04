package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ScheduleController {
    @FXML private TableView<RegisterSection> scheduleList;
    @FXML private TableColumn<RegisterSection, String> crnColumn;
    @FXML private TableColumn<RegisterSection, String> courseNameColumn;
    @FXML private TableColumn<RegisterSection, String> semesterColumn;
    @FXML private TableColumn<RegisterSection, String> timeColumn;
    @FXML private TableColumn<RegisterSection, String> professorColumn;
    @FXML private TableColumn<RegisterSection, String> daysColumn;
    @FXML private Label nameLabel;
    static boolean key;

    public void initialize() {
        nameLabel.setText(StudentDashboardController.user.getFirstName() + " " + StudentDashboardController.user.getLastName());
        crnColumn.setCellValueFactory(new PropertyValueFactory<>("crn"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<>("professor"));
        daysColumn.setCellValueFactory(new PropertyValueFactory<>("days"));

        readSchedule();
    }

    public void handleHome() throws IOException {
        SchoolRegistrarApplication.openNewStage("studentdashboard.fxml", "Student Dashboard");
    }

    public boolean readSchedule() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh").collection("students")
                .document(StudentDashboardController.user.getFirstName() + " " + StudentDashboardController.user.getLastName())
                .collection("schedule").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty()) {
                for (QueryDocumentSnapshot doc : documents) {
                    scheduleList.getItems().add(new RegisterSection(doc.getData().get("CRN").toString(),
                            doc.getData().get("Course Name").toString(),
                            doc.getData().get("Semester").toString(),
                            doc.getData().get("Time").toString(),
                            doc.getData().get("Professor").toString(),
                            doc.getData().get("Days").toString()));
                }
            }
            key=true;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }
}

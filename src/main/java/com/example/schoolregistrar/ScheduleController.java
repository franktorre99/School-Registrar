package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ScheduleController {
    @FXML private TableView<RegisterSection> scheduleList;
    @FXML private TableColumn<RegisterSection, String> crnColumn;
    @FXML private TableColumn<RegisterSection, String> courseNameColumn;
    @FXML private TableColumn<RegisterSection, String> semesterColumn;
    @FXML private TableColumn<RegisterSection, String> timeColumn;
    @FXML private TableColumn<RegisterSection, String> professorColumn;
    static boolean key;

    public void initialize() {
        crnColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("crn"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("courseName"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("semester"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("time"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<RegisterSection, String>("professor"));

        readSchedule();
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
                            doc.getData().get("Professor").toString()));
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
}

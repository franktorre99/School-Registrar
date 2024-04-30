package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.schoolregistrar.StudentDashboardController.user;

public class FinalGradesController {
    @FXML private MenuButton selectSemester;
    @FXML private TableView<Grade> gradeTableView;
    @FXML private TableColumn<Grade, String> crnColumn;
    @FXML private TableColumn<Grade, String> courseNameColumn;
    @FXML private TableColumn<Grade, String> gradeColumn;
    static boolean key;
    private String selectedSemester;

    public void initialize() {
        crnColumn.setCellValueFactory(new PropertyValueFactory<>("crn"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        for (MenuItem item : selectSemester.getItems()) {
            item.setOnAction(e -> {
                gradeTableView.getItems().clear();
                selectedSemester = item.getText();
                selectSemester.setText(selectedSemester);
                readSchedule();
            });
        }
    }

    public void handleHome() throws IOException {
        SchoolRegistrarApplication.openNewStage("studentdashboard.fxml", "Student Dashboard");
    }

    public boolean readSchedule() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh")
                .collection("students")
                .document(user.getFirstName() + " " + user.getLastName())
                .collection("grades").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                for (QueryDocumentSnapshot doc : documents) {
                    if (doc.getData().get("Semester").equals(selectedSemester)) {
                        gradeTableView.getItems().add(new Grade(doc.getData().get("CRN").toString()
                                , doc.getData().get("Course Name").toString()
                                , doc.getData().get("Grade").toString()));
                    }
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
}

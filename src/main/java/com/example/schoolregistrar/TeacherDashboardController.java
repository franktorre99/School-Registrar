package com.example.schoolregistrar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class TeacherDashboardController {
    @FXML private TableView<Assignment> upcomingAssignmentsTable;
    @FXML private TableColumn<Assignment, Date> dateTableColumn;
    @FXML private TableColumn<Assignment, String> nameTableColumn;
    @FXML private TableColumn<Assignment, Time> timeTableColumn;
    @FXML private ListView<String> announcementList;

    public void initialize() {
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<Assignment, Date>("Date"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<Assignment, String>("Name"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<Assignment, Time>("Time"));
    }

    public void handleAddAssignmentButton() throws IOException {
        SchoolRegistrarApplication.openNewStage("addassignment.fxml", "New Assignment");
    }

    public void handleAddGradeButton() throws IOException {
        SchoolRegistrarApplication.openNewStage("addgrade.fxml", "Add Grade");
    }
}

package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentDashboardController {
    @FXML public TableView<UpcomingAssignment> upcomingAssignmentsTable;
    @FXML private Label upcomingAssignments1;
    @FXML private TableColumn<UpcomingAssignment, String> dateTableColumn;
    @FXML private TableColumn<UpcomingAssignment, String> nameTableColumn;
    @FXML private TableColumn<UpcomingAssignment, String> timeTableColumn;
    @FXML public ListView<String> announcementList;
    static boolean key;
    public static Student user;
    private ArrayList<UpcomingAssignment> upcomingAssignments = new ArrayList<>();
    private static ArrayList<String> courses = new ArrayList<>();
    private static ArrayList<String> crns = new ArrayList<>();
    private ArrayList<Announcement> announcements = new ArrayList<>();

    public void initialize() {
        upcomingAssignmentsTable.getItems().clear();
        announcementList.getItems().clear();
        upcomingAssignments.clear();
        announcements.clear();
        courses.clear();
        crns.clear();
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<>("dueTime"));
        readSchedule();

        Iterator<String> courseIterator = courses.iterator();
        Iterator<String> crnIterator = crns.iterator();
        while (courseIterator.hasNext() && crnIterator.hasNext()) {
            readAssignments(courseIterator.next(), crnIterator.next());
        }
        Iterator<String> courseIterator2 = courses.iterator();
        Iterator<String> crnIterator2 = crns.iterator();
        while (courseIterator2.hasNext() && crnIterator2.hasNext()) {
            readAnnouncements(courseIterator2.next(), crnIterator2.next());
        }

        populateUpcomingAssignments(upcomingAssignments, upcomingAssignmentsTable);
        populateAnnouncements(announcements, announcementList);
    }

    public void handleLogout() throws IOException {
        upcomingAssignmentsTable.getItems().clear();
        announcementList.getItems().clear();
        upcomingAssignments.clear();
        announcements.clear();
        courses.clear();
        crns.clear();
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Stage stage = SchoolRegistrarApplication.getStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleRegisterForClasses() throws IOException {
        SchoolRegistrarApplication.openNewStage("studentregistration.fxml", "Register for Courses");
    }

    public void handleViewSchedule() throws IOException {
        SchoolRegistrarApplication.openNewStage("schedule.fxml", "Schedule");
    }

    public void handleViewFinalGrades() throws IOException {
        SchoolRegistrarApplication.openNewStage("finalgrades.fxml", "Final Grades");
    }

    public boolean readAnnouncements(String course, String section) {
        key = false;
        ApiFuture<QuerySnapshot> future = SchoolRegistrarApplication.fstore.collection("courses")
                .document(course).collection("sections")
                .document(section).collection("announcements").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                for (QueryDocumentSnapshot document : documents) {
                    announcements.add(new Announcement(document.getData().get("Name").toString()
                            , document.getData().get("Description").toString()));
                }
            }
            key=true;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    public boolean readAssignments(String course, String section) {
        key = false;
        ApiFuture<QuerySnapshot> future = SchoolRegistrarApplication.fstore.collection("courses")
                .document(course).collection("sections")
                .document(section).collection("assignments").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                for (QueryDocumentSnapshot document : documents) {
                    upcomingAssignments.add(new UpcomingAssignment(document.getData().get("Name").toString()
                            , document.getData().get("Due Date").toString()
                            , document.getData().get("Due Time").toString()));
                }
            }
            key=true;
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return key;
    }

    public boolean readSchedule() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users")
                .document("BqVyZx5WE4qULEQy7GXh")
                .collection("students")
                .document(user.getFirstName() + " " + user.getLastName())
                .collection("schedule").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                for (QueryDocumentSnapshot doc : documents) {
                    crns.add(doc.getData().get("CRN").toString());
                    courses.add(doc.getData().get("Department").toString() + " " + doc.getData().get("Course Number").toString());
                }
            }
            key=true;
        }
        catch (InterruptedException | ExecutionException ex)
        {
            ex.printStackTrace();
        }
        return key;
    }

    private void populateUpcomingAssignments(ArrayList<UpcomingAssignment> assignments, TableView<UpcomingAssignment> tableView) {
        for (UpcomingAssignment assignment : assignments) {
            tableView.getItems().add(assignment);
        }
    }

    private void populateAnnouncements(ArrayList<Announcement> announcements, ListView<String> listVIew) {
        for (Announcement announcement : announcements) {
            listVIew.getItems().add(announcement.toString());
        }
    }
}

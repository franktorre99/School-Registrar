package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProfessorDashboardController {
    @FXML private TableView<UpcomingAssignment> upcomingAssignmentsTable;
    @FXML private TableColumn<UpcomingAssignment, String> dateTableColumn;
    @FXML private TableColumn<UpcomingAssignment, String> nameTableColumn;
    @FXML private TableColumn<UpcomingAssignment, String> timeTableColumn;
    @FXML private ListView<String> announcementList;
    static boolean key;
    public static Professor user;
    private ArrayList<UpcomingAssignment> upcomingAssignments = new ArrayList<>();
    public static ArrayList<Course> coursesAvailable = new ArrayList<>();
    private ArrayList<Announcement> announcements = new ArrayList<>();

    public void initialize() {
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<UpcomingAssignment, String>("DueDate"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<UpcomingAssignment, String>("Name"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<UpcomingAssignment, String>("DueTime"));

        readCourses();

        for (Course course : coursesAvailable) {
            readSections(course);
        }
        for (Section section : user.getSectionsTaught()) {
            readAssignments(section.getCourse().getDepartment() + " " + section.getCourse().getCourseNumber(), String.valueOf(section.getCrn()));
            readAnnouncements(section.getCourse().getDepartment() + " " + section.getCourse().getCourseNumber(), String.valueOf(section.getCrn()));
        }

        populateUpcomingAssignments(upcomingAssignments, upcomingAssignmentsTable);
        populateAnnouncements(announcements, announcementList);
    }

    public void handleAddAssignmentButton() throws IOException {
        SchoolRegistrarApplication.openNewWindow("addassignment.fxml", "New Assignment");
    }

    public void handleAddGradeButton() throws IOException {
        SchoolRegistrarApplication.openNewWindow("addgrade.fxml", "Add Grade");
    }

    public void handleNewAnnouncement() throws IOException {
        SchoolRegistrarApplication.openNewWindow("addannouncement.fxml", "New Announcement");
    }

    public boolean readSections(Course course) {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("courses")
                .document(course.getDepartment() + " " + course.getCourseNumber())
                .collection("sections").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                for (QueryDocumentSnapshot doc : documents) {
                    if (Integer.parseInt(doc.getData().get("Professor ID").toString()) == user.getId()) {
                        user.getSectionsTaught().add(new Section(new Course(doc.getData().get("Department").toString()
                                , Integer.parseInt(doc.getData().get("Course Number").toString())
                                , doc.getData().get("Course Name").toString())
                                , Integer.parseInt(doc.getData().get("CRN").toString())
                                , new Professor(doc.getData().get("Professor First Name").toString()
                                , doc.getData().get("Professor Last Name").toString()
                                , Integer.parseInt(doc.getData().get("Professor ID").toString()))
                                , doc.getData().get("Semester").toString()));

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

    public boolean readAssignments(String course, String section) {
        key = false;
        ApiFuture<QuerySnapshot> future = SchoolRegistrarApplication.fstore.collection("courses").document(course).collection("sections").document(section).collection("assignments").get();
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

    public boolean readCourses() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("courses").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    coursesAvailable.add(new Course(doc.getData().get("Department").toString()
                            , Integer.parseInt(doc.getData().get("Course Number").toString())
                            , doc.getData().get("Course Name").toString()));
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

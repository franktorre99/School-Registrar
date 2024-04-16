package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private ArrayList<Assignment> assignments = new ArrayList<Assignment>();
    private ArrayList<UpcomingAssignment> upcomingAssignments = new ArrayList<>();
    private ArrayList<String> announcements = new ArrayList<>();


    public void initialize() {
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<UpcomingAssignment, String>("DueDate"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<UpcomingAssignment, String>("Name"));
        timeTableColumn.setCellValueFactory(new PropertyValueFactory<UpcomingAssignment, String>("DueTime"));
        /*
        readSections("CSC 311");
        readSections("CSC 325");
        readAssignments("CSC 311", "11209");
        readAssignments("CSC 325", "90210");
        */

        upcomingAssignments.add(new UpcomingAssignment("Homework 1", "2024-04-08", "11:59 PM"));
        upcomingAssignments.add(new UpcomingAssignment("Homework 2", "2024-04-15", "11:59 PM"));
        upcomingAssignments.add(new UpcomingAssignment("Homework 3", "2024-04-11", "11:59 PM"));
        upcomingAssignments.add(new UpcomingAssignment("Lab 1", "2024-04-15", "11:59 PM"));
        upcomingAssignments.add(new UpcomingAssignment("Homework 4", "2024-04-15", "11:59 PM"));
        upcomingAssignments.add(new UpcomingAssignment("Lab 2", "2024-04-17", "11:59 PM"));

        announcements.add("3/29:    Test 1 grades will be given out next class.");
        announcements.add("4/1:     Quiz moved to next Tuesday.");
        announcements.add("4/3:     Class Canceled Thursday.");

        populateTable(upcomingAssignments, upcomingAssignmentsTable);
        displayAnnouncements(announcements, announcementList);
    }

    public void handleAddAssignmentButton() throws IOException {
        SchoolRegistrarApplication.openNewStage("addassignment.fxml", "New Assignment");
    }

    public void handleAddGradeButton() throws IOException {
        SchoolRegistrarApplication.openNewStage("addgrade.fxml", "Add Grade");
    }

    public boolean readSections(String courseName) {
        key = false;

        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("courses").document(courseName).collection("sections").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                for (QueryDocumentSnapshot doc : documents) {
                    System.out.println(this.user.getId());
                    if (Integer.parseInt(doc.getData().get("ProfessorID").toString()) == user.getId()) {
                        user.getSectionsTaught().add(new Section(new Course(doc.getData().get("Department").toString()
                                , Integer.parseInt(doc.getData().get("CourseNumber").toString())
                                , doc.getData().get("CourseName").toString())
                                , Integer.parseInt(doc.getData().get("CRN").toString())
                                , new Professor(doc.getData().get("ProfessorFirstName").toString()
                                , doc.getData().get("ProfessorLastName").toString()
                                , Integer.parseInt(doc.getData().get("ProfessorID").toString()))));

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

    public boolean readAssignments(String courseName, String sectionNumber) {
        key = false;

        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future = SchoolRegistrarApplication.fstore.collection("courses").document(courseName).collection("sections").document(sectionNumber).collection("assignments").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            if(documents.size()>0) {
                for (QueryDocumentSnapshot document : documents) {
                    upcomingAssignments.add(new UpcomingAssignment(document.getData().get("Name").toString(), document.getData().get("Due Date").toString(), document.getData().get("Due Time").toString()));
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

    private void populateTable(ArrayList<UpcomingAssignment> assignments, TableView<UpcomingAssignment> tableView) {
        for (UpcomingAssignment assignment : assignments) {
            tableView.getItems().add(assignment);
        }
    }

    private void displayAnnouncements(ArrayList<String> announcements, ListView<String> announcementList) {
        for(String announcement: announcements) {
            announcementList.getItems().add(announcement);
        }
    }
}

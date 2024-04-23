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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class StudentDashboardController {

    @FXML private TableView<UpcomingAssignment> gradeTable;
    @FXML private ListView<String> gradeList;
    static boolean key;
    public static Student user;

    private HashMap<String, String> grades = new HashMap<>();

    public String userID;


    @FXML
    public void initialize() {

        System.out.println(user.toString());

        userID = Integer.toString(user.getId());

        readSchedule();

        readGrades();

        System.out.println("ToString: ");
        System.out.println(grades.toString());


    }



    public boolean readGrades() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("ids").document(userID).collection("Grades").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    System.out.println(doc.getId());
                    for (HashMap.Entry<String, Object> entry : doc.getData().entrySet()) {
                        grades.put(entry.getKey(), entry.getValue().toString());
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

    public boolean readSchedule() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("ids").document(userID).collection("Next Semester Schedule").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    System.out.println(doc.getId());
                    System.out.println(doc.getData().toString());
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

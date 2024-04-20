package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class ValidateID {
    static boolean key;
    static ArrayList<Integer> ids = new ArrayList<>();
    static int id;
    public static int validateID(int id) {
        readIDs();
        if (ids.contains(id)) {
            id = validateID(generateID());
        }
        return id;
    }

    public static boolean readIDs() {
        key = false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("ids").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(!documents.isEmpty())
            {
                for (QueryDocumentSnapshot doc : documents) {
                    ids.add(Integer.parseInt(doc.getId()));
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

    public static int generateID() {
        Random random = new Random();

        int min = 10000000;
        int max = 99999999;
        id = random.nextInt(max - min + 1) + min;
        return id;
    }
}

package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.schoolregistrar.AddProfessorController.*;
import static com.example.schoolregistrar.SchoolRegistrarApplication.fstore;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class AddProfessorControllerTest {
    static final FirestoreContext contxtFirebase = new FirestoreContext();
    static boolean found=false;
    @BeforeAll
    static void setUp() {
        fstore = contxtFirebase.firebase();
    }
    @Test
    void addProfessor() throws ExecutionException, InterruptedException {
        found=false;
        CollectionReference base = fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection("professors");
        ApiFuture<QuerySnapshot> future = base.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (documents.size() == 0) {
            fail("No data exists");
        }
        for (QueryDocumentSnapshot document : documents) {
            if (document.getReference().getId().equals(firstName + " " + lastName)) {
                assertEquals(firstName, document.getData().get("First Name"));
                assertEquals(lastName, document.getData().get("Last Name"));
                assertEquals(email, document.getData().get("email"));
                assertEquals(password, document.getData().get("password"));
                assertEquals(UID, document.getData().get("UID"));
                found=true;
                return;
            }
        }
        if(found){fail("Error inserting data");}
    }
    @Test
    void addID() throws ExecutionException, InterruptedException {
        found=false;
        CollectionReference base = fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection("professors");
        ApiFuture<QuerySnapshot> future = base.get();
        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        if (documents.size() == 0) {
            fail("No data exists");
        }
        for (QueryDocumentSnapshot document : documents) {
            if (document.getReference().getId().equals(firstName + " " + lastName)) {
                assertEquals(profID, document.getData().get("ID"));
                found=true;
                return;
            }
        }
        if(found){fail("Error inserting data");}
    }
}
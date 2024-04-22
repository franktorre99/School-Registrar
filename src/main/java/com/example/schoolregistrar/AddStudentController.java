package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddStudentController {
    @FXML
    private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private PasswordField passwordTextField;
    @FXML private Label idLabel;
    private String userID;

    public void handleAdd() {
        addProfessor();
        SchoolRegistrarApplication.openNewStage("administratordashboard.fxml","Home");
    }

    public void handleGenerateID() {
        Random random = new Random();

        int min = 10000000;
        int max = 99999999;
        int id = random.nextInt(max - min + 1) + min;
        idLabel.setText(String.valueOf(id));
    }

    public void addProfessor() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(emailTextField.getText())
                .setEmailVerified(false)
                .setDisabled(false);
        String password=passwordTextField.getText();
        for(int i=6; i>=password.length(); i--){
            password+="*";
        }
        request.setPassword(password);
        try {
            UserRecord userRecord = SchoolRegistrarApplication.fauth.createUser(request);
            userID=userRecord.getUid();
            System.out.println("Successfully created new user with Firebase Uid: " + userRecord.getUid()
                    + " check Firebase > Authentication > Users tab");

        } catch (FirebaseAuthException ex) {
            // Logger.getLogger(FirestoreContext.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error creating a new user in the firebase");
        }
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("users")
                .document(LoginController.docID)
                .collection("students")
                .document(firstNameTextField.getText() + " " + lastNameTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("First Name", firstNameTextField.getText());
        data.put("Last Name", lastNameTextField.getText());
        data.put("UID",userID);
        data.put("email", emailTextField.getText());
        data.put("password", passwordTextField.getText());
        data.put("ID", Integer.parseInt(idLabel.getText()));

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

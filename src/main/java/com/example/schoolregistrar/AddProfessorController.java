package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.WriteResult;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddProfessorController {
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private Label idLabel;

    public void handleAdd() {
        addProfessor();
    }

    public void handleGenerateID() {
        int id = ValidateID.validateID(ValidateID.generateID());
        idLabel.setText(String.valueOf(id));
    }

    public void addProfessor() {
        DocumentReference docRef = SchoolRegistrarApplication.fstore.collection("users")
                .document(LoginController.docID)
                .collection("professors")
                .document(firstNameTextField.getText() + " " + lastNameTextField.getText());

        Map<String, Object> data = new HashMap<>();
        data.put("First Name", firstNameTextField.getText());
        data.put("Last Name", lastNameTextField.getText());
        data.put("email", emailTextField.getText());
        data.put("password", passwordTextField.getText());
        data.put("ID", Integer.parseInt(idLabel.getText()));

        ApiFuture<WriteResult> result = docRef.set(data);
    }
}

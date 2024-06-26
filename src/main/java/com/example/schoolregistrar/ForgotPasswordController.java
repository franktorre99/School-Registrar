package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ForgotPasswordController {
    @FXML
    private VBox labels;
    @FXML
    private Label badPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField verification;
    @FXML
    private Label invalidPassword;
    @FXML
    private Label incorrectMessage;
    @FXML
    private TextField user;
    @FXML
    private Button setPassword;

    public void initialize(){
        setPassword.disableProperty().bind(user.textProperty().isEmpty().or(newPassword.textProperty().isEmpty().or(verification.textProperty().isEmpty())));
    }

    public void handleBack() throws IOException {
        SchoolRegistrarApplication.openNewStage("login.fxml", "Login");
    }

    public void handleSetNewPasswordButtonClicked() throws IOException, FirebaseAuthException {
        if(isAUser()&&newPassword.getText().equals(verification.getText())&&newPassword.getText().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")){
            changePassword();
            LoginController.dashboardChooser(LoginController.type);
        }
        else{
            badPassword.setOpacity(1);
            invalidPassword.setOpacity(1);
            incorrectMessage.setOpacity(1);
            newPassword.clear();
            verification.clear();
        }
    }
    private boolean isAUser() throws FirebaseAuthException {
        LoginController.key=false;
        ListUsersPage page = SchoolRegistrarApplication.fauth.listUsers(null);
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(LoginController.type.toLowerCase()+"s").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            for (ExportedUserRecord userF : page.iterateAll()) {
                if (user.getText().equals(userF.getEmail())) {
                    for (QueryDocumentSnapshot document : documents) {
                        if (userF.getUid().equals(document.getData().get("UID"))) {
                            LoginController.userSetter(document);
                            return true;
                        }
                    }
                }
            }
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        LoginController.key=true;
        return false;
    }

    private void changePassword(){
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(LoginController.type.toLowerCase()+"s").get();
        List<QueryDocumentSnapshot> documents;
        DocumentReference docRef = null;
        try {
            documents = future.get().getDocuments();
            if(!documents.isEmpty()) {
                System.out.println("Getting (reading) data from firebase database....");
                for (QueryDocumentSnapshot document : documents) {
                    if(document.getData().get("email").equals(user.getText())){
                        docRef= document.getReference();
                    }
                }
            }
        }
        catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        LoginController.key=true;
        ApiFuture<WriteResult> result=docRef.update("password", newPassword.getText());
    }
}

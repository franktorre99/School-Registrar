package com.example.schoolregistrar;

import com.example.schoolregistrar.LoginController;
import com.example.schoolregistrar.SchoolRegistrarApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import com.google.cloud.firestore.*;
import com.google.api.core.ApiFuture;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class ForgotPasswordController {
    @FXML
    private Label badPassword;
    @FXML
    private TextField newPassword;
    @FXML
    private TextField verification;
    @FXML
    private Label invalidPassword;
    @FXML
    private Label incorrectMessage;
    @FXML
    private TextField user;

    public void handleSetNewPasswordButtonClicked() throws IOException {
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
    private boolean isAUser(){
        LoginController.key=false;
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(LoginController.type.toLowerCase()+"s").get();
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Getting (reading) data from firebase database....");
                for (QueryDocumentSnapshot document : documents) {
                        if(document.getData().get("email").equals(user.getText())){
                            return true;
                        }
                }
            }
            else
            {
                System.out.println("No data");
            }
        }
        catch (InterruptedException | ExecutionException ex){
            ex.printStackTrace();
        }
        LoginController.key=true;
        return false;
    }
//    private String getID(){
//
//    }
        private void changePassword(){
            ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(LoginController.type.toLowerCase()+"s").get();
            List<QueryDocumentSnapshot> documents;
            DocumentReference docRef = null;
            try
            {
                documents = future.get().getDocuments();
                if(documents.size()>0)
                {
                    System.out.println("Getting (reading) data from firebase database....");
                    for (QueryDocumentSnapshot document : documents) {
                        if(document.getData().get("email").equals(user.getText())){
                               docRef= document.getReference();
                        }
                    }
                }
                else
                {
                    System.out.println("No data");
                }
            }
            catch (InterruptedException | ExecutionException ex){
                ex.printStackTrace();
            }
            LoginController.key=true;
            ApiFuture<WriteResult> result=docRef.update("password", newPassword.getText());
        }

}

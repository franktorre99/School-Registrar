package com.example.login;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;


import com.google.cloud.firestore.*;
import com.google.api.core.ApiFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginController {
    @FXML
    private ChoiceBox<String> userType;
    @FXML
    private TextField userEmail;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Button loginButton;
    @FXML
    private Label incorrect;
    @FXML
    private Label forgotPassword;
    static String type;
    static boolean key;
    static String docID="BqVyZx5WE4qULEQy7GXh";

    static String user=null;

    public void initialize() {
        loginButton.disableProperty().bind(userEmail.textProperty().isEmpty().or(userPassword.textProperty().isEmpty()).or(userType.valueProperty().isNull()));
        userEmail.disableProperty().bind(userType.valueProperty().isNull());
        userPassword.disableProperty().bind(userType.valueProperty().isNull());
        forgotPassword.visibleProperty().bind(userType.valueProperty().isNotNull());
        ObservableList<String> users = userType.getItems();
        users.add("Student");
        users.add("Professor");
        users.add("Administrator");
    }

    public void handleForgotPasswordLabelClicked(MouseEvent mouseEvent){
        type=(String)userType.getSelectionModel().getSelectedItem();
        user=userEmail.getText();
        SchoolRegistrarApplication.openNewStage("forgotpassword.fxml","Forgot Password");
    }
    public void handleLoginButtonClicked() {
        type=(String) userType.getSelectionModel().getSelectedItem();
        user=userEmail.getText();
        readFirebase();
        }
    static void dashboardChooser(String user){
        switch (user) {
            case "Student":
                SchoolRegistrarApplication.openNewStage(".fxml","Student Dashboard");
                break;
            case "Professor":
                SchoolRegistrarApplication.openNewStage(".fxml","Professor Dashboard");
                break;
            case "Administrator":
                SchoolRegistrarApplication.openNewStage(".fxml","Administrator Dashboard");
                break;
            default:
                System.out.println("Logic error");
        }
    }
    public boolean readFirebase() {
        key = false;

        //asynchronously retrieve all documents
        ApiFuture<QuerySnapshot> future =  SchoolRegistrarApplication.fstore.collection("users").document(docID).collection(type.toLowerCase()+"s").get();
        // future.get() blocks on response
        List<QueryDocumentSnapshot> documents;
        try
        {
            documents = future.get().getDocuments();
            if(documents.size()>0)
            {
                System.out.println("Getting (reading) data from firebase database....");
                boolean userFound=false;
                for (QueryDocumentSnapshot document : documents) {
                    if(document.getData().get("email").equals(userEmail.getText())&&document.getData().get("password").equals(userPassword.getText())){
                        userFound=true;
                    }
                }
                if(userFound){
                    String user = (String) userType.getSelectionModel().getSelectedItem();
                    dashboardChooser(user);
                }
                else{
                    incorrect.setOpacity(1);
                }
                userEmail.clear();
                userPassword.clear();
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
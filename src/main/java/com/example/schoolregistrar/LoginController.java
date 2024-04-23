package com.example.schoolregistrar;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
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
    static String docID = "BqVyZx5WE4qULEQy7GXh";
    static String user = null;


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

    public void handleForgotPasswordLabelClicked() {
        type = userType.getSelectionModel().getSelectedItem();
        user = userEmail.getText();
        SchoolRegistrarApplication.openNewStage("forgotpassword.fxml", "Forgot Password");
    }

    public void handleLoginButtonClicked() throws FirebaseAuthException {
        type = userType.getSelectionModel().getSelectedItem();
        user = userEmail.getText();
        readFirebase();
    }

    static void dashboardChooser(String user) throws IOException {
        switch (user) {
            case "Student":
                SchoolRegistrarApplication.openNewStage(".fxml", "Student Dashboard");
                break;
            case "Professor":
                SchoolRegistrarApplication.openNewStage("professordashboard.fxml", "Professor Dashboard");
                break;
            case "Administrator":
                SchoolRegistrarApplication.openNewStage("administratordashboard.fxml", "Administrator Dashboard");
                break;
            default:
                System.out.println("Logic error");
        }
    }

    public void readFirebase() throws FirebaseAuthException {
        key = false;
        ListUsersPage page = SchoolRegistrarApplication.fauth.listUsers(null);
        ApiFuture<QuerySnapshot> future = SchoolRegistrarApplication.fstore.collection("users").document("BqVyZx5WE4qULEQy7GXh").collection(type.toLowerCase() + "s").get();
        List<QueryDocumentSnapshot> documents;
        try {
            documents = future.get().getDocuments();
            for (ExportedUserRecord user : page.iterateAll()) {
                 if (userEmail.getText().equals(user.getEmail().substring(0, user.getEmail().indexOf("@")))) {
                    for (QueryDocumentSnapshot document : documents) {
                        if (user.getUid().equals(document.getData().get("UID"))) {
                            if (userPassword.getText().equals(document.getData().get("password")))
                                if (type.equals("Professor"))
                                    ProfessorDashboardController.user = new Professor(document.getData().get("First Name").toString()
                                            , document.getData().get("Last Name").toString()
                                            , Integer.parseInt(document.getData().get("ID").toString()));
                                else if (type.equals("Administrator"))
                                    AdministratorDashboardController.user = new Administrator(document.getData().get("First Name").toString()
                                            , document.getData().get("Last Name").toString()
                                            , Integer.parseInt(document.getData().get("ID").toString()));
                                else if (type.equals("Student"))
                                    //Student dashboard should have an instance of user like administrator/professor which gets defined here
                            dashboardChooser(type);
                        }
                    }
                }
            }
            incorrect.setOpacity(1);
            userEmail.clear();
            userPassword.clear();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } catch (ExecutionException exception) {
            throw new RuntimeException(exception);
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }
}
<<<<<<< HEAD
module com.example.schoolregistrar {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
=======
module com.example.login {
    requires javafx.controls;
    requires javafx.fxml;
>>>>>>> 7819f9a (Needs to be able to add data from Firebase into ChoiceBoxes and needs connections to dashboards)
    requires firebase.admin;
    requires com.google.auth;
    requires com.google.auth.oauth2;
    requires google.cloud.firestore;
    requires google.cloud.core;
    requires com.google.api.apicommon;

<<<<<<< HEAD
    opens com.example.schoolregistrar to javafx.fxml;
    exports com.example.schoolregistrar;
=======

    opens com.example.login to javafx.fxml;
    exports com.example.login;
>>>>>>> 7819f9a (Needs to be able to add data from Firebase into ChoiceBoxes and needs connections to dashboards)
}
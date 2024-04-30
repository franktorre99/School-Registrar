package com.example.schoolregistrar;

import java.io.IOException;

public class AdministratorDashboardController {
    public static Administrator user;

    public void handleAddCourse() throws IOException {
        SchoolRegistrarApplication.openNewStage("addcourse.fxml", "Add Course");
    }

    public void handleAddSection() throws IOException {
        SchoolRegistrarApplication.openNewStage("addsection.fxml", "Add Section");
    }

    public void handleAddProfessor() throws IOException {
        SchoolRegistrarApplication.openNewStage("addprofessor.fxml", "Add Professor");
    }

    public void handleAddStudent() throws IOException {
        SchoolRegistrarApplication.openNewStage("addstudent.fxml", "Add Student");
    }

    public void handleLogout() throws IOException {
        SchoolRegistrarApplication.openNewStage("login.fxml", "Login");
    }
}

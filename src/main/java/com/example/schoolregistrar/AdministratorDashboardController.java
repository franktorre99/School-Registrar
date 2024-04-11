package com.example.schoolregistrar;

import java.io.IOException;

public class AdministratorDashboardController {
    public static Administrator user;

    public void handleAddCourse() throws IOException {
        SchoolRegistrarApplication.openNewWindow("addcourse.fxml", "Add Course");
    }

    public void handleAddSection() throws IOException {
        SchoolRegistrarApplication.openNewWindow("addsection.fxml", "Add Section");
    }
}

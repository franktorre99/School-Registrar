package com.example.schoolregistrar;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AddGradeController {
    @FXML
    private TableView<Submission> submissionsView;
    @FXML private TableColumn<Submission, String> nameColumn;
    @FXML private TableColumn<Submission, String> attachmentsColumn;
    @FXML private TextField scoreTextField;
    @FXML private TextArea feedbackTextArea;
    @FXML private Button submitGradeButton;
    @FXML private MenuButton selectAssignment;

    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<Submission, String>("Name"));
        attachmentsColumn.setCellValueFactory(new PropertyValueFactory<Submission, String>("Attachments"));
        scoreTextField.disableProperty().bind(submissionsView.getSelectionModel().selectedItemProperty().isNull());
        feedbackTextArea.disableProperty().bind(submissionsView.getSelectionModel().selectedItemProperty().isNull());
        submitGradeButton.disableProperty().bind(submissionsView.getSelectionModel().selectedItemProperty().isNull());
    }
}

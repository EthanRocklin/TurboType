package com.typingspeed.app;

import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/** @author Ethan Rocklin */

public class BrowseController {
    
    @FXML
    private Button browseButton;
    @FXML
    private TextField selectedDirectoryField;
    
    private DialogPane dialogPane;
    
    @FXML
    public void initialize() {
        browseButton.setOnAction(e -> selectDirectory());
    }
    
    @FXML
    private void selectDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Export Folder");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Links chooser dialog window to the parent stage
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        File directory = chooser.showDialog(stage);
        if (directory != null) {
            selectedDirectoryField.setText(directory.getAbsolutePath());
        }
    }
    
    public String getSelectedDirectory() {
        return selectedDirectoryField.getText();
    }
    
    public void setDialogPane(DialogPane dialogPane) {
        this.dialogPane = dialogPane;
    }
    
}

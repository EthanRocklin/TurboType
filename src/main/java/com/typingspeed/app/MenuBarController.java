package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuBar;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/** @author Ethan Rocklin */

public class MenuBarController {
    
    private static final Logger logger = Logger.getLogger(MenuBarController.class.getName());

    @FXML
    private ComboBox<String> colorBox;
    @FXML
    private MenuBar menuBar;
    @FXML
    private SVGPath pauseBtnIcon;
    @FXML
    private Text currentTrackText;
    
    public void initialize() {
        colorBox.setVisibleRowCount(11);
        currentTrackText.setText(MusicManager.getCurrentTrack());
        // Gives controller access to Music Manager for consistent currentTrack updates
        MusicManager.setMenuBarController(this);
    }
    
    // FXML Handler Methods for Menu Tabs
    
    @FXML
    private void handleExport(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String fileType = menuItem.getText().replace(".", "");
        String resourcePath = "";
        resourcePath = "/fxml/export_" + fileType + ".fxml";
        displayExportDialog(resourcePath, fileType);
    }
    
    // This method helps handleExport() create and display Export dialog windows
    private void displayExportDialog(String resourcePath, String fileType) {
        try {
            Scene scene = menuBar.getScene();
            Stage currentStage = (Stage) scene.getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            DialogPane pane = loader.load();
            // Sets CSS for the window
            URL cssUrl = MenuBarController.class.getResource("/css/export_" + Settings.getThemeStatic() + ".css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
            // Links Dialog window to parent stage
            Dialog<List<String>> dialog = new Dialog<>();
            dialog.setDialogPane(pane);
            dialog.initOwner(currentStage);
            // Sets configurations
            dialog.setTitle("." + fileType + " Export");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Export dialog window failed to display." , e);
        }
    }
    
    @FXML
    private void handleGenreChange(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource(); 
        String newGenre = menuItem.getText().toLowerCase();
        try {
            MusicManager.readyNewGenre(newGenre);
            MusicManager.updateCurrentTrack();
            currentTrackText.setText(MusicManager.getCurrentTrack());
            // Saves the genre to the user's settings
            TurboType.settings.setGenre(newGenre);
            TurboType.settings.save();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to change genre." , e);
        }
        MusicManager.play();
    }
    
    @FXML
    private void handleThemeChange(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource();
        String theme = menuItem.getText().toLowerCase().replaceAll(" ", "_");
        TurboType.changeTheme(theme);
        try {
            TurboType.settings.setTheme(theme);
            TurboType.settings.save();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to change theme." , e);
        }
    }
    
    @FXML
    private void handleTronTheme() {
        String color = switch (colorBox.getValue()) {
            case "White" -> "#ffffff";
            case "Red" -> "#ff0000";
            case "Orange" -> "#ff9900";
            case "Yellow" -> "#ffff00";
            case "Lime" -> "#aaff00";
            case "Green" -> "#0fff00";
            case "Turquoise" -> "#00ff88";
            case "Cyan" -> "#00ffff";
            case "Purple" -> "#dd00ff";
            case "Pink" -> "#ff00bb";
            case "Hot Magenta" -> "#ff0088";
            default -> {
                System.out.println("ERROR : Could not retrieve selected color");
                yield "#00ffff";
            }
        };
        TurboType.changeTheme("tron");
        TurboType.changeThemeColor(color);
        // Saves the color's name to the user's settings
        try {
            TurboType.settings.setTheme("tron");
            TurboType.settings.setTronColor(color);
            TurboType.settings.save();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to change Tron theme." , e);
        }
    }
    
    // FXML Handler Methods for Music Panel
    
    @FXML
    private void handlePreviousTrack() {
        MusicManager.readyNewTrack(false);
        currentTrackText.setText(MusicManager.getCurrentTrack());
        MusicManager.play();
    }
    
    @FXML
    private void handlePausePlay() {
        // Checks if the MediaPlayer in MusicManager.java is playing
        MediaPlayer musicPlayer = MusicManager.getMusicPlayer();
        if (musicPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            MusicManager.pause();
            pauseBtnIcon.setContent("M6 5v14l12-7-12-7z");
        } else {
            MusicManager.play();
            pauseBtnIcon.setContent("M6 19h4V5H6v14zm8-14v14h4V5h-4z");
        }
    }
    
    @FXML
    private void handleNextTrack() {
        MusicManager.readyNewTrack(true);
        currentTrackText.setText(MusicManager.getCurrentTrack());
        MusicManager.play();
    }
    
    public void setCurrentTrackText(String currentTrack) {
        currentTrackText.setText(currentTrack);
    }

}

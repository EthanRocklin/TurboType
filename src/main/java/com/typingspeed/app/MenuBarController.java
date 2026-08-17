package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;


/** @author Ethan Rocklin */

public class MenuBarController {

    // Fields
    @FXML
    private ComboBox<String> colorBox;
    @FXML
    private SVGPath pauseBtnIcon;
    @FXML
    private Text currentTrackText;
    
    public void initialize() {
        colorBox.setVisibleRowCount(11);
        currentTrackText.setText(MusicManager.getCurrentTrack());
    }
    
    // FXML Handler Methods for Menu Tabs
    
    @FXML
    private void handleExport(ActionEvent event) {
        
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleTronTheme(ActionEvent event) {
        String colorName = colorBox.getValue(),
               color = "";
        // Determines color to send
        switch (colorName) {
            case "White":
                color = "#ffffff";
                break;
            case "Red":
                color = "#ff0000";
                break;
            case "Orange":
                color = "#ff9900";
                break;
            case "Yellow":
                color = "#ffff00";
                break;
            case "Lime":
                color = "#aaff00";
                break;
            case "Green":
                color = "#0fff00";
                break;
            case "Turquoise":
                color = "#00ff88";
                break;
            case "Cyan":
                color = "#00ffff";
                break;
            case "Purple":
                color = "#dd00ff";
                break;
            case "Pink":
                color = "#ff00bb";
                break;
            case "Hot Magenta":
                color = "#ff0088";
                break;
        }
        TurboType.changeTheme("tron");
        TurboType.changeThemeColor(color);
        // Saves the color's name to the user's settings
        try {
            TurboType.settings.setTheme("tron");
            TurboType.settings.setTronColor(color);
            TurboType.settings.save();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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

}
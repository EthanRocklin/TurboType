package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import java.util.List;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/** @author Ethan Rocklin */

public class MenuBarController {

    // Fields
    @FXML
    private ComboBox<String> colorBox;
    @FXML
    private SVGPath pauseBtnIcon;
    @FXML
    private Text currentSong;
    
    public void initialize() {
        colorBox.setVisibleRowCount(11);
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
            setCurrentTrackText();
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
        setCurrentTrackText();
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
        setCurrentTrackText();
        MusicManager.play();
    }
    
    // Other Methods
    
    // SEE ABOUT PLACING THIS METHOD AS A getter in MusicManager.java
    
    private void setCurrentTrackText() {
        String genre = MusicManager.getGenre();
        int trackIndex = MusicManager.getTrackIndex();
        // Gets the name and artist of the current song
        String path = "/music/credits/" + genre + "_credits.txt";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(path)))) {
            List<String> credits = reader.lines().collect(Collectors.toList());
            currentSong.setText(credits.get(trackIndex));
        } catch (IOException e) {
            System.out.println("ERROR : Credits file not found.");
            e.printStackTrace();
        }
    }
}

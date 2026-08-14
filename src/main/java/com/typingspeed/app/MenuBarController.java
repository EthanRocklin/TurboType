package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.media.MediaPlayer;


/** @author Ethan Rocklin */

public class MenuBarController {

    // Fields
    @FXML
    private ComboBox<String> colorBox;
    
    // Initializer
    
    public void initialize() {
        colorBox.setVisibleRowCount(11);
    }
    
    // FXML Methods
    
    @FXML
    private void handleExport(ActionEvent event) {
        
    }
    
    @FXML
    private void handleMusicChange(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource(); 
        String newGenre = menuItem.getText().toLowerCase();
        MusicManager.readyNewSong(newGenre);
        try {
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
}

package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.MenuItem;


/** @author Ethan Rocklin */

public class MenuBarController {

    // Fields
    private MusicManager music;
    private ThemeManager theme;
    
    // FXML Methods
    
    @FXML
    private void handleExport(ActionEvent event) {
        
    }
    
    @FXML
    private void handleMusicChange(ActionEvent event) {
        MenuItem menuItem = (MenuItem) event.getSource(); 
        String option = menuItem.getText();
        music = new MusicManager(option);
        music.play();
    }
    
    @FXML
    private void handleThemeChange(ActionEvent event) {
        
    }
}

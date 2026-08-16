package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;

/** @author Ethan Rocklin */

public class MusicController {

    // Fields
    @FXML
    private Button backBtn, pauseBtn, forwardBtn;
    @FXML
    private SVGPath pauseBtnIcon;
    @FXML
    private Text currentSong;
    
    // FXML Handler Methods
    
    @FXML
    private void handlePreviousTrack() {
        MusicManager.readyNewTrack(false);
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
        MusicManager.play();
    }
    
}

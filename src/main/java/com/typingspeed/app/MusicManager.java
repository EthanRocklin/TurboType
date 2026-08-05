package com.typingspeed.app;

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.lang.IllegalArgumentException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** @author Ethan Rocklin */

public class MusicManager {
    
    private MediaPlayer mediaPlayer;
    
    public MusicManager(String resourcePath) {
        URI resourceURI = null;
        try {
            URL resourceURL = MusicManager.class.getResource(resourcePath);
            resourceURI = resourceURL.toURI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (resourceURI == null) {
            throw new IllegalArgumentException("Resource not found: " + resourceURI);
        }
        Media media = new Media(resourceURI.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnError(() -> {
           System.out.println("MediaPlayer ERROR : " + mediaPlayer.getError()); 
        });
    }
    
    public void play() {
        mediaPlayer.play();
    }
    
    private void dispose() {
        mediaPlayer.dispose();
    }
    
}

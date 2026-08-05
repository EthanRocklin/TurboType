package com.typingspeed.app;

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.lang.IllegalArgumentException;
import java.util.Random;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** @author Ethan Rocklin */

public class MusicManager {
    
    private MediaPlayer mediaPlayer;
    private String genre;
    
    public MusicManager(String genre) {
        this.genre = genre.toLowerCase();
        String resourcePath = getResourcePath();
        Media media = new Media(getResourceURI(resourcePath));
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnError(() -> {
           System.out.println("MediaPlayer ERROR : " + mediaPlayer.getError()); 
        });
    }
    
    /**
     * This method gets the resource path of a random song for the given genre.
     * @return a String representing the path of the randomized song
     */
    private String getResourcePath() {
        Random rand = new Random();
        File dir = new File("/music/" + genre);
        int count = dir.list().length,
            songNumber = rand.nextInt(1, count + 1);;
        System.out.println("Number of Songs : " + count);
        return String.format("/music/%s/%s%d.mp3", genre, genre, songNumber);
    }
    
    /**
     * 
     * @param resourcePath
     * @return 
     */
    private String getResourceURI(String resourcePath) {
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
        return resourceURI.toString();
    }
    
    public void play() {
        mediaPlayer.play();
    }
    
    private void dispose() {
        mediaPlayer.dispose();
    }
    
}

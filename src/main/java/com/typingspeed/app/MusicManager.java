package com.typingspeed.app;

import java.io.File;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.lang.IllegalArgumentException;
import java.lang.RuntimeException;
import java.util.Random;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/** @author Ethan Rocklin */

public class MusicManager {
    
    private MediaPlayer mediaPlayer;
    private String genre;
    
    public MusicManager(String genre) {
        this.genre = genre.toLowerCase();
        Media media = new Media(getResourceURI());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnError(() -> {
           System.out.println("MediaPlayer ERROR : " + mediaPlayer.getError()); 
        });
    }
    
    /**
     * This method gets the resource URI of a random song within the MusicManager's genre.
     * @return a String representing the resource's URI 
     */
    private String getResourceURI() {
        Random rand = new Random();
        
        URL dirURL = MusicManager.class.getResource("/music/" + genre);
        if (dirURL == null) {
            throw new IllegalArgumentException("Resource not found: " + dirURL);
        }
        File dir = null;
        try {
            dir = new File(dirURL.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI for genre folder : " + genre, e);
        }
        int count = dir.list().length,
            songNumber = rand.nextInt(1, count + 1);
        System.out.println("Number of Songs : " + count);
        System.out.println("Song Number : " + songNumber);
        String resourcePath = String.format("/music/%s/%s%d.mp3", genre, genre, songNumber);
        System.out.println("Resource Path : " + resourcePath);
        URI resourceURI = null;
        try {
            URL resourceURL = MusicManager.class.getResource(resourcePath);
            if (resourcePath == null) {
                throw new IllegalArgumentException("Resource not found : " + resourcePath);
            }
            resourceURI = resourceURL.toURI();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
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

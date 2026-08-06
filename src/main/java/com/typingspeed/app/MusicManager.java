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
    
    public MusicManager(String genre) {
        Media media = new Media(getResourceURI(genre));
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnError(() -> {
           System.out.println("MediaPlayer ERROR : " + mediaPlayer.getError()); 
        });
    }
    
    /**
     * This method gets the resource URI of a random song within the MusicManager's genre.
     * @return a String representing the resource's URI 
     */
    private String getResourceURI(String genre) {
        Random rand = new Random();
        // Gets the URL for the music's directory
        URL dirURL = MusicManager.class.getResource("/music/" + genre);
        if (dirURL == null) {
            throw new IllegalArgumentException("Resource not found: " + dirURL);
        }
        // Tries to convert the URL into a URI
        File dir = null;
        try {
            dir = new File(dirURL.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI for genre folder : " + genre, e);
        }
        // Gets the song number for the given genre
        int count = dir.list().length,
            songNumber = rand.nextInt(1, count + 1);
        // Creates the path of the targeted song and tries to get its URI
        String resourcePath = String.format("/music/%s/%s%d.mp3", genre, genre, songNumber);
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
    
    // This method 
    public void readyNewSong(String genre) {
        mediaPlayer.pause();
        Media media = new Media(getResourceURI(genre));
        mediaPlayer = new MediaPlayer(media);
    }
    
    public void play() {
        mediaPlayer.play();
    }
    
    private void dispose() {
        mediaPlayer.dispose();
    }
    
}

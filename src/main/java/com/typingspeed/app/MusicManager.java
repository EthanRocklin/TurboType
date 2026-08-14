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
    
    private static MediaPlayer musicPlayer;
    private static String genre;
    
    // Utility class; it should not be instantiated beyond the first time in TurboType.java
    private MusicManager() {}
    
    // This method initializes the MediaPlayer with a song from the user's last genre listen to.
    public static void initializePlayer(String initialGenre) {
        genre = initialGenre;
        try {
            Media media = new Media(getResourceURI(genre));
            musicPlayer = new MediaPlayer(media);
            // Saves the genre to the user's settings
            musicPlayer.setOnError(() -> {
                System.out.println("MediaPlayer ERROR : " + musicPlayer.getError()); 
        });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * This method gets the resource URI of a random song within the MusicManager's genre.
     * @return a String representing the resource's URI 
     */
    private static String getResourceURI(String genre) {
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
    
    // This method switches the music to a new genre
    public static void readyNewSong(String newGenre) {
        genre = newGenre;
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
        try {
            Media media = new Media(getResourceURI(genre));
            musicPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }
    
    public static void setGenre(String newGenre) {
        genre = newGenre;
    }
    
    public static void play() {
        musicPlayer.play();
    }
    public static void pause() {
        musicPlayer.pause();
    }
    private static void dispose() {
        musicPlayer.dispose();
    }
    
}

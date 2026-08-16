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
import java.util.LinkedList;
import java.io.IOException;

/** @author Ethan Rocklin */

public class MusicManager {
    
    private static MediaPlayer musicPlayer;
    private static String currentGenre;
    private static String[] tracks;
    private static int tracksIndex;
    
    // Utility class; it should not be instantiated beyond the first time in TurboType.java
    private MusicManager() {}
    
    // This method initializes the MediaPlayer with a song from the user's last genre listen to.
    public static void initializePlayer(String initialGenre) {
        currentGenre = initialGenre;
        tracksIndex = 0;
        createTracksArray();
        for (String s : tracks) {
            System.out.println(s);
        }
        try {
            Media media = new Media(getResourceURI(tracks[0]));
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
     * This mehtod creates an array of the tracks within the "currentGenre" to
     * be cycled through.
     */
    private static void createTracksArray() {
        try {
            File dir = getGenreDirectory(currentGenre);
            tracks = dir.list();            
        } catch (IOException e) {
            System.out.println("RESOURCE ERROR : Could not get genre's directory");
            e.printStackTrace();
        }
    }
    
    // This mehtod gets the directory containing the tracks of the selected genre.
    private static File getGenreDirectory(String genre) throws IOException {
        URL dirURL = MusicManager.class.getResource("/music/" + genre);
        if (dirURL == null) {
            throw new IllegalArgumentException("Resource not found: " + dirURL);
        }
        // Tries to convert the URL into a URI, returns if successful
        File dir = null;
        try {
            return new File(dirURL.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URI for genre folder : " + genre, e);
        }
    }
    
    /**
     * This method gets the resource URI of the newTrack within the current genre.
     * @return a String representing the resource's URI 
     */
    private static String getResourceURI(String newTrack) {
        // Creates the path of the targeted track and tries to get its URI
        String resourcePath = String.format("/music/%s/%s", currentGenre, newTrack);
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
    public static void readyNewGenre(String newGenre) {
        currentGenre = newGenre;
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
        try {
            Media media = new Media(getResourceURI(currentGenre));
            musicPlayer = new MediaPlayer(media);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    // This method prepares the next or previous track 
    public static void readyNewTrack(boolean next) {
        // Releases resources of current MediaPlayer
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
        // Tries to create a new MediaPlayer for the new track
        try {
            // If the user is at the end of the genre's tracks
            if (next) {
                if (tracksIndex != tracks.length - 1) {
                    tracksIndex++;
                } else {
                    tracksIndex = 0;
                }
            } // If the user is at the beginning of the genre's tracks 
            else {
                if (tracksIndex != 0) {
                    tracksIndex--;
                } else {
                    tracksIndex = tracks.length - 1;
                }
            }
            Media media = new Media(getResourceURI(tracks[tracksIndex]));
            musicPlayer = new MediaPlayer(media);
            System.out.println(tracks[tracksIndex]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }
    
    public static void setGenre(String newGenre) {
        currentGenre = newGenre;
    }
    
    public static void play() {
        musicPlayer.play();
    }
    public static void playNextSong() {
        
        if (tracksIndex < tracks.length - 1) {
            
            tracksIndex++;
        } else {
            
        }
    }
    public static void pause() {
        musicPlayer.pause();
    }
    private static void dispose() {
        musicPlayer.dispose();
    }
    
}

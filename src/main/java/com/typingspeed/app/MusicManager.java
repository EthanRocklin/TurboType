package com.typingspeed.app;

import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.lang.IllegalArgumentException;
import java.lang.RuntimeException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/** @author Ethan Rocklin */

public class MusicManager {
    
    private static final Logger logger = Logger.getLogger(MusicManager.class.getName());
    
    private static int trackIndex;
    private static MenuBarController menuBarController;
    private static MediaPlayer musicPlayer;
    private static String currentGenre;
    private static String currentTrackText;
    private static String[] tracks;
    
    // Utility class; it should not be instantiated beyond the first time in TurboType.java
    private MusicManager() {}
    
    // This method initializes the MediaPlayer with a song from the user's last genre listen to.
    public static void initializePlayer(String initialGenre) {
        currentGenre = initialGenre;
        trackIndex = 0;
        createTracksArray();
        try {
            Media media = new Media(getResourceURI(tracks[0]));
            musicPlayer = new MediaPlayer(media);
            setMusicPlayerListeners();
            updateCurrentTrack();
            menuBarController.setCurrentTrackText(currentTrackText);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not initialize music player.", e);
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
            logger.log(Level.WARNING, "Could not get genre's directory", e);
        }
    }
    
    // This mehtod gets the directory containing the tracks of the selected genre.
    private static File getGenreDirectory(String genre) throws IOException {
        URL dirURL = MusicManager.class.getResource("/music/" + genre);
        if (dirURL == null) {
            throw new IllegalArgumentException("Resource not found: " + dirURL);
        }
        // Tries to convert the URL into a URI, returns if successful
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
        trackIndex = 0;
        createTracksArray();
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
        }
        try {
            String firstTrack = currentGenre + "1.mp3";
            Media media = new Media(getResourceURI(firstTrack));
            musicPlayer = new MediaPlayer(media);
            updateCurrentTrack();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not prepare music for the new genre.", e);
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
                if (trackIndex != tracks.length - 1) {
                    trackIndex++;
                } else {
                    trackIndex = 0;
                }
            } // If the user is at the beginning of the genre's tracks 
            else {
                if (trackIndex != 0) {
                    trackIndex--;
                } else {
                    trackIndex = tracks.length - 1;
                }
            }
            Media media = new Media(getResourceURI(tracks[trackIndex]));
            musicPlayer = new MediaPlayer(media);
            setMusicPlayerListeners();
            updateCurrentTrack();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not prepare the new track for play.", e);
        }
    }
    
    // This method updates the information of the current track for UI display
    public static void updateCurrentTrack() {
        // Gets the name and artist of the current song
        String path = "/music/credits/" + currentGenre + "_credits.txt";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(MusicManager.class.getResourceAsStream(path)))) {
            List<String> credits = reader.lines().collect(Collectors.toList());
            currentTrackText = credits.get(trackIndex);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not retrieve artist attribution.", e);
        }
    }
    
    // Getters
    
    public static String getCurrentTrack() {
        return currentTrackText;
    }
    public static String getGenre() {
        return currentGenre;
    }
    public static MediaPlayer getMusicPlayer() {
        return musicPlayer;
    }
    public static int getTrackIndex() {
        return trackIndex;
    }
    
    // Setters
    
    public static void setGenre(String newGenre) {
        currentGenre = newGenre;
    }
    public static void setMenuBarController(MenuBarController controller) {
        menuBarController = controller;
    }
    private static void setMusicPlayerListeners() {
        // If the track ends
        musicPlayer.setOnEndOfMedia(() -> {
           readyNewTrack(true); 
           updateCurrentTrack();
           menuBarController.setCurrentTrackText(currentTrackText);
           musicPlayer.play();
        });
    }
    
    // State Setters
    
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

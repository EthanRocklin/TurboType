package com.typingspeed.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/** @author Ethan Rocklin */
public class Settings {

    // Fields
    private String theme, genre, tronColor;
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();
    private final static String SETTINGS_DATA_DIR = System.getProperty("user.home") + File.separator + ".turbotype";
    private final static String URL = SETTINGS_DATA_DIR + File.separator + "settings.json";
    
    public Settings() {}
    
    // This method loads the user's locally saved settings.
    public static Settings load() throws IOException {
        File dir = new File(SETTINGS_DATA_DIR);
        File file = new File(URL);
        if (!file.exists()) {
            dir.mkdirs();
            // Copies default settings from resources
            try (InputStream in = Settings.class.getResourceAsStream("/settings.json")) {
                Files.copy(in, file.toPath());
            }
        }
        return MAPPER.readValue(file, Settings.class);
    }
    
    public void save() throws IOException {
        WRITER.writeValue(new File(URL), this);
    }
    
    public String getGenre() {
        return genre;
    }
    public String getTheme() {
        return theme;
    }
    public String getTronColor() {
        return tronColor;
    }
    
    public void setGenre(String genre) {
       this.genre = genre;
    }
    public void setTheme(String theme) {
       this.theme = theme;
    }
    public void setTronColor(String tronColor) {
        this.tronColor = tronColor;
    }
    
}
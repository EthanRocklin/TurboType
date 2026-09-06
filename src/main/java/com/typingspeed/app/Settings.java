package com.typingspeed.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/** @author Ethan Rocklin 
 *  This class relies on the Jackson library for data serialization
 *  and deserialization.
 */

public class Settings {

    private static String theme = "original";
    private static String genre = "techno";
    private static String tronColor = "#00ffff";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectWriter WRITER = MAPPER.writerWithDefaultPrettyPrinter();
    private final static String SETTINGS_DATA_DIR = System.getProperty("user.home") + File.separator + ".turbotype";
    private final static String URL = SETTINGS_DATA_DIR + File.separator + "settings.json";
    
    // Utility class; should not be directly instantiated
    public Settings() {}
    
    // This method loads the user's locally saved settings.
    public static Settings load() throws IOException {
        File dir = new File(SETTINGS_DATA_DIR);
        File file = new File(URL);
        if (!file.exists() || file.length() == 0) {
            dir.mkdirs();
            // Copies default settings from resources
            try (InputStream in = Settings.class.getResourceAsStream("/settings.json")) {
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
    // Static getter for other classes to call
    public static String getThemeStatic() {
        return theme;
    }
    public static String getTronColor() {
        return tronColor;
    }
    
    public void setGenre(String genre) {
       Settings.genre = genre;
    }
    public void setTheme(String theme) {
       Settings.theme = theme;
    }
    public void setTronColor(String tronColor) {
        Settings.tronColor = tronColor;
    }
    
}

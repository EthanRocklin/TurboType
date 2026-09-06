package com.typingspeed.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Turbo Type launch point
 * @author Ethan Rocklin
 */
public class TurboType extends Application {
    
    private static final Logger logger = Logger.getLogger(TurboType.class.getName());

    private static Scene scene;
    protected static Settings settings;
    private static String currentFXML = "homepage";
    private static String currentTheme = "original";
    private static String tronColor;

    @Override
    public void start(Stage stage) {
        try {
            // Checks if there is no database for the user to save attempt data to
            if (!checkForDatabase()) {
                DatabaseManager.createDatabase();
            }
            // Loads the app's icon
            Image icon = new Image(TurboType.class.getResourceAsStream("/images/app_icon.png"));
            stage.getIcons().add(icon);
            // Loads any fonts not available with JavaFX CSS
            Font.loadFont(TurboType.class.getResourceAsStream("/fonts/VT323-Regular.ttf"), 14);
            Font.loadFont(TurboType.class.getResourceAsStream("/fonts/Quantico-Regular.ttf"), 14);
            // Default size included
            scene = new Scene(loadFXML(currentFXML), 640, 480);
            loadSettings();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not start TurboType application. ", e);
        }
    }

    static void setRoot(String fxml) {
        try {
            // Sets the fxml and theme for the app
            currentFXML = fxml;
            scene.getStylesheets().clear();
            // Sets the styles for the FXML file
            URL menuBarCssURL = TurboType.class.getResource("/css/menu_bar_" + currentTheme + ".css");
            // The Practice and Timed FXML files have the same style structure "attempt_<theme>.css"
            URL cssUrl = (currentFXML.equals("practice") || currentFXML.equals("timed")) 
                    ? TurboType.class.getResource("/css/attempt_" + currentTheme + ".css")
                    : TurboType.class.getResource("/css/" + currentFXML + "_" + currentTheme + ".css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                scene.getStylesheets().add(menuBarCssURL.toExternalForm());
            }
            scene.setRoot(loadFXML(fxml));
            // Maintains the Tron color of the previous page if Tron is the theme
            if (currentTheme.equals("tron")) {
                changeTronColor(tronColor);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not set FXML root. ", e);
        }
    }
    
    // This method sets the stylesheets for the scene based on the theme recieved.
    public static void changeTheme(String newTheme) {
        currentTheme = newTheme;
        // Clears existing stylesheets and adds new stylesheets
        scene.getStylesheets().clear();
        URL menuBarCssURL = TurboType.class.getResource("/css/menu_bar" + "_" + currentTheme + ".css");
        // The Practice and Timed FXML files have the same style structure "attempt_<theme>.css"
        URL cssUrl = (currentFXML.equals("practice") || currentFXML.equals("timed")) 
                ? TurboType.class.getResource("/css/attempt_" + currentTheme + ".css")
                : TurboType.class.getResource("/css/" + currentFXML + "_" + currentTheme + ".css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            scene.getStylesheets().add(menuBarCssURL.toExternalForm());
        }
    }
    
    // This method changes the secondary color(s) of eligible themes.
    public static void changeTronColor(String color) {
        tronColor = color;
        scene.getRoot().setStyle("-fx-tron: " + color + ";");
    }
    
    // This method checks if the local database for the user's attempt data exists.
    private boolean checkForDatabase() {
        String dir = DatabaseManager.getAppDataDirectory() + File.separator + "user_data.db";
        File database = new File(dir);
        return database.exists();
    }
    
    /**
     * This method loads the FXML content of any file in the fxml directory.
     * @param fxml : the name of the fxml file
     * @return a Parent node for containing all children under the root node.
     * @throws IOException 
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(TurboType.class.getResource("/fxml/" + fxml + ".fxml"));
        return loader.load();
    }

    // This method loads and applies all settings the user had in their last session.
    private static void loadSettings() {
        try {
            settings = Settings.load();
            // Loads and applies the last theme
            String theme = settings.getTheme();
            changeTheme(theme);
            if (theme.equals("tron")) {
                changeTronColor(settings.getTronColor());
            }
            // Loads and applies the last genre played
            MusicManager.initializePlayer(settings.getGenre());
            MusicManager.play();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not load settings. ", e);
        }
    }
    
    public static void main(String[] args) {
        launch();
    }

}

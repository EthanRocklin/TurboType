package com.typingspeed.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.IOException;

/**
 * Turbo Type starting point
 * @author Ethan Rocklin
 */
public class TurboType extends Application {

    private static Scene scene;
    private static String fxml, theme;

    @Override
    public void start(Stage stage) {
        try {
            loadFonts();
            loadWords();
            loadSentences();
            fxml = "homepage";
            scene = new Scene(loadFXML(fxml), 640, 480);
            scene.getStylesheets().add(getClass().getResource("/css/homepage_original.css").toExternalForm());
            scene.getStylesheets().add(getClass().getResource("/css/menu_bar_original.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            // Sets the fxml and theme for the app
            TurboType.fxml = fxml;
            TurboType.theme = "original";
            scene.getStylesheets().clear();
            // Sets the styles for the FXML file
            var cssUrl = TurboType.class.getResource("/css/" + fxml + "_" + theme + ".css");
            var menuBarCssURL = TurboType.class.getResource("/css/menu_bar" + "_" + theme + ".css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
                scene.getStylesheets().add(menuBarCssURL.toExternalForm());
            }
            scene.setRoot(loadFXML(fxml));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method loads the FXML content of any file in the fxml directory.
     * @param fxml : the name of the fxml file
     * @return a Parent node for containing all children under the root node.
     * @throws IOException 
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TurboType.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    /**
     * This method loads any special fonts used by the application that
     * is not standard with CSS.
     */
    private void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/VT323-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/Quantico-Regular.ttf"), 14);
    }
    
    private static void loadSentences() {
        SentenceBank bank = new SentenceBank();
    }
    
    private static void loadWords() {
        WordBank bank = new WordBank();
    }
    
    public static void changeTheme(String newTheme) {
        TurboType.theme = newTheme;
        // Clears existing stylesheets and adds new stylesheets
        scene.getStylesheets().clear();
        var cssUrl = TurboType.class.getResource("/css/" + fxml + "_" + theme + ".css");
        var menuBarCssURL = TurboType.class.getResource("/css/menu_bar" + "_" + theme + ".css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
            scene.getStylesheets().add(menuBarCssURL.toExternalForm());
        }
    }
    
    // This method changes the secondary color(s) of eligible themes.
    public static void changeThemeColor(String color) {
        scene.getRoot().setStyle("-fx-secondary: " + color + ";");
    }
    
    public static void main(String[] args) {
        launch();
    }

}
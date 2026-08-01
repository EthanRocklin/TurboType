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
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        try {
            loadFonts();
            loadWords();
            loadSentences();
            scene = new Scene(loadFXML("homepage"), 640, 480);
            scene.getStylesheets().add(getClass().getResource("/css/homepage.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            System.out.println("ERROR : " + e.getMessage());
        }
    }

    static void setRoot(String fxml) {
        try {
            Parent root = loadFXML(fxml);
            scene.getStylesheets().clear();
            var cssUrl = App.class.getResource("/css/" + fxml + ".css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
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
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    /**
     * This method loads any special fonts used by the application that
     * is not standard with CSS.
     */
    private void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Micro5-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/NovaFlat-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/VT323-Regular.ttf"), 14);
    }
    
    private void loadSentences() {
        SentenceBank bank = new SentenceBank();
    }
    
    private void loadWords() {
        WordBank bank = new WordBank();
    }
    
    public static void main(String[] args) {
        launch();
    }

}
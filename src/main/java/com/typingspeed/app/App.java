package com.typingspeed.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.IOException;

/**
 * Turbo Type application
 * @author Ethan Rocklin
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        loadFonts();
        scene = new Scene(loadFXML("homepage"), 640, 480);
        scene.getStylesheets().add(getClass().getResource("/css/homepage.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.getStylesheets().clear();
        var cssUrl = App.class.getResource("/css/" + fxml + ".css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    
    private void loadFonts() {
        Font.loadFont(getClass().getResourceAsStream("/fonts/Micro5-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/NovaFlat-Regular.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/fonts/VT323-Regular.ttf"), 14);
    }

}
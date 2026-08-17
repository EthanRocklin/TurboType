package com.typingspeed.app;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;


/** @author Ethan Rocklin */

public class CreditsController {
    
    // Fields
    @FXML
    private Text classicalCredits, dubstepCredits, jazzCredits,
                     popCredits, rockCredits, technoCredits;
    @FXML
    private VBox leftContainer, rightContainer;
    @FXML
    private HBox creditsContainer;
    
    @FXML
    public void initialize() {
        classicalCredits.setText(getGenreCredits("classical"));
        dubstepCredits.setText(getGenreCredits("dubstep"));
        jazzCredits.setText(getGenreCredits("jazz"));
        popCredits.setText(getGenreCredits("pop"));
        rockCredits.setText(getGenreCredits("rock"));
        technoCredits.setText(getGenreCredits("techno"));
        leftContainer.prefWidthProperty().bind(creditsContainer.widthProperty().divide(2));
        rightContainer.prefWidthProperty().bind(creditsContainer.widthProperty().divide(2));
    }
    
    private String getGenreCredits(String genre) {
        String path = "/music/credits/" + genre + "_credits.txt";
        StringBuilder credits = new StringBuilder();
        // Tries to get and format the credits for the given genre
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(path)))) {
            List<String> rawCredits = reader.lines().collect(Collectors.toList());
            int creditCount = rawCredits.size();
            for (int i = 0; i < creditCount - 1; i++) {
                credits.append(rawCredits.get(i) + "\n");
            }
            credits.append(rawCredits.get(creditCount - 1) + "");
        } catch (IOException e) {
            System.out.println("ERROR : Credits file not found.");
            e.printStackTrace();
        }
        return credits.toString();
    }
    
    @FXML
    private void displayHomepage() throws IOException {
        TurboType.setRoot("homepage");
    }
    @FXML
    private void displayPractice() throws IOException {
        TurboType.setRoot("practice");
    }
    @FXML
    private void displayTimed() throws IOException {
        TurboType.setRoot("timed");
    }
    @FXML
    private void displayStats() throws IOException {
        TurboType.setRoot("stats");
    }
    
}

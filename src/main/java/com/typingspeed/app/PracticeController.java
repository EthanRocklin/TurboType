package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.event.Event;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.text.Text;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Arrays;

/**
 * @author Ethan Rocklin
 */

public class PracticeController {
    
    // Fields
    @FXML
    private AnchorPane practicePane;
    @FXML
    private Text correctCounter, totalCounter;
    @FXML
    private TextArea wordsArea, typingArea;
    @FXML
    private ToggleButton sentencesToggle;
    @FXML
    private Spinner<Integer> sentenceCounter;
    
    private List<String> practiceWords, typedWords;   
    
    //This method initializes the Practice Mode page after the page elements load.
    @FXML
    public void initialize() {
        initializeWords();
        initializeUIRestrictions();
        initializeSizeBindings();
        initializeUIValues();
    }
    // This method sets size bindings for initialize().
    private void initializeSizeBindings() {
        wordsArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
           if (newScene != null) {
               wordsArea.prefWidthProperty().bind(newScene.widthProperty().multiply(0.5));
               wordsArea.prefHeightProperty().bind(newScene.heightProperty().multiply(0.2));
               typingArea.prefWidthProperty().bind(newScene.widthProperty().multiply(0.5));
               typingArea.prefHeightProperty().bind(newScene.heightProperty().multiply(0.2));
           } 
        });
    }
    // This method sets UI restrictions for initialize().
    private void initializeUIRestrictions() {
        // Disables scrolling in the text areas
        wordsArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
        typingArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
        // Disables the user's ability to copy text
        practicePane.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        wordsArea.addEventFilter(KeyEvent.KEY_PRESSED, this::blockCopyPaste);
        typingArea.addEventFilter(KeyEvent.KEY_PRESSED, this::blockCopyPaste);
    }
    // This method helps initializeUIRestrictions().
    private void blockCopyPaste(KeyEvent event) {
        KeyCombination copy = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
        KeyCombination copyAlt = new KeyCodeCombination(KeyCode.INSERT, KeyCombination.CONTROL_DOWN);
        KeyCombination paste = new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN);
        KeyCombination pasteAlt = new KeyCodeCombination(KeyCode.INSERT, KeyCombination.SHIFT_DOWN);
        if (copy.match(event) || copyAlt.match(event) || paste.match(event) || pasteAlt.match(event)) {
            event.consume();
        }
    }
    // This method sets UI values for initialize() that are required. 
    private void initializeUIValues() {
        // Sets counters
        totalCounter.setText(String.valueOf(practiceWords.size()));
        correctCounter.setText("0");
        // Sets Spinner values
        int min = 1, max = 6, initial = 3;
        sentenceCounter.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial));
        sentenceCounter.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
        sentenceCounter.setVisible(false);
    }
    // This method loads the words and sentences for initialize().
    private void initializeWords() {
        String words = WordBank.getWords(30);
        wordsArea.setText(words);
        practiceWords = createWordList(words);
    }
    
    // Class Methods
    
    /**
     * This method takes a String of words that are delimited by spaces
     * and returns them as a list, with each index being one word.
     * @param str : A string consisting of one or more words.
     * @return a List object
     */
    private List<String> createWordList(String str) {
        String[] words = str.split(" ");
        return Arrays.asList(words);
    }
    
    @FXML
    private void togglePracticeWords() {
        String words = "";
        int count = 0;
        // If "Sentences" is selected
        if (sentencesToggle.isSelected()) {
            count = 3;
            updatePracticeWords(count, true);
            sentencesToggle.setText("Sentences");
            sentenceCounter.setVisible(true);
        } else {
            count = 30;
            updatePracticeWords(count, false);
            sentencesToggle.setText("Random");
            sentenceCounter.setVisible(false);
        }
    }
    
    /**
     * This method changes the practice words to random words or sentences, and
     * allows the count of words or sentences to be set.
     * @param count : the number of words or sentences
     * @param sentences : if the content should be full sentences
     */
    private void updatePracticeWords(int count, boolean sentences) {
        String words = "";
        if (sentences)
            words = SentenceBank.getSentences(count);
        else
            words = WordBank.getWords(count);
        practiceWords = createWordList(words);
        wordsArea.setText(words);
        setTotalWordsCounter();
    }
    
    // Updaters
    
    @FXML
    private void updateSentenceCount() {
        updatePracticeWords(sentenceCounter.getValue(), true);
    }
    
    /**
     * 
     * 
     */
    @FXML
    private void updateAreaShadowColor(int correctCount) {
       // Style to be applied to the TextArea 
       String style = "-fx-effect: dropshadow(gaussian, rgba(%d,%d,%d,0.5), 20, 0.5, 0, 0);";
       // Variables for calculations
       int wordCount = practiceWords.size(),
           targetR = 0, targetG = 0, targetB = 0,
           startR = 255, startG = 0, startB = 0;
       // Gets the targeted RGB values
       if (wordsArea.getEffect() instanceof DropShadow shadow) {
           Color color = shadow.getColor();
           targetR = (int) (color.getRed() * 255);
           targetG = (int) (color.getGreen() * 255);
           targetB = (int) (color.getBlue() * 255);
       }
       // Calculates tpyingArea RGB values using linear interpolation
       double progress = (double) correctCount / wordCount;
       int typingR = lerp(startR, targetR, progress),
           typingG = lerp(startG, targetG, progress),
           typingB = lerp(startB, targetB, progress);
       // Sets style with the new RGB values
       String result = String.format(style, typingR, typingG, typingB);
       typingArea.setStyle(result); 
    }
    
    // This helper method is for computing linear interpolation for the updateAreaShadowColor() method.
    private int lerp(int start, int end, double progress) {
        return (int) Math.round((start + (end - start) * progress));
    }
    
    // Checkers
    
    @FXML
    private void checkTypedWords() {
        int correctCount = 0;
        String typedContent = typingArea.getText();
        String[] words = typedContent.split(" ");
        typedWords = Arrays.asList(words);
        // Checks typed words against practice words
        for (int i = 0; i < typedWords.size(); i++) {
            String practiceWord = practiceWords.get(i),
                   typedWord = typedWords.get(i);
            if (practiceWord.equals(typedWord))
                correctCount++;
        }
        updateAreaShadowColor(correctCount);
        correctCounter.setText(String.valueOf(correctCount));
    }
    
    // Getters
    
    @FXML 
    private int getSentenceCount() {
        return sentenceCounter.getValue();
    }
    
    @FXML
    private String getTypedWords() {
        System.out.println(typingArea.getText());
        return "";
    }
    
    // Setters
    
    @FXML
    private void setTotalWordsCounter() {
        String count = String.valueOf(practiceWords.size());
        totalCounter.setText(count);
    }
 
    // FXML Controller Methods
    
    /**
     * This method deloads the Practice Mode page and loads the hoempage.
     * @throws IOException 
     */
    @FXML
    private void displayHomepage() throws IOException {
        App.setRoot("homepage");
    }
    
    /**
     * This method deloads the Practice Mode page and loads the Timed Type page.
     * @throws IOException 
     */
    @FXML
    private void displayTimed() throws IOException {
        App.setRoot("timed");
    } 
    
    /**
     * This method deloads the Practice Mode page and loads the Statistics page.
     * @throws IOException 
     */
    @FXML
    private void displayStats() throws IOException {
        App.setRoot("stats");
    }
    
}
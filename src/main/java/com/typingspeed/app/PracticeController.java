package com.typingspeed.app;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.event.Event;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Slider;
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
    private ImageView targetImage;
    @FXML
    private Label statsMessage;
    @FXML
    private Text correctCounter, totalCounter, accuracyValue, wpmValue, typingScore;
    @FXML
    private TextArea wordsArea, typingArea;
    @FXML
    private ToggleButton sentencesToggle;
    @FXML
    private Slider randomSlider;
    @FXML
    private Spinner<Integer> sentenceSpinner;
    @FXML
    private VBox statsBox, controlBox;
    
    private List<String> practiceWords, typedWords;   
    private boolean beganTyping = false;
    private AttemptManager attempt;
    
    // Initializers
    
    // This method initializes the Practice Mode page after the page elements load.
    @FXML
    public void initialize() {
        initializeWords();
        initializeUIRestrictions();
        initializeSizeBindings();
        initializeUIValues();
        initializeUIListeners();
        initializeVisibilities();
        initializeGraphics();
    }
    private void initializeVisibilities() {
        // Elements in the stats container (left side)
//        targetImage.setVisible(false);
//        accuracyLabel.setVisible(false);
//        accuracyValue.setVisible(false);
//        wpmLabel.setVisible(false);
//        wpmValue.setVisible(false);
//        statsMessage.setVisible(false);
        // Elements in the control box (right side)
        sentenceSpinner.setVisible(false);
        sentenceSpinner.setManaged(false);
        randomSlider.setVisible(false);
        randomSlider.setManaged(false);
    }
    private void initializeUIListeners() {
        // Listeners for Sliders
        randomSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            int oldWordCount = practiceWords.size(),
            newWordCount = (int) randomSlider.getValue();
            if (oldWordCount != newWordCount)
                updatePracticeWords(newWordCount, false);
        });
    }
    private void initializeGraphics() {
        Image image = new Image(getClass().getResource("/images/target1.png").toExternalForm());
        targetImage.setImage(image);
    }
    private void initializeSizeBindings() {
        // Binds proportional size for wordsArea and other elements in the same scene
        wordsArea.sceneProperty().addListener((obs, oldScene, newScene) -> {
           if (newScene != null) {
               wordsArea.prefWidthProperty().bind(newScene.widthProperty().multiply(0.5));
               wordsArea.prefHeightProperty().bind(newScene.heightProperty().multiply(0.2));
               typingArea.prefWidthProperty().bind(newScene.widthProperty().multiply(0.5));
               typingArea.prefHeightProperty().bind(newScene.heightProperty().multiply(0.2));
               statsBox.prefWidthProperty().bind(newScene.widthProperty().multiply(0.25));
               controlBox.prefWidthProperty().bind(newScene.widthProperty().multiply(0.25));
           } 
        });
    }
    private void initializeUIRestrictions() {
        // Disables scrolling in the text areas
        wordsArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
        typingArea.addEventFilter(ScrollEvent.SCROLL, ScrollEvent::consume);
        // Disables input in Spinner objects
        sentenceSpinner.addEventFilter(KeyEvent.ANY, KeyEvent::consume);
        // Disables the user's ability to copy text
        practicePane.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        wordsArea.addEventFilter(KeyEvent.KEY_PRESSED, this::blockCopyPaste);
        typingArea.addEventFilter(KeyEvent.KEY_PRESSED, this::blockCopyPaste);
    }
    private void blockCopyPaste(KeyEvent event) {
        KeyCombination copy = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
        KeyCombination copyAlt = new KeyCodeCombination(KeyCode.INSERT, KeyCombination.CONTROL_DOWN);
        KeyCombination paste = new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN);
        KeyCombination pasteAlt = new KeyCodeCombination(KeyCode.INSERT, KeyCombination.SHIFT_DOWN);
        if (copy.match(event) || copyAlt.match(event) || paste.match(event) || pasteAlt.match(event)) {
            event.consume();
        }
    }
    private void initializeUIValues() {
        // Sets counters
        totalCounter.setText(String.valueOf(practiceWords.size()));
        correctCounter.setText("0");
        // Sets Spinner values
        int min = 1, max = 6, initial = 3;
        sentenceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial));
        // Sets Slider values
        min = 10;
        max = 60;
        initial = 30;
        randomSlider.setMin(min);
        randomSlider.setMax(max);
        randomSlider.setValue(initial);    
        randomSlider.setSnapToTicks(true);
    }
    private void initializeWords() {
        String words = WordBank.getWords(30);
        wordsArea.setText(words);
        practiceWords = createWordList(words);
    }
    
    // Methods
    
    @FXML
    private void checkTypedWords() {
        // Checks if the user has not began typing and is not typing
        if (!beganTyping) startAttempt(); 
       
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
        updateTypingShadowColor(correctCount);
        correctCounter.setText(String.valueOf(correctCount));
        // Processes the attempt if the user completed typing the displayed words
        if (typedWords.size() == practiceWords.size()) {
            processAttempt();
        }
    }
    
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
    
    // This method runs processes needed for each attempt completion.
    private void processAttempt() {
        attempt.setEndTimeMilli();
        // Disables typing area and onKeyTyped listener
        typingArea.setEditable(false);
        typingArea.setOnKeyTyped(null);
        // Computes and displays stats
        attempt.setCorrectWordCount(Integer.valueOf(correctCounter.getText()));
        attempt.calculateAccuracy();
        accuracyValue.setText(String.valueOf(attempt.getAccuracy()) + "%");
        attempt.calculateWPM();
        wpmValue.setText(String.valueOf(attempt.getWPM()));
        attempt.calculateTypingScore();
        typingScore.setText(String.valueOf(attempt.getTypingScore()));
        // Displays message based on the attempt's performance
        statsMessage.setText(attempt.getAttemptMessage());
    }
    
    // This method begins the attempt upon the first keystroke in typingArea.
    private void startAttempt() {
            int wordCount = practiceWords.size();
            boolean sentences = sentencesToggle.isSelected();
            attempt = new AttemptManager(wordCount, sentences, 'p');
            beganTyping = true;
    }
    
    // This method toggles the format of the words between random words and full sentences.
    @FXML
    private void togglePracticeWords() {
        String words = "";
        int count = 0;
        // If "Sentences" is selected
        if (sentencesToggle.isSelected()) {
            count = 3;
            updatePracticeWords(count, true);
            randomSlider.setVisible(false);
            randomSlider.setManaged(false);
            sentenceSpinner.setVisible(true);
            sentenceSpinner.setManaged(true);
            sentencesToggle.setText("Sentences");
        } else {
            count = 30;
            updatePracticeWords(count, false);
            sentencesToggle.setText("Random");
            sentenceSpinner.setVisible(false);
            sentenceSpinner.setManaged(false);
            randomSlider.setVisible(true);
            randomSlider.setManaged(true);
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
        typingArea.clear();
        beganTyping = false;
        setTotalWordsCounter();
    }
    
    @FXML
    private void updateSentenceCount() {
        updatePracticeWords(sentenceSpinner.getValue(), true);
    }
    
    /**
     * This method updates the color of the typingArea color based on the 
     * progress of the user's correct words.
     */
    @FXML
    private void updateTypingShadowColor(int correctCount) {
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
    
    // Getters
    
    @FXML 
    private int getSentenceCount() {
        return sentenceSpinner.getValue();
    }
    @FXML
    private String getTypedWords() {
        System.out.println(typingArea.getText());
        return "";
    }
    public int getWordCount() {
        return practiceWords.size();
    }
    public int getCorrectWordCount() {
        return Integer.valueOf(correctCounter.getText());
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
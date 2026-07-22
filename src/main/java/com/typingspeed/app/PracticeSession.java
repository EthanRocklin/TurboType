package com.typingspeed.app;

import java.util.List;
import java.util.ArrayList;

/**
 * 
 * @author Ethan Rocklin
 */

public class PracticeSession {
    
    // Fields
    private List<String> practiceWords;
    private List<String> typedWords;
    
    // Constructors
    public PracticeSession() {
        typedWords = new ArrayList<>();
        
    }
    
    // Getters and Setters
    public List<String> getTypedWords() {
        return typedWords;
    }
    
    
    
}

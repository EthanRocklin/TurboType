package com.typingspeed.app;

/** @author Ethan Rocklin */

public record DataExportOptions(
        
    boolean accuracy,
    boolean averageAccuracy,
    boolean averageTypingScore,
    boolean averageWpm,
    boolean date,
    boolean format,
    boolean milliseconds,
    boolean timeElapsed,
    boolean wordCounts,
    boolean wpm
             
) {}
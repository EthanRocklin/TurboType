package com.typingspeed.app;

/** @author Ethan Rocklin */

public record ModelExportOptions(
        
    boolean barAccuracyFormat,
    boolean barAccuracyTotalWords,
    boolean barScoreFormat,
    boolean barScoreTotalWords,
    boolean barWpmFormat,
    boolean barWpmTotalWords,
    boolean histoAccuracy,
    boolean histoCorrectWords,
    boolean histoScore,
    boolean histoWpm,
    boolean lineAccuracy,
    boolean lineCorrectWords,
    boolean lineScore,
    boolean lineWpm,
    boolean scatterAccuracy,
    boolean scatterCorrectWords,
    boolean scatterScore
             
) {}

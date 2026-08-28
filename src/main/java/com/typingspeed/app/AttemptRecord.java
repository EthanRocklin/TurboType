package com.typingspeed.app;

/** @author Ethan Rocklin */

public record AttemptRecord (
    
    Integer attemptId,
    Integer typingScore,
    Integer correctCount,
    Integer totalCount,
    Double accuracy,
    Integer wpm,
    Integer milliseconds,
    String time,
    String format,
    String date
    
) {}

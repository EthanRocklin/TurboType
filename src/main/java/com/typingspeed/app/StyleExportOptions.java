package com.typingspeed.app;

/** @author Ethan Rocklin */

public record StyleExportOptions(
        
    boolean boldLabelsBox,
    boolean coloredCellsBox,
    boolean dataAlignmentBox,
    boolean dataCenterAlignBox,
    boolean dataLeftAlignBox,
    boolean dataRightAlignBox,
    boolean labelCenterAlignBox,
    boolean labelLeftAlignBox,
    boolean labelRightAlignBox,
    boolean selectAllStylesBox
             
) {}
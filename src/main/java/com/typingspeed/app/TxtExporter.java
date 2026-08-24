package com.typingspeed.app;

/** @author Ethan Rocklin */

public class TxtExporter {
    
    private boolean accuracySelected;
    private boolean averageAccuracySelected;
    private boolean averageTypingScoreSelected;
    private boolean averageWPMSelected;
    private boolean dateSelected;
    private boolean timeElapsedSelected;
    private boolean wordCountsSelected;
    private boolean wpmSelected;
    private String path;
    
    public TxtExporter(
            boolean accuracySelected,
            boolean averageAccuracySelected,
            boolean averageTypingScoreSelected,
            boolean averageWPMSelected,
            boolean dateSelected,
            boolean timeElapsedSelected,
            boolean wordCountsSelected,
            boolean wpmSelected,
            String path
    ) {
        this.accuracySelected = accuracySelected;
        this.averageAccuracySelected = averageAccuracySelected;
        this.averageTypingScoreSelected = averageTypingScoreSelected;
        this.averageWPMSelected = averageWPMSelected;
        this.dateSelected = dateSelected;
        this.timeElapsedSelected = timeElapsedSelected;
        this.wordCountsSelected = wordCountsSelected;
        this.wpmSelected = wpmSelected;
        this.path = path;
        export();
    }
    
    private void export() {
        System.out.println(accuracySelected);
        System.out.println(averageAccuracySelected);
        System.out.println(averageTypingScoreSelected);
        System.out.println(averageWPMSelected);
        System.out.println(dateSelected);
        System.out.println(timeElapsedSelected);
        System.out.println(wordCountsSelected);
        System.out.println(wpmSelected);
        System.out.println(path);
    }
    
}

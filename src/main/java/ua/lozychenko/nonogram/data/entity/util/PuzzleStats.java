package ua.lozychenko.nonogram.data.entity.util;

public class PuzzleStats {
    private long played;
    private long solved;
    private int bestAttempts;
    private int bestHints;

    public PuzzleStats() {
    }

    public PuzzleStats(long played, long solved, int bestAttempts, int bestHints) {
        this.played = played;
        this.solved = solved;
        this.bestAttempts = bestAttempts;
        this.bestHints = bestHints;
    }

    public long getPlayed() {
        return played;
    }

    public void setPlayed(long played) {
        this.played = played;
    }

    public long getSolved() {
        return solved;
    }

    public void setSolved(long solved) {
        this.solved = solved;
    }

    public int getBestAttempts() {
        return bestAttempts;
    }

    public void setBestAttempts(int bestAttempts) {
        this.bestAttempts = bestAttempts;
    }

    public int getBestHints() {
        return bestHints;
    }

    public void setBestHints(int bestHints) {
        this.bestHints = bestHints;
    }
}
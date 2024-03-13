package ua.lozychenko.nonogram.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("game.score")
public record GameScoreProperty(
        Integer minPuzzleScore,
        Integer perfectMultiplier,
        Double puzzleSizeMultiplier,
        Integer perfectThreshold,
        Integer hintsPerfectThreshold,
        Integer hintsThreshold,
        Integer withoutHints,
        Integer withoutHintsMultiplier,
        Integer almostWithoutHintsThreshold,
        Double almostWithoutHintsMultiplier,
        Double minPuzzleCellPercent,
        Double maxPuzzleCellPercent,
        Double outOfPuzzleSizePenalty) {
}
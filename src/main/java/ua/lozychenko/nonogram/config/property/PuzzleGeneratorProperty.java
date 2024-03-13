package ua.lozychenko.nonogram.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("puzzle.generator")
public record PuzzleGeneratorProperty(Double fillPercent, Double fillPercentRange) {
}
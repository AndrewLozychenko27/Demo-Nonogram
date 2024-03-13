package ua.lozychenko.nonogram.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("page.puzzle")
public record PuzzlePageProperty(Integer defaultSize, Integer[] sizeRange) {
}
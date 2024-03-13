package ua.lozychenko.nonogram.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("game.hints")
public record GameHintsProperty(Integer min, Double threshold) {
}
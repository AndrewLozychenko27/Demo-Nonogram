package ua.lozychenko.nonogram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ua.lozychenko.nonogram.config.property.GameHintsProperty;
import ua.lozychenko.nonogram.config.property.GameScoreProperty;
import ua.lozychenko.nonogram.config.property.OAuth2Property;
import ua.lozychenko.nonogram.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.nonogram.config.property.PuzzlePageProperty;
import ua.lozychenko.nonogram.config.property.UserPageProperty;

@SpringBootApplication
@EnableConfigurationProperties({GameHintsProperty.class,
        GameScoreProperty.class,
        OAuth2Property.class,
        PuzzlePageProperty.class,
        UserPageProperty.class,
        PuzzleGeneratorProperty.class})
public class NonogramApplication {
    public static void main(String[] args) {
        SpringApplication.run(NonogramApplication.class, args);
    }
}
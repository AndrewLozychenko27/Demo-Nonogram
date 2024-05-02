package ua.lozychenko.generatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ua.lozychenko.generatorservice.config.property.PuzzleGeneratorProperty;

@SpringBootApplication
@EnableConfigurationProperties({PuzzleGeneratorProperty.class})
public class GeneratorServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GeneratorServiceApplication.class, args);
    }
}
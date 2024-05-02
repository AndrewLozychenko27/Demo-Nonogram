package ua.lozychenko.generatorservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import ua.lozychenko.generatorservice.config.property.PuzzleGeneratorProperty;
import ua.lozychenko.generatorservice.data.Cell;
import ua.lozychenko.generatorservice.dto.CellsDto;
import ua.lozychenko.generatorservice.dto.PuzzleGenerateRequestDto;
import ua.lozychenko.generatorservice.generator.PuzzleByImageGenerator;
import ua.lozychenko.generatorservice.generator.PuzzleGenerator;
import ua.lozychenko.generatorservice.service.util.CellHelper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class PuzzleGeneratorListener {
    public static final String RANDOM = "random";
    public static final String IMAGE = "image";
    private final List<PuzzleGenerator> randomGenerators;
    private final PuzzleByImageGenerator puzzleByImageGenerator;
    private final PuzzleGeneratorProperty properties;

    public PuzzleGeneratorListener(List<PuzzleGenerator> randomGenerators, PuzzleByImageGenerator puzzleByImageGenerator, PuzzleGeneratorProperty properties) {
        this.randomGenerators = randomGenerators;
        this.puzzleByImageGenerator = puzzleByImageGenerator;
        this.properties = properties;
    }

    @KafkaListener(topics = "puzzle-generation-request", groupId = "puzzle-generator-group", containerFactory = "listenerContainerFactory")
    @SendTo
    public CellsDto generatePuzzleRequestListener(@Payload PuzzleGenerateRequestDto request, @Header String generationMode) throws IOException {
        CellsDto response = null;

        if (RANDOM.equals(generationMode)) {
            PuzzleGenerator generator = randomGenerators.get(new Random().nextInt(randomGenerators.size()));

            response = new CellsDto(List.of(generator.generate(request.getCells(), request.getWidth(), request.getHeight())));
        } else if (IMAGE.equals(generationMode)) {
            List<Set<Cell>> puzzles = new LinkedList<>();
            Set<Cell> puzzle;

            for (Double multiplier : properties.thresholdMultipliers()) {
                puzzle = puzzleByImageGenerator.generate(request.getCells(), request.getWidth(), request.getHeight(), request.getImage(), multiplier);
                if (isPuzzleEmpty(puzzle, request.getWidth(), request.getHeight()) || isPuzzleFull(puzzle, request.getWidth(), request.getHeight())) {
                    continue;
                }
                puzzles.add(puzzle);
                puzzles.add(CellHelper.reverse(puzzle, request.getCells()));
            }

            response = new CellsDto(puzzles);
        }

        return response;
    }

    private boolean isPuzzleFull(Set<Cell> cells, short width, short height) {
        return cells.size() >= (width * height) - (width * height * properties.emptyPuzzle());
    }

    private boolean isPuzzleEmpty(Set<Cell> cells, short width, short height) {
        return cells.size() <= width * height * properties.emptyPuzzle();
    }
}
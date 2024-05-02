package ua.lozychenko.generatorservice.generator.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.generatorservice.data.Cell;
import ua.lozychenko.generatorservice.generator.PuzzleByImageGenerator;
import ua.lozychenko.generatorservice.service.util.CellHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ConfigurablePuzzleByImageGenerator implements PuzzleByImageGenerator {
    @Override
    public Set<Cell> generate(Set<Cell> cells, short width, short height, byte[] bytes, double thresholdMultiplier) throws IOException {
        Set<Cell> res = new HashSet<>();
        Cell[][] parsedCells = CellHelper.parseCells(cells, width, height);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        int threshold = (int) (getAverageRGB(image, 0, 0, width, height) * thresholdMultiplier);
        int sectorWidth = image.getWidth() / width;
        int sectorHeight = image.getHeight() / height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (getAverageRGB(image, x * sectorWidth, y * sectorHeight, sectorWidth, sectorHeight) >= threshold) {
                    res.add(parsedCells[x][y]);
                }
            }
        }

        return res;
    }

    private int getAverageRGB(BufferedImage image, int startX, int startY, int width, int height) {
        return (int) Arrays.stream(image.getRGB(startX, startY, width, height, null, 0, width))
                .average()
                .orElseThrow();
    }
}
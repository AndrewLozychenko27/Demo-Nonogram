package ua.lozychenko.nonogram.data.service.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.repo.CellRepo;
import ua.lozychenko.nonogram.data.service.CellService;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DefaultCellService extends DefaultBaseService<Cell> implements CellService {
    private final CellRepo repo;

    public DefaultCellService(CellRepo repo) {
        super(repo);
        this.repo = repo;
    }

    @Override
    public Cell parseCell(String coordinates) {
        Pattern pattern = Pattern.compile("(\\d+):(\\d+)");
        Matcher matcher = pattern.matcher(coordinates);
        Cell cell = new Cell();

        if (matcher.find()) {
            cell.setX(Short.parseShort(matcher.group(1)));
            cell.setY(Short.parseShort(matcher.group(2)));
        } else {
            throw new IllegalArgumentException("Wrong coordinates format");
        }

        return cell;
    }

    @Override
    public List<Cell> parseCells(String[] coordinates) {
        return Arrays.stream(coordinates)
                .map(coordinate -> findOrCreate(parseCell(coordinate)))
                .toList();
    }

    @Override
    public Cell findOrCreate(Cell cell) {
        return repo.findByXAndY(cell.getX(), cell.getY()).orElse(cell);
    }
}
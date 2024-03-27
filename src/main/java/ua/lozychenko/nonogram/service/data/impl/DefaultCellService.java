package ua.lozychenko.nonogram.service.data.impl;

import org.springframework.stereotype.Service;
import ua.lozychenko.nonogram.data.entity.Cell;
import ua.lozychenko.nonogram.data.repo.CellRepo;
import ua.lozychenko.nonogram.service.data.CellService;

import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    public Set<Cell> parseCells(String[] coordinates) {
        return Arrays.stream(coordinates)
                .map(coordinate -> findOrCreate(parseCell(coordinate)))
                .collect(Collectors.toSet());
    }

    @Override
    public Cell findOrCreate(Cell cell) {
        return repo.findByXAndY(cell.getX(), cell.getY()).orElse(cell);
    }

    @Override
    public Set<Cell> findAllByLimit(short x, short y) {
        return repo.findAllByLimit(x, y);
    }

    @Override
    public Set<Cell> findAllByLimitDiagonally(short x, short y, boolean isReverse) {
        return isReverse
                ? repo.findAllByLimitDiagonallyReverse(x, y)
                : repo.findAllByLimitDiagonally(x, y);
    }
}
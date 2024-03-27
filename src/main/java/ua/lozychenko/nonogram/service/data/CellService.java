package ua.lozychenko.nonogram.service.data;

import ua.lozychenko.nonogram.data.entity.Cell;

import java.util.List;
import java.util.Set;

public interface CellService extends BaseService<Cell> {
    Cell parseCell(String coordinates);

    Set<Cell> parseCells(String[] coordinates);

    Cell findOrCreate(Cell cell);

    Set<Cell> findAllByLimit(short x, short y);

    Set<Cell> findAllByLimitDiagonally(short x, short y, boolean isReverse);
}
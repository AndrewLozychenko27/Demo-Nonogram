package ua.lozychenko.nonogram.service.data;

import ua.lozychenko.nonogram.data.entity.Cell;

import java.util.List;

public interface CellService extends BaseService<Cell> {
    Cell parseCell(String coordinates);

    List<Cell> parseCells(String[] coordinates);

    Cell findOrCreate(Cell cell);
}
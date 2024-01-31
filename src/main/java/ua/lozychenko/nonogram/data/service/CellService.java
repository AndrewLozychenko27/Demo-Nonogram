package ua.lozychenko.nonogram.data.service;

import ua.lozychenko.nonogram.data.entity.Cell;

public interface CellService extends BaseService<Cell> {
    Cell parseCell(String coordinates);

    Cell findOrCreate(Cell cell);
}
package ua.lozychenko.generatorservice.dto;


import ua.lozychenko.generatorservice.data.Cell;

import java.util.List;
import java.util.Set;

public class CellsDto {
    private List<Set<Cell>> cells;

    public CellsDto() {
    }

    public CellsDto(List<Set<Cell>> cells) {
        this.cells = cells;
    }

    public List<Set<Cell>> getCells() {
        return cells;
    }

    public void setCells(List<Set<Cell>> cells) {
        this.cells = cells;
    }
}
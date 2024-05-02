package ua.lozychenko.generatorservice.service.util;

import ua.lozychenko.generatorservice.data.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CellHelper {
    public static Cell[][] parseCells(Set<Cell> cells, int width, int height) {
        Cell[][] res = new Cell[width][height];

        cells.forEach(cell -> res[cell.getX()][cell.getY()] = cell);

        return res;
    }

    public static Set<Cell> reverse(Set<Cell> target, Set<Cell> source) {
        List<Cell> reversed = new ArrayList<>(source);

        reversed.removeAll(target);

        return Set.copyOf(reversed);
    }
}
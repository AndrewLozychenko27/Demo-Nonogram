package ua.lozychenko.nonogram.data.entity.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hints {
    private Map<Integer, List<Short>> vertical;
    private Map<Integer, List<Short>> horizontal;

    public Hints() {
        this.vertical = new HashMap<>();
        this.horizontal = new HashMap<>();
    }

    public Map<Integer, List<Short>> getVertical() {
        return vertical;
    }

    public void setVertical(Map<Integer, List<Short>> vertical) {
        this.vertical = vertical;
    }

    public Map<Integer, List<Short>> getHorizontal() {
        return horizontal;
    }

    public void setHorizontal(Map<Integer, List<Short>> horizontal) {
        this.horizontal = horizontal;
    }

    public void addVertical(int rowNum, List<Short> hint) {
        initiateIfNull(vertical, rowNum);
        vertical.get(rowNum).addAll(hint);
    }

    public void addHorizontal(int colNum, List<Short> hint) {
        initiateIfNull(horizontal, colNum);
        horizontal.get(colNum).addAll(hint);
    }

    private void initiateIfNull(Map<Integer, List<Short>> line, int index) {
        if (!line.containsKey(index)) {
            line.put(index, new ArrayList<>());
        }
    }
}
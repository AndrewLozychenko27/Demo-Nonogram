package ua.lozychenko.generatorservice.dto;

import ua.lozychenko.generatorservice.data.Cell;

import java.util.Set;

public class PuzzleGenerateRequestDto {
    private Set<Cell> cells;
    private short width;
    private short height;

    private byte[] image;

    public PuzzleGenerateRequestDto() {
    }

    public PuzzleGenerateRequestDto(Set<Cell> cells, short width, short height, byte[] image) {
        this.cells = cells;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    public Set<Cell> getCells() {
        return cells;
    }

    public void setCells(Set<Cell> cells) {
        this.cells = cells;
    }

    public short getWidth() {
        return width;
    }

    public void setWidth(short width) {
        this.width = width;
    }

    public short getHeight() {
        return height;
    }

    public void setHeight(short height) {
        this.height = height;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
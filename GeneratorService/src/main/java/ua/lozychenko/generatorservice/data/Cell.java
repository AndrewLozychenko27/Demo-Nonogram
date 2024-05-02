package ua.lozychenko.generatorservice.data;

public class Cell {
    private Long id;
    private Short x;
    private Short y;

    public Cell() {
    }

    public Cell(int x, int y) {
        this.x = (short) x;
        this.y = (short) y;
    }

    public Cell(Long id, Short x, Short y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short getX() {
        return x;
    }

    public void setX(Short x) {
        this.x = x;
    }

    public Short getY() {
        return y;
    }

    public void setY(Short y) {
        this.y = y;
    }
}
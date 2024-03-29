package ua.lozychenko.nonogram.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import java.util.Arrays;

@Entity
public class Cell {
    @Id
    @SequenceGenerator(name = "cell_seq", sequenceName = "cell_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cell_seq")
    private Long id;

    private Short x;

    private Short y;

    public Cell() {
    }

    public Cell(int x, int y) {
        this.x = (short) x;
        this.y = (short) y;
    }

    public Cell(Short x, Short y) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        return (obj instanceof Cell cell)
                && (this.getX().equals(cell.getX()))
                && (this.getY().equals(cell.getY()));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new short[]{x, y});
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
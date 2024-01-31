package ua.lozychenko.nonogram.data.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

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
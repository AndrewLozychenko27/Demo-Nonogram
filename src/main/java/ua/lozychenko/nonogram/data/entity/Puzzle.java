package ua.lozychenko.nonogram.data.entity;

import org.hibernate.validator.constraints.Length;
import ua.lozychenko.nonogram.constraint.UniquePuzzleField;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Puzzle {
    @Id
    @SequenceGenerator(name = "puzzle_seq", sequenceName = "puzzle_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puzzle_seq")
    private Long id;

    @NotEmpty(message = "Puzzle name is required")
    @Length(max = 256, message = "Puzzle name must be no longer than {max} characters")
    @UniquePuzzleField
    private String name;

    @NotNull(message = "Width is required")
    @Min(value = 10, message = "Width must be at least {value}")
    @Max(value = 40, message = "Width must be no longer than {value}")
    private Short width;

    @NotNull(message = "Height is required")
    @Min(value = 10, message = "Height must be at least {value}")
    @Max(value = 40, message = "Height must be no longer than {value}")
    private Short height;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "solution",
            joinColumns = @JoinColumn(name = "puzzle_id"),
            inverseJoinColumns = @JoinColumn(name = "cell_id")
    )
    private List<Cell> cells;

    public Puzzle() {
        this.cells = new ArrayList<>();
    }

    public Puzzle(String name, Short width, Short height, User user) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getWidth() {
        return width;
    }

    public void setWidth(Short width) {
        this.width = width;
    }

    public Short getHeight() {
        return height;
    }

    public void setHeight(Short height) {
        this.height = height;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }

    public void addCells(List<Cell> cells) {
        this.cells.addAll(cells);
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }
}
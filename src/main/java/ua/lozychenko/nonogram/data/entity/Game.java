package ua.lozychenko.nonogram.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Game {
    @Id
    @SequenceGenerator(name = "game_seq", sequenceName = "game_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_seq")
    private Long id;

    private Integer attempts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "puzzle_id")
    private Puzzle puzzle;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "suggestion",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "cell_id")
    )
    private Set<Cell> cells;

    @OneToMany(mappedBy = "game", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Hint> hints;

    @Enumerated(EnumType.STRING)
    private State state;

    public Game() {
        this.attempts = 0;
        this.cells = new HashSet<>();
        this.hints = new ArrayList<>();
        this.state = State.IN_PROGRESS;
    }

    public Game(Puzzle puzzle, User user) {
        this();
        this.puzzle = puzzle;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public Set<Cell> getCells() {
        return cells;
    }

    public void setCells(Set<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getHints() {
        return hints.stream().map(Hint::getCell).toList();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void addCell(Cell cell) {
        cells.add(cell);
    }

    public void addCells(List<Cell> cells) {
        this.cells.addAll(cells);
    }

    public void addHints(Set<Cell> hints) {
        this.hints.addAll(hints.stream().map(cell -> new Hint(cell, this)).toList());
    }

    public void addRemoved(Set<Cell> removed) {
        this.hints.addAll(removed.stream().map(cell -> new Hint(cell, this, true)).toList());
        removed.forEach(this.cells::remove);
    }

    public void clearAndAddCells(Set<Cell> cells) {
        this.cells.clear();
        this.cells.addAll(cells);
    }

    public int getHintsPercent() {
        return (int) ((hints.size() * 100.0f) / puzzle.getCells().size());
    }

    public boolean containsCell(short x, short y) {
        return cells.contains(new Cell(x, y));
    }

    public boolean containsHint(short x, short y) {
        return hints.stream().anyMatch(hint -> hint.getCell().equals(new Cell(x, y)));
    }

    public boolean containsRemoval(short x, short y) {
        return hints.stream().anyMatch(hint -> hint.getCell().equals(new Cell(x, y)) && hint.isRemoval());
    }
}
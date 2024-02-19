package ua.lozychenko.nonogram.data.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import java.util.ArrayList;
import java.util.List;

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
    private List<Cell> cells;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "hint",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "cell_id")
    )
    private List<Cell> hints;

    @Enumerated(EnumType.STRING)
    private State state;

    public Game() {
    }

    public Game(Puzzle puzzle, User user) {
        this.puzzle = puzzle;
        this.user = user;
        this.attempts = 0;
        this.cells = new ArrayList<>();
        this.hints = new ArrayList<>();
        this.state = State.IN_PROGRESS;
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

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getHints() {
        return hints;
    }

    public void setHints(List<Cell> hints) {
        this.hints = hints;
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

    public void addHints(List<Cell> hints) {
        this.hints.addAll(hints);
    }

    public void clearAndAddCells(List<Cell> cells) {
        this.cells.clear();
        this.cells.addAll(cells);
    }

    public String[] getCellsAsStrings() {
        return cells.stream()
                .map(cell -> String.format("%d:%d%s", cell.getX(), cell.getY(), hints.contains(cell) ? " hint" : ""))
                .toList().toArray(String[]::new);
    }

    public int getHintsCount() {
        return (int) ((hints.size() * 100.0f) / puzzle.getCells().size());
    }
}
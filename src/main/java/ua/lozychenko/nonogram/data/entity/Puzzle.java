package ua.lozychenko.nonogram.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import ua.lozychenko.nonogram.constraint.UniquePuzzleName;
import ua.lozychenko.nonogram.constraint.group.NameGroup;
import ua.lozychenko.nonogram.constraint.group.SizeGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Entity
@UniquePuzzleName(groups = NameGroup.class)
public class Puzzle {
    @Id
    @SequenceGenerator(name = "puzzle_seq", sequenceName = "puzzle_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puzzle_seq")
    private Long id;

    @NotEmpty(message = "Puzzle name is required", groups = NameGroup.class)
    @Length(max = 256, message = "Puzzle name must be no longer than {max} characters", groups = NameGroup.class)
    private String name;

    @NotNull(message = "Width is required", groups = SizeGroup.class)
    @Min(value = 10, message = "Width must be at least {value}", groups = SizeGroup.class)
    @Max(value = 40, message = "Width must be no longer than {value}", groups = SizeGroup.class)
    private Short width;

    @NotNull(message = "Height is required", groups = SizeGroup.class)
    @Min(value = 10, message = "Height must be at least {value}", groups = SizeGroup.class)
    @Max(value = 40, message = "Height must be no longer than {value}", groups = SizeGroup.class)
    private Short height;

    private Boolean visible;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "solution",
            joinColumns = @JoinColumn(name = "puzzle_id"),
            inverseJoinColumns = @JoinColumn(name = "cell_id")
    )
    private Set<Cell> cells;

    @OneToMany(mappedBy = "puzzle")
    private List<Game> games;

    public Puzzle() {
        this.cells = new HashSet<>();
        this.visible = true;
    }

    public Puzzle(String name, Short width, Short height, User user) {
        this();
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

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Cell> getCells() {
        return cells;
    }

    public void setCells(Set<Cell> cells) {
        this.cells = cells;
    }

    public Optional<Game> getGame(User user) {
        return games.stream().filter(game -> game.getUser().getId().equals(user.getId())).findFirst();
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public void addCell(Cell cell) {
        this.cells.add(cell);
    }

    public void addCells(Set<Cell> cells) {
        this.cells.addAll(cells);
    }

    public boolean isEmpty() {
        return cells.isEmpty();
    }

    public boolean containsCell(short x, short y) {
        return cells.contains(new Cell(x, y));
    }
}
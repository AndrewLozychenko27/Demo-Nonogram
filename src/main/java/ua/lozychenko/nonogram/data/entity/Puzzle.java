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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import ua.lozychenko.nonogram.constraint.UniquePuzzleName;
import ua.lozychenko.nonogram.constraint.group.NameGroup;
import ua.lozychenko.nonogram.constraint.group.SizeGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@UniquePuzzleName(groups = NameGroup.class)
public class Puzzle {
    @Id
    @SequenceGenerator(name = "puzzle_seq", sequenceName = "puzzle_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "puzzle_seq")
    private Long id;

    @NotBlank(message = "Puzzle name is required", groups = NameGroup.class)
    @Length(max = 256, message = "Puzzle name must be no longer than {max} characters", groups = NameGroup.class)
    @Pattern(regexp = "^[\\w ]+$", message = "Puzzle name must contain only a-z, A-Z, 0-9 and _", groups = NameGroup.class)
    private String name;

    @NotNull(message = "Width is required", groups = SizeGroup.class)
    @Min(value = 10, message = "Width must be at least {value}", groups = SizeGroup.class)
    @Max(value = 40, message = "Width must be no longer than {value}", groups = SizeGroup.class)
    private Short width;

    @NotNull(message = "Height is required", groups = SizeGroup.class)
    @Min(value = 10, message = "Height must be at least {value}", groups = SizeGroup.class)
    @Max(value = 40, message = "Height must be no longer than {value}", groups = SizeGroup.class)
    private Short height;

    private Boolean visible = true;

    private Boolean generated = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "solution",
            joinColumns = @JoinColumn(name = "puzzle_id"),
            inverseJoinColumns = @JoinColumn(name = "cell_id")
    )
    private Set<Cell> cells = new HashSet<>();

    @OneToMany(mappedBy = "puzzle")
    @ToString.Exclude
    private List<Game> games;

    public Puzzle(String name, Short width, Short height, User user) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.user = user;
    }

    public Boolean isVisible() {
        return visible;
    }

    public Boolean isGenerated() {
        return generated;
    }

    public Optional<Game> getGame(User user) {
        return games.stream().filter(game -> game.getUser().getId().equals(user.getId())).findFirst();
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
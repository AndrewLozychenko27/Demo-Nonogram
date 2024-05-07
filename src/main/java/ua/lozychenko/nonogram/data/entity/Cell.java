package ua.lozychenko.nonogram.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Cell {
    @Id
    @SequenceGenerator(name = "cell_seq", sequenceName = "cell_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cell_seq")
    @EqualsAndHashCode.Exclude
    private Long id;

    private Short x;

    private Short y;

    public Cell(int x, int y) {
        this.x = (short) x;
        this.y = (short) y;
    }

    public Cell(Short x, Short y) {
        this.x = x;
        this.y = y;
    }
}
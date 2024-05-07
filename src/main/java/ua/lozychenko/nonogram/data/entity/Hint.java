package ua.lozychenko.nonogram.data.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.lozychenko.nonogram.data.entity.pk.HintPrimaryKey;

@Data
@NoArgsConstructor
@Entity
public class Hint {
    @EmbeddedId
    private HintPrimaryKey id;

    @ManyToOne
    @MapsId("cellId")
    @JoinColumn(name = "cell_id")
    private Cell cell;

    @ManyToOne
    @MapsId("gameId")
    @JoinColumn(name = "game_id")
    private Game game;

    private Boolean removal = false;

    public Hint(Cell cell, Game game) {
        this.id = new HintPrimaryKey(game.getId(), cell.getId());
        this.cell = cell;
        this.game = game;
    }

    public Hint(Cell cell, Game game, Boolean removal) {
        this(cell, game);
        this.removal = removal;
    }

    public Boolean isRemoval() {
        return removal;
    }
}
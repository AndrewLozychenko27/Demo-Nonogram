package ua.lozychenko.nonogram.data.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import ua.lozychenko.nonogram.data.entity.pk.HintPrimaryKey;

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

    private Boolean isRemoved;

    public Hint() {
        isRemoved = false;
    }

    public Hint(Cell cell, Game game) {
        this();
        this.id = new HintPrimaryKey(game.getId(), cell.getId());
        this.cell = cell;
        this.game = game;
    }

    public Hint(Cell cell, Game game, Boolean isRemoved) {
        this(cell, game);
        this.isRemoved = isRemoved;
    }

    public HintPrimaryKey getId() {
        return id;
    }

    public void setId(HintPrimaryKey id) {
        this.id = id;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(Boolean removed) {
        isRemoved = removed;
    }
}
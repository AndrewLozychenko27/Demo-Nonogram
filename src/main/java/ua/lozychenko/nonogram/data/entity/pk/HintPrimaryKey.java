package ua.lozychenko.nonogram.data.entity.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class HintPrimaryKey implements Serializable {
    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "cell_id")
    private Long cellId;

    public HintPrimaryKey() {
    }

    public HintPrimaryKey(Long gameId, Long cellId) {
        this.gameId = gameId;
        this.cellId = cellId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getCellId() {
        return cellId;
    }

    public void setCellId(Long cellId) {
        this.cellId = cellId;
    }
}
package split.io.splitapi.game.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import split.io.splitapi.game.models.ActionType;

@Getter
@Entity
@Table(name = "actions")
public class ActionEntity {

    @Id
    private String id;

    @Column(name = "game_id")
    private String gameId;

    private String playerId;

    private int x;

    private int y;

    @Enumerated(EnumType.STRING)
    private ActionType type;

    private int round;

    public ActionEntity() {
    }

    public ActionEntity(String id, String gameId, String playerId, int x, int y, ActionType type, int round) {
        this.id = id;
        this.gameId = gameId;
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.type = type;
        this.round = round;
    }
}
